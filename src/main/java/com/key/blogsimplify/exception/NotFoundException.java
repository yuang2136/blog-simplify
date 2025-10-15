package com.key.blogsimplify.exception;

/**
 * 当资源不存在时抛出。
 */
public class NotFoundException extends BusinessException {
    public NotFoundException(String message) {
        super(message);
    }
}
