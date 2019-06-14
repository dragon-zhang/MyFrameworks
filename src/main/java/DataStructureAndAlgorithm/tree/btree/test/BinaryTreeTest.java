package DataStructureAndAlgorithm.tree.btree.test;

import DataStructureAndAlgorithm.tree.btree.BinaryTree;

/**
 * @author SuccessZhang
 */
public class BinaryTreeTest {
    public static void main(String[] args) {
        BinaryTree top = new BinaryTree(4);
        BinaryTree firstLeft = new BinaryTree(2);
        BinaryTree firstRight = new BinaryTree(6);
        top.setLeftChild(firstLeft);
        top.setRightChild(firstRight);
        firstLeft.setParents(top);
        firstRight.setParents(top);
        BinaryTree secondLeftOne = new BinaryTree(1);
        BinaryTree secondRightOne = new BinaryTree(3);
        firstLeft.setLeftChild(secondLeftOne);
        firstLeft.setRightChild(secondRightOne);
        secondLeftOne.setParents(firstLeft);
        secondRightOne.setParents(firstLeft);
        BinaryTree secondLeftTwo = new BinaryTree(5);
        BinaryTree secondRightTwo = new BinaryTree(7);
        firstRight.setLeftChild(secondLeftTwo);
        firstRight.setRightChild(secondRightTwo);
        secondLeftTwo.setParents(firstRight);
        secondRightTwo.setParents(firstRight);
        System.out.print("前序遍历:");
        top.preorderTraversal();
        System.out.print("\n中序遍历:");
        top.intermediateTraversal();
        System.out.print("\n后序遍历:");
        top.postorderTraversal();
        System.out.print("\n层次遍历:");
        top.levelTraversal();
        System.out.print("\n创建二叉树\n");
        BinaryTree test = BinaryTree.create();
        test.intermediateTraversal();
        System.out.print("\n创建二叉排序树\n");
        BinaryTree test2 = BinaryTree.createWithSort();
        test2.intermediateTraversal();
        System.out.print("\n创建平衡二叉排序树\n");
        BinaryTree test3 = BinaryTree.createWithSortAndBalance();
        test3.intermediateTraversal();
        System.out.print("\n");
    }
}
