package com.mytlx.compiler.syntax.tree;

import java.io.FileNotFoundException;
import java.io.PrintStream;

/**
 * 语法树结构
 *
 * @author TLX
 * @date 2019.5.21
 * @time 20:32
 */
public class SyntaxTree {
    private TreeNode root;
    private PrintStream out;

    public SyntaxTree() {
    }

    public SyntaxTree(TreeNode root) {
        this.root = root;
    }

    public PrintStream getOut() {
        return out;
    }

    public void setOut(PrintStream out) {
        this.out = out;
    }


    public void preOrderRecursive() throws FileNotFoundException {
        preOrderRecursiveCore(root, 0, 0, new int[100]);
    }

    /**
     * 递归打印树结构，竖版，类似于tree命令
     *
     * @param node  当前结点
     * @param level 当前结点的层级，root=0
     * @param cnt   当前结点前面应该打印的"|"次数，貌似最后没用到
     * @param b     当前结点前面打印“|”的依据，0 -> 不打印，1 -> 打印
     * @throws FileNotFoundException
     */
    public void preOrderRecursiveCore(TreeNode node, int level, int cnt, int[] b) throws FileNotFoundException {
        if (node == null) return;
        // int temp = cnt;
        for (int i = 0; i < level - 1; i++) {
            if (b[i] == 1) {
                System.out.print("|  ");
                out.print("|  ");
            } else {
                System.out.print("   ");
                out.print("   ");
            }
        }

        if (node == root) {
            System.out.println(node.getValue());
            out.println(node.getValue());
        } else {
            System.out.println("|__" + node.getValue());
            out.println("|__" + node.getValue());
        }
        if (node.hasChild()) {
            if (node.hasSiblings()) {
                if (level > 0)
                    b[level - 1] = 1;
                preOrderRecursiveCore(node.getChildren(), level + 1, cnt + 1, b);
            } else {
                if (level > 0)
                    b[level - 1] = 0;
                preOrderRecursiveCore(node.getChildren(), level + 1, cnt, b);
            }
        }

        if (node.hasSiblings()) {

            preOrderRecursiveCore(node.getSiblings(), level, cnt, b);
        }
    }

    /**
     * 单元测试
     *
     * @param args
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {

        TreeNode t1 = new TreeNode("root");
        TreeNode t2_1 = new TreeNode("a");
        TreeNode t2_2 = new TreeNode("b");
        TreeNode t2_3 = new TreeNode("c");
        TreeNode t3_1_1 = new TreeNode("d");
        TreeNode t3_2_1 = new TreeNode("e");
        TreeNode t3_2_2 = new TreeNode("f");
        TreeNode t3_3_1 = new TreeNode("g");
        TreeNode t4_2_1_1 = new TreeNode("h");
        TreeNode t4_1_1_1 = new TreeNode("j");
        t1.setChildren(t2_1);
        t2_1.setSiblings(t2_2);
        t2_2.setSiblings(t2_3);
        t2_1.setChildren(t3_1_1);
        t3_1_1.setChildren(t4_1_1_1);
        t2_2.setChildren(t3_2_1);
        t3_2_1.setChildren(t4_2_1_1);
        t3_2_1.setSiblings(t3_2_2);
        t2_3.setChildren(t3_3_1);


        SyntaxTree s = new SyntaxTree(t1);
        // s.preOrderRecursive();
        s.preOrderRecursive();
    }
}
