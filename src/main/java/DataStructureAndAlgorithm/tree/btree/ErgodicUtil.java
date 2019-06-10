package DataStructureAndAlgorithm.tree.btree;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author SuccessZhang
 */
public class ErgodicUtil {
    /**
     * 前序遍历
     */
    public static void preorderTraversal(BinaryTree binaryTree) {
        System.out.print(binaryTree.getData());
        if (binaryTree.getLeftChild() != null) {
            preorderTraversal(binaryTree.getLeftChild());
        }
        if (binaryTree.getRightChild() != null) {
            preorderTraversal(binaryTree.getRightChild());
        }
    }

    /**
     * 中序遍历
     */
    public static void intermediateTraversal(BinaryTree binaryTree) {
        if (binaryTree.getLeftChild() != null) {
            intermediateTraversal(binaryTree.getLeftChild());
        }
        System.out.print(binaryTree.getData());
        if (binaryTree.getRightChild() != null) {
            intermediateTraversal(binaryTree.getRightChild());
        }
    }

    /**
     * 后序遍历
     */
    public static void postorderTraversal(BinaryTree binaryTree) {
        if (binaryTree.getLeftChild() != null) {
            postorderTraversal(binaryTree.getLeftChild());
        }
        if (binaryTree.getRightChild() != null) {
            postorderTraversal(binaryTree.getRightChild());
        }
        System.out.print(binaryTree.getData());
    }

    /**
     * 层次遍历
     */
    public static void levelTraversal(BinaryTree binaryTree) {
        Queue<BinaryTree> children = new LinkedList<>();
        children.offer(binaryTree);
        while (!children.isEmpty()) {
            BinaryTree node = children.poll();
            System.out.print(node.getData());
            if (node.getLeftChild() != null) {
                children.offer(node.getLeftChild());
            }
            if (node.getRightChild() != null) {
                children.offer(node.getRightChild());
            }
        }
    }
}
