package ru.practicum.shareit.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.ValidationErrorResponse;
import ru.practicum.shareit.exception.Violation;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ErrorResponseTest {
    private ValidationErrorResponse validationErrorResponse;

    @Test
    void whenCreateValidationResponseThenGetIt() {
        validationErrorResponse = new ValidationErrorResponse();
        List<Violation> list = List.of(new Violation("name", "empty"));
        validationErrorResponse.setViolations(list);
        assertEquals(1, list.size());
        assertEquals("empty", list.get(0).getMessage());
    }

}