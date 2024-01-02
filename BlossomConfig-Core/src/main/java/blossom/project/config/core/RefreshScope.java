package blossom.project.config.core;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: ZhangBlossom
 * @date: 2023/12/31 11:50
 * @contact: QQ:4602197553
 * @contact: WX:qczjhczs0114
 * @blog: https://blog.csdn.net/Zhangsama1
 * @github: https://github.com/ZhangBlossom
 * RefreshScope类
 */
public class RefreshScope  implements Scope {

    private final Map<String, Object> scopeMap = new ConcurrentHashMap<>();

    private final Map<String, Runnable> destructionCallbacks = new ConcurrentHashMap<>();


    @Override
    public Object get(String beanName, ObjectFactory<?> objectFactory) {
        if (scopeMap.containsKey(beanName)){
            return scopeMap.get(beanName);
        }
        //不存在 那么就获取（获取的时候会在内部进行创建）
        Object object = objectFactory.getObject();
        scopeMap.put(beanName,object);
        return object;
    }

    @Override
    public Object remove(String beanName) {
        return scopeMap.remove(beanName);
    }

    @Override
    public void registerDestructionCallback(String s, Runnable runnable) {

    }

    @Override
    public Object resolveContextualObject(String s) {
        return null;
    }

    @Override
    public String getConversationId() {
        return null;
    }
}
