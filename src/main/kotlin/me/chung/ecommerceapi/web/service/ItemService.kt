package me.chung.ecommerceapi.web.service

import me.chung.ecommerceapi.domain.category.Category
import me.chung.ecommerceapi.domain.item.Item
import me.chung.ecommerceapi.domain.item.ItemRepos
import me.chung.ecommerceapi.domain.user.User
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

  fun addItem(itemRequest: NewItemRequest, loginId: String): ItemResponse {
    val seller = userService.findUserByLoginId(loginId)
    val category = categoryService.findCategoryById(itemRequest.categoryId)
    val item = itemRequest.toEntity(seller, category)
    return ItemResponse(itemRepos.save(item))
  }

  fun editItem(id: Long, loginId: String, editItemRequest: EditItemRequest): ItemResponse {
    val (title, price, contents, categoryId) = editItemRequest

    val item = getEditingTarget(id, loginId)

    title?.let {
      item.title = it
    }

    price?.let {
      item.price = it
    }

    contents?.let {
      item.contents = it
    }

    categoryId?.let {
      val category = categoryService.findCategoryById(it)
      item.category = category
    }

    return ItemResponse(item)
  }

  private fun getEditingTarget(id: Long, loginId: String): Item {
    val itemOptional = itemRepos.findById(id)

    if (itemOptional.isEmpty) {
      throw IllegalArgumentException("Item not found : $id")
    }

    val item = itemOptional.get()

    if (item.seller.loginId != loginId) {
      throw SecurityException("Item $id is not $loginId's item")
    }

    return item
  }

  fun changeStopSelling(id: Long, loginId: String, stopSelling: Boolean): Boolean {
    val item = getEditingTarget(id, loginId)

    item.stopSelling = stopSelling

    return item.stopSelling ?: false
  }
}

data class NewItemRequest(
  val title: String,
  val price: BigInteger,
  val contents: String,
  val categoryId: Long,
) {
  fun toEntity(seller: User, category: Category): Item {
    return Item(title, price, contents, seller, false, category)
  }
}

data class EditItemRequest(
  val title: String? = null,
  val price: BigInteger? = null,
  val contents: String? = null,
  val categoryId: Long? = null,
)

data class ItemResponse(
  val id: Long,
  val title: String,
  val price: BigInteger,
  val contents: String,
  val categoryId: Long,
) {
  constructor(item: Item) : this(item.id, item.title, item.price, item.contents, item.category.id)
}
