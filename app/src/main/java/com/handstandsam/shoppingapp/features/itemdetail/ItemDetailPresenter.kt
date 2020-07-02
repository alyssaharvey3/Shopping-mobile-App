package com.handstandsam.shoppingapp.features.itemdetail

import android.content.Intent
import androidx.lifecycle.LifecycleCoroutineScope
import com.handstandsam.shoppingapp.cart.ShoppingCart
import com.handstandsam.shoppingapp.models.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ItemDetailPresenter(
    private val view: ItemDetailActivity.ItemDetailView,
    private var cart: ShoppingCart,
    private val lifecycleScope: LifecycleCoroutineScope
) : CoroutineScope by lifecycleScope {
    internal lateinit var item: Item

    fun onResume(intent: Intent) {
        val extras = intent.extras
        val item = extras!!.get(BUNDLE_PARAM_ITEM) as Item
        this.item = item

        view.setLabel(item.label)
        view.setImageUrl(item.image)
        view.setActionBarTitle(item.label)
    }

    fun addToCardClicked() {
        launch {
            cart.incrementItemInCart(item)
        }
    }

    companion object {
        const val BUNDLE_PARAM_ITEM = "item"
    }
}
