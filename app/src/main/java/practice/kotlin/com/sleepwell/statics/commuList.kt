package practice.kotlin.com.sleepwell.statics

import practice.kotlin.com.sleepwell.recycler.CommentItem
import practice.kotlin.com.sleepwell.recycler.RecyclerItem

class commuList {
    companion object{
        var mList = arrayListOf<RecyclerItem>()
        var cList = arrayListOf<CommentItem>()
    }
}