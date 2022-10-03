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
import ru.practicum.shareit.constant.Constant;
import ru.practicum.shareit.requests.ItemRequestController;
import ru.practicum.shareit.requests.ItemRequestService;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {
    @MockBean
    private ItemRequestService itemRequestServiceStub;
    @Autowired
    ObjectMapper mapper;
    private MockMvc mvc;
    private ItemRequestDto itemRequestDto;

    @BeforeEach
    void setUp(WebApplicationContext wac) {
        mvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .build();
        UserDto userDto = new UserDto();
        userDto.setName("name");
        userDto.setEmail("email@email.com");
        userDto.setId(1L);
        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("desc");
        itemRequestDto.setId(1L);
    }

    @Test
    void whenCreateRequestThenIsOk() throws Exception {
        when(itemRequestServiceStub.createRequest(any(), any())).thenReturn(itemRequestDto);
        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .header(Constant.USER_ID_HEADER, "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto.getId()), Long.class));
    }

    @Test
    void whenGetAllRequestsByUserIdThenIsOk() throws Exception {
        List<ItemRequestDto> list = List.of(itemRequestDto);
        when(itemRequestServiceStub.getAllRequestsByUserId(any()))
                .thenReturn(list);
        mvc.perform(get("/requests")
                        .header(Constant.USER_ID_HEADER, "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1), Integer.class));
    }

    @Test
    void whenGetAllRequestsThenIsOk() throws Exception {
        List<ItemRequestDto> list = List.of(itemRequestDto);
        when(itemRequestServiceStub.getAllRequests(any(), any(), any()))
                .thenReturn(list);
        mvc.perform(get("/requests/all")
                        .header(Constant.USER_ID_HEADER, "1")
                        .param("from", "0")
                        .param("size", "2")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1), Integer.class));
    }

    @Test
    void whenGetItemRequestThenIsOk() throws Exception {
        when(itemRequestServiceStub.getItemRequest(any(), any()))
                .thenReturn(itemRequestDto);
        mvc.perform(get("/requests/1")
                        .header(Constant.USER_ID_HEADER, "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription()), String.class));
    }
}
