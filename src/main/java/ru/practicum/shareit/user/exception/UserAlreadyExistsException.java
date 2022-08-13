package ru.practicum.shareit.user.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserAlreadyExistsException extends Exception {
    public UserAlreadyExistsException(String text) {
        super(text);
    }
}
