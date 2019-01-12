package com.handstandsam.shoppingapp.di

import android.content.Context
import com.handstandsam.shoppingapp.debugDimensionAddInterceptors
import com.handstandsam.shoppingapp.models.NetworkConfig
import com.handstandsam.shoppingapp.network.ShoppingService
import com.handstandsam.shoppingapp.repository.*
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

interface NetworkGraph {
    val categoryRepo: CategoryRepo
    val itemRepo: ItemRepo
    val userRepo: UserRepo
}

open class BaseNetworkGraph(
    appContext: Context,
    networkConfig: NetworkConfig
) : NetworkGraph {

    private val okHttpClientBuilder =
        OkHttpClient.Builder().debugDimensionAddInterceptors(appContext)

    private val retrofitBuilder: Retrofit.Builder =
        Retrofit.Builder()
            .baseUrl(networkConfig.fullUrl)
            .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().build()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClientBuilder.build())

    private val retrofit: Retrofit = retrofitBuilder.build()

    private val shoppingService: ShoppingService = retrofit.create(ShoppingService::class.java)

    override val categoryRepo: CategoryRepo = NetworkCategoryRepo(shoppingService)

    override val itemRepo: ItemRepo = NetworkItemRepo(shoppingService)

    override val userRepo: UserRepo = NetworkUserRepo(shoppingService)

}