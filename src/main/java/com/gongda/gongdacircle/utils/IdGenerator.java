package com.gongda.gongdacircle.utils;

import java.util.concurrent.atomic.AtomicLong;

/**
 * ID生成器工具类
 * 简单的雪花算法实现
 */
public class IdGenerator {
    
    private static final long EPOCH = 1640995200000L; // 2022-01-01 00:00:00
    private static final long WORKER_ID_BITS = 5L;
    private static final long DATACENTER_ID_BITS = 5L;
    private static final long SEQUENCE_BITS = 12L;
    
    private static final long MAX_WORKER_ID = -1L ^ (-1L << WORKER_ID_BITS);
    private static final long MAX_DATACENTER_ID = -1L ^ (-1L << DATACENTER_ID_BITS);
    
    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;
    private static final long DATACENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
    private static final long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATACENTER_ID_BITS;
    private static final long SEQUENCE_MASK = -1L ^ (-1L << SEQUENCE_BITS);
    
    private static final long WORKER_ID = 1L;
    private static final long DATACENTER_ID = 1L;
    
    private static final AtomicLong SEQUENCE = new AtomicLong(0L);
    private static volatile long lastTimestamp = -1L;
    
    /**
     * 生成唯一ID
     */
    public static synchronized long generateId() {
        long timestamp = timeGen();
        
        if (timestamp < lastTimestamp) {
            throw new RuntimeException("Clock moved backwards. Refusing to generate id");
        }
        
        if (lastTimestamp == timestamp) {
            long sequence = SEQUENCE.incrementAndGet() & SEQUENCE_MASK;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            SEQUENCE.set(0L);
        }
        
        lastTimestamp = timestamp;
        
        return ((timestamp - EPOCH) << TIMESTAMP_LEFT_SHIFT) |
               (DATACENTER_ID << DATACENTER_ID_SHIFT) |
               (WORKER_ID << WORKER_ID_SHIFT) |
               SEQUENCE.get();
    }
    
    private static long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }
    
    private static long timeGen() {
        return System.currentTimeMillis();
    }
} 