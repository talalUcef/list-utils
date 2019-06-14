package com.talalucef.java.list;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class PartitionTest {

    private static final Logger LOG = LoggerFactory.getLogger(PartitionTest.class);

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();


    @Test
    public void testPartitionInNormalCases() {

        final List<Integer> numbers = IntStream
                .rangeClosed(1, 100)
                .boxed()
                .collect(Collectors.toList());

        numbers.forEach(i -> assertTrue(isWellPartitioned(numbers, i)));
    }

    @Test
    public void testPartitionIfChunkNumberGreaterThanListSize() {
        final List<Integer> numbers = List.of(1, 2);
        final Partition<Integer> lists = Partition.ofSize(numbers, 100);
        assertEquals(getPartitionsNumber(numbers, 100), lists.size());
        assertEquals(lists.get(0).size(), numbers.size());
    }

    @Test
    public void testPartitionWithNullList() {
        exceptionRule.expect(InvalidParameterException.class);
        exceptionRule.expectMessage("list parameter cannot be null");
        Partition.ofSize(null, 100);
    }

    @Test
    public void testPartitionWithChunkSizeIsNegativeOrZero() {
        exceptionRule.expect(InvalidParameterException.class);
        exceptionRule.expectMessage("chunkSize parameter should be a positive number");
        Partition.ofSize(List.of(1), 0);
        Partition.ofSize(List.of(1), -1);
    }

    private <T> boolean isWellPartitioned(final List<T> list, final int chunkSize) {

        final Partition<T> lists = Partition.ofSize(list, chunkSize);

        final int partitionSNumber = getPartitionsNumber(list, chunkSize);

        LOG.debug("List of {} elements is partitioned into {} partition of chunk {}",
                list.size(), partitionSNumber, chunkSize);

        return partitionSNumber == lists.size();
    }

    private <T> int getPartitionsNumber(final List<T> list, final int chunkSize) {
        return (int) Math.ceil((double) list.size() / (double) chunkSize);
    }
}