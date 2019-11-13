package style.kiwi.consoled;

import java.util.HashMap;
import java.util.Map;

public class ExecutorContext {
    private Map<Class<?>, Object> instanceMap = new HashMap<>();

    public <T> void putInstance(T t) {
        instanceMap.put(t.getClass(), t);
    }

    public <T> T getInstance(Class<T> key) {
        return key.cast(instanceMap.get(key));
    }
}
