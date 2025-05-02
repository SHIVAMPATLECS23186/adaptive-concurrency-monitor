package acm;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

public class ShardManager {
    private static final ConcurrentHashMap<String, ReentrantLock> locks = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, AtomicInteger> contentionCounter = new ConcurrentHashMap<>();

    public static void acquire(List<String> shards) throws InterruptedException {
        Collections.sort(shards);
        List<ReentrantLock> acquired = new java.util.ArrayList<>();

        try {
            for (String shard : shards) {
                locks.putIfAbsent(shard, new ReentrantLock(true));
                contentionCounter.putIfAbsent(shard, new AtomicInteger(0));

                ReentrantLock lock = locks.get(shard);

                if (!lock.tryLock()) {
                    contentionCounter.get(shard).incrementAndGet();
                    lock.lockInterruptibly();
                }
                acquired.add(lock);
            }
        } catch (InterruptedException e) {
            for (ReentrantLock lock : acquired) {
                lock.unlock();
            }
            throw e;
        }
    }

    public static void release(List<String> shards) {
        List<String> reversed = new java.util.ArrayList<>(shards);
        Collections.reverse(reversed);
        for (String shard : reversed) {
            locks.get(shard).unlock();
        }
    }

    public static int getContention(String shard) {
        return contentionCounter.getOrDefault(shard, new AtomicInteger(0)).get();
    }
}
