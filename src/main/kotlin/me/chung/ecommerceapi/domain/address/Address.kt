package me.chung.ecommerceapi.domain.address

import jakarta.persistence.*
import me.chung.ecommerceapi.domain.BaseEntity
import me.chung.ecommerceapi.domain.user.User

@Entity
@Table(name = "address", uniqueConstraints = [UniqueConstraint(name = "address_uq", columnNames = ["user_id", "address"])])
class Address(
    @ManyToOne(targetEntity = User::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,

    @Column(length = 20)
    val name: String,

    @Column(length = 100)
    val address: String,
): BaseEntity(){
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id", nullable = false)
    var id: Long? = null
}