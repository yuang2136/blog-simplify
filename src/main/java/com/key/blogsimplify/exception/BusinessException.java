package com.key.blogsimplify.exception;

/**
 * 业务异常基类，使用运行时异常方便在事务中抛出后回滚。
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
