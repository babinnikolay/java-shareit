package ru.practicum.shareit.requests;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.constant.Constant;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.requests.dto.ItemRequestDto;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto createRequest(@Valid @RequestBody ItemRequestDto requestDto,
                                        @RequestHeader(Constant.USER_ID_HEADER) Long userId)
            throws NotFoundException {
        return itemRequestService.createRequest(requestDto, userId);
    }

    @GetMapping
    public Collection<ItemRequestDto> getAllRequestsByUserId(@RequestHeader(Constant.USER_ID_HEADER) Long userId)
            throws NotFoundException {
        return itemRequestService.getAllRequestsByUserId(userId);
    }

    @GetMapping("/all")
    public Collection<ItemRequestDto> getAllRequest(@RequestHeader(Constant.USER_ID_HEADER) Long userId,
                                                    @RequestParam Integer from,
                                                    @RequestParam Integer size) throws NotFoundException {
        return itemRequestService.getAllRequests(userId, from, size);
    }

    @GetMapping("/{itemId}")
    public ItemRequestDto getItemRequest(@RequestHeader(Constant.USER_ID_HEADER) Long ownerId,
                                         @PathVariable Long itemId) throws NotFoundException {
        return itemRequestService.getItemRequest(ownerId, itemId);
    }
}
