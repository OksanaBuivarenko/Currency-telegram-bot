package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.exception.CommandArgumentException;
import com.skillbox.cryptobot.service.CryptoCurrencyService;
import com.skillbox.cryptobot.service.SubscribersService;
import com.skillbox.cryptobot.utils.CommandUtil;
import com.skillbox.cryptobot.utils.TextUtil;
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
public class SubscribeCommand implements IBotCommand {

    private final SubscribersService subscribersService;

    private final CryptoCurrencyService cryptoCurrencyService;

    @Override
    public String getCommandIdentifier() {
        return "subscribe";
    }

    @Override
    public String getDescription() {
        return "Подписывает пользователя на стоимость биткоина";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        SendMessage answer = new SendMessage();
        answer.setChatId(message.getChatId());
        try {
            Double price = CommandUtil.getCommandArgs(message.getText());
            subscribersService.addSubscribe(message.getChatId(), price);
            answer.setText("Текущая цена биткоина "
                    + TextUtil.toString(cryptoCurrencyService.getBitcoinPrice()) + " USD");
            absSender.execute(answer);
            answer.setText("Новая подписка создана на стоимость " + TextUtil.toString(price));
            absSender.execute(answer);
        } catch (CommandArgumentException e) {
            answer.setText(e.getMessage());
            try {
                absSender.execute(answer);
            } catch (Exception ex) {
                log.error("Ошибка возникла в /subscribe методе", e);
            }
        }catch (Exception e) {
            log.error("Ошибка возникла в /subscribe методе", e);
        }
    }
}