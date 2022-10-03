package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.validator.ValidBookingDates;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ValidBookingDates
public class BookingDto {
    private Long id;
    @Valid
    @NotNull(message = "Start date is required")
    private LocalDateTime start;
    @Valid
    @NotNull(message = "End date is required")
    private LocalDateTime end;
    private BookingStatus status;
    @Valid
    @NotNull(message = "Item id is required")
    private Long itemId;
    private UserDto booker;
    private ItemDto item;
}
