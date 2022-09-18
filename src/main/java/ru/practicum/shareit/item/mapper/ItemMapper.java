package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.Item;

import java.util.stream.Collectors;

public class ItemMapper {

    private ItemMapper() {
    }

    public static ItemDto toItemDto(Item item) {
        ItemDto itemDto =  new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.isAvailable());
        if (item.getRequest() != null) {
            itemDto.setRequestId(item.getRequest().getId());
        }
        itemDto.setLastBooking(BookingMapper.toNextBookingDto(item.getLastBooking()));
        itemDto.setNextBooking(BookingMapper.toNextBookingDto(item.getNextBooking()));
        itemDto.setComments(item.getComments()
                .stream()
                .map(CommentMapper::toDto)
                .collect(Collectors.toSet()));
        return itemDto;
    }

    public static Item toItem(ItemDto itemDto) {
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        return item;
    }
}
