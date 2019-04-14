package it.sevenbits.backend.taskmanager.web.model.meta;

import org.junit.Test;

import static org.junit.Assert.*;

public class GetTasksMetaDataTest {
    @Test
    public void testMetaData() {
        int total = 5;
        int page = 2;
        int size = 10;
        String next = "next/link";
        String prev = "prev/link";
        String first = "first/link";
        String last = "last/link";
        GetTasksMetaData metaData = new GetTasksMetaData(total, page, size, next, prev, first, last);

        assertNotNull(metaData);
        assertEquals(total, metaData.getTotal());
        assertEquals(page, metaData.getPage());
        assertEquals(size, metaData.getSize());
        assertEquals(next, metaData.getNext());
        assertEquals(prev, metaData.getPrev());
        assertEquals(first, metaData.getFirst());
        assertEquals(last, metaData.getLast());
    }
}