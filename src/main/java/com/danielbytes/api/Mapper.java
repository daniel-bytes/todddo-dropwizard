package com.danielbytes.api;

/**
 * Simple interface for mapping between two data types.
 * @param <TFrom> The type to map from
 * @param <TTo> The type to map to
 */
public interface Mapper<TFrom, TTo> {
    TTo map(TFrom from);
}
