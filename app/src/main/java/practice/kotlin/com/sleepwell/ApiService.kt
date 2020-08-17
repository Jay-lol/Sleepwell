package practice.kotlin.com.sleepwell

import com.google.gson.JsonObject
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface ApiService {

    @POST("/user")
    fun sendBoardInfo(@Query("boardIp") boardIp : String?, @Query("linkUrl") linkUrl: String?, @Query("linkChannel") linkChannel: String?,@Query("linkTitle") linkTitle: String?,@Query("writerTitle") writerTitle: String?, @Query("writer") writer: String?, @Query("password") password: String): Call<ResponseBody>

    @PUT("/user/like")
    fun sendLikeButton(@Query("id") id : Int? , @Query("boardIp") boardIp: String?) : Call<ResponseBody>

    @PUT("/user/dislike")
    fun sendDisLikeButton(@Query("id") id : Int? , @Query("boardIp") boardIp: String?) : Call<ResponseBody>

    @POST("/reply")
    fun sendComment(@Query("id") id : Int , @Query("writer") writer : String, @Query("replyContent")
    replyContent : String , @Query("boardIp") boardIp: String, @Query("password") password: String? ) : Call<ResponseBody>

    @POST("/rereply")
    fun sendReComment(@Query("id") rootid : Int, @Query("rid") id : Int?, @Query("writer") writer : String, @Query("rereplyContent")
    replyContent : String, @Query("boardIp") boardIp: String, @Query("password") password: String? ) : Call<ResponseBody>

    @GET("/user")
    fun getTotalUser(@Query("page") page : Int , @Query("pageChoice") likeOrRecent : Int): Observable<JsonObject>

    @GET("/reply")
    fun getComment(@Query("id") contentUid : Int?,@Query("page") position : Int? ) : Observable<JsonObject>

    @GET("/reply/best")
    fun getBest(@Query("id") contentUid : Int?) : Observable<JsonObject>

    @PUT("/reply/like")
    fun sendReplyLike( @Query("rid")rid : Int , @Query("boardIp")boardIp : String) : Call<ResponseBody>

    @PUT("/rereply/like")
    fun sendRereplyLike( @Query("rrid")rrid : Int , @Query("boardIp")boardIp : String) : Call<ResponseBody>

    @DELETE("/user/delete")
    fun deleteBoard(@Query("id") id : Int ,@Query("password") password: String) : Call<ResponseBody>

    @DELETE("/reply/delete")
    fun deleteComment(@Query("rid") rid : Int ,@Query("password") password: String) : Call<ResponseBody>

    @DELETE("/rereply/delete")
    fun deleteReComment(@Query("rrid") rid : Int ,@Query("password") password: String) : Call<ResponseBody>

    @PUT("/user/fire")
    fun fireBoard(@Query("id") id : Int , @Query("boardIp") boardIp: String) : Call<ResponseBody>

    @PUT("/reply/fire")
    fun fireComment(@Query("id") id : Int , @Query("boardIp") boardIp: String) : Call<ResponseBody>

    @PUT("/rereply/fire")
    fun fireReComment(@Query("id") id : Int , @Query("boardIp") boardIp: String) : Call<ResponseBody>

}