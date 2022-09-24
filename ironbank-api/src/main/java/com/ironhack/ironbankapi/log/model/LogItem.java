package com.ironhack.ironbankapi.log.model;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    private Instant timestamp;

    private String requestPath;

    private String method;

    private String responseStatus;

    @Override
    public String toString() {
        return "[%s] %s - %s with response %s".formatted(this.getTimestamp().toString(), this.getMethod(), this.getRequestPath(), this.getResponseStatus());
    }
}
