package me.chung.ecommerceapi.domain.user

import jakarta.persistence.*
import me.chung.ecommerceapi.domain.BaseEntity
import me.chung.ecommerceapi.domain.address.Address

@Entity
@Table(
    name = "user", uniqueConstraints = [
        UniqueConstraint(columnNames = arrayOf("loginId")),
        UniqueConstraint(columnNames = arrayOf("phone")),
        UniqueConstraint(columnNames = arrayOf("email")),
    ]
)
class User(
    @Column(nullable = false)
    val loginId: String,

    @Column(length = 20, nullable = false)
    val name: String,

    @Column(length = 100, nullable = false)
    val email: String,

    @Column(nullable = false)
    val phone: String,

    @Column
    @OneToMany
    val address: List<Address>?,

    @Column(nullable = false)
    val password: String,

    @Column(nullable = false)
    val salt: String,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val role: Role

): BaseEntity()