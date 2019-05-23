package com.mytlx.compiler.syntax.tree;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Stack;

/**
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

    // public void traversal() {
    //     if (root == null) return;
    //     int cnt = 1;
    //     int cntChild = 0;
    //     int cntCurr = 1;
    //     int cntNull = 0;
    //     Queue<TreeNode> queue = new LinkedList<>();
    //     TreeNode pNode = root;
    //     queue.add(root);
    //     TreeNode[] curr = {root};
    //     while (cntCurr != 0 && cnt != 0) {
    //         pNode = curr[curr.length - cntCurr];
    //         if (pNode.hasChild()) {
    //             Collections.addAll(queue, pNode.getChildren());
    //             cntChild += pNode.getChildren().length;
    //         } else {
    //             // TODO:
    //             cntChild++;     // null的位置
    //         }
    //         System.out.print(pNode + " ");
    //         if (--cntCurr == 0) {
    //             System.out.print("| ");
    //             TreeNode temp;
    //             while (true) {
    //                 if ((temp = queue.poll()) != null)
    //                     curr = temp.getChildren();
    //                 else return;
    //                 if (curr != null) {
    //                     cntCurr = curr.length;
    //                     break;
    //                 } else {
    //                     if ((cnt - 1) == 0)
    //                         cntNull++;
    //                     else System.out.print("null | ");
    //                 }
    //             }
    //         }
    //         if (--cnt == 0) {
    //             System.out.println("\n=================");
    //             for (int i = 0; i < cntNull; i++) {
    //                 System.out.print("null | ");
    //             }
    //             cnt = cntChild;
    //             cntChild = 0;
    //         }
    //         // pNode = curr[curr.length - cntCurr];
    //
    //     }
    // }

    public void preOrder() throws FileNotFoundException {
        if (root == null) return;
        Stack<TreeNode> stack = new Stack<>();
        // PrintStream out = new PrintStream(new File("./tree.txt"));
        stack.push(root);
        TreeNode temp;
        while (!stack.isEmpty()) {
            temp = stack.pop();
            if (temp.hasChild()) {
                TreeNode child = temp.getChildren();
                if (child.hasSiblings()) {
                    TreeNode siblings = child.getSiblings();
                    for (int i = child.siblingsSize() - 1; i >= 0; i--) {
                        for (int j = 0; j < i; j++) {
                            siblings = siblings.getSiblings();
                        }
                        stack.push(siblings);
                        siblings = child.getSiblings();
                    }
                }
                stack.push(child);
            }
            System.out.println("|");
            System.out.println("|_" + temp.getValue() + " ");
        }
    }

    public void preOrderRecursive() throws FileNotFoundException {
        PrintStream out = new PrintStream(new File("./tree.txt"));
        preOrderRecursiveCore(root, 0, 0, new int[100]);
    }

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
