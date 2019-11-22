package prototype.deep;

import prototype.common.Man;

import java.io.*;

/**
 * @author SuccessZhang
 */
public class ManDeep extends Man implements Cloneable, Serializable {

    @Override
    public Object clone() {
        //如果克隆的是单例，直接return this;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(this);
            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bis);
            return ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
