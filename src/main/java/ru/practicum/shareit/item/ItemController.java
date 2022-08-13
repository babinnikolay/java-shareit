package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import java.util.Collection;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ItemDto createItem(@RequestBody ItemDto itemDto,
                              @RequestHeader(USER_ID_HEADER) Long userId) throws UserNotFoundException {
        return itemService.createItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestBody ItemDto itemDto,
                              @PathVariable Long itemId,
                              @RequestHeader(USER_ID_HEADER) Long userId)
            throws UserNotFoundException, ItemNotFoundException {
        return itemService.updateItem(itemDto, itemId, userId);
    }

    @GetMapping("{itemId}")
    public ItemDto getItem(@PathVariable Long itemId,
                           @RequestHeader(USER_ID_HEADER) Long userId) throws ItemNotFoundException {
        return itemService.getItem(itemId, userId);
    }

    @GetMapping
    public Collection<ItemDto> getAllItems(@RequestHeader(USER_ID_HEADER) Long userId) throws UserNotFoundException {
        return itemService.getAllItemsByUser(userId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchItems(@RequestHeader(value = USER_ID_HEADER) Long userId,
                                           @RequestParam String text) throws UserNotFoundException {
        return itemService.searchItems(text);
    }
}
