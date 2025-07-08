package com.skillbox.cryptobot.service;

import com.skillbox.cryptobot.entity.Subscriber;
import com.skillbox.cryptobot.repository.SubscribersRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscribersServiceTest {

    @Mock
    SubscribersRepository subscribersRepository;

    @InjectMocks
    SubscribersService subscribersService;

    private List<Subscriber> subscriberList;
    private Subscriber subscriber1;
    private Subscriber subscriber2;
    private Subscriber subscriber3;


    @BeforeEach
    void setUp() {
        subscriberList = new ArrayList<>();

        subscriber1 = new Subscriber()
                .setId(1L)
                .setUuid(UUID.randomUUID())
                .setLastNotification(LocalDateTime.now().minusMonths(1))
                .setPrice(1000.00);

        subscriber2 = new Subscriber()
                .setId(2L)
                .setUuid(UUID.randomUUID())
                .setLastNotification(null)
                .setPrice(1000.00);

        subscriber3 = new Subscriber()
                .setId(1L)
                .setUuid(UUID.randomUUID())
                .setLastNotification(LocalDateTime.now().minusMonths(1))
                .setPrice(null);

        subscriberList.add(subscriber1);
        subscriberList.add(subscriber2);
    }

    @AfterEach
    void tearDown() {
        subscriberList = null;
        subscriber1 = null;
        subscriber2 = null;
    }

    @Test
    void getSubscriberByIdSuccess() {
        when(subscribersRepository.findById(3L)).thenReturn(Optional.ofNullable(subscriber3));

        subscribersService.getSubscriberById(3L);

        verify(subscribersRepository, times(1)).findById(3L);
    }

    @Test
    void getSubscriberByIdFail() {
        when(subscribersRepository.findById(300L)).thenReturn(Optional.empty());


        Exception exception = assertThrows(EntityNotFoundException.class, () ->
                 subscribersService.getSubscriberById(300L));

        assertEquals("Subscriber with id 300 not found", exception.getMessage());
        verify(subscribersRepository, times(1)).findById(300L);
    }

    @Test
    void findAllByPriceAndNotificationsIntervalSuccess() {
        when(subscribersRepository.findAllByPriceAndNotificationsIntervalSuccess(any(LocalDateTime.class),
                any(LocalDateTime.class),eq(100.0))).thenReturn(subscriberList);

        List<Subscriber> result = subscribersService
                .findAllByPriceAndNotificationsInterval(100.00, 10);

        assertEquals(2, result.size());
        verify(subscribersRepository, times(1))
                .findAllByPriceAndNotificationsIntervalSuccess(any(LocalDateTime.class),
                        any(LocalDateTime.class),eq(100.0));
    }

    @Test
    void createSubscribersSuccess() {
        subscribersService.createSubscribers(3L);
        verify(subscribersRepository, times(1)).save(any(Subscriber.class));
    }

    @Test
    void getSubscriptionWithNotNullPriceSuccess() {
        when(subscribersRepository.findById(1L)).thenReturn(Optional.ofNullable(subscriber1));

        String result = subscribersService.getSubscription(1L);

        assertEquals("Вы подписаны на стоимость биткоина 1000,000 USD", result);
        verify(subscribersRepository, times(1)).findById(1L);
    }

    @Test
    void getSubscriptionWithNullPriceSuccess() {
        when(subscribersRepository.findById(3L)).thenReturn(Optional.ofNullable(subscriber3));

        String result = subscribersService.getSubscription(3L);

        assertEquals("Активные подписки отсутствуют", result);
        verify(subscribersRepository, times(1)).findById(3L);
    }

    @Test
    void addSubscribeSuccess() {
        when(subscribersRepository.findById(3L)).thenReturn(Optional.ofNullable(subscriber3));

        subscribersService.addSubscribe(3L, 100.00);
        verify(subscribersRepository, times(1)).findById(3L);
        verify(subscribersRepository, times(1)).save(any(Subscriber.class));
    }

    @Test
    void deleteSubscribeSuccess() {
        when(subscribersRepository.findById(3L)).thenReturn(Optional.ofNullable(subscriber3));

        String result = subscribersService.deleteSubscribe(3L);

        assertEquals("Подписка отменена", result);
        verify(subscribersRepository, times(1)).findById(3L);
        verify(subscribersRepository, times(1)).save(any(Subscriber.class));
    }

    @Test
    void setPriceSuccess() {
        when(subscribersRepository.findById(3L)).thenReturn(Optional.ofNullable(subscriber3));

        subscribersService.setPrice(3L, 100.00);

        verify(subscribersRepository, times(1)).findById(3L);
        verify(subscribersRepository, times(1)).save(any(Subscriber.class));
    }

    @Test
    void setLastNotificationSuccess() {
        subscribersService.setLastNotification(subscriber3);

        verify(subscribersRepository, times(1)).save(any(Subscriber.class));
    }
}