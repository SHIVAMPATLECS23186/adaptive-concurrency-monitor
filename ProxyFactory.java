package acm;

import java.lang.reflect.*;
import java.util.List;
import java.util.Arrays;

public class ProxyFactory {
    @SuppressWarnings("unchecked")
    public static <T> T createProxy(T target) {
        return (T) Proxy.newProxyInstance(
            target.getClass().getClassLoader(),
            target.getClass().getInterfaces(),
            (proxy, method, args) -> {
                Shard ann = method.getAnnotation(Shard.class);
                if (ann != null) {
                    List<String> shards = Arrays.asList(ann.value());
                    ShardManager.acquire(shards);
                    try {
                        return method.invoke(target, args);
                    } finally {
                        ShardManager.release(shards);
                    }
                }
                return method.invoke(target, args);
            }
        );
    }
}
