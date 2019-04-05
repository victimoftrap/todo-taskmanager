package it.sevenbits.backend.taskmanager.web.model.requests;

import org.junit.Test;

import static org.junit.Assert.*;

public class GetTasksRequestTest {
    @Test
    public void testRequest() {
        String status = "inbox";
        String order = "asc";
        int page = 3;
        int size = 25;
        GetTasksRequest request = new GetTasksRequest(status, order, page, size);

        assertNotNull(request);
        assertEquals(status, request.getStatus());
        assertEquals(order, request.getOrder());
        assertEquals(page, request.getPage().intValue());
        assertEquals(size, request.getSize().intValue());
    }
}