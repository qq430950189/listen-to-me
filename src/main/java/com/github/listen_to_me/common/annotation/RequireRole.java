package com.github.listen_to_me.common.annotation;

import java.lang.annotation.*;

/**
 * 角色校验注解
 * 标注在 Controller 方法上，用于校验用户是否具有指定角色
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequireRole {

    /**
     * 需要的角色编码数组
     * 用户只要满足其中一个角色即可通过校验
     */
    String[] value() default {};

    /**
     * 是否需要满足所有角色
     * 默认为 false，即满足任意一个角色即可
     */
    boolean requireAll() default false;
}
