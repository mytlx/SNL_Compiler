package com.mytlx.compiler.syntax.symbol;

import com.mytlx.compiler.syntax.tree.TreeNode;

/**
 * 非终结符
 *
 * @author TLX
 * @date 2019.5.20
 * @time 21:45
 */
public class NonTerminal extends Symbol {
    private final TreeNode node;
    private final String value;

    public NonTerminal(String value) {
        this.value = value;
        node = new TreeNode(value);
    }

    @Override
    public TreeNode getNode() {
        return node;
    }

    public String getValue() {
        return value;
    }

    /**
     * 非终结符工厂
     *
     * @param s
     * @return
     */
    public static NonTerminal nonFactory(String s) {
        return new NonTerminal(s);
    }

    public boolean isBlank() {
        return value.equals("blank");
    }

}
