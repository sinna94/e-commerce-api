package me.chung.ecommerceapi.domain.item

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import me.chung.ecommerceapi.domain.item.QItem.item
import me.chung.ecommerceapi.web.controller.SearchQuery
import me.chung.ecommerceapi.web.controller.SortType
import org.springframework.stereotype.Repository

@Repository
class ItemQueryRepos(
  private val queryFactory: JPAQueryFactory,
  private val em: EntityManager,
) {
  fun findItems(searchQuery: SearchQuery, sortType: SortType, offset: Long, limit: Long): List<Item> {
    val (categoryId, minPrice, maxPrice, query) = searchQuery

    val predicates = listOfNotNull(
      categoryId?.let { item.category.id.eq(it).or(item.category.path.contains("$it.")) },
      minPrice?.let { item.price.goe(it) },
      maxPrice?.let { item.price.loe(it) },
      query?.let { item.title.contains(it).or(item.contents.contains(it)) }
    ).toTypedArray()

    val graph = em.createEntityGraph(Item::class.java)
    graph.addAttributeNodes("category")

    val jpaQuery = queryFactory.selectFrom(item)
      .where(*predicates)
      .orderBy(getOrderBy(sortType), getOrderBy(SortType.DATE))
      .offset(offset)
      .limit(limit)

    jpaQuery.setHint("jakarta.persistence.fetchgraph", graph)

    return jpaQuery
      .fetch()
  }

  private fun getOrderBy(sortType: SortType) = when (sortType) {
    SortType.PRICE_ASC -> item.price.asc()
    SortType.PRICE_DESC -> item.price.desc()
    SortType.DATE -> item.createdAt.desc()
    SortType.REVIEW -> item.createdAt.desc() // 리뷰 미구현이므로 임시
  }
}
