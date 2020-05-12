package practice.kotlin.com.sleepwell

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import org.jetbrains.anko.toast
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.io.IOException
import java.util.regex.Pattern


class Utils(context : Context) {
    private var mContext  = context
    private var get_Image: String = "null"
    private var get_Title: String = "null"
    private var get_Channel: String = "null"

    @SuppressLint("StaticFieldLeak")
    fun getMetaDataFromUrl(url: String) {

        object : AsyncTask<Void, Void, Void>() {

            override fun doInBackground(vararg params: Void?): Void? {
                try {

                    val doc = Jsoup.connect(url).get()

                    var ogTags = doc.select("meta[property^=og:]")
                    var scriptTags = doc.select("script")

                    var findFirst = 0
//                    Log.d("How many", ogTags.size.toString())
//                    Log.d("PPPP", ogTags[12].toString())
                    aloop@ for (i in 10 until scriptTags.size) {
                        if (scriptTags[i].toString().indexOf("var ytplayer") >= 0) {
                            findFirst = i
                            break@aloop
                        }
                    }
                    val findChannel = scriptTags[findFirst].toString()

                    var int = findChannel.indexOf("ownerChannelName")

                    get_Channel = findChannel.substring(
                        int + 21, (findChannel.substring(int + 21)
                            .indexOf("\\\",")) + int + 21)
                    // find.substring(int)에서 indexof로 찾기 때문에
                    // \\\", 는  find.substring(int)를 0기준으로 인덱스가 설정됨 그레서 +int를 한번더 해줘야함

                    if (ogTags.size <= 0) {
                        return null
                    }
//                    Log.d("ggggggggg", classTags.toString())
                    for (i in 0 until ogTags.size) {
                        val tag: Element = ogTags[i]

                        val text: String = tag.attr("property")

                        if ("og:title" == text) {
                            get_Title = tag.attr("content")     // 제목
                        } else if ("og:image" == text) {
                            get_Image = tag.attr("content")     // 썸네일
                        }
                    }
                } catch (e: IOException) {
                    Log.e("submit error ", e.message.toString())
                }
                return null
            }

            override fun onPostExecute(result: Void?) {
                Log.d("Jsoup test", get_Title)
                Log.d("Jsoup test", "$get_Image")
                Log.d("Jsoup test", "$get_Channel")
                mContext.toast("제목: $get_Title\n채널명 : $get_Channel")
            }
        }.execute()


    }

}