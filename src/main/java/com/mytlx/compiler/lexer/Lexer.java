package com.mytlx.compiler.lexer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * 词法分析（直接转向法）
 *
 * @author TLX
 * @date 2019/5/15
 * @time 15:37
 */
public class Lexer {

    /**
     * 状态
     */
    private enum StateEnum {
        NORMAL,             // 初始状态
        INID,               // 标识符状态
        INNUM,              // 数字状态
        INASSIGN,           // 赋值状态
        INCOMMENT,          // 注释状态
        INDOT,              // 点状态
        INRANGE,            // 数组下标界限状态
        INCHAR,             // 字符标志状态
        ERROR,              // 出错
    }

    private static Logger LOG = LoggerFactory.getLogger(Lexer.class);
    private int line = 1;
    private int column = 0;
    private int getMeFirst = -1;
    private boolean lf = false;
    private Reader reader;
    private List<String> errors;

    /**
     * 获取词法分析结果
     *
     * @param reader snl文件的输入字符流
     * @return 词法分析结果
     * @throws IOException 字符输入流为空
     */
    public LexerResult getResult(Reader reader) throws IOException {
        LexerResult result = new LexerResult();
        List<Token> tokenList = new ArrayList<>();

        if (reader == null) {
            LOG.error("字符输入流为空");
            result.setTokenList(tokenList);
            return result;
        }

        this.reader = reader;
        Token token = getToken();
        while (token != null) {
            tokenList.add(token);
            token = getToken();
        }
        result.setTokenList(tokenList);
        return result;
    }

