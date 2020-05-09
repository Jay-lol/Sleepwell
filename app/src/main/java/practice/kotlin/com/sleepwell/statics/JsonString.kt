package practice.kotlin.com.sleepwell.statics

import com.google.gson.JsonArray
import com.google.gson.JsonPrimitive
import org.json.JSONArray

class JsonString {
    companion object{
        var jsonArray : JsonArray? = null
        var jsonA : JsonPrimitive? = null
        var jsonCommuArray : JSONArray? = null
        var cnt = 0
        var macAddress : String? = null

        var cJsonArray : JsonArray? = null
    }
}