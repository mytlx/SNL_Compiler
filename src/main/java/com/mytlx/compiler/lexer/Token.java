package com.mytlx.compiler.lexer;

/**
 * Created by IntelliJ IDEA.
 * User: TLX
 * Date: 2019/5/15
 * Time: 15:15
 */
public class Token {
    private int line;       //行
    private int column;     //列
    private TokenType type; //类型
    private String value;   //含义

    public Token() {
        this(TokenType.EMPTY);
    }

    public Token(TokenType type) {
        this(0, 0, type, type.getStr());
    }

    Token(int line, int column, TokenType type, String value) {
        this.line = line;
        this.column = column;
        this.type = type;
        this.value = value;
    }


}