    /**
     * 直接转向法（程序中心法）
     *
     * @return 识别的Token，如果出错，返回空Token
     */
    private Token getToken() throws IOException {
        LOG.trace("调用getToken方法");
        StateEnum stateEnum = StateEnum.NORMAL;
        StringBuilder sb = new StringBuilder();
        int dotLine = 0, dotColumn = 0;
        int ch = getChar();
        while (ch != -1) {
            sb.append((char) ch);
            Token token;
            //<editor-fold desc="switch stateEnum">
            switch (stateEnum) {
                //<editor-fold desc="case NORMAL">
                case NORMAL:
                    LOG.trace("进入NORMAL状态");
                    if (isAlpha(ch)) {
                        stateEnum = StateEnum.INID;
                    } else if (isDigit(ch)) {
                        stateEnum = StateEnum.INNUM;
                    }
                    //region 单字符分界符
                    else if (isBlank(ch)) {
                        sb.deleteCharAt(sb.length() - 1);
                        stateEnum = StateEnum.NORMAL;
                    } else if (ch == '+') {
                        token = new Token(line, column, TokenType.PLUS, sb.toString());
                        LOG.debug("已识别Token：" + token);
                        return token;
                    } else if (ch == '-') {
                        token = new Token(line, column, TokenType.MINUS, sb.toString());
                        LOG.debug("已识别Token：" + token);
                        return token;
                    } else if (ch == '*') {
                        token = new Token(line, column, TokenType.TIMES, sb.toString());
                        LOG.debug("已识别Token：" + token);
                        return token;
                    } else if (ch == '/') {
                        token = new Token(line, column, TokenType.DIVIDE, sb.toString());
                        LOG.debug("已识别Token：" + token);
                        return token;
                    } else if (ch == '(') {
                        token = new Token(line, column, TokenType.LPAREN, sb.toString());
                        LOG.debug("已识别Token：" + token);
                        return token;
                    } else if (ch == ')') {
                        token = new Token(line, column, TokenType.RPAREN, sb.toString());
                        LOG.debug("已识别Token：" + token);
                        return token;
                    } else if (ch == ',') {
                        token = new Token(line, column, TokenType.COMMA, sb.toString());
                        LOG.debug("已识别Token：" + token);
                        return token;
                    } else if (ch == ';') {
                        token = new Token(line, column, TokenType.SEMI, sb.toString());
                        LOG.debug("已识别Token：" + token);
                        return token;
                    } else if (ch == '[') {
                        token = new Token(line, column, TokenType.LMIDPAREN, sb.toString());
                        LOG.debug("已识别Token：" + token);
                        return token;
                    } else if (ch == ']') {
                        token = new Token(line, column, TokenType.RMIDPAREN, sb.toString());
                        LOG.debug("已识别Token：" + token);
                        return token;
                    } else if (ch == '=') {
                        token = new Token(line, column, TokenType.EQ, sb.toString());
                        LOG.debug("已识别Token：" + token);
                        return token;
                    } else if (ch == '<') {
                        token = new Token(line, column, TokenType.LT, sb.toString());
                        LOG.debug("已识别Token：" + token);
                        return token;
                    }
                    //endregion 单字符分界符
                    else if (ch == ':') {
                        stateEnum = StateEnum.INASSIGN;
                    } else if (ch == '{') {
                        stateEnum = StateEnum.INCOMMENT;
                        sb.deleteCharAt(sb.length() - 1);       // 删除注释头符号'{'
                    } else if (ch == '.') {
                        stateEnum = StateEnum.INDOT;            // 域的点运算符
                    } else if (ch == '\'') {
                        sb.deleteCharAt(sb.length() - 1);       // 删除符号'\''
                        stateEnum = StateEnum.INCHAR;
                    } else {
                        LOG.error("当前字符不在分析范围之内：{}({})[{}:{}]", showChar(ch), ch, line, column);
                        stateEnum = StateEnum.ERROR;
                    }
                    // TODO: 多个注释结束符的情况{ commment }}}}}}}
                    break;
                //</editor-fold>
                //<editor-fold desc="case INID 识别标识符">
                case INID:
                    LOG.trace("进入ID状态");
                    if (isAlpha(ch) || isDigit(ch)) {
                        stateEnum = StateEnum.INID;
                    } else {
                        // 当前字符已经不是标识符范围的了，标识符已经结束了
                        // 回溯一个字符
                        LOG.trace("当前字符已经不是标识符范围的了：\"{}\"({})[{}:{}]", showChar(ch), ch, line, column);
                        LOG.trace("回溯字符：\"{}\"({})[{}:{}]", showChar(ch), ch, line, column);
                        backTrackChar(sb.charAt(sb.length() - 1));
                        token = new Token(line, column, TokenType.ID, sb.substring(0, sb.length() - 1));
                        // 判断是否为保留字
                        token.checkKeyWords();
                        LOG.debug("已识别Token：" + token);
                        return token;
                    }
                    break;
                //</editor-fold>
                //<editor-fold desc="case INNUM 识别数字">
                case INNUM:
                    LOG.trace("进入NUM状态");
                    // 如果是数字会自动保持数字状态
                    // 不是数字表示，数字识别已完成
                    if (!isDigit(ch)) {
                        LOG.trace("回溯字符：\"{}\"({})[{}:{}]", showChar(ch), ch, line, column);
                        backTrackChar(sb.charAt(sb.length() - 1));  // ch
                        token = new Token(line, column, TokenType.INTC, sb.substring(0, sb.length() - 1));
                        LOG.debug("已识别Token：" + token);
                        return token;
                    }
                    break;
                //</editor-fold>
                //<editor-fold desc="case INASSIGN 识别赋值符号">
                case INASSIGN:
                    LOG.trace("进入ASSIGN状态");
                    if (ch == '=') {
                        token = new Token(line, column, TokenType.ASSIGN, sb.toString());
                        LOG.debug("已识别Token：" + token);
                        return token;
                    } else {
                        LOG.error("':'后面不是'='");
                        stateEnum = StateEnum.ERROR;
                    }
                    break;
                //</editor-fold>
                //<editor-fold desc="case INCOMMENT 去除注释">
                case INCOMMENT:
                    LOG.trace("进入COMMENT状态");
                    // 没有注释结束符的情况下，报错
                    int tmpLine = line;
                    int tmpColumn = column - 1;
                    // 注释不需要出现在Token中，所以循环删除注释
                    // 删除刚刚读入的注释的第一个字符
                    sb.deleteCharAt(sb.length() - 1);
                    while (ch != '}' && ch != -1) {
                        ch = getChar();
                    }
                    // 注释结束后继续识别
                    stateEnum = StateEnum.NORMAL;
                    if (ch != '}') {
                        // 直到文件结束也没有注释结束符
                        LOG.error("没有注释结束符，注释开始符在：[{}:{}]", tmpLine, tmpColumn);
                        stateEnum = StateEnum.ERROR;
                    }
                    break;
                //</editor-fold>
                //<editor-fold desc="case DOT 点号">
                case INDOT:
                    LOG.trace("进入DOT状态");
                    if (isAlpha(ch)) {          // record域中的点运算符
                        LOG.trace("record域中的点运算符");
                        LOG.trace("回溯字符：\"{}\"({})[{}:{}]", showChar(ch), ch, line, column);
                        backTrackChar(ch);      // 回溯多读的字符
                        sb.deleteCharAt(sb.length() - 1);
                        token = new Token(line, column, TokenType.DOT, sb.toString());
                        LOG.debug("已识别Token：" + token);
                        return token;
                    } else if (ch == '.') {     // 数组的下标运算符
                        stateEnum = StateEnum.INRANGE;
                        break;
                    }
                    // 因为结束符"."后面可能有多个空白字符，需要记录行和列
                    dotLine = line;
                    dotColumn = column - 1;
                    while (isBlank(ch)) ch = getChar();
                    if (ch == -1) {             // 结束标志
                        LOG.trace("结束标志");
                        token = new Token(dotLine, dotColumn, TokenType.EOF, ".");
                        LOG.debug("已识别Token：" + token);
                        return token;
                    }
                    // 错误，回溯
                    LOG.error("错误的点运算符");
                    LOG.trace("回溯字符：\"{}\"({})[{}:{}]", showChar(ch), ch, line, column);
                    backTrackChar(ch);
                    token = new Token(line, column, TokenType.ERROR, sb.toString());
                    LOG.debug("已识别Token：" + token);
                    break;
                //</editor-fold>
                //<editor-fold desc="case INRANGE 数组下标界限符">
                case INRANGE:
                    LOG.trace("进入RANGE状态");
                    // 如果是数组下标界限符，那么后面一定是数字
                    if (isDigit(ch)) {
                        // 回溯
                        LOG.trace("回溯字符：\"{}\"({})[{}:{}]", showChar(ch), ch, line, column);
                        backTrackChar(ch);
                        sb.deleteCharAt(sb.length() - 1);
                        token = new Token(line, column, TokenType.UNDERRANGE, sb.toString());
                        LOG.debug("已识别Token：" + token);
                        return token;
                    }
                    // 如果后面不是数字，那么error
                    LOG.error("数组下标界限符后面不是数字");
                    stateEnum = StateEnum.ERROR;
                    break;
                //</editor-fold>
                //<editor-fold desc="case INCHAR 识别字符">
                case INCHAR:
                    LOG.trace("进入CHAR状态");
                    if (isDigit(ch) || isAlpha(ch)) {
                        // 判断后面是否跟着字符结束标志'\''
                        ch = getChar();
                        if (ch == '\'') {
                            token = new Token(line, column, TokenType.CHAR, sb.toString());
                            LOG.debug("已识别Token：" + token);
                            return token;
                        }
                    }
                    // 其他符号，error
                    LOG.error("没有字符结束标志");
                    stateEnum = StateEnum.ERROR;
                    break;
                //</editor-fold>
                //<editor-fold desc="case ERROR 错误返回空Token">
                case ERROR:
                    LOG.error("[Error] Unrecognized token. near " + line + ":" + column);
                    // TODO: 词法分析错误处理
                    LOG.trace("回溯字符：\"{}\"({})[{}:{}]", showChar(ch), ch, line, column);
                    backTrackChar(ch);
                    sb.deleteCharAt(sb.length() - 1);
                    token = new Token(line, column, TokenType.ERROR, sb.toString());
                    return token;
                //</editor-fold>
                default:
                    stateEnum = StateEnum.ERROR;
                    break;
            }
            //</editor-fold>
            ch = getChar();
        }
        //region 文件结束处理

        // 如果文件结束前一个符号是'.'，则程序结束
        // 如果'.'是文件的最后一个字符，那么将跳到下面
        // if (stateEnum == StateEnum.INDOT) {
        //     Token token = new Token(line, column, TokenType.DOT, ".");
        //     LOG.debug("已识别Token：" + token);
        //     return token;
        // }
        if (stateEnum != StateEnum.NORMAL) {
            LOG.error("[错误]在 " + line + "行 " + column + "列");
        }
        //endregion
        return null;
    }

    /**
     * 回溯读到的字符，保存在getMeFirst，供下次循环使用。并将列数减1
     *
     * @param ch 需要回溯的字符
     */
    private void backTrackChar(int ch) {
        getMeFirst = ch;
        // 如果要回溯的字符在列首的情况，CRLF应该不存在此情况
        // TODO：但是LF会存在此种情况
        column--;
    }

    /**
     * 从字符流中读取一个字符
     *
     * @return
     * @throws IOException
     */
    private int getChar() throws IOException {
        int ch;
        // 先判断getMeFirst中是否存储了回溯的字符
        if (getMeFirst != -1 && getMeFirst != ' ' && getMeFirst != '\r' && getMeFirst != '\n') {
            ch = getMeFirst;
            getMeFirst = -1;
        } else if (getMeFirst == ' ' || getMeFirst == '\r') {
            column++;
            getMeFirst = -1;
            ch = reader.read();
        } else {
            ch = reader.read();
        }

        if (lf) {
            column = 0;
            line++;
        }

        if (ch == '\n') {
            lf = true;
            column++;
            // column = 0;
            // line++;
        } else if (ch != -1) {
            column++;
            lf = false;
        }

        LOG.trace("当前字符是：\"{}\"({})[{}:{}]", showChar(ch), ch, line, column);
        return ch;
    }

