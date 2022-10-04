package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.dto.NextBookingDto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ItemDto {
    private Long id;
    @Valid
    @NotBlank(message = "Name is required")
    private String name;
    @Valid
    @NotBlank(message = "Description is required")
    private String description;
    @Valid
    @NotNull
    private Boolean available;
    private Long requestId;
    private NextBookingDto lastBooking;
    private NextBookingDto nextBooking;
    private Set<CommentDto> comments;

    public ItemDto(Long id, String name, String description, Boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
    }
}
