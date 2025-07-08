package com.skillbox.cryptobot.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.UUID;

@Accessors(chain = true)
@Data
@Entity
@Table(name = "subscribers")
public class Subscriber {

    @Id
    private Long id;

    @NotNull
    private UUID uuid;

    private Double price;

    @Column(name = "last_notification")
    private LocalDateTime lastNotification;
}