    /**
     * 显示空白字符，用于日志记录
     *
     * @param ch 空白字符
     * @return 空白字符的符号表示
     */
    private String showChar(int ch) {
        if (ch == '\n')
            return "\\n";
        else if (ch == '\r')
            return "\\r";
        else return "" + (char) ch;
    }

    /**
     * 判断是否为字母
     *
     * @param ch 要判断的字符
     * @return true or false
     */
    private boolean isAlpha(int ch) {
        return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z');
    }

    /**
     * 判断是否为数字
     *
     * @param ch 要判断的字符
     * @return true or false
     */
    private boolean isDigit(int ch) {
        return (ch >= '0' && ch <= '9');
    }

    /**
     * 判断是否为空白（空格，制表符，回车，换行）
     *
     * @param ch 要判断的字符
     * @return true or false
     */
    private boolean isBlank(int ch) {
        return ((char) ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r');
    }

    /**
     * 单元测试，词法分析
     */
    public static void main(String[] args) {
        InputStream in = Lexer.class.getClassLoader().getResourceAsStream("p.snl");
        Lexer lexer = new Lexer();
        try {
            LexerResult result = lexer.getResult(new InputStreamReader(in));
            List<Token> list = result.getTokenList();
            System.out.println();
            if (!list.isEmpty()) {
                System.out.print("[ 行:列 ]| 词素信息 | 词法单元 \n");
                System.out.print("---------+----------+----------\n");
            }
            for (Token t : list) {
                System.out.printf("[%3d:%-3d]| %8s | %8s\n", t.getLine(), t.getColumn(), t.getValue(), t.getType());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
