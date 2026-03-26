package com.github.listen_to_me.common.exception;

/**
 * 业务异常类
 */
public class BizException extends BaseException {

    private static final long serialVersionUID = 1L;

    public BizException(String message) {
        super(message);
    }

    public BizException(Integer code, String message) {
        super(code, message);
    }

    public BizException(Integer code, String message, Throwable cause) {
        super(code, message, cause);
    }

    /**
     * 快速创建业务异常
     */
    public static BizException of(String message) {
        return new BizException(message);
    }

    /**
     * 快速创建业务异常（带错误码）
     */
    public static BizException of(Integer code, String message) {
        return new BizException(code, message);
    }
}
