package com.godel.learnkafka.consumer.client;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "client")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ClientEntity {

    @Id
    private Long id;

    private String email;

    private boolean template;

}
