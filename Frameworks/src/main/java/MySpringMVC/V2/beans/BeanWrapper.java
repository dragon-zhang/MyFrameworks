package MySpringMVC.V2.beans;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BeanWrapper {

    /**
     * 原生对象
     */
    private FactoryBean rootObject;

    public <T> BeanWrapper(FactoryBean<T> rootObject) {
        this.rootObject = rootObject;
    }

    /**
     * 代理包装之后的对象
     */
    private Object wrappedObject;

    /**
     * 代理包装之后的对象Class
     */
    private Class<?> wrappedClass;

}
