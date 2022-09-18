package ru.practicum.shareit.json;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NextBookingDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingDtoJsonTest {

    @Autowired
    private JacksonTester<BookingDto> json;

    @Test
    void testItemDto() throws IOException {

        LocalDateTime start = LocalDateTime.of(2022, 12, 1, 10, 10, 10);
        LocalDateTime end = LocalDateTime.of(2022, 12, 1, 12, 10, 10);

        UserDto userDto = new UserDto();
        userDto.setName("name");
        userDto.setEmail("email@email.com");
        userDto.setId(1L);

        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setAvailable(true);
        itemDto.setDescription("desc");
        itemDto.setName("name");

        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setItemId(2L);
        bookingDto.setStatus(BookingStatus.APPROVED);
        bookingDto.setBooker(userDto);
        bookingDto.setStart(start);
        bookingDto.setEnd(end);
        bookingDto.setItem(itemDto);

        JsonContent<BookingDto> result = json.write(bookingDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(2);
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("APPROVED");
        assertThat(result).extractingJsonPathNumberValue("$.booker.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.booker.name").isEqualTo("name");
        assertThat(result).extractingJsonPathStringValue("$.booker.email").isEqualTo("email@email.com");
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo("2022-12-01T10:10:10");
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo("2022-12-01T12:10:10");
        assertThat(result).extractingJsonPathStringValue("$.item.description").isEqualTo("desc");
        assertThat(result).extractingJsonPathStringValue("$.item.name").isEqualTo("name");
    }
}

