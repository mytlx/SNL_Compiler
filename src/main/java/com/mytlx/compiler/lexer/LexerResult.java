package com.mytlx.compiler.lexer;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: TLX
 * Date: 2019.5.16
 * Time: 9:26
 */
public class LexerResult {
    private List<Token> tokenList;

    public List<Token> getTokenList() {
        return tokenList;
    }

    public void setTokenList(List<Token> tokenList) {
        this.tokenList = tokenList;
    }

}
