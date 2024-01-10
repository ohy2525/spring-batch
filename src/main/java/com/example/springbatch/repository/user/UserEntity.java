package com.example.springbatch.repository.user;

import com.example.springbatch.repository.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
public class UserEntity extends BaseEntity {

    @Id
    private String userId;

    private String userName;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    private String phone;
    private String meta;
}
