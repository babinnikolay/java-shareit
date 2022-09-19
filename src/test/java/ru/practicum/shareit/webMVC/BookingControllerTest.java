package ru.practicum.shareit.webMVC;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.constant.Constant;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {
    @MockBean
    private BookingService bookingServiceStub;
    @Autowired
    private ObjectMapper mapper;
    private MockMvc mvc;
    private BookingDto bookingDto;

    @BeforeEach
    void setUp(WebApplicationContext wac) {
        mvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .build();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.plusHours(1);
        LocalDateTime end = now.plusHours(2);
        bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setStart(start);
        bookingDto.setEnd(end);
    }

    @Test
    void whenCreateBookingThenIsOk() throws Exception {
        when(bookingServiceStub.createBooking(any(), any())).thenReturn(bookingDto);
        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDto))
                        .header(Constant.USER_ID_HEADER, "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class));
    }

    @Test
    void whenApproveBookingThenIsOk() throws Exception {
        bookingDto.setStatus(BookingStatus.APPROVED);
        when(bookingServiceStub.approveBooking(any(), any(), any()))
                .thenReturn(bookingDto);
        mvc.perform(patch("/bookings/1")
                        .param("approved", "true")
                        .header(Constant.USER_ID_HEADER, "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status",
                        is("APPROVED"), String.class));
    }

    @Test
    void whenGetBookingThenIsOk() throws Exception {
        when(bookingServiceStub.getBookingById(any(), any()))
                .thenReturn(bookingDto);
        mvc.perform(get("/bookings/1")
                        .header(Constant.USER_ID_HEADER, "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class));
    }

    @Test
    void whenGetAllBookingsByBookerThenIsOk() throws Exception {
        List<BookingDto> list = List.of(bookingDto);
        when(bookingServiceStub.getAllBookingsByBookerId(any(), any(), any(), any()))
                .thenReturn(list);
        mvc.perform(get("/bookings")
                        .header(Constant.USER_ID_HEADER, "1")
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "2")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1), Integer.class));
    }

    @Test
    void whenGetAllBookingsByOwnerThenIsOk() throws Exception {
        List<BookingDto> list = List.of(bookingDto);
        when(bookingServiceStub.getAllBookingsByOwnerId(any(), any(), any(), any()))
                .thenReturn(list);
        mvc.perform(get("/bookings/owner")
                        .header(Constant.USER_ID_HEADER, "1")
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "2")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1), Integer.class));
    }
}
