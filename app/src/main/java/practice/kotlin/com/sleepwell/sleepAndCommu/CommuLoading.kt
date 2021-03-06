package practice.kotlin.com.sleepwell.sleepAndCommu

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_commu.view.*
import practice.kotlin.com.sleepwell.recycler.RecyclerItem
import practice.kotlin.com.sleepwell.statics.JsonString.Companion.jsonArray
import practice.kotlin.com.sleepwell.statics.commuList.Companion.commuview
import practice.kotlin.com.sleepwell.statics.commuList.Companion.mList
import practice.kotlin.com.sleepwell.statics.commuList.Companion.position

class CommuLoading {

//    var tempList : ArrayList<RecyclerItem> = arrayListOf()

    fun refreshList(recycler: RecyclerView?, submitFlag: Int?) {

        var end = jsonArray!!.length()

        for (i in 0 until end) {       // 최신순
            addItem(
                jsonArray!!.getJSONObject(i).get("id") as Int,
                jsonArray!!.getJSONObject(i).get("linkUrl").toString(),
                jsonArray!!.getJSONObject(i).get("thumbnailUrl").toString(),

                if(jsonArray!!.getJSONObject(i).get("writerTitle").toString() == "null@title")
                {
                    jsonArray!!.getJSONObject(i).get("linkTitle").toString()
                } else{
                    jsonArray!!.getJSONObject(i).get("writerTitle").toString()
                }
                ,
                jsonArray!!.getJSONObject(i).get("linkChannel").toString(),
                jsonArray!!.getJSONObject(i).get("writer").toString(),
                (jsonArray!!.getJSONObject(i).get("likeCount") as Int-
                jsonArray!!.getJSONObject(i).get("dislikeCount") as Int), jsonArray!!.getJSONObject(i).get("replyCnt") as Int
            )
        }

//        mList.sort()      // 좋아요순


        Log.d("사이즈체크",  position.toString() +"\n"+ mList.size.toString())

//        recycler?.adapter?.notifyItemRangeInserted(position*20, mList.size-1)

        recycler?.adapter?.notifyDataSetChanged()
        commuview?.progressBar2?.visibility = View.GONE
        if(submitFlag==1) {
            recycler?.scrollToPosition(0)
        }
    }

    fun addItem(id: Int, linkUrl: String, icon: String, title: String, linkChannel: String
                , writer : String , likeNumber: Int, commentNumber : Int) {

        var item: RecyclerItem? =
            RecyclerItem()

        item?.id = id
        item?.linkUri = linkUrl
        item?.iconUri = icon
        item?.titleStr = title
        item?.writer = writer
        item?.channelStr = linkChannel
        item?.likeNumber = likeNumber
        item?.commentNumber = commentNumber

//        tempList.add(item!!)
        mList.add(item!!)
    }

}