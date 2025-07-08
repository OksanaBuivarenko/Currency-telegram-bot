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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class UnsubscribeCommandTest {

    @Mock
    SubscribersService subscribersService;

    @InjectMocks
    UnsubscribeCommand command;

    AbsSender mockSender = mock(AbsSender.class);

    Message message = mock(Message.class);

    @Test
    void processMessageSuccess() throws TelegramApiException {
        String resultText = "Подписка успешно отменена.";
        when(subscribersService.deleteSubscribe(anyLong())).thenReturn(resultText);

        command.processMessage(mockSender, message, new String[]{});

        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
        verify(mockSender).execute(captor.capture());
        SendMessage sentMessage = captor.getValue();
        assertEquals(message.getChatId().toString(), sentMessage.getChatId());
        assertEquals(resultText, sentMessage.getText());
        verify(subscribersService).deleteSubscribe(anyLong());
    }

    @Test
    void testProcessMessageFail() throws Exception {
        when(subscribersService.deleteSubscribe(anyLong())).thenThrow(new RuntimeException("Ошибка"));
        LogCaptor logCaptor = LogCaptor.forClass(UnsubscribeCommand.class);

        command.processMessage(mockSender, message, new String[]{});

        verify(mockSender, never()).execute((SendDocument) any());
        assertTrue(logCaptor.getErrorLogs().stream()
                .anyMatch(log -> log.contains("Ошибка возникла в /unsubscribe методе")));
    }
}