package me.chung.ecommerceapi.domain.user

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepos: JpaRepository<User, Long> {
    fun findByLoginId(loginId: String): User?
}