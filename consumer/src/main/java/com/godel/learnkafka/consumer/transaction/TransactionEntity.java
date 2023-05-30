package com.godel.learnkafka.consumer.transaction;

import com.godel.learnkafka.consumer.client.ClientEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "transaction")
@Getter
@Setter
public class TransactionEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String bank;

    @ManyToOne(fetch = LAZY)
    private ClientEntity client;

    private OrderType orderType;

    private Integer quantity;

    private Double price;

    private LocalDateTime createdAt;

}
