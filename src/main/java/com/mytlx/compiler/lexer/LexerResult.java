package com.mytlx.compiler.lexer;

import java.util.List;

/**
 * 词法分析的结果
 *
 * @author TLX
 * @date 2019.5.16
 * @time 9:26
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
