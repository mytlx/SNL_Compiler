package com.mytlx.compiler.syntax.tree;

/**
 * 树结点
 *
 * @author TLX
 * @date 2019.5.20
 * @time 21:38
 */
public class TreeNode {
    private TreeNode[] children;
    private String value;


    public TreeNode(String value) {
        this.value = value;
    }

    public boolean hasChild() {
        return children != null;
    }

    public TreeNode[] getChildren() {
        return children;
    }

    public void setChildren(TreeNode[] children) {
        this.children = children;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
