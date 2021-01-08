package practice.kotlin.com.sleepwell.statics

import com.google.gson.JsonPrimitive
import org.json.JSONArray

class JsonString {
    companion object{
        var jsonArray : JSONArray? = null
        var jsonP : JsonPrimitive? = null
        var jsonA : JsonPrimitive? = null
        var jsonCommuArray : JSONArray? = null
        var jsonRereArray : JSONArray? = null
        var replyArray = mutableListOf<String>()
        var cnt = 0
        var isCommentFirstLoading : Boolean = true
        var macAddress : String = ""
    }
}