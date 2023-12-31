package com.handstandsam.shoppingapp.features.category

import com.handstandsam.shoppingapp.MviViewModel
import com.handstandsam.shoppingapp.models.Item
import com.handstandsam.shoppingapp.network.Response
import com.handstandsam.shoppingapp.repository.ItemRepo

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val scope: CoroutineScope,
    private val itemRepo: ItemRepo
) : MviViewModel<
        CategoryViewModel.State,
        CategoryViewModel.Intention,
        CategoryViewModel.SideEffect>(
    scope = scope,
    initialState = State()
) {

    private fun getItemsForCategory(categoryLabel: String) {
        scope.launch(Dispatchers.IO) {
            val categoriesResult = itemRepo.getItemsForCategory(categoryLabel)
            when (categoriesResult) {
                is Response.Success -> {
                    send(Intention.CategoriesReceived(categoriesResult.body))
                }
                is Response.Failure -> {
                    error("Ooops")
                }
            }
        }
    }

    override fun reduce(state: State, intention: Intention): State {
        return when (intention) {
            is Intention.ItemClicked -> {
                sideEffect(SideEffect.LaunchItemDetailActivity(intention.item))
                state
            }
            is Intention.CategoriesReceived -> {
                state.copy(
                    items = intention.items
                )
            }
            is Intention.CategoryLabelSet -> {
                getItemsForCategory(intention.categoryLabel)
                state.copy(
                    categoryLabel = intention.categoryLabel
                )
            }
        }
    }

    data class State(
        val items: List<Item> = listOf(),
        val categoryLabel: String? = null
    )

    sealed class Intention {
        data class CategoriesReceived(val items: List<Item>) : Intention()
        data class ItemClicked(val item: Item) : Intention()
        data class CategoryLabelSet(val categoryLabel: String) : Intention()
    }

    sealed class SideEffect {
        data class LaunchItemDetailActivity(val item: Item) : SideEffect()

    }
}
