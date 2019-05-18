package com.mytlx.compiler.lexer;

/**
 * Created by IntelliJ IDEA.
 * User: TLX
 * Date: 2019/5/15
 * Time: 15:37
 */

/**
 * 直接转向法（程序中心法）
 */
public class Lexer {

    /**
     * 状态
     */
    private enum StateEnum {
        NORMAL,             //
        INID,               // 标识符状态
        INNUM,              // 数字状态
        INCOMMENT,          // 注释状态
        INCHAR,             // 字符标志状态
        ERROR,              //
        INASSIGN,           // 赋值状态
        INRANGE,            // 数组下标界限状态
        INDOT,              //
    }






    /**
     * 判断是否为字母
     *
     * @param ch
     * @return
     */
    private boolean isAlpha(int ch) {
        return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z');
    }

    /**
     * 判断是否为数字
     *
     * @param ch
     * @return
     */
    private boolean isDigit(int ch) {
        return (ch >= '0' && ch <= '9');
    }

    /**
     * 判断是否为空白（空格，制表符，回车，换行）
     *
     * @param ch
     * @return
     */
    private boolean isBlank(int ch) {
        return ((char) ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r');
    }

}
