package me.chung.ecommerceapi.config

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class JpaQueryFactoryConfig {

  @PersistenceContext
  private lateinit var entityManager: EntityManager

  @Bean
  fun jpaQueryFactory() = JPAQueryFactory(entityManager)
}
