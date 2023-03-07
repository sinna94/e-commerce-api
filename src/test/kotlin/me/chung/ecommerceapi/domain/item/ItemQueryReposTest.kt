package me.chung.ecommerceapi.domain.item

import me.chung.ecommerceapi.config.JpaQueryFactoryConfig
import me.chung.ecommerceapi.domain.category.Category
import me.chung.ecommerceapi.domain.category.CategoryRepos
import me.chung.ecommerceapi.domain.user.Role
import me.chung.ecommerceapi.domain.user.User
import me.chung.ecommerceapi.domain.user.UserRepos
import me.chung.ecommerceapi.web.controller.SearchQuery
import me.chung.ecommerceapi.web.controller.SortType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.TestConstructor
import java.math.BigInteger

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@Import(JpaQueryFactoryConfig::class, ItemQueryRepos::class)
class ItemQueryReposTest(
  private val itemRepos: ItemRepos,
  private val categoryRepos: CategoryRepos,
  private val userRepos: UserRepos,
  private val itemQueryRepos: ItemQueryRepos,
) {

  @BeforeEach
  fun setup() {
    itemRepos.deleteAllInBatch()
    categoryRepos.deleteAllInBatch()
    userRepos.deleteAllInBatch()
  }

  @DisplayName("아이템 검색 정렬 테스트")
  @ParameterizedTest
  @CsvSource(
    value = [
      "PRICE_ASC,'item3,item2,item1,item4,item5'",
      "PRICE_DESC,'item5,item4,item2,item1,item3'",
      "DATE,'item5,item4,item3,item2,item1'",
      "REVIEW,'item5,item4,item3,item2,item1'",
    ]
  )
  fun findItemsWithSort(sortType: SortType, expectedItemTitles: String) {
    val user = userRepos.save(User("u", "n", "e", "p", null, "", Role.SELLER))
    val category = categoryRepos.save(Category("category", 0, false, 0))
    itemRepos.saveAll(
      listOf(
        Item("item1", BigInteger.valueOf(10000L), "", user, false, category),
        Item("item2", BigInteger.valueOf(10000L), "", user, false, category),
        Item("item3", BigInteger.valueOf(5000L), "", user, false, category),
        Item("item4", BigInteger.valueOf(20000L), "", user, false, category),
        Item("item5", BigInteger.valueOf(30000L), "", user, false, category),
      )
    )

    val searchQuery = SearchQuery()

    val itemList = itemQueryRepos.findItems(searchQuery, sortType, 0, 100)
    assertThat(itemList)
      .extracting("title")
      .containsExactly(*expectedItemTitles.split(",").toTypedArray())
  }

  @Test
  @DisplayName("최상위 카테고리로 아이템 조회")
  fun findItemWithTopCategory() {
    val user = userRepos.save(User("u", "n", "e", "p", null, "", Role.SELLER))
    val categoryTop = categoryRepos.save(Category("category", 0, false, 0))
    val categoryMiddle =
      categoryRepos.save(Category("category", 1, false, 0).also { it.addParentCategory(categoryTop) })
    val categoryLeaf =
      categoryRepos.save(Category("category", 2, true, 0).also { it.addParentCategory(categoryMiddle) })

    itemRepos.saveAll(
      listOf(
        Item("itemTop1", BigInteger.valueOf(10000L), "", user, false, categoryTop),
        Item("itemTop2", BigInteger.valueOf(50000L), "", user, false, categoryTop),
        Item("itemMiddle1", BigInteger.valueOf(10000L), "", user, false, categoryMiddle),
        Item("itemMiddle2", BigInteger.valueOf(5000L), "", user, false, categoryMiddle),
        Item("itemLeaf1", BigInteger.valueOf(20000L), "", user, false, categoryLeaf),
        Item("itemLeaf2", BigInteger.valueOf(30000L), "", user, false, categoryLeaf),
      )
    )

    val searchQuery = SearchQuery(categoryTop.id)

    val itemList = itemQueryRepos.findItems(searchQuery, SortType.DATE, 0, 100)
    assertThat(itemList)
      .extracting("title")
      .containsExactly(
        "itemLeaf2",
        "itemLeaf1",
        "itemMiddle2",
        "itemMiddle1",
        "itemTop2",
        "itemTop1",
      ).hasSize(6)
  }

  @Test
  @DisplayName("중간 카테고리로 아이템 조회")
  fun findItemWithMiddleCategory() {
    val user = userRepos.save(User("u", "n", "e", "p", null, "", Role.SELLER))
    val categoryTop = categoryRepos.save(Category("category", 0, false, 0))
    val categoryMiddle =
      categoryRepos.save(Category("category", 1, false, 0).also { it.addParentCategory(categoryTop) })
    val categoryLeaf =
      categoryRepos.save(Category("category", 2, true, 0).also { it.addParentCategory(categoryMiddle) })

    itemRepos.saveAll(
      listOf(
        Item("itemTop1", BigInteger.valueOf(10000L), "", user, false, categoryTop),
        Item("itemTop2", BigInteger.valueOf(50000L), "", user, false, categoryTop),
        Item("itemMiddle1", BigInteger.valueOf(10000L), "", user, false, categoryMiddle),
        Item("itemMiddle2", BigInteger.valueOf(5000L), "", user, false, categoryMiddle),
        Item("itemLeaf1", BigInteger.valueOf(20000L), "", user, false, categoryLeaf),
        Item("itemLeaf2", BigInteger.valueOf(30000L), "", user, false, categoryLeaf),
      )
    )

    val searchQuery = SearchQuery(categoryMiddle.id)

    val itemList = itemQueryRepos.findItems(searchQuery, SortType.DATE, 0, 100)
    assertThat(itemList)
      .extracting("title")
      .containsExactly(
        "itemLeaf2",
        "itemLeaf1",
        "itemMiddle2",
        "itemMiddle1",
      ).hasSize(4)
  }

  @Test
  @DisplayName("하위 카테고리로 아이템 조회")
  fun findItemWithLeafCategory() {
    val user = userRepos.save(User("u", "n", "e", "p", null, "", Role.SELLER))
    val categoryTop = categoryRepos.save(Category("category", 0, false, 0))
    val categoryMiddle =
      categoryRepos.save(Category("category", 1, false, 0).also { it.addParentCategory(categoryTop) })
    val categoryLeaf =
      categoryRepos.save(Category("category", 2, true, 0).also { it.addParentCategory(categoryMiddle) })

    itemRepos.saveAll(
      listOf(
        Item("itemTop1", BigInteger.valueOf(10000L), "", user, false, categoryTop),
        Item("itemTop2", BigInteger.valueOf(50000L), "", user, false, categoryTop),
        Item("itemMiddle1", BigInteger.valueOf(10000L), "", user, false, categoryMiddle),
        Item("itemMiddle2", BigInteger.valueOf(5000L), "", user, false, categoryMiddle),
        Item("itemLeaf1", BigInteger.valueOf(20000L), "", user, false, categoryLeaf),
        Item("itemLeaf2", BigInteger.valueOf(30000L), "", user, false, categoryLeaf),
      )
    )

    val searchQuery = SearchQuery(categoryLeaf.id)

    val itemList = itemQueryRepos.findItems(searchQuery, SortType.DATE, 0, 100)
    assertThat(itemList)
      .extracting("title")
      .containsExactly(
        "itemLeaf2",
        "itemLeaf1",
      ).hasSize(2)
  }

  @Test
  @DisplayName("최솟값, 최댓값으로 아이템 조회 테스트")
  fun findItemWithPrice() {
    val user = userRepos.save(User("u", "n", "e", "p", null, "", Role.SELLER))
    val categoryTop = categoryRepos.save(Category("category", 0, false, 0))
    val categoryMiddle =
      categoryRepos.save(Category("category", 1, false, 0).also { it.addParentCategory(categoryTop) })
    val categoryLeaf =
      categoryRepos.save(Category("category", 2, true, 0).also { it.addParentCategory(categoryMiddle) })

    itemRepos.saveAll(
      listOf(
        Item("itemTop1", BigInteger.valueOf(10000L), "", user, false, categoryTop),
        Item("itemTop2", BigInteger.valueOf(50000L), "", user, false, categoryTop),
        Item("itemMiddle1", BigInteger.valueOf(10000L), "", user, false, categoryMiddle),
        Item("itemMiddle2", BigInteger.valueOf(5000L), "", user, false, categoryMiddle),
        Item("itemLeaf1", BigInteger.valueOf(20000L), "", user, false, categoryLeaf),
        Item("itemLeaf2", BigInteger.valueOf(30000L), "", user, false, categoryLeaf),
      )
    )

    val searchQuery = SearchQuery(minPrice = BigInteger.valueOf(10000L), maxPrice = BigInteger.valueOf(25000L))

    val itemList = itemQueryRepos.findItems(searchQuery, SortType.DATE, 0, 100)
    assertThat(itemList)
      .extracting("title")
      .containsExactly(
        "itemLeaf1",
        "itemMiddle1",
        "itemTop1",
      ).hasSize(3)
  }

  @ParameterizedTest
  @DisplayName("쿼리로 조회 테스트")
  @CsvSource(
    value = [
      "item,6",
      "Middle,2"
    ]
  )
  fun findItemWithQuery(query: String, expectedCount: Int) {
    val user = userRepos.save(User("u", "n", "e", "p", null, "", Role.SELLER))
    val categoryTop = categoryRepos.save(Category("category", 0, false, 0))
    val categoryMiddle =
      categoryRepos.save(Category("category", 1, false, 0).also { it.addParentCategory(categoryTop) })
    val categoryLeaf =
      categoryRepos.save(Category("category", 2, true, 0).also { it.addParentCategory(categoryMiddle) })

    itemRepos.saveAll(
      listOf(
        Item("itemTop1", BigInteger.valueOf(10000L), "", user, false, categoryTop),
        Item("itemTop2", BigInteger.valueOf(50000L), "", user, false, categoryTop),
        Item("itemMiddle1", BigInteger.valueOf(10000L), "", user, false, categoryMiddle),
        Item("itemMiddle2", BigInteger.valueOf(5000L), "", user, false, categoryMiddle),
        Item("itemLeaf1", BigInteger.valueOf(20000L), "", user, false, categoryLeaf),
        Item("itemLeaf2", BigInteger.valueOf(30000L), "", user, false, categoryLeaf),
      )
    )

    val searchQuery = SearchQuery(query = query)

    val itemList = itemQueryRepos.findItems(searchQuery, SortType.DATE, 0, 100)
    assertThat(itemList)
      .hasSize(expectedCount)
  }

  @Test
  @DisplayName("아이템 페이징 조회 테스트")
  fun findItemWithOffsetAndLimit() {
    val user = userRepos.save(User("u", "n", "e", "p", null, "", Role.SELLER))
    val categoryTop = categoryRepos.save(Category("category", 0, true, 0))

    itemRepos.saveAll(
      (1..100).map {
        Item("item$it", BigInteger.valueOf(10000L), "", user, false, categoryTop)
      }
    )

    val itemList = itemQueryRepos.findItems(SearchQuery(), SortType.DATE, 20, 40)
    assertThat(itemList)
      .extracting("title")
      .containsExactly(*(80 downTo 41).map { "item$it" }.toTypedArray())
      .hasSize(40)
  }
}
