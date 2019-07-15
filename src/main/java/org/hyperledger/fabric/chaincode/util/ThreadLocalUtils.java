package org.hyperledger.fabric.chaincode.util;


import java.util.HashMap;
import java.util.Map;

/**
 * .
 */
public final class ThreadLocalUtils {
    private static final ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<Map<String, Object>>() {
        protected Map<String, Object> initialValue() {
            return new HashMap<String, Object>(4);
        }
    };

    public static Map<String, Object> getThreadLocal(){
        return threadLocal.get();
    }
    
    @SuppressWarnings("unchecked")
  public static <T> T get(String key) {
        Map<String, Object> map = threadLocal.get();
        return (T)map.get(key);
    }

    @SuppressWarnings("unchecked")
  public static <T> T get(String key,T defaultValue) {
      Map<String, Object> map = threadLocal.get();
        return map.get(key) == null ? defaultValue : (T)map.get(key);
    }

    public static void set(String key, Object value) {
        Map<String, Object> map = threadLocal.get();
        map.put(key, value);
    }

    public static void set(Map<String, Object> keyValueMap) {
        Map<String, Object> map = threadLocal.get();
        map.putAll(keyValueMap);
    }

    public static void remove() {
        threadLocal.remove();
    }

    @SuppressWarnings("unchecked")
  public static <T> T remove(String key) {
        Map<String, Object> map = threadLocal.get();
        return (T)map.remove(key);
    }
}