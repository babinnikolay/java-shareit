package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.constant.Constant;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.Min;
import java.util.Collection;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
@Validated
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(@RequestBody ItemDto itemDto,
                              @RequestHeader(Constant.USER_ID_HEADER) Long userId) throws NotFoundException {
        return itemService.createItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestBody ItemDto itemDto,
                              @PathVariable Long itemId,
                              @RequestHeader(Constant.USER_ID_HEADER) Long userId)
            throws NotFoundException {
        return itemService.updateItem(itemDto, itemId, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable Long itemId,
                           @RequestHeader(Constant.USER_ID_HEADER) Long userId) throws NotFoundException {
        return itemService.getItem(itemId, userId);
    }

    @GetMapping
    public Collection<ItemDto> getAllItems(@RequestHeader(Constant.USER_ID_HEADER) Long userId,
                                           @RequestParam(required = false, defaultValue = "0") @Min(0) Integer from,
                                           @RequestParam(required = false, defaultValue = "100")
                                               @Min(1) Integer size) throws NotFoundException {
        return itemService.getAllItemsByUser(userId, from, size);
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchItems(@RequestHeader(value = Constant.USER_ID_HEADER) Long userId,
                                           @RequestParam String text,
                                           @RequestParam(required = false, defaultValue = "0") @Min(0) Integer from,
                                           @RequestParam(required = false, defaultValue = "100")
                                               @Min(1) Integer size) throws NotFoundException {
        return itemService.searchItems(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@PathVariable Long itemId,
                                 @RequestBody CommentDto commentDto,
                                 @RequestHeader(Constant.USER_ID_HEADER) Long userId)
            throws NotFoundException, BadRequestException {
        return itemService.createComment(itemId, commentDto, userId);
    }
}
