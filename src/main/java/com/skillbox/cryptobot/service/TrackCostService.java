package com.skillbox.cryptobot.service;

import com.skillbox.cryptobot.bot.CryptoBot;
import com.skillbox.cryptobot.entity.Subscriber;
import com.skillbox.cryptobot.utils.TextUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@EnableScheduling
@RequiredArgsConstructor
public class TrackCostService {

    private final CryptoCurrencyService cryptoCurrencyService;

    private final SubscribersService subscribersService;

    private final CryptoBot bot;

    @Value("${scheduler.notificationInterval}")
    private Integer notificationInterval;

    @Scheduled(cron = "${scheduler.getPriceInterval}")
    public void trackCost() {
        double currentPrice = cryptoCurrencyService.getBitcoinPrice();
        List<Subscriber> subscribers = subscribersService.findAllByPriceAndNotificationsInterval(currentPrice, notificationInterval);
        for (Subscriber subscriber: subscribers) {
            bot.sendMessageToUser(subscriber.getId(),
                    "Пора покупать, стоимость биткоина " + TextUtil.toString(currentPrice));
            subscribersService.setLastNotification(subscriber);
            log.info("Send notification user with id " + subscriber.getId());
        }
    }
}
