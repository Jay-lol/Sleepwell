package practice.kotlin.com.sleepwell

import android.util.Log
import java.net.MalformedURLException
import java.net.URISyntaxException
import java.net.URL
import java.util.regex.Pattern

class Validator {
    fun isUrl(text : String) : Boolean {
        val p = Pattern.compile("^(http(s)?:\\/\\/)?((w){3}.)?youtu(be|.be)(\\.com)?\\/(?!embed|channel).+")
        return p.matcher(text).matches()
    }
}
