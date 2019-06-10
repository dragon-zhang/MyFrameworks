package DataStructureAndAlgorithm.tree.btree;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/**
 * @author SuccessZhang
 */
public class ErgodicUtil {

    /**
     * 前序顺序创建二叉树
     */
    public static BinaryTree createBinaryTree() {
        Scanner scanner = new Scanner(System.in);
        String data = scanner.nextLine();
        BinaryTree binaryTree;
        if ("#".equals(data)) {
            binaryTree = null;
        } else {
            binaryTree = new BinaryTree(data);
            binaryTree.setLeftChild(createBinaryTree());
            binaryTree.setRightChild(createBinaryTree());
        }
        return binaryTree;
    }

    /**
     * 创建二叉排序树
     */
    public static BinaryTree createBinarySortingTree() {
        Scanner scanner = new Scanner(System.in);
        String data = scanner.nextLine();
        BinaryTree binaryTree;
        if ("#".equals(data)) {
            binaryTree = null;
        } else {
            binaryTree = new BinaryTree(Integer.valueOf(data));
            while (!"#".equals(data)) {
                data = scanner.nextLine();
                if (!"#".equals(data)) {
                    insertChild(binaryTree, Integer.valueOf(data));
                }
            }
        }
        return binaryTree;
    }

    /**
     * 创建二叉排序树，插入子节点
     */
    private static void insertChild(BinaryTree binaryTree, int data) {
        if (data < (int) binaryTree.getData()) {
            if (binaryTree.getLeftChild() == null) {
                binaryTree.setLeftChild(new BinaryTree(data));
            } else {
                insertChild(binaryTree.getLeftChild(), data);
            }
        } else {
            if (binaryTree.getRightChild() == null) {
                binaryTree.setRightChild(new BinaryTree(data));
            } else {
                insertChild(binaryTree.getRightChild(), data);
            }
        }
    }

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
