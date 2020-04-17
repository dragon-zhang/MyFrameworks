package MySpringMVC.V2.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * 深克隆工具类
 *
 * @author SuccessZhang
 * @date 2020/1/7 17:38:31
 */
@SuppressWarnings("unused")
@Slf4j
public class CloneUtils {

    /**
     * 先用FastJson序列化对象，再反序列化对象，以此得到一个全新的对象,
     * 相较于{@link org.apache.http.client.utils.CloneUtils},
     * 这里传入的对象可以不实现{@link java.io.Serializable},
     * 相较于FastJson原生api,这里不用传入Class类型。
     *
     * @param object 需要深克隆的对象
     * @return 全新的对象(不单是对象本身全新, 该对象内的字段引用的对象也是全新)
     */
    @SuppressWarnings("unchecked")
    public static <T> T clone(@Nullable T object) {
        if (object == null) {
            return null;
        }
        try {
            Class<?> clazz = object.getClass();
            if (Collection.class.isAssignableFrom(clazz)) {
                return (T) CloneUtils.cloneCollection((Collection) (object));
            }
            if (Map.class.isAssignableFrom(clazz)) {
                return (T) CloneUtils.cloneMap((Map) (object));
            }
            return CloneUtils.cloneBean(object);
        } catch (Exception e) {
            return object;
        }
    }

    public static <T> T cloneBean(@Nonnull T object) {
        Type clazz = object.getClass();
        return JSONObject.parseObject(JSON.toJSONString(object), clazz);
    }

    @SuppressWarnings("unchecked")
    public static <T> Collection<T> cloneCollection(@Nullable Collection<T> collection) throws IllegalAccessException, InstantiationException {
        if (collection == null || collection.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        Collection<T> result = collection.getClass().newInstance();
        for (T object : collection) {
            //这里考虑到List<List<T>>或者更复杂的情况
            result.add(CloneUtils.clone(object));
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> cloneMap(@Nullable Map<K, V> map) throws IllegalAccessException, InstantiationException {
        if (map == null || map.isEmpty()) {
            return Collections.EMPTY_MAP;
        }
        Map<K, V> result = map.getClass().newInstance();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            //Key一般不会出现过于复杂的情况
            K key = CloneUtils.cloneBean(entry.getKey());
            //这里考虑到Map<String, Map<String, Object>>或者更复杂的情况
            V value = CloneUtils.clone(entry.getValue());
            result.put(key, value);
        }
        return result;
    }
}
