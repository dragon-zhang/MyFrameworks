package prototype.shallow;

import prototype.common.Man;

/**
 * @author SuccessZhang
 */
public class ManShallow extends Man implements Cloneable {

    @Override
    public Object clone() {
        //如果克隆的是单例，直接return this;
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
