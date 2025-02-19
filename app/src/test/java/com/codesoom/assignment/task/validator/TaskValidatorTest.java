package com.codesoom.assignment.task.validator;

import com.codesoom.assignment.task.validator.TaskValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TaskValidatorTest {

    TaskValidator taskValidator = new TaskValidator();

    @Test
    @DisplayName("Task ID Vaild 체크")
    void testValidTaskId() {
        String testNumber1 = "-1";
        String testNumber2 = "abd";
        String testNumber3 = "3";

        assertFalse(taskValidator.validTaskId(testNumber1));
        assertFalse(taskValidator.validTaskId(testNumber2));
        assertTrue(taskValidator.validTaskId(testNumber3));
    }

    @Test
    @DisplayName("Body 값 Vaild 체크")
    void testValidBody() {
        String testString1 = "";
        String testString2 = "{\"id\":\"1\", \"title\":\"작업하기\"}";

        assertFalse(taskValidator.vaildBody(testString1));
        assertTrue(taskValidator.vaildBody(testString2));
    }
}