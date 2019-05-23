package com.mytlx.compiler.lexer;

import com.mytlx.compiler.syntax.symbol.NON_TERMINAL;
import com.mytlx.compiler.syntax.symbol.NonTerminal;
import com.mytlx.compiler.syntax.symbol.Symbol;
import com.mytlx.compiler.syntax.symbol.Terminal;
import com.mytlx.compiler.syntax.tree.SyntaxTree;
import com.mytlx.compiler.syntax.tree.TreeNode;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

import static com.mytlx.compiler.syntax.symbol.NonTerminal.nonFactory;

/**
 * @author TLX
 * @date 2019.5.20
 * @time 22:49
 */
public class SyntaxTest {

    private static Logger LOG = LoggerFactory.getLogger(SyntaxTest.class);
    private List<Token> tokenList;
    private int currentIndex = 0;

    @Before
    public void test02() {
        InputStream in = Lexer.class.getClassLoader().getResourceAsStream("p.snl");
        Lexer lexer = new Lexer();
        try {
            LexerResult result = lexer.getResult(new InputStreamReader(in));
            tokenList = result.getTokenList();
            LOG.info("=========================已完成词法分析============================");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test01() {
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
                LOG.trace("symbol是终极符：<" + ((Terminal) symbol).getToken().getType() + ">");
                // 判断是否symbol和当前token是否匹配
                // 新建TreeNode，通过match返回
                TreeNode match = match(((Terminal) symbol).getToken().getType(), Objects.requireNonNull(getNextToken()));
                // 改变ID等的值
                assert match != null;
                symbol.getNode().setValue(match.getValue());
                LOG.trace("##########终极符识别完毕：<" + symbol.getNode().getValue() + "> ###########");

            } else if (symbol instanceof NonTerminal) {
                LOG.trace("symbol是非终极符：<" + ((NonTerminal) symbol).getValue() + ">");
                // 查预测分析表，将symbol替换成右端生成式
                List<Symbol> symbols = find((NonTerminal) symbol, getCurrToken());
                LOG.info("预测分析表返回" + symbols);

                // 右端生成式为symbol的children
                int size = symbols.size();
                List<TreeNode> children = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    children.add(symbols.get(i).getNode());
                }
                // symbol.getNode().setChildren(children);
                // 右端生成式逆序入栈
                for (int i = size - 1; i >= 0; i--) {
                    Symbol item = symbols.get(i);
                    if (item instanceof Terminal || !((NonTerminal) item).isBlank())
                        stack.push(item);
                }
            } else {
                System.out.println("error");
                // error
            }
        }
        SyntaxTree syntaxTree = new SyntaxTree(root);
        // syntaxTree.traversal();
    }

    private List<Symbol> find(NonTerminal symbol, Token token) {
        String value = symbol.getValue();
        NON_TERMINAL n = NON_TERMINAL.valueOf(value);
        LOG.trace("NON_TERMINAL\t" + n);
        return n.predict(token);
    }

    private TreeNode match(TokenType type, Token t) {
        assert t != null;
        if (t.getType().equals(type)) {
            return new TreeNode(t.getValue());
        }
        return null;
    }

    private Token getNextToken() {
        Token token;
        if (currentIndex < tokenList.size()) {
            token = tokenList.get(currentIndex++);
            LOG.trace("当前Token是" + token);
            return token;
        }
        return null;
    }

    private Token getCurrToken() {
        Token token = null;
        if (currentIndex < tokenList.size()) {
            token = tokenList.get(currentIndex);
            LOG.trace("当前Token是" + token);
        }
        return token;
    }
}
