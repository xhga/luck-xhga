package org.luck.xhga.test;

import lombok.SneakyThrows;
import lombok.val;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author GEEX1928
 * @date 2022/9/21
 */
public class StreamTest {
    public static void main(String[] args) {
        StreamTest.streamInt();
    }
    public static void streamInt() {
        val threads = IntStream.range(0, 10_000).mapToObj(index -> Thread.ofVirtual().unstarted(() -> {
            if (index == 0){
                System.out.println(Thread.currentThread());
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (index == 0) {
                System.out.println(Thread.currentThread());
            }
        })).toList();
        threads.forEach(Thread::start);
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
