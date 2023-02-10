package me.chung.ecommerceapi.web.service

import me.chung.ecommerceapi.domain.category.Category
import me.chung.ecommerceapi.domain.item.Item
import me.chung.ecommerceapi.domain.item.ItemRepos
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigInteger

@Service
@Transactional
class ItemService(
  private val categoryService: CategoryService,
  private val userService: UserService,
  private val itemRepos: ItemRepos,
) {

  fun addItem(itemRequest: NewItemRequest, loginId: String): Item {
    val seller = userService.findUserByLoginId(loginId)
    val category = categoryService.findCategoryById(itemRequest.categoryId)
    val item = itemRequest.toEntity(seller.id, category)
    return itemRepos.save(item)
  }
}

data class NewItemRequest(
  val title: String,
  val price: BigInteger,
  val contents: String,
  val categoryId: Long,
) {
  fun toEntity(seller: Long, category: Category): Item {
    return Item(title, price, contents, seller, false, category)
  }
}
