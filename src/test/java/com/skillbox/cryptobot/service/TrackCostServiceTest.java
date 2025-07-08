package com.skillbox.cryptobot.service;

import com.skillbox.cryptobot.bot.CryptoBot;
import com.skillbox.cryptobot.entity.Subscriber;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrackCostServiceTest {

    @Mock
    CryptoCurrencyService cryptoCurrencyService;

    @Mock
    SubscribersService subscribersService;

    @Mock
    CryptoBot bot;

    @InjectMocks
    TrackCostService trackCostService;

    private List<Subscriber> subscribers;
    private Subscriber subscriber1;
    private Subscriber subscriber2;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(trackCostService, "notificationInterval", 5);
        subscribers = new ArrayList<>();

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

        subscribers.add(subscriber1);
        subscribers.add(subscriber2);
    }

    @AfterEach
    void tearDown() {
        subscribers = null;
        subscriber1 = null;
        subscriber2 = null;
    }

    @Test
    void trackCost() {
        when(cryptoCurrencyService.getBitcoinPrice()).thenReturn(2000.00);
        when(subscribersService.findAllByPriceAndNotificationsInterval(2000.00, 5))
                .thenReturn(subscribers);

        trackCostService.trackCost();

        verify(cryptoCurrencyService, times(1)).getBitcoinPrice();
        verify(subscribersService, times(1))
                .findAllByPriceAndNotificationsInterval(2000.00, 5);
        verify(bot, times(2)).sendMessageToUser(anyLong(), anyString());
        verify(subscribersService, times(2)).setLastNotification(any(Subscriber.class));
    }
}