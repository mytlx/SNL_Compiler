package com.mytlx.compiler.GUI;

import com.mytlx.compiler.lexer.Lexer;
import com.mytlx.compiler.lexer.LexerResult;
import com.mytlx.compiler.lexer.Token;
import com.mytlx.compiler.syntax.LL1.LL1Parse;
import com.mytlx.compiler.syntax.tree.SyntaxTree;

import java.io.*;
import java.util.List;

import static com.mytlx.compiler.utils.FileUtils.clearFile;

/**
 * GUI中使用到的词法分析和语法分析
 *
 * @author TLX
 * @date 2019.5.23
 * @time 22:24
 */
public class ParseForGUI {
    /**
     * GUI用到的词法分析，用来生成Token序列
     *
     * @param inPath  源文件
     * @param outPath Token序列输出的文件
     */
    public static void getToken(String inPath, String outPath) {
        // InputStream in = Lexer.class.getClassLoader().getResourceAsStream(inPath);
        InputStream in = null;
        List<Token> list = null;
        Lexer lexer = new Lexer();
        try {
            in = new FileInputStream(new File(inPath));
            PrintStream out = new PrintStream(outPath);
            LexerResult result = lexer.getResult(new InputStreamReader(in));

            if (result != null) {
                list = result.getTokenList();
                if (!list.isEmpty()) {
                    System.out.print("< [ 行: 列 ],  词素信息,  词法单元 >\n");
                    System.out.print("-----------------------------------\n");
                    out.print("< [ 行: 列 ],   词素信息,   词法单元 >\n");
                    out.print("------------------------------------\n");
                }
                for (Token t : list) {
                    // System.out.println(t);
                    System.out.printf("< [%3d:%3d ],%10s,%10s >\n", t.getLine(), t.getColumn(), t.getValue(), t.getType());
                    out.printf("< [%3d:%3d ],%10s,%10s >\n", t.getLine(), t.getColumn(), t.getValue(), t.getType());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * GUI用到的语法分析，用来生成语法分析树
     * 流程：词法分析 -> 语法分析
     *
     * @param inPath  源文件
     * @param outPath 语法分析树输出的文件
     */
    public static void getSyntaxTree(String inPath, String outPath) {
        LL1Parse parse = new LL1Parse();
        parse.lexParse(inPath);
        SyntaxTree result = parse.syntaxParse();
        try {
            clearFile(new File(outPath));
            if (result != null) {
                result.setOut(new PrintStream(new File(outPath)));
                result.preOrderRecursive();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


}
