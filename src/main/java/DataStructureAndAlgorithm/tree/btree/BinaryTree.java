package DataStructureAndAlgorithm.tree.btree;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/**
 * @author SuccessZhang
 */
public class BinaryTree {

    /**
     * 数据
     */
    private Object data;

    /**
     * 双亲节点
     */
    private BinaryTree parents;

    /**
     * 左子树
     */
    private BinaryTree leftChild;

    /**
     * 右子树
     */
    private BinaryTree rightChild;

    private static Queue<BinaryTree> parent = new LinkedList<>();

    public BinaryTree() {
    }

    public BinaryTree(Object data) {
        this.data = data;
    }

    public BinaryTree(Object data, BinaryTree leftChild, BinaryTree rightChild) {
        this.data = data;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    /**
     * 前序顺序创建二叉树
     */
    public static BinaryTree create() {
        Scanner scanner = new Scanner(System.in);
        String data = scanner.nextLine();
        BinaryTree binaryTree;
        if ("#".equals(data)) {
            binaryTree = null;
            parent.poll();
        } else {
            binaryTree = new BinaryTree(data);
            binaryTree.setParents(parent.poll());
            //二叉树有2个节点
            parent.offer(binaryTree);
            binaryTree.setLeftChild(create());
            parent.offer(binaryTree);
            binaryTree.setRightChild(create());
        }
        return binaryTree;
    }

    /**
     * 创建二叉排序树
     */
    public static BinaryTree createWithSort() {
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
    public static void insertChild(BinaryTree binaryTree, int data) {
        if (data < (int) binaryTree.getData()) {
            if (binaryTree.getLeftChild() == null) {
                BinaryTree leftChild = new BinaryTree(data);
                leftChild.setParents(binaryTree);
                binaryTree.setLeftChild(leftChild);
            } else {
                insertChild(binaryTree.getLeftChild(), data);
            }
        } else {
            if (binaryTree.getRightChild() == null) {
                BinaryTree rightChild = new BinaryTree(data);
                rightChild.setParents(binaryTree);
                binaryTree.setRightChild(rightChild);
            } else {
                insertChild(binaryTree.getRightChild(), data);
            }
        }
    }

    /**
     * 前序遍历
     */
    public void preorderTraversal() {
        preorderTraversal(this);
    }

    /**
     * 前序遍历核心算法
     */
    private void preorderTraversal(BinaryTree binaryTree) {
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
    public void intermediateTraversal() {
        intermediateTraversal(this);
    }

    /**
     * 中序遍历核心算法
     */
    private void intermediateTraversal(BinaryTree binaryTree) {
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
    public void postorderTraversal() {
        postorderTraversal(this);
    }

    /**
     * 后序遍历核心算法
     */
    private void postorderTraversal(BinaryTree binaryTree) {
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
    public void levelTraversal() {
        Queue<BinaryTree> children = new LinkedList<>();
        children.offer(this);
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

    /**
     * 根据传入的data查找数据
     *
     * @param data 传入的key
     * @return 查找成功返回true，找不到返回false
     */
    public BinaryTree findData(Integer data) {
        return findData(this, data);
    }

    /**
     * 根据传入的data查找数据核心算法
     *
     * @param binaryTree 二叉树本体
     * @param data       传入的key
     * @return 查找成功返回true，找不到返回false
     */
    private BinaryTree findData(BinaryTree binaryTree, Integer data) {
        if (data == null || binaryTree == null) {
            return null;
        } else if (data < (int) binaryTree.getData()) {
            return findData(binaryTree.getLeftChild(), data);
        } else if (data > (int) binaryTree.getData()) {
            return findData(binaryTree.getRightChild(), data);
        } else {
            return binaryTree;
        }
    }

    /**
     * 删除节点，并保证删除节点后仍保持有序二叉排序树
     */
    public boolean deleteData(Integer data) {
        return deleteData(this, data);
    }

    private boolean deleteData(BinaryTree binaryTree, Integer data) {
        if (data == null || binaryTree == null || findData(data) == null) {
            return false;
        } else if (data < (int) binaryTree.getData()) {
            return deleteData(binaryTree.getLeftChild(), data);
        } else if (data > (int) binaryTree.getData()) {
            return deleteData(binaryTree.getRightChild(), data);
        } else {
            return doDelete(binaryTree);
        }
    }

    private boolean doDelete(BinaryTree binaryTree) {
        BinaryTree parents = binaryTree.getParents();
        if (binaryTree.getRightChild() == null && binaryTree.getLeftChild() == null) {
            //叶子节点
            if ((int) binaryTree.getData() < (int) parents.getData()) {
                //左节点
                parents.setLeftChild(null);
            } else {
                //右节点
                parents.setRightChild(null);
            }
        } else if (binaryTree.getRightChild() == null) {
            parents.setRightChild(binaryTree.getLeftChild());
        } else if (binaryTree.getLeftChild() == null) {
            parents.setLeftChild(binaryTree.getRightChild());
        } else {
            //将找到节点的数据域设置为该节点左边所有节点的最大值
            BinaryTree leftMax = binaryTree.getLeftChild();
            BinaryTree left = leftMax.getRightChild();
            while (left != null) {
                leftMax = leftMax.getRightChild();
                left = left.getRightChild();
            }
            binaryTree.setData(leftMax.getData());
            //释放无用资源
            BinaryTree leftMaxParent = leftMax.getParents();
            if (binaryTree.getLeftChild().equals(leftMax)) {
                leftMaxParent.setLeftChild(leftMax.getLeftChild());
            } else {
                leftMaxParent.setRightChild(leftMax.getLeftChild());
            }
        }
        return true;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public BinaryTree getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(BinaryTree leftChild) {
        this.leftChild = leftChild;
    }

    public BinaryTree getRightChild() {
        return rightChild;
    }

    public void setRightChild(BinaryTree rightChild) {
        this.rightChild = rightChild;
    }

    public BinaryTree getParents() {
        return parents;
    }

    public void setParents(BinaryTree parents) {
        this.parents = parents;
    }
}
