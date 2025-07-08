package com.skillbox.cryptobot.service;

import com.skillbox.cryptobot.entity.Subscriber;
import com.skillbox.cryptobot.repository.SubscribersRepository;
import com.skillbox.cryptobot.utils.TextUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class SubscribersService {

    private final SubscribersRepository subscribersRepository;

    public Subscriber getSubscriberById(Long id) {
        return subscribersRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("Subscriber with id %d not found", id)));
    }

    public List<Subscriber> findAllByPriceAndNotificationsInterval(Double price, int notificationInterval) {
        LocalDateTime startTime = LocalDateTime.of(2025, 1, 1, 1, 0, 0);
        LocalDateTime endTime = LocalDateTime.now().minusMinutes(notificationInterval);
        return subscribersRepository.findAllByPriceAndNotificationsIntervalSuccess(startTime, endTime, price);
    }

    public void createSubscribers(Long chatId) {
        Subscriber newSubscriber = new Subscriber()
            .setId(chatId)
            .setUuid(UUID.randomUUID())
            .setLastNotification(null);
        subscribersRepository.save(newSubscriber);
    }

    public String getSubscription(Long chatId) {
        Subscriber subscriber = getSubscriberById(chatId);
        if (subscriber.getPrice() == null) {
            return "Активные подписки отсутствуют";
        }
        return "Вы подписаны на стоимость биткоина " + TextUtil.toString(subscriber.getPrice())+ " USD";
    }

    public void addSubscribe(Long chatId, Double price) {
        setPrice(chatId, price);
    }

    public String deleteSubscribe(Long chatId) {
        setPrice(chatId, null);
        return "Подписка отменена";
    }

    public void setPrice(Long id, Double price) {
        Subscriber subscriber = getSubscriberById(id);
        subscriber.setPrice(price);
        subscribersRepository.save(subscriber);
    }

    public void setLastNotification(Subscriber subscriber) {
        subscriber.setLastNotification(LocalDateTime.now());
        subscribersRepository.save(subscriber);
    }
}
