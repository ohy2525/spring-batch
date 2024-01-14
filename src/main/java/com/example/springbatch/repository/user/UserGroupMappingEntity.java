package com.example.springbatch.repository.user;

import com.example.springbatch.repository.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter
@ToString
@Table(name = "user_group_mapping")
@IdClass(UserGroupMappingId.class)
public class UserGroupMappingEntity extends BaseEntity {

    @Id
    private String userGroupId;
    @Id
    private String userId;

    private String userGroupName;
    private String description;
}
