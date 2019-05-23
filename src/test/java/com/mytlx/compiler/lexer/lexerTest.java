package com.mytlx.compiler.lexer;

import org.junit.Test;

import java.io.*;
import java.util.List;

/**
 * @author TLX
 * @date 2019.5.23
 * @time 11:02
 */
public class lexerTest {
    @Test
    public void test01() throws FileNotFoundException {
        InputStream in = Lexer.class.getClassLoader().getResourceAsStream("p.snl");
        PrintStream out = new PrintStream("./tokenList.txt");
        Lexer lexer = new Lexer();
        try {
            LexerResult result = lexer.getResult(new InputStreamReader(in));

            List<Token> list = result.getTokenList();
            for (Token token : list) {
                System.out.println(token);
                out.println(token);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
