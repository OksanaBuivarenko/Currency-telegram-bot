package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.service.SubscribersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Service
@Slf4j
@RequiredArgsConstructor
public class UnsubscribeCommand implements IBotCommand {

    private final SubscribersService subscribersService;

    @Override
    public String getCommandIdentifier() {
        return "unsubscribe";
    }

    @Override
    public String getDescription() {
        return "Отменяет подписку пользователя";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        SendMessage answer = new SendMessage();
        answer.setChatId(message.getChatId());
        try {
            answer.setText(subscribersService.deleteSubscribe(message.getChatId()));
            absSender.execute(answer);
        } catch (Exception e) {
            log.error("Ошибка возникла в /unsubscribe методе", e);
        }
    }
}