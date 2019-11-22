package template;

/**
 * @author SuccessZhang
 */
public class CabbageCook extends AbstractCook {
    @Override
    protected void buyVegetables() {
        System.out.println("买大白菜");
    }

    @Override
    protected void washVegetables() {
        System.out.println("洗大白菜");
    }

    @Override
    protected void cutUpVegetables() {
        System.out.println("切大白菜");
    }

    @Override
    protected void stirFry() {
        System.out.println("一号大厨的大白菜炒法");
    }

    @Override
    protected void putOnPlate() {
        System.out.println("一号大厨的摆盘法");
    }
}
