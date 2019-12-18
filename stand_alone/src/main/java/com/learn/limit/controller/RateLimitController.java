package com.learn.limit.controller;

import com.learn.limit.annotation.RateLimit;
import com.learn.limit.constant.RespType;
import com.learn.limit.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: limit
 * @description:
 * @author: zjj
 * @create: 2019-12-18 12:42
 **/
@RestController
@RequestMapping(value = "/rateLimit")
public class RateLimitController {
    private static final Logger log = LoggerFactory.getLogger(RateLimitController.class);

    @RateLimit(limitNum = 150.0)
    @RequestMapping(value = "/getResults", method = RequestMethod.GET)
    public Result getResults() {
        log.info("调用了方法getResults");
        return new Result(RespType.SUCCESS);
    }

    @RateLimit(limitNum = 30.0)
    @RequestMapping(value = "/getResultTwo", method = RequestMethod.GET)
    public Result getResultTwo() {
        log.info("调用了方法getResultTwo");
        return new Result(RespType.SUCCESS);
    }
}
