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
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {
    @MockBean
    private ItemService itemServiceStub;
    @Autowired
    ObjectMapper mapper;
    private MockMvc mvc;
    private ItemDto itemDto;
    private CommentDto commentDto;

    @BeforeEach
    void setUp(WebApplicationContext wac) {
        mvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .build();
        itemDto = new ItemDto();
        itemDto.setName("name");
        itemDto.setDescription("desc");
        itemDto.setAvailable(true);

        commentDto = new CommentDto();
        commentDto.setText("text");
        commentDto.setId(1L);
    }

    @Test
    void whenCreateItemThenIsOk() throws Exception {
        itemDto.setId(1L);
        when(itemServiceStub.createItem(any(), any())).thenReturn(itemDto);
        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .header(Constant.USER_ID_HEADER, "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class));
    }

    @Test
    void whenUpdateItemThenIsOk() throws Exception {
        itemDto.setId(1L);
        itemDto.setName("newName");
        when(itemServiceStub.updateItem(any(), any(), any()))
                .thenReturn(itemDto);
        mvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(itemDto))
                        .header(Constant.USER_ID_HEADER, "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(itemDto.getName()), String.class));
    }

    @Test
    void whenGetItemThenIsOk() throws Exception {
        itemDto.setId(1L);
        when(itemServiceStub.getItem(any(), any()))
                .thenReturn(itemDto);
        mvc.perform(get("/items/1")
                        .header(Constant.USER_ID_HEADER, "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(itemDto.getName()), String.class));
    }

    @Test
    void whenGetAllItemsThenIsOk() throws Exception {
        itemDto.setId(1L);
        List<ItemDto> list = List.of(itemDto);
        when(itemServiceStub.getAllItemsByUser(any(), any(), any()))
                .thenReturn(list);
        mvc.perform(get("/items")
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
    void whenSearchItemsThenIsOk() throws Exception {
        itemDto.setId(1L);
        List<ItemDto> list = List.of(itemDto);
        when(itemServiceStub.searchItems(any(), any(), any()))
                .thenReturn(list);
        mvc.perform(get("/items/search")
                        .header(Constant.USER_ID_HEADER, "1")
                        .param("text", "text")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1), Integer.class));
    }

    @Test
    void whenCreateCommentThenIsOk() throws Exception {
        itemDto.setId(1L);
        when(itemServiceStub.createComment(any(), any(), any()))
                .thenReturn(commentDto);
        mvc.perform(post("/items/1/comment")
                        .header(Constant.USER_ID_HEADER, "1")
                        .content(mapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text", is(commentDto.getText()), String.class));
    }

}
