package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.requestdto.BookingCreateRequest;
import ru.practicum.shareit.booking.dto.responsedto.BookingResponse;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingDbRepository;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.exception.model.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemDbRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserDbRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class BookingService {
    private final BookingDbRepository bookingRepository;
    private final UserDbRepository userRepository;
    private final ItemDbRepository itemRepository;

    public BookingResponse create(BookingCreateRequest request, Long bookerId) {
        if (request.getEnd().isBefore(request.getStart())) {
            throw new ValidationException("End date cannot be is before start date");
        }
        User booker = userRepository.findById(bookerId).orElseThrow(() -> {
            throw new NotFoundException("User with id: " + bookerId + " is not found.");
        });
        Item item = itemRepository.findById(request.getItemId()).orElseThrow(() -> {
            throw new NotFoundException("Item with id: " + request.getItemId() + " is not found.");
        });
        if (item.getAvailable().equals(false)) {
            throw new ValidationException("This item cannot be booked");
        }
        if (item.getOwner().getId().equals(bookerId)) {
            throw new NotFoundException("Booker cannot be item owner");
        }
        Booking booking = Booking.builder()
                .start(request.getStart())
                .end(request.getEnd())
                .status(BookingStatus.WAITING)
                .booker(booker)
                .item(item)
                .build();

        return BookingMapper.fromBookingToResponse(bookingRepository.save(booking));
    }

    public BookingResponse updateStatus(Long bookingId, Long ownerId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> {
            throw new NotFoundException("Booking with id: " + bookingId + " is not found.");
        });
        userRepository.findById(ownerId).orElseThrow(() -> {
            throw new NotFoundException("User with id: " + ownerId + " is not found.");
        });
        if (!booking.getItem().getOwner().getId().equals(ownerId)) {
            throw new NotFoundException("User with id: " + ownerId + " can't update status");
        }
        if (booking.getStatus().equals(BookingStatus.APPROVED) && approved) {
            throw new ValidationException("Status already approved");
        }
        booking.setStatus(approved.equals(true) ? BookingStatus.APPROVED : BookingStatus.REJECTED);

        return BookingMapper.fromBookingToResponse(bookingRepository.save(booking));
    }

    public BookingResponse getById(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> {
            throw new NotFoundException("Booking with id: " + bookingId + " is not found.");
        });
        if (!booking.getBooker().getId().equals(userId) && !booking.getItem().getOwner().getId().equals(userId)) {
            throw new NotFoundException("Booking can only be viewed by item owner or booker");
        }

        return BookingMapper.fromBookingToResponse(booking);
    }

    public List<BookingResponse> getBookingsByBooker(Long bookerId, BookingState state, Long from, Integer size) {
        Pageable pageable = PageRequest.of((int) (from / size), size);
        User booker = userRepository.findById(bookerId).orElseThrow(() -> {
            throw new NotFoundException("User with id: " + bookerId + " is not found.");
        });

        switch (state) {
            case ALL: {
                return BookingMapper.fromBookingsToResponses(bookingRepository.findByBookerOrderByStartDesc(booker, pageable).getContent());
            }
            case CURRENT: {
                return BookingMapper.fromBookingsToResponses(bookingRepository
                        .findByBookerCurrent(booker, pageable).getContent());
            }
            case PAST: {
                return BookingMapper.fromBookingsToResponses(bookingRepository
                        .findByBookerPast(booker, pageable).getContent());
            }
            case FUTURE: {
                return BookingMapper.fromBookingsToResponses(bookingRepository
                        .findByBookerFuture(booker, pageable).getContent());
            }
            case WAITING: {
                return BookingMapper.fromBookingsToResponses(bookingRepository
                        .findByBookerStatus(booker, BookingStatus.WAITING, pageable).getContent());
            }
            case REJECTED: {
                return BookingMapper.fromBookingsToResponses(bookingRepository
                        .findByBookerStatus(booker, BookingStatus.REJECTED, pageable).getContent());
            }

            default:
                throw new NotFoundException("State: " + state + " is not found.");
        }
    }

    public List<BookingResponse> getBookingByOwner(Long ownerId, BookingState state, Long from, Integer size) {
        User owner = userRepository.findById(ownerId).orElseThrow(() -> {
            throw new NotFoundException("User with id: " + ownerId + " is not found.");
        });
        Pageable pageable = PageRequest.of((int) (from / size), size);

        switch (state) {
            case ALL: {
                return BookingMapper.fromBookingsToResponses(bookingRepository.findByItemOwnerOrderByStartDesc(owner, pageable).getContent());
            }
            case CURRENT: {
                return BookingMapper.fromBookingsToResponses(bookingRepository
                        .findByItemOwnerCurrent(owner, pageable).getContent());
            }
            case PAST: {
                return BookingMapper.fromBookingsToResponses(bookingRepository
                        .findByItemOwnerPast(owner, pageable).getContent());
            }
            case FUTURE: {
                return BookingMapper.fromBookingsToResponses(bookingRepository
                        .findByItemOwnerFuture(owner, pageable).getContent());
            }
            case WAITING: {
                return BookingMapper.fromBookingsToResponses(bookingRepository
                        .findByItemOwnerStatus(owner, BookingStatus.WAITING, pageable).getContent());
            }
            case REJECTED: {
                return BookingMapper.fromBookingsToResponses(bookingRepository
                        .findByItemOwnerStatus(owner, BookingStatus.REJECTED, pageable).getContent());
            }
            default:
                throw new NotFoundException("State: " + state + " is not found.");
        }
    }
}

