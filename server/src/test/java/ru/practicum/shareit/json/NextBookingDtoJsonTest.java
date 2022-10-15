package ru.practicum.shareit.json;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.NextBookingDto;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class NextBookingDtoJsonTest {

    @Autowired
    private JacksonTester<NextBookingDto> json;

    @Test
    void testItemDto() throws IOException {
        NextBookingDto nextBookingDto = new NextBookingDto();
        nextBookingDto.setBookerId(1L);
        nextBookingDto.setId(1L);

        JsonContent<NextBookingDto> result = json.write(nextBookingDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.bookerId").isEqualTo(1);
    }
}

