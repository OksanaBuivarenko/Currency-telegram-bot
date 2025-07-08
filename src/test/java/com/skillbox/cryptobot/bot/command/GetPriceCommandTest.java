package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.service.CryptoCurrencyService;
import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetPriceCommandTest {

    @Mock
    CryptoCurrencyService cryptoCurrencyService;

    @InjectMocks
    GetPriceCommand command;

    AbsSender mockSender = mock(AbsSender.class);

    Message message = mock(Message.class);

    @Test
    void processMessageSuccess() throws TelegramApiException {
            when(message.getChatId()).thenReturn(12345L);
            when(cryptoCurrencyService.getBitcoinPrice()).thenReturn(50000.0);

            command.processMessage(mockSender, message, new String[]{});

            ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
            verify(mockSender).execute(captor.capture());
            SendMessage sentMessage = captor.getValue();

            assertEquals("12345", sentMessage.getChatId());
            assertTrue(sentMessage.getText().contains("Текущая цена биткоина 50000,000 USD"));
        }

    @Test
    void testProcessMessageFail() throws Exception {
        when(message.getChatId()).thenReturn(12345L);
        when(cryptoCurrencyService.getBitcoinPrice()).thenThrow(new RuntimeException("Ошибка сервиса"));
        LogCaptor logCaptor = LogCaptor.forClass(GetPriceCommand.class);

        command.processMessage(mockSender, message, new String[]{});

        verify(mockSender, never()).execute((SendDocument) any());
        assertTrue(logCaptor.getErrorLogs().stream()
                .anyMatch(log -> log.contains("Ошибка возникла в /get_price методе")));
    }
}