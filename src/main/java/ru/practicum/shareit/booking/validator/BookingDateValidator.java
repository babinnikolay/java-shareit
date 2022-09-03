package ru.practicum.shareit.booking.validator;

import ru.practicum.shareit.booking.dto.BookingDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class BookingDateValidator implements ConstraintValidator<ValidBookingDates, BookingDto> {
    @Override
    public boolean isValid(BookingDto bookingDto, ConstraintValidatorContext constraintValidatorContext) {
        return bookingDto.getEnd().isAfter(LocalDateTime.now())
                && bookingDto.getEnd().isAfter(bookingDto.getStart())
                && bookingDto.getStart().isAfter(LocalDateTime.now());
    }
}
