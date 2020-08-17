package practice.kotlin.com.sleepwell.retrofit

import practice.kotlin.com.sleepwell.ApiService
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitCreator {
    companion object {
        fun defaultRetrofit(): ApiService {
            return Retrofit.Builder()
                .baseUrl("https://www.nodap.xyz/user/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}
