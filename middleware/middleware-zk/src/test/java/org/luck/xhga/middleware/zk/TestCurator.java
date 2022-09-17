package org.luck.xhga.middleware.zk;

import org.apache.zookeeper.CreateMode;
import org.luck.xhga.middleware.zk.curator.ZkCuratorOperation;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author GEEX1928
 * @date 2021/6/16
 */
public class TestCurator {
    ZkCuratorOperation operation = ZkCuratorOperation.builder()
            .setSessionTimeoutMs(100000)
            .setConnectionTimeoutMs(100000)
            .setConnectString("192.168.52.111:2181").build();
    int syncCnt = 0;

    public static void main(String[] args){
        TestCurator testCurator = new TestCurator();
        int cnt = 100;
        CountDownLatch countDownLatch = new CountDownLatch(cnt);
        for (int i = 0; i < cnt; i++) {
            new Thread(()->{
                try {
                    countDownLatch.await();
                    testCurator.testSync();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
            countDownLatch.countDown();
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(testCurator.syncCnt);

        // testCurator.operation.close();
    }
    private void testSync() {
        operation.sync("/sync", 10, TimeUnit.SECONDS, () -> {
            syncCnt = syncCnt +1;
            System.out.println("该我上场了");
        });
    }
    private void testBaseOperation() {
        String path = "/aaaa";
        boolean exists = operation.exists(path, false);
        System.out.println("是否存在:" + exists);
        if (!exists) {
            System.out.println("创建结果:" + operation.create(CreateMode.EPHEMERAL, path, "testdata"));
        }
        System.out.println("子节点：" + operation.getChildren("/", false));
        System.out.println("更新数据:" + operation.update(path, "updata data"));
        System.out.println("读取数据:" + new String(operation.readData(path, null, false)));
        operation.watcher("/");
        try {
            Thread.sleep(1000);
            System.out.println("更新数据2:" + operation.update(path, "updata data2"));
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        operation.close();
    }
}
