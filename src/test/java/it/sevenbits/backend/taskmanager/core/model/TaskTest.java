package it.sevenbits.backend.taskmanager.core.model;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.UUID;

import org.junit.Test;

import static org.junit.Assert.*;

public class TaskTest {
    @Test
    public void testTask() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        String id = UUID.randomUUID().toString();
        String text = "Create tests";
        String status = "inbox";
        String created = dateFormat.format(new Date());
        String owner = UUID.randomUUID().toString();
        Task task = new Task(id, text, status, created, created, owner);

        assertNotNull(task);
        assertEquals(id, task.getId());
        assertEquals(text, task.getText());
        assertEquals(status, task.getStatus());
        assertEquals(created, task.getCreatedAt());
        assertEquals(created, task.getUpdatedAt());
        assertEquals(owner, task.getOwner());
    }
}