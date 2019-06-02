package DesignPattern.template;

/**
 * @author SuccessZhang
 * 炒菜的类需要继承本类，重写相应的方法
 */
public abstract class AbstractCook {
    public final void cook() {
        //1.买菜
        this.buyVegetables();
        //2.洗菜
        this.washVegetables();
        //3.切菜
        this.cutUpVegetables();
        //4.生火
        this.lightFire();
        //5.炒菜
        this.stirFry();
        //6.装盘
        this.putOnPlate();
        //7.上桌
        this.putOnTable();
    }

    /**
     * 炒不同的菜需要买不同的菜
     */
    protected abstract void buyVegetables();

    /**
     * 不同的菜洗法不一样
     */
    protected abstract void washVegetables();

    /**
     * 不同的菜切法不一样
     */
    protected abstract void cutUpVegetables();

    private void lightFire() {
        System.out.println("生火完毕，准备开始炒菜");
    }

    /**
     * 不同的菜炒法不一样
     */
    protected abstract void stirFry();

    /**
     * 不同大厨的摆盘方式不一样
     */
    protected abstract void putOnPlate();

    private void putOnTable() {
        System.out.println("菜炒好了，也装好盘了，上桌");
    }
}
