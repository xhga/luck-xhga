package org.luck.xhga.middleware.zk.curator;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.GetDataBuilder;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author hwb
 * @date 2021/6/16
 */
@Slf4j
public class ZkCuratorOperation {
    private String connectString;
    private String namespace;
    private int sessionTimeoutMs;
    private int connectionTimeoutMs;
    private RetryPolicy retryPolicy;

    private final CuratorFramework curatorFramework;
    private static final WeakHashMap<String, InterProcessMutex> lockMap = new WeakHashMap<>();

    private ZkCuratorOperation(String connectString, String namespace, int sessionTimeoutMs, int connectionTimeoutMs, RetryPolicy retryPolicy) {
        this.connectString = connectString;
        this.namespace = namespace;
        this.sessionTimeoutMs = sessionTimeoutMs;
        this.connectionTimeoutMs = connectionTimeoutMs;
        this.retryPolicy = retryPolicy;
        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(StringUtils.isBlank(connectString)?"127.0.0.1:2181":connectString)
                .sessionTimeoutMs(sessionTimeoutMs == 0?10000:sessionTimeoutMs)
                .connectionTimeoutMs(connectionTimeoutMs == 0?10000:connectionTimeoutMs)
                .retryPolicy(retryPolicy != null?retryPolicy:new ExponentialBackoffRetry(1000, 5))
                .namespace(namespace)
                .build();
        curatorFramework.start();
    }

    public static ZkCuratorOperation.Builder builder() {
        return new ZkCuratorOperation.Builder();
    }
    public static class Builder {
        private String connectString;
        private String namespace;
        private int sessionTimeoutMs;
        private int connectionTimeoutMs;
        private RetryPolicy retryPolicy;
        public ZkCuratorOperation build() {
            return new ZkCuratorOperation(connectString, namespace, sessionTimeoutMs, connectionTimeoutMs, retryPolicy);
        }
        public Builder setConnectString(String connectString) {
            this.connectString = connectString;
            return this;
        }

        public Builder setNamespace(String namespace) {
            this.namespace = namespace;
            return this;
        }

        public Builder setSessionTimeoutMs(int sessionTimeoutMs) {
            this.sessionTimeoutMs = sessionTimeoutMs;
            return this;
        }

        public Builder setConnectionTimeoutMs(int connectionTimeoutMs) {
            this.connectionTimeoutMs = connectionTimeoutMs;
            return this;
        }

        public Builder setRetryPolicy(RetryPolicy retryPolicy) {
            this.retryPolicy = retryPolicy;
            return this;
        }
    }

    /**
     * 创建节点
     * @param createMode 节点类型
     * @param path  节点路径
     * @param data  节点数据
     * @return
     */
    @SneakyThrows
    public String create(CreateMode createMode, String path, String data) {
        return curatorFramework.create().withMode(createMode).forPath(path, data.getBytes());
    }

    /**
     * 更新节点
     * @param path  节点路径
     * @param data  节点数据
     * @return
     */
    @SneakyThrows
    public Stat update(String path, String data) {
        return curatorFramework.setData().forPath(path, data.getBytes());
    }

    /**
     * 删除节点
     * @param path 路径
     */
    @SneakyThrows
    public void delete(String path) {
        curatorFramework.delete().guaranteed().forPath(path);
    }
    @SneakyThrows
    public void delete(String path, int version) {
        curatorFramework.delete().guaranteed().withVersion(version).forPath(path);
    }
    /**
     * 删除节点-递归
     * @param path 路径
     */
    @SneakyThrows
    public void deleteRecursion(String path) {
        curatorFramework.delete().guaranteed().deletingChildrenIfNeeded().forPath(path);
    }
    @SneakyThrows
    public void deleteRecursion(String path, int version) {
        curatorFramework.delete().guaranteed().deletingChildrenIfNeeded().withVersion(version).forPath(path);
    }

    /**
     * 是否存在节点
     * @param path
     * @param watch
     * @return
     */
    @SneakyThrows
    public boolean exists(String path, boolean watch) {
        return watch ? (curatorFramework.checkExists().watched().forPath(path) != null) : (curatorFramework.checkExists().forPath(path) != null);
    }

    /**
     * 获取子节点
     * @param path
     * @param watch
     * @return
     */
    @SneakyThrows
    public List<String> getChildren(String path, boolean watch) {
        return watch ? curatorFramework.getChildren().watched().forPath(path) : curatorFramework.getChildren().forPath(path);
    }

    /**
     * 读取数据
     * @param path
     * @param stat
     * @param watch
     * @return
     */
    @SneakyThrows
    public byte[] readData(String path, Stat stat, boolean watch) {
        GetDataBuilder builder = curatorFramework.getData();
        if (stat != null ) {
            builder.storingStatIn(stat);
        }
        if (watch){
            builder.watched();
        }
        return builder.forPath(path);
    }

    /**
     * 关闭
     */
    public void close(){
        CloseableUtils.closeQuietly(curatorFramework);
    }

    /**
     * 监听
     * @param path 路径
     */
    @SneakyThrows
    public void watcher(String path) {
        CuratorCache curatorCache = CuratorCache.builder(curatorFramework, path).build();
        CuratorCacheListener listener = CuratorCacheListener.builder()
                .forCreates(node -> log.info("监听-创建节点: {}", node))
                .forChanges((oldNode, node) -> log.info("监听-节点改变: Old：{} New: {}", oldNode, node))
                .forDeletes(oldNode -> log.info("监听-节点删除. Old value: {}", oldNode))
                .forInitialized(() -> log.info("监听-Cache initialized"))
                .build();
        curatorCache.listenable().addListener(listener);
        curatorCache.start();
    }

    /**
     * 获取锁
     * @param path 路径
     * @param time 时间
     * @param unit 单位
     * @return 执行成功 true, 否则 false
     */
    public boolean sync(String path, long time, TimeUnit unit, ExecuteLockTask task) {
        InterProcessMutex lock;
        if (lockMap.containsKey(path)) {
            lock = lockMap.get(path);
        } else {
            lock = new InterProcessMutex(curatorFramework, path);
            lockMap.put(path, lock);
        }
        boolean rst = false;
        try {
            if (lock.acquire(time, unit)) {
                log.info("获取到锁. {}", path);
                rst = true;
                task.execute();
            } else {
                log.info("未获取到锁. {}", path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rst) {
                try {
                    lock.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return rst;
    }
}
