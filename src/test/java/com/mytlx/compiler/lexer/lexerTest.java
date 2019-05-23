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
            if (result.getErrors().isEmpty()) {
                List<Token> list = result.getTokenList();
                for (Token token : list) {
                    out.println(token);
                }
            } else {
                System.out.println("词法分析错误");
                result.getErrors().forEach(System.out::println);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
