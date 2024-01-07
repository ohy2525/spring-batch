package com.example.springbatch.repository;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 공통 매핑 정보를 가지는 Entity
 */
@MappedSuperclass  //상속받은 entity에서 아래 필드를 칼럼으로 사용할 수 있다.
@EntityListeners(AuditingEntityListener.class)  //Auditing 정보를 캡쳐하는 Listener
public class BaseEntity {

    @CreatedDate  //생성일시
    @Column(updatable = false, nullable = false)  //업데이트 불가능, null 불가능
    private LocalDateTime createdAt;

    @LastModifiedDate  //마지막 수정 일시
    private LocalDateTime modifiedAt;
}
