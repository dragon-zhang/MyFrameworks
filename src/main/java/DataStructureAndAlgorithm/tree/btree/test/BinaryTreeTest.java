package DataStructureAndAlgorithm.tree.btree.test;

import DataStructureAndAlgorithm.tree.btree.BinaryTree;
import DataStructureAndAlgorithm.tree.btree.ErgodicUtil;

/**
 * @author SuccessZhang
 */
public class BinaryTreeTest {
    public static void main(String[] args) {
        BinaryTree top = new BinaryTree("1");
        BinaryTree firstLeft = new BinaryTree("2");
        BinaryTree firstRight = new BinaryTree("3");
        top.setLeftChild(firstLeft);
        top.setRightChild(firstRight);
        BinaryTree secondLeftOne = new BinaryTree("4");
        BinaryTree secondRightOne = new BinaryTree("5");
        firstLeft.setLeftChild(secondLeftOne);
        firstLeft.setRightChild(secondRightOne);
        BinaryTree secondLeftTwo = new BinaryTree("6");
        BinaryTree secondRightTwo = new BinaryTree("7");
        firstRight.setLeftChild(secondLeftTwo);
        firstRight.setRightChild(secondRightTwo);
        System.out.print("前序遍历:");
        ErgodicUtil.preorderTraversal(top);
        System.out.print("\n中序遍历:");
        ErgodicUtil.intermediateTraversal(top);
        System.out.print("\n后序遍历:");
        ErgodicUtil.postorderTraversal(top);
        System.out.print("\n层次遍历:");
        ErgodicUtil.levelTraversal(top);
        System.out.print("\n");
    }
}
