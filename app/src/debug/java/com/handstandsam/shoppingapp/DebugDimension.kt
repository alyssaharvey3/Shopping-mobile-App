package com.handstandsam.shoppingapp

import android.content.Context
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.readystatesoftware.chuck.ChuckInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

fun OkHttpClient.Builder.debugDimensionAddInterceptors(appContext: Context): OkHttpClient.Builder {
    this
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .addInterceptor(StethoInterceptor())
        .addInterceptor(ChuckInterceptor(appContext))
    return this
}