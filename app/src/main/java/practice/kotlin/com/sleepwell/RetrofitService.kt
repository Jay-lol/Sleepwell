package practice.kotlin.com.sleepwell

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class RetrofitService {
    fun callBackPost(res: String) {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.nodap.xyz/user/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val server = retrofit.create(apiService::class.java)
        val hi = res
        val editor = "작성자"
        val testcall = server.testcall("$hi", "영상제목", "$editor")
        var ret = ""
        testcall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.d("test", response?.body().toString())
                // Toast.makeText(applicationContext, "$cnt" + "번 " + "$ms", Toast.LENGTH_LONG).show();
                Log.d("Print", "헬로")
                ret = response?.body().toString()
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("error", t.message.toString())
            }
        })
    }
    fun callBackGet() : apiService {
//        val okHttpClient = OkHttpClient.Builder()
//            .connectTimeout(1, TimeUnit.MINUTES)
//            .readTimeout(60, TimeUnit.SECONDS)
//            .writeTimeout(60, TimeUnit.SECONDS)
//            .build()
        var retrofit = Retrofit.Builder()
            .baseUrl("https://www.nodap.xyz/user/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(apiService::class.java)

        return retrofit
    }

}
