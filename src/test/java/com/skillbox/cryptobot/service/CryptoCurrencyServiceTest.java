package com.skillbox.cryptobot.service;

import com.skillbox.cryptobot.client.BinanceClient;
import com.skillbox.cryptobot.exception.BinanceException;
import com.skillbox.cryptobot.repository.SubscribersRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CryptoCurrencyServiceTest {

    @Mock
    BinanceClient client;

    @InjectMocks
    CryptoCurrencyService cryptoCurrencyService;

    @Test
    void getBitcoinPriceSuccess() throws IOException {
        when(client.getBitcoinPrice()).thenReturn(1000.00);

        double result = cryptoCurrencyService.getBitcoinPrice();

        assertEquals(1000.00, result);
    }

    @Test
    void getBitcoinPriceFail() throws IOException {
        when(client.getBitcoinPrice()).thenThrow(new BinanceException("Ошибка Binance"));
        assertThrows(BinanceException.class, () -> {
            cryptoCurrencyService.getBitcoinPrice();
        });
    }
}