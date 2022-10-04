package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
public class CommentDto {
    private Long id;
    @Valid
    @NotBlank(message = "Text is required")
    private String text;
    private LocalDateTime created;
    private String authorName;
}
