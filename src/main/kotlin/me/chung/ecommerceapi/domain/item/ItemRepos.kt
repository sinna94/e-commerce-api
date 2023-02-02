package me.chung.ecommerceapi.domain.item

import org.springframework.data.jpa.repository.JpaRepository

interface ItemRepos : JpaRepository<Item, Long>
