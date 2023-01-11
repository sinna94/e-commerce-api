package me.chung.ecommerceapi.domain

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@EntityListeners(AuditingEntityListener::class)
@MappedSuperclass
abstract class BaseEntity {
    @CreatedDate
    var createdAt: LocalDateTime? = null
        private set

    @LastModifiedDate
    var updatedAt: LocalDateTime? = null
        private set
}