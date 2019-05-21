package com.mytlx.compiler.syntax.symbol;

import com.mytlx.compiler.syntax.tree.TreeNode;

/**
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
        return null;
    }

    public String getValue() {
        return value;
    }

    public static NonTerminal nonFactory(String s) {
        return new NonTerminal(s);
    }


}
