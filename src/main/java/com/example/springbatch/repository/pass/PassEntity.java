package com.example.springbatch.repository.pass;

import com.example.springbatch.repository.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Entity
public class PassEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer passSeq;
    private Integer packageSeq;
    private String userId;

    @Enumerated(EnumType.STRING)
    private PassStatus status;
    private Integer remainingCount;

    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private LocalDateTime expiredAt;
}
