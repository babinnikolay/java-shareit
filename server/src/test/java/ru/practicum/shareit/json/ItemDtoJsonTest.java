package ru.practicum.shareit.json;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.NextBookingDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemDtoJsonTest {

    @Autowired
    private JacksonTester<ItemDto> json;

    @Test
    void testItemDto() throws IOException {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setAvailable(true);
        itemDto.setDescription("desc");
        itemDto.setName("name");

        CommentDto commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setText("text");
        commentDto.setCreated(LocalDateTime.of(2022, 12, 1, 10, 10, 10));
        commentDto.setAuthorName("author");
        Set<CommentDto> comments = Set.of(commentDto);
        itemDto.setComments(comments);

        NextBookingDto lastBooking = new NextBookingDto();
        lastBooking.setId(1L);
        lastBooking.setBookerId(2L);
        itemDto.setLastBooking(lastBooking);

        NextBookingDto nextBooking = new NextBookingDto();
        nextBooking.setId(1L);
        nextBooking.setBookerId(2L);
        itemDto.setNextBooking(nextBooking);

        JsonContent<ItemDto> result = json.write(itemDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("name");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("desc");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.comments.length()").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.comments[0].text").isEqualTo("text");
        assertThat(result).extractingJsonPathStringValue("$.comments[0].authorName")
                .isEqualTo("author");
        assertThat(result).extractingJsonPathStringValue("$.comments[0].created")
                .isEqualTo("2022-12-01T10:10:10");
        assertThat(result).extractingJsonPathNumberValue("$.nextBooking.bookerId").isEqualTo(2);
        assertThat(result).extractingJsonPathNumberValue("$.lastBooking.bookerId").isEqualTo(2);
    }
}

