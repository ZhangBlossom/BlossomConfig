package blossom.project.config.core.scope;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

import java.util.HashMap;
import java.util.Map;

public class TenantScope implements Scope {

    // 存储不同租户的Bean实例
    private final ThreadLocal<Map<String, Object>> tenantBeans = new ThreadLocal<>();

    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        // 获取当前线程的租户特定的Bean Map
        Map<String, Object> scopedObjects = tenantBeans.get();
        if (scopedObjects == null) {
            scopedObjects = new HashMap<>();
            tenantBeans.set(scopedObjects);
        }

        // 获取或创建Bean实例
        return scopedObjects.computeIfAbsent(name, key -> objectFactory.getObject());
    }

    @Override
    public Object remove(String name) {
        Map<String, Object> scopedObjects = tenantBeans.get();
        return scopedObjects != null ? scopedObjects.remove(name) : null;
    }

    @Override
    public void registerDestructionCallback(String name, Runnable callback) {
        // 可以实现销毁逻辑，但在这个简单示例中不做处理
    }

    @Override
    public Object resolveContextualObject(String key) {
        // 不需要实现
        return null;
    }

    @Override
    public String getConversationId() {
        // 不需要实现
        return null;
    }
}
