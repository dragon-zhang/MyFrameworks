package DataStructureAndAlgorithm.tree.btree;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/**
 * @author SuccessZhang
 */
@SuppressWarnings("unused")
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

    private BalanceFactor balanceFactor;

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
            Util.queue.poll();
        } else {
            binaryTree = new BinaryTree(data);
            binaryTree.setParents(Util.queue.poll());
            //二叉树有2个节点
            Util.queue.offer(binaryTree);
            binaryTree.setLeftChild(create());
            Util.queue.offer(binaryTree);
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
     * 创建平衡二叉排序树
     */
    public static BinaryTree createWithSortAndBalance() {
        Scanner scanner = new Scanner(System.in);
        String data = scanner.nextLine();
        BinaryTree binaryTree;
        if ("#".equals(data)) {
            Util.root = null;
        } else {
            binaryTree = new BinaryTree(Integer.valueOf(data));
            binaryTree.setBalanceFactor(BalanceFactor.BALANCE);
            Util.root = binaryTree;
            Util.taller = true;
            while (!"#".equals(data)) {
                data = scanner.nextLine();
                if (!"#".equals(data)) {
                    insertChildWithBalance(Util.root, Integer.valueOf(data));
                }
            }
        }
        return Util.root;
    }

    /**
     * 创建平衡二叉排序树，插入子节点
     */
    public static void insertChildWithBalance(BinaryTree binaryTree, Integer data) {
        if (data < (int) binaryTree.getData()) {
            if (binaryTree.getLeftChild() == null) {
                BinaryTree leftChild = new BinaryTree(data);
                leftChild.setParents(binaryTree);
                leftChild.setBalanceFactor(BalanceFactor.LEFT_TALLER);
                binaryTree.setLeftChild(leftChild);
                binaryTree.setBalanceFactor(BalanceFactor.LEFT_TALLER);
                Util.taller = true;
            } else {
                insertChildWithBalance(binaryTree.getLeftChild(), data);
                if (Util.taller) {
                    switch (binaryTree.getBalanceFactor()) {
                        case BALANCE:
                            binaryTree.setBalanceFactor(BalanceFactor.LEFT_TALLER);
                            Util.taller = true;
                            break;
                        case LEFT_TALLER:
                            leftBalance(binaryTree);
                            Util.taller = false;
                            break;
                        case RIGHT_TALLER:
                            binaryTree.setBalanceFactor(BalanceFactor.BALANCE);
                            Util.taller = false;
                            break;
                        default:
                            break;
                    }
                }
            }
        } else {
            if (binaryTree.getRightChild() == null) {
                BinaryTree rightChild = new BinaryTree(data);
                rightChild.setParents(binaryTree);
                rightChild.setBalanceFactor(BalanceFactor.RIGHT_TALLER);
                binaryTree.setRightChild(rightChild);
                binaryTree.setBalanceFactor(BalanceFactor.RIGHT_TALLER);
                Util.taller = true;
            } else {
                insertChildWithBalance(binaryTree.getRightChild(), data);
                if (Util.taller) {
                    switch (binaryTree.getBalanceFactor()) {
                        case BALANCE:
                            binaryTree.setBalanceFactor(BalanceFactor.RIGHT_TALLER);
                            Util.taller = true;
                            break;
                        case LEFT_TALLER:
                            binaryTree.setBalanceFactor(BalanceFactor.BALANCE);
                            Util.taller = false;
                            break;
                        case RIGHT_TALLER:
                            rightBalance(binaryTree);
                            Util.taller = false;
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }

    /**
     * 左平衡
     */
    private static void leftBalance(BinaryTree binaryTree) {
        BinaryTree leftChild, leftChildRightChild;
        leftChild = binaryTree.getLeftChild();
        switch (leftChild.getBalanceFactor()) {
            case LEFT_TALLER:
                binaryTree.setBalanceFactor(BalanceFactor.BALANCE);
                leftChild.setBalanceFactor(BalanceFactor.BALANCE);
                rotateRight(binaryTree, leftChild, false);
                break;
            case RIGHT_TALLER:
                leftChildRightChild = leftChild.getRightChild();
                //修改BalanceFactor值
                switch (leftChildRightChild.getBalanceFactor()) {
                    case BALANCE:
                        binaryTree.setBalanceFactor(BalanceFactor.BALANCE);
                        leftChild.setBalanceFactor(BalanceFactor.BALANCE);
                        break;
                    case LEFT_TALLER:
                        binaryTree.setBalanceFactor(BalanceFactor.RIGHT_TALLER);
                        leftChild.setBalanceFactor(BalanceFactor.BALANCE);
                        break;
                    case RIGHT_TALLER:
                        binaryTree.setBalanceFactor(BalanceFactor.BALANCE);
                        leftChild.setBalanceFactor(BalanceFactor.LEFT_TALLER);
                        break;
                    default:
                        break;
                }
                leftChildRightChild.setBalanceFactor(BalanceFactor.BALANCE);
                rotateLeft(leftChild, leftChildRightChild, true);
                rotateRight(binaryTree, binaryTree.getLeftChild(), false);
                break;
            default:
                break;
        }
    }

    /**
     * 右平衡
     */
    private static void rightBalance(BinaryTree binaryTree) {
        BinaryTree rightChild, rightChildLeftChild;
        rightChild = binaryTree.getRightChild();
        switch (rightChild.getBalanceFactor()) {
            case LEFT_TALLER:
                rightChildLeftChild = rightChild.getLeftChild();
                //修改BalanceFactor值
                switch (rightChildLeftChild.getBalanceFactor()) {
                    case BALANCE:
                        binaryTree.setBalanceFactor(BalanceFactor.BALANCE);
                        rightChild.setBalanceFactor(BalanceFactor.BALANCE);
                        break;
                    case LEFT_TALLER:
                        binaryTree.setBalanceFactor(BalanceFactor.BALANCE);
                        rightChild.setBalanceFactor(BalanceFactor.LEFT_TALLER);
                        break;
                    case RIGHT_TALLER:
                        binaryTree.setBalanceFactor(BalanceFactor.RIGHT_TALLER);
                        rightChild.setBalanceFactor(BalanceFactor.BALANCE);
                        break;
                    default:
                        break;
                }
                rightChildLeftChild.setBalanceFactor(BalanceFactor.BALANCE);
                rotateRight(rightChild, rightChildLeftChild, true);
                rotateLeft(binaryTree, binaryTree.getRightChild(), false);
                break;
            case RIGHT_TALLER:
                binaryTree.setBalanceFactor(BalanceFactor.BALANCE);
                rightChild.setBalanceFactor(BalanceFactor.BALANCE);
                rotateLeft(binaryTree, rightChild, false);
                break;
            default:
                break;
        }
    }

    /**
     * 左旋转
     */
    private static void rotateLeft(BinaryTree binaryTree, BinaryTree rightChild, boolean half) {
        BinaryTree parent = binaryTree.getParents();
        binaryTree.setRightChild(rightChild.getLeftChild());
        rightChild.setLeftChild(binaryTree);
        rightChild.setParents(parent);
        if (parent != null) {
            if (half) {
                parent.setLeftChild(rightChild);
            } else {
                parent.setRightChild(rightChild);
            }
        } else {
            Util.root = rightChild;
        }
        binaryTree.setParents(rightChild);
    }

    /**
     * 右旋转
     */
    private static void rotateRight(BinaryTree binaryTree, BinaryTree leftChild, boolean half) {
        BinaryTree parent = binaryTree.getParents();
        binaryTree.setLeftChild(leftChild.getRightChild());
        leftChild.setRightChild(binaryTree);
        leftChild.setParents(parent);
        if (parent != null) {
            if (half) {
                parent.setRightChild(leftChild);
            } else {
                parent.setLeftChild(leftChild);
            }
        } else {
            Util.root = leftChild;
        }
        binaryTree.setParents(leftChild);
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

    public enum BalanceFactor {
        //平衡
        BALANCE,
        //左子树高了
        LEFT_TALLER,
        //右子树高了
        RIGHT_TALLER
    }

    public BalanceFactor getBalanceFactor() {
        return balanceFactor;
    }

    public void setBalanceFactor(BalanceFactor balanceFactor) {
        this.balanceFactor = balanceFactor;
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
