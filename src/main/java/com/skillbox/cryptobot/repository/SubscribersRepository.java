package com.skillbox.cryptobot.repository;

import com.skillbox.cryptobot.entity.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SubscribersRepository extends JpaRepository<Subscriber, Long> {

    @Query("SELECT s FROM Subscriber s " +
            "WHERE s.lastNotification IS NULL " +
            "OR s.lastNotification between :start AND :end " +
            "AND s.price > :price")
    List<Subscriber> findAllByPriceAndNotificationsIntervalSuccess(@Param("start")LocalDateTime start,
                                                                   @Param("end")LocalDateTime end, @Param("price") Double price);

}
