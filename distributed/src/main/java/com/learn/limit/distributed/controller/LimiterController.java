package com.learn.limit.distributed.controller;

import com.learn.limit.distributed.annotation.Limit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: limit
 * @description:
 * @author: zjj
 * @create: 2019-12-19 18:57
 **/
@RestController
@RequestMapping(value = "/limit")
public class LimiterController {
    private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger();

    /* 意味著 100S 内最多允許訪問10次 */
    @Limit(key = "test", period = 100, count = 10)
    @GetMapping("/test")
    public int testLimiter() {
        return ATOMIC_INTEGER.incrementAndGet();
    }
}
