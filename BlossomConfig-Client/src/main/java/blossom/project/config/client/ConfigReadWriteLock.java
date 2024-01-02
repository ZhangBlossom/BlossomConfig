package blossom.project.config.client;

/**
 * @author: ZhangBlossom
 * @date: 2024/1/2 20:30
 * @contact: QQ:4602197553
 * @contact: WX:qczjhczs0114
 * @blog: https://blog.csdn.net/Zhangsama1
 * @github: https://github.com/ZhangBlossom
 * @description:
 * 考虑类似于可重入锁一样的方法，我们可以用status的方式进行计数
 * 通过计数的方式来判断当前的锁状态
 */
public class ConfigReadWriteLock {

 /**
  * 用数字就可以判断读写锁状态了
  * 0表示无锁
  * 大于0表示读锁
  * -1表示写锁
  * 读写锁互斥
  */
 private int status = 0;


 /**
  * 获取一个读锁
  * @return
  */
 public synchronized boolean tryReadLock() {
        if (isWriteLocked()) {
            return false;
        } else {
            status++;
            return true;
        }
    }

    /**
     * 释放一个读锁
     */
    public synchronized void releaseReadLock() {
        status--;
    }

    /**
     * 只要当前存在读锁就失败
     */
    public synchronized boolean tryWriteLock() {
        if (!isFree()) {
            return false;
        } else {
            status = -1;
            return true;
        }
    }

    public synchronized void releaseWriteLock() {
        status = 0;
    }

    private boolean isWriteLocked() {
        return status < 0;
    }

    private boolean isFree() {
        return status == 0;
    }


}
