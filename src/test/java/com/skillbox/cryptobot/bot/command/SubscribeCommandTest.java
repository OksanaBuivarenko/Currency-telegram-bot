package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.service.CryptoCurrencyService;
import com.skillbox.cryptobot.service.SubscribersService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class SubscribeCommandTest {

    @Mock
    SubscribersService subscribersService;

    @Mock
    CryptoCurrencyService cryptoCurrencyService;

    @InjectMocks
    SubscribeCommand command;

    AbsSender mockSender = mock(AbsSender.class);

    Message message = mock(Message.class);

    @Test
    void processMessageSuccess() throws Exception {
        when(message.getChatId()).thenReturn(12345L);
        when(message.getText()).thenReturn("/subscribe 50000");
        when(cryptoCurrencyService.getBitcoinPrice()).thenReturn(60000.0);

        command.processMessage(mockSender, message, new String[]{"50000"});

        verify(mockSender, times(2)).execute(any(SendMessage.class));

        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
        verify(mockSender, times(2)).execute(captor.capture());

        SendMessage sentMessage = captor.getValue();
        assert sentMessage.getText().contains("Новая подписка создана на стоимость 50000,000");
        assert sentMessage.getChatId().equals("12345");
    }

    @Test
    void testProcessMessageFail() throws Exception {
        when(message.getChatId()).thenReturn(12345L);
        when(message.getText()).thenReturn("/subscribe invalid");

        command.processMessage(mockSender, message, new String[]{"invalid"});

        verify(mockSender).execute(any(SendMessage.class));
        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
        verify(mockSender).execute(captor.capture());
        SendMessage msg = captor.getValue();
        assert msg.getText().contains("Введите число после /subscribe");
    }
}