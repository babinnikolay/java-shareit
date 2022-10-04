package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {

    private final RequestsClient requestsClient;

    @PostMapping
    public ResponseEntity<Object> createRequest(@Valid @RequestBody ItemRequestDto requestDto,
                                                @RequestHeader("X-Sharer-User-Id") Long userId) {
        return requestsClient.createRequest(requestDto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllRequestsByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return requestsClient.getAllRequestsByUserId(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                    @RequestParam(required = false, defaultValue = "0") @Min(0) Integer from,
                                                    @RequestParam(required = false, defaultValue = "100")
                                                    @Min(1) Integer size) {
        return requestsClient.getAllRequests(userId, from, size);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemRequest(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                         @PathVariable Long itemId) {
        return requestsClient.getItemRequest(ownerId, itemId);
    }
}
