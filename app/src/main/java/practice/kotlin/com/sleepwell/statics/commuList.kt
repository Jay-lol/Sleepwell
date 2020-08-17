package practice.kotlin.com.sleepwell.statics

import android.view.View
import practice.kotlin.com.sleepwell.alarm.AlarmItem
import practice.kotlin.com.sleepwell.alarm.AlarmTable
import practice.kotlin.com.sleepwell.recycler.CommentItem
import practice.kotlin.com.sleepwell.recycler.RecyclerItem

class commuList {
    companion object{
        var mList = arrayListOf<RecyclerItem>()
        var cList = arrayListOf<CommentItem>()
        var aList = arrayListOf<AlarmItem>()
        var commuview : View? = null
        var commentView : View? = null
        var likeOrRecentOrHot : Int = 2
        var position  = 0
        var commentPosition = 0

        var commentActivityCnt = 0

        var isRere : Boolean = false // 댓글 : false(Default), 대댓글 : true

        // 댓글,대댓글 양식
        var idx : Int = -1
        var rid : Int = 0


    }
}