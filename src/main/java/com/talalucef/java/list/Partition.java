package com.talalucef.java.list;

import java.security.InvalidParameterException;
import java.util.AbstractList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public final class Partition<T> extends AbstractList<List<T>> {

    private final List<T> list;
    private final int chunkSize;

    private Partition(List<T> list, int chunkSize) {
        this.list = new CopyOnWriteArrayList<>(list);
        this.chunkSize = chunkSize;
    }

    /**
     * Return list partitioned into chunkSize sublist
     * if the chunk number is greater than the list size, return same list as partition
     *
     * @param list      : List to be partitioned
     * @param chunkSize : Size of partition
     * @return Partition : List of sublist
     * @throws InvalidParameterException if chunkSize less than or equal to 0 or list is null
     */
    public static <T> Partition<T> ofSize(List<T> list, int chunkSize) {

        if (Objects.isNull(list)) throw new InvalidParameterException("list parameter cannot be null");

        if (chunkSize <= 0) throw new InvalidParameterException("chunkSize parameter should be a positive number");

        return new Partition<>(list, chunkSize);
    }

    @Override
    public List<T> get(int index) {

        int start = index * chunkSize;

        int end = Math.min(start + chunkSize, list.size());

        if (start > end) {
            throw new IndexOutOfBoundsException("Index " + index + " is out of the list range <0," + (size() - 1) + ">");
        }

        return new CopyOnWriteArrayList<>(list.subList(start, end));
    }

    @Override
    public int size() {
        return (int) Math.ceil((double) list.size() / (double) chunkSize);
    }
}