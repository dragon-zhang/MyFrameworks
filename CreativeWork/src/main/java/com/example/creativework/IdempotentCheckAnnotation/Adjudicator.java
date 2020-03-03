package com.example.creativework.IdempotentCheckAnnotation;

/**
 * @author SuccessZhang
 */
public interface Adjudicator<T> {

    boolean judgeRedisResult(T result);

    boolean judgeMethodResult(T result);
}
