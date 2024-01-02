package blossom.project.config.common.utils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author: ZhangBlossom
 * @date: 2024/1/2 16:21
 * @contact: QQ:4602197553
 * @contact: WX:qczjhczs0114
 * @blog: https://blog.csdn.net/Zhangsama1
 * @github: https://github.com/ZhangBlossom
 * @description:
 */
public class ReadWriteLockUtil {

    private static final ConcurrentHashMap<String, ReadWriteLock> lockMap = new ConcurrentHashMap<>();

    private static  ReadWriteLock getLock(String configId) {
        return lockMap.computeIfAbsent(configId, k -> new ReentrantReadWriteLock());
    }


    /**
     * 获取写锁 写锁内容自定义
     * @param key
     * @param configWriter
     */
    public static void withWriteLock(String key, Consumer<String> configWriter) {
        ReadWriteLock lock = getLock(key);
        lock.writeLock().lock();
        try {
            configWriter.accept(key);
        } finally {
            lock.writeLock().unlock();
        }
    }


    /**
     * 获取读锁 读锁内容自定义
     * @param key
     * @param configReader
     * @return
     * @param <T>
     */
    public static  <T> T withReadLock(String key, Supplier<T> configReader) {
        ReadWriteLock lock = getLock(key);
        lock.readLock().lock();
        try {
            return configReader.get();
        } finally {
            lock.readLock().unlock();
        }
    }

}
