package practice.kotlin.com.sleepwell

import practice.kotlin.com.sleepwell.recycler.CommentRecycler

interface onItemClick {
    fun onClick(holder: CommentRecycler.cViewH)
    fun onClick(like: String)
    fun onClick(delete : String ,rid : Int)
}