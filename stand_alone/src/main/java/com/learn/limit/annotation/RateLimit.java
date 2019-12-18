package com.learn.limit.annotation;

import java.lang.annotation.*;

/**
 * @author jingjing.zhang
 */
@Inherited
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {
    /* 默认每秒放入桶中的token */
    double limitNum() default 20;
}
