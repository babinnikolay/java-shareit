package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleIllegalArgumentException(IllegalArgumentException e) {
        final String bookingStateMessage = "No enum constant ru.practicum.shareit.booking.BookingState.";
        if (e.getMessage().contains(bookingStateMessage)) {
            String unknownState = e.getMessage().replace(bookingStateMessage, "");
            return new ErrorResponse(String.format("Unknown state: %s", unknownState));
        }
        return new ErrorResponse(e.getMessage());
    }
}
