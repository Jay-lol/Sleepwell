package practice.kotlin.com.sleepwell

import android.annotation.SuppressLint
import android.app.Activity
import android.os.AsyncTask
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.anko.toast
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.io.IOException


class Utils(context : Activity) {
    private var mContext  = context
    private var get_Image: String = "null"
    private var get_Title: String = ""
    private var get_Channel: String = ""
    private val TAG: String = "로그"
    @SuppressLint("StaticFieldLeak")
    fun getMetaDataFromUrl(writer : String, password: String, writerTitle : String,
                           url: String,  context: Activity, recycler : RecyclerView) {

        object : AsyncTask<Void, Void, Void>() {

            override fun doInBackground(vararg params: Void?): Void? {
                try {

                    val doc = Jsoup.connect(url).get()

                    val ogTags = doc.select("meta[property^=og:]")

                    if (ogTags.size <= 0) {
                        return null
                    }

                    val scriptTags = doc.select("script")

                    var findFirst = 0

                    Log.d("로그", ogTags.size.toString())
                    Log.d("로그", ogTags[12].toString())

                    for (i in 10 until scriptTags.size) {
                        if (scriptTags[i].toString().indexOf("var ytplayer") >= 0) {
                            findFirst = i
                            break
                        }
                    }

                    val findChannel = scriptTags[findFirst].toString()

                    val channelIndex = findChannel.indexOf("ownerChannelName")

                    Log.d("로그", findChannel)
                    Log.d("로그", "$channelIndex")

                    val x = doc.select("link")

                    for (zxc in x) {
                        if (zxc.attr("itemprop") == "name"){
                            Log.d(TAG, zxc.attr("content"))
                            get_Channel = zxc.attr("content") // 채널명
                        }
                    }

//                    get_Channel = findChannel.substring(
//                        channelIndex + 21, (findChannel.substring(channelIndex + 21)
//                            .indexOf("\\\",")) + channelIndex + 21)
                        // find.substring(int)에서 indexof로 찾기 때문에
                        // \\\", 는  find.substring(int)를 0기준으로 인덱스가 설정됨 그레서 +int를 한번더 해줘야함

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
//                Log.d("Jsoup test", get_Title)
//                Log.d("Jsoup test", "$get_Image")
//                Log.d("Jsoup test", "$get_Channel")
                if(get_Title!="" && get_Channel!="") {
                    mContext.toast("제목: $get_Title\n채널명 : $get_Channel")
                    ClickEvents().submit(writer, password, writerTitle , url, get_Title, get_Channel,context, recycler)
                }
                else
                    mContext.toast("잘못된 주소입니다!!")
            }
        }.execute()


    }

}