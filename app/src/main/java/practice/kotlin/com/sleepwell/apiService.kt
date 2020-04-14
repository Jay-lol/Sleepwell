package practice.kotlin.com.sleepwell

import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface apiService {

    @POST("/user")
    fun testcall(@Query("linkUrl") linkUrl: String?, @Query("writerTitle") writerTitle: String?, @Query("writer") writer: String?): Call<ResponseBody>

    @POST("/users")
    fun test(): Call<ResponseBody?>?

    @GET("/users")
    fun getUser(@Query("name") name: String?): Observable<JsonObject>

    @GET("/user")
    fun getTotalUser(): Observable<JsonObject>

    @GET
    @Streaming
    fun getImageDetails(@Url url : String) : Call<ResponseBody>
}