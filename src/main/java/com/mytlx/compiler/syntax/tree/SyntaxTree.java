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

    public SyntaxTree() {
    }

    public SyntaxTree(TreeNode root) {
        this.root = root;
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

    public void preOrderRecursive() {
        preOrderRecursiveCore(root, 0);
    }

    public void preOrderRecursiveCore(TreeNode node, int cnt) {
        if (node == null) return;
        if (node != root) System.out.print("|");
        for (int i = 0; i < cnt; i++) {
            if (cnt > 1)
                System.out.print("  ");
            if (i != 0 && i == cnt - 1)
                System.out.print("|_");
            else if (i == 0 && cnt == 1) {
                System.out.print("_");
            }
        }
        System.out.println(node.getValue());
        if (node.hasChild()) preOrderRecursiveCore(node.getChildren(), cnt + 1);
        if (node.hasSiblings()) preOrderRecursiveCore(node.getSiblings(), cnt);
    }


    public static void main(String[] args) throws FileNotFoundException {

        // TreeNode t1 = new TreeNode("root");
        // TreeNode t2_1 = new TreeNode("2_1");
        // TreeNode t2_2 = new TreeNode("2_2");
        // TreeNode t2_3 = new TreeNode("2_3");
        // TreeNode t3_1_1 = new TreeNode("3_1_1");
        // TreeNode t3_2_1 = new TreeNode("3_2_1");
        // TreeNode t3_2_2 = new TreeNode("3_2_2");
        // TreeNode t3_3_1 = new TreeNode("3_3_1");
        // TreeNode[] c1 = {t2_1, t2_2, t2_3};
        // TreeNode[] c2 = {t3_1_1};
        // TreeNode[] c3 = {t3_2_1, t3_2_2};
        // TreeNode[] c4 = {t3_3_1};
        // t1.setChildren(c1);
        // t2_1.setChildren(c2);
        // t2_2.setChildren(c3);
        // t2_3.setChildren(c4);
        TreeNode t1 = new TreeNode("root");
        TreeNode t2_1 = new TreeNode("a");
        TreeNode t2_2 = new TreeNode("b");
        TreeNode t2_3 = new TreeNode("c");
        TreeNode t3_1_1 = new TreeNode("d");
        TreeNode t3_2_1 = new TreeNode("e");
        TreeNode t3_2_2 = new TreeNode("f");
        TreeNode t3_3_1 = new TreeNode("g");
        t1.setChildren(t2_1);
        t2_1.setSiblings(t2_2);
        t2_2.setSiblings(t2_3);
        t2_1.setChildren(t3_1_1);
        t2_2.setChildren(t3_2_1);
        t3_2_1.setSiblings(t3_2_2);
        t2_3.setChildren(t3_3_1);


        SyntaxTree s = new SyntaxTree(t1);
        // s.preOrderRecursive();
        s.preOrder();
    }
}
