package com.github.listen_to_me.common.annotation;

import java.lang.annotation.*;

/**
 * 操作日志注解
 * 标注在 Controller 方法上，用于记录操作日志
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {

    /**
     * 操作模块
     */
    String module() default "";

    /**
     * 操作类型
     */
    String action() default "";

    /**
     * 操作描述
     */
    String description() default "";
}
