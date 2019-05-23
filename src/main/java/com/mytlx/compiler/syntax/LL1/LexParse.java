package com.mytlx.compiler.syntax.LL1;

import com.mytlx.compiler.lexer.Lexer;
import com.mytlx.compiler.lexer.LexerResult;
import com.mytlx.compiler.lexer.Token;
import com.mytlx.compiler.lexer.TokenType;
import com.mytlx.compiler.syntax.symbol.NON_TERMINAL;
import com.mytlx.compiler.syntax.symbol.NonTerminal;
import com.mytlx.compiler.syntax.symbol.Symbol;
import com.mytlx.compiler.syntax.tree.SyntaxTree;
import com.mytlx.compiler.syntax.tree.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;

/**
 * 语法解析之前需要做的词法解析工作,
 * 对Token列表的访问，匹配等
 *
 * @author TLX
 * @date 2019.5.22
 * @time 13:51
 */
public abstract class LexParse {
    private static Logger LOG = LoggerFactory.getLogger(LexParse.class);
    private List<Token> tokenList;
    private int currentIndex = 0;

    public abstract SyntaxTree syntaxParse();

    /**
     * 语法分析前做的词法分析工作，获得token列表
     *
     * @param inPath 源文件
     */
    public void lexParse(String inPath) {
        // InputStream in = Lexer.class.getClassLoader().getResourceAsStream(inPath);
        InputStream in = null;
        Lexer lexer = new Lexer();
        try {
            in = new FileInputStream(new File(inPath));
            LexerResult result = lexer.getResult(new InputStreamReader(in));
            tokenList = result.getTokenList();
            LOG.info("=========================词法分析结束============================");

        } catch (IOException e) {
            LOG.error("加载文件失败");
            e.printStackTrace();
        }

    }

    /**
     * 用于匹配终结符和当前Token列表头的token的type
     *
     * @param type 终结符的token type
     * @return 成功：值为token value的树结点
     * 不成功：null
     */
    TreeNode match(TokenType type) {
        Token t = popToken();
        TreeNode node = null;
        if (t != null) {
            if (t.getType().equals(type)) {
                node = new TreeNode(t.getValue());
                LOG.trace("已匹配：" + t);
            } else {
                LOG.error("未成功匹配：[ " + type + "]和" + t);
            }
        } else {
            LOG.error("输入流已空");
        }
        return node;
    }

    /**
     * 通过预测分析表，查询该非终结符应该替换的列表
     *
     * @param symbol 当前的非终结符
     * @return 预测分析表查询到的列表
     */
    List<Symbol> find(NonTerminal symbol) {
        Token token = peekToken();
        String value = symbol.getValue();
        LOG.trace("查询预测分析表：[ 非终结符：" + value + " ] [ 输入符号：" + token.getValue() + " ]");
        NON_TERMINAL n = NON_TERMINAL.valueOf(value);
        LOG.trace("NON_TERMINAL：<" + n + ">");
        return n.predict(token);
    }

    /**
     * 获取Token列表中当前Token，
     * 并将指针指向下一个Token，
     * 相同于栈中的pop
     *
     * @return 成功：列表中当前Token
     * 不成功：null
     */
    private Token popToken() {
        Token token = null;
        if (currentIndex < tokenList.size()) {
            token = tokenList.get(currentIndex++);
            LOG.trace("获取下一个Token：" + token);
        } else {
            LOG.error("输入流已空");
        }
        return token;
    }

    /**
     * 获取Token列表中指针当前指向的Token,
     * 相当于栈中的peek
     *
     * @return 成功：指针当前指向的Token
     * 不成功：null
     */
    private Token peekToken() {
        Token token = null;
        if (currentIndex < tokenList.size()) {
            token = tokenList.get(currentIndex);
            LOG.trace("当前Token是" + token);
        } else {
            LOG.error("输入流已空");
        }
        return token;
    }


}
