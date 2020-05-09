package practice.kotlin.com.sleepwell

import com.google.gson.JsonObject
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query


interface apiService {

    @POST("/user")
    fun sendBoardInfo(@Query("boardIp") boardIp : String?, @Query("linkUrl") linkUrl: String?, @Query("writerTitle") writerTitle: String?, @Query("writer") writer: String?, @Query("password") password: String): Call<ResponseBody>

    @PUT("/user/like")
    fun sendLikeButton(@Query("id") id : Int? , @Query("boardIp") boardIp: String?) : Call<ResponseBody>

    @PUT("/user/dislike")
    fun sendDisLikeButton(@Query("id") id : Int? , @Query("boardIp") boardIp: String?) : Call<ResponseBody>

    @POST("/reply")
    fun sendComment(@Query("id") id : Int? , @Query("writer") writer : String, @Query("replyContent")
    replyContent : String , @Query("boardIp") boardIp: String, @Query("password") password: String? ) : Call<ResponseBody>

    @POST("/users")
    fun test(): Call<ResponseBody?>?

    @GET("/users")
    fun getUser(@Query("name") name: String?): Observable<JsonObject>

    @GET("/user")
    fun getTotalUser(): Observable<JsonObject>

    @GET("/reply")
    fun getComment(@Query("id") contentUid : Int?) : Observable<JsonObject>

}