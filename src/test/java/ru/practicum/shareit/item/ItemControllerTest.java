package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.comment.dto.requestDto.CommentCreateRequest;
import ru.practicum.shareit.comment.dto.responseDto.CommentResponse;
import ru.practicum.shareit.comment.service.CommentService;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.requestDto.ItemCreateRequest;
import ru.practicum.shareit.item.dto.requestDto.ItemUpdateRequest;
import ru.practicum.shareit.item.dto.responseDto.ItemResponse;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ItemController.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemControllerTest {
    @MockBean
    private ItemServiceImpl itemService;

    @MockBean
    private CommentService commentService;

    @Autowired
    MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    private ItemCreateRequest createRequest;
    private CommentCreateRequest commentCreateRequest;
    private ItemUpdateRequest updateRequest;
    private ItemResponse itemResponse;
    private CommentResponse commentResponse;


    @BeforeEach
    public void setup() {
        createRequest = new ItemCreateRequest("book", "cool book", true, 1L);
        itemResponse = new ItemResponse();
        itemResponse.setId(1L);
        updateRequest = new ItemUpdateRequest();
        commentCreateRequest = new CommentCreateRequest("wow");
        commentResponse = new CommentResponse();
    }

    @Test
    public void getItemFailParam() throws Exception {
        mockMvc.perform(get("/items/search")
                        .param("from", "-1")
                        .param("size", "-1")
                        .param("text", "book")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getItemFailHeader() throws Exception {
        mockMvc.perform(get("/items/99")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getItemNotFound() throws Exception {
        when(itemService.getById(any(), any()))
                .thenThrow(new NotFoundException("not found"));

        mockMvc.perform(get("/items/99")
                        .content(mapper.writeValueAsString(createRequest))
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void create() throws Exception {
        when(itemService.create(any(), any()))
                .thenReturn(itemResponse);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", "1")
                        .content(mapper.writeValueAsString(createRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemResponse.getId()));
    }

    @Test
    public void update() throws Exception {
        when(itemService.update(any(), any(), any()))
                .thenReturn(itemResponse);

        mockMvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", "1")
                        .content(mapper.writeValueAsString(updateRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemResponse.getId()));
    }

    @Test
    public void getItemsByOwner() throws Exception {
        when(itemService.getByOwner(any(), any(), any()))
                .thenReturn(List.of());

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", "1")
                        .param("from", "0")
                        .param("size", "10")
                        .content(mapper.writeValueAsString(updateRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getItemsBySearch() throws Exception {
        when(itemService.getBySearch(any(), any(), any()))
                .thenReturn(List.of());

        mockMvc.perform(get("/items/search")
                        .param("text", "book")
                        .param("from", "0")
                        .param("size", "10")
                        .content(mapper.writeValueAsString(updateRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getItemsById() throws Exception {
        when(itemService.getById(any(), any()))
                .thenReturn(itemResponse);

        mockMvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", "1")
                        .content(mapper.writeValueAsString(createRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemResponse.getId()));
    }

    @Test
    public void createComment() throws Exception {
        when(commentService.create(any(), any(), any()))
                .thenReturn(commentResponse);

        mockMvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", "1")
                        .content(mapper.writeValueAsString(commentCreateRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}