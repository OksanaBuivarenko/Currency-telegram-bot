package com.skillbox.cryptobot.bot.command;

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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetSubscriptionCommandTest {

    @Mock
    SubscribersService subscribersService;

    @InjectMocks
    GetSubscriptionCommand command;

    AbsSender mockSender = mock(AbsSender.class);

    Message message = mock(Message.class);

    @Test
    void processMessageSuccess() throws TelegramApiException {
        when(message.getChatId()).thenReturn(12345L);
        when(subscribersService.getSubscription(message.getChatId())).thenReturn("Подписка активна");

        command.processMessage(mockSender, message, new String[]{});

        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
        verify(mockSender).execute(captor.capture());
        SendMessage sentMessage = captor.getValue();

        assertEquals("12345", sentMessage.getChatId());
        assertTrue(sentMessage.getText().contains("Подписка активна"));
    }

    @Test
    void testProcessMessageFail() throws Exception {
        when(message.getChatId()).thenReturn(12345L);
        when(subscribersService.getSubscription(message.getChatId())).thenThrow(new RuntimeException("Ошибка сервиса"));
        LogCaptor logCaptor = LogCaptor.forClass(GetSubscriptionCommand.class);

        command.processMessage(mockSender, message, new String[]{});

        verify(mockSender, never()).execute((SendDocument) any());
        assertTrue(logCaptor.getErrorLogs().stream()
                .anyMatch(log -> log.contains("Ошибка возникла в /get_subscription методе")));
    }
}