package com.codesoom.assignment.task.handler.crud;

import com.codesoom.assignment.task.domain.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class CrudTaskHandlerTest {

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Task 객체로 변경 테스트")
    void testToTask() throws JsonProcessingException {
        String content = "{\"id\":\"1\", \"title\":\"작업하기\"}";

        Task task = objectMapper.readValue(content, Task.class);
        assertNotNull(task);

        System.out.println(task);
    }

    @Test
    @DisplayName("Json 으로 변경 테스트")
    void testToJson() throws IOException {
        Task task = Task.builder().id(10L).title("테스트하기").build();

        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, task);

        assertNotNull(outputStream);

        System.out.println(outputStream.toString());
    }
}