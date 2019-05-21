package com.mytlx.compiler.syntax.tree;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

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

    public void traversal() {
        if (root == null) return;
        int cnt = 1;
        int cntChild = 0;
        int cntCurr = 1;
        Queue<TreeNode> queue = new LinkedList<>();
        TreeNode pNode = root;
        queue.add(root);
        TreeNode[] curr = {root};
        while (cntCurr != 0 && cnt != 0) {
            pNode = curr[curr.length - cntCurr];
            if (pNode.hasChild()) {
                Collections.addAll(queue, pNode.getChildren());
                cntChild += pNode.getChildren().length;
            }
            System.out.print(pNode + " ");
            if (--cntCurr == 0) {
                System.out.print("|");
                TreeNode temp;
                if ((temp = queue.poll()) != null)
                    curr = temp.getChildren();
                else return;
                if (curr != null)
                    cntCurr = curr.length;
            }
            if (--cnt == 0) {
                System.out.println("\n=================");
                cnt = cntChild;
                cntChild = 0;
            }
            // pNode = curr[curr.length - cntCurr];

        }
    }


    public static void main(String[] args) {

        TreeNode t1 = new TreeNode("root");
        TreeNode t2_1 = new TreeNode("2_1");
        TreeNode t2_2 = new TreeNode("2_2");
        TreeNode t2_3 = new TreeNode("2_3");
        TreeNode t3_1_1 = new TreeNode("3_1_1");
        TreeNode t3_2_1 = new TreeNode("3_2_1");
        TreeNode t3_2_2 = new TreeNode("3_2_2");
        TreeNode t3_3_1 = new TreeNode("3_3_1");
        TreeNode[] c1 = {t2_1, t2_2, t2_3};
        TreeNode[] c2 = {t3_1_1};
        TreeNode[] c3 = {t3_2_1, t3_2_2};
        TreeNode[] c4 = {t3_3_1};
        t1.setChildren(c1);
        t2_1.setChildren(c2);
        t2_2.setChildren(c3);
        t2_3.setChildren(c4);

        SyntaxTree s = new SyntaxTree(t1);
        s.traversal();
    }
}
