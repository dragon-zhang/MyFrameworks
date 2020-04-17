package MySpringMVC.V2.beans;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BeanWrapper {

    /**
     * 原生对象
     */
    private Object rootObject;

    /**
     * 原生对象Class
     */
    private Class<?> rootClass;

    /**
     * 代理包装之后的对象
     */
    private Object wrappedObject;

    /**
     * 代理包装之后的对象Class
     */
    private Class<?> wrappedClass;

    public BeanWrapper(Object rootObject) {
        this.rootObject = rootObject;
        this.rootClass = rootObject.getClass();
    }

    public BeanWrapper(Object rootObject, Class<?> rootClass) {
        this.rootObject = rootObject;
        this.rootClass = rootClass;
    }
}
