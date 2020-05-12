package practice.kotlin.com.sleepwell.statics

import practice.kotlin.com.sleepwell.recycler.CommentItem
import practice.kotlin.com.sleepwell.recycler.RecyclerItem

class commuList {
    companion object{
        var mList = arrayListOf<RecyclerItem>()
        var cList = arrayListOf<CommentItem>()
        var check : Boolean = true
        var isRere : Boolean = false // 댓글 : false(Default), 대댓글 : true

        // 댓글,대댓글 양식
        var idx : Int? = null
        var rid : Int = 0
        var replycontent : String = ""


    }
}