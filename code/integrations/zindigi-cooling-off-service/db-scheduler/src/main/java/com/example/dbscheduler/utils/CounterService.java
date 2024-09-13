package com.example.dbscheduler.utils;

import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Service
public class CounterService {
    private final AtomicLong count = new AtomicLong(0L);

    public void increase() {
        count.incrementAndGet();
    }

    public long read() {
        return count.get();
    }
}
