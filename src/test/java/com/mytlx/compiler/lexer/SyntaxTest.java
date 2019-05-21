package com.mytlx.compiler.lexer;

import com.mytlx.compiler.syntax.symbol.NON_TERMINAL;
import com.mytlx.compiler.syntax.symbol.NonTerminal;
import com.mytlx.compiler.syntax.symbol.Symbol;
import com.mytlx.compiler.syntax.symbol.Terminal;
import com.mytlx.compiler.syntax.tree.TreeNode;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Stack;

import static com.mytlx.compiler.syntax.symbol.NonTerminal.nonFactory;

/**
 * @author TLX
 * @date 2019.5.20
 * @time 22:49
 */
public class SyntaxTest {


    private List<Token> tokenList;
    private int currentIndex = 0;

    @Before
    public void test02() {
        InputStream in = Lexer.class.getClassLoader().getResourceAsStream("p.snl");
        Lexer lexer = new Lexer();
        try {
            LexerResult result = lexer.getResult(new InputStreamReader(in));
            tokenList = result.getTokenList();

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
                // 判断是否symbol和当前token是否匹配
                // 新建TreeNode，通过match返回
                TreeNode match = match(((Terminal) symbol).getToken().getType());
                // 改变ID等的值
                assert match != null;
                symbol.getNode().setValue(match.getValue());

            } else if (symbol instanceof NonTerminal) {
                // 查预测分析表，将symbol替换成右端生成式
                List<Symbol> symbols = find((NonTerminal) symbol, new Token());
                // 右端生成式为symbol的children
                int size = symbols.size();
                TreeNode[] children = new TreeNode[size];
                symbol.getNode().setChildren(children);
                // 右端生成式逆序入栈
                for (int i = size - 1; i >= 0; i--) {
                    stack.push(symbols.get(i));
                }
            } else {
                System.out.println("error");
                // error
            }
        }
    }

    private List<Symbol> find(NonTerminal symbol, Token token) {
        String value = symbol.getValue();
        NON_TERMINAL n = NON_TERMINAL.valueOf(value);
        return n.predict(token);
    }

    private TreeNode match(TokenType type) {
        Token t = getNextToken();
        assert t != null;
        if (t.getType().equals(type)) {
            return new TreeNode(t.getValue());
        }
        return null;
    }

    private Token getNextToken() {
        Token token;
        if (currentIndex < tokenList.size()) {
            token = tokenList.get(currentIndex);
        }
        return null;
    }
}
