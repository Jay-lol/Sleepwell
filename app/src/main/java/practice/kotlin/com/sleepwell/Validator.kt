package practice.kotlin.com.sleepwell

import java.util.regex.Pattern

class Validator {
    fun isUrl(text : String) : Boolean {
        val p = Pattern.compile("^(http(s)?:\\/\\/)?((w){3}.)?youtu(be|.be)(\\.com)?\\/(?!embed|channel).+")
        return p.matcher(text).matches()
    }
}
