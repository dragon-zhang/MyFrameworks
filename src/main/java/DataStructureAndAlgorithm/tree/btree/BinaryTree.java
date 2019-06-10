package DataStructureAndAlgorithm.tree.btree;

/**
 * @author SuccessZhang
 */
public class BinaryTree {

    /**
     * 数据
     */
    private Object data;

    /**
     * 左子树
     */
    private BinaryTree leftChild;

    /**
     * 右子树
     */
    private BinaryTree rightChild;

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
}
