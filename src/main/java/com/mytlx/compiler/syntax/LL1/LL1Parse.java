package com.mytlx.compiler.syntax.LL1;

import com.mytlx.compiler.syntax.symbol.NonTerminal;
import com.mytlx.compiler.syntax.symbol.Symbol;
import com.mytlx.compiler.syntax.symbol.Terminal;
import com.mytlx.compiler.syntax.tree.SyntaxTree;
import com.mytlx.compiler.syntax.tree.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import static com.mytlx.compiler.syntax.symbol.NonTerminal.nonFactory;

/**
 * LL1，自顶向下的非递归分析
 *
 * @author TLX
 * @date 2019.5.22
 * @time 14:53
 */
public class LL1Parse extends LexParse {

    private static Logger LOG = LoggerFactory.getLogger(LL1Parse.class);
    private SyntaxTree syntaxTree;

    @Override
    public SyntaxTree syntaxParse() {
        LOG.info("=========================语法分析开始=============================");
        Stack<Symbol> stack = new Stack<>();
        // 开始符 start： Program 并且是root
        NonTerminal start = nonFactory("Program");
        TreeNode root = start.getNode();
        // 开始符压栈
        stack.push(start);

        Symbol symbol;
        while (!stack.isEmpty()) {
            symbol = stack.pop();
            if (symbol instanceof Terminal) {
                LOG.trace("symbol是终结符：<" + ((Terminal) symbol).getToken().getType() + ">");
                // 判断是否symbol和当前token是否匹配
                // 新建TreeNode，通过match返回
                TreeNode match = match(((Terminal) symbol).getToken().getType());
                if (match != null) {
                    symbol.getNode().setValue(match.getValue());
                    LOG.trace("##########终结符识别完毕：<" + symbol.getNode().getValue() + "> ###########");
                } else {
                    LOG.error("匹配终结符失败");
                }

            } else if (symbol instanceof NonTerminal) {
                LOG.trace("symbol是非终结符：<" + ((NonTerminal) symbol).getValue() + ">");
                // 查预测分析表，将symbol替换成右端生成式
                List<Symbol> symbols = find((NonTerminal) symbol);
                LOG.info("预测分析表返回：" + symbols);

                if (symbols != null) {
                    // 右端生成式为symbol的children
                    int size = symbols.size();
                    for (int i = 0; i < size - 1; i++) {
                        symbols.get(i).getNode().setSiblings(symbols.get(i + 1).getNode());
                    }
                    symbol.getNode().setChildren(symbols.get(0).getNode());
                    LOG.trace("构建" + ((NonTerminal) symbol).getValue() + "的子树：" + symbol.getNode().getChildren());
                    // 右端生成式逆序入栈
                    for (int i = size - 1; i >= 0; i--) {
                        Symbol item = symbols.get(i);
                        if (item instanceof Terminal || !((NonTerminal) item).isBlank())
                            stack.push(item);
                    }
                } else {
                    LOG.error("预测分析表查询失败");
                }
            } else {
                // error
                LOG.error("未识别的字符，出现了不应该出现的字符：[ " + symbol.getNode().getValue() + " ]");
            }
        }

        syntaxTree = new SyntaxTree(root);
        return syntaxTree;
    }

    /**
     * 自顶向下的非递归语法分析，单元测试
     *
     * @param args
     */
    public static void main(String[] args) throws FileNotFoundException {
        LL1Parse parse = new LL1Parse();
        parse.lexParse();
        SyntaxTree result = parse.syntaxParse();
        result.setOut(new PrintStream(new File("./tree.txt")));
        result.preOrderRecursive();
    }
}
