package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
public class UserDto {
    private Long id;
    @Valid
    @NotBlank(message = "Name is required")
    private String name;
    @Valid
    @Email
    @NotEmpty
    private String email;
}
