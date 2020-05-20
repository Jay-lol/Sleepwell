package practice.kotlin.com.sleepwell.statics

import practice.kotlin.com.sleepwell.alarm.AlarmItem
import practice.kotlin.com.sleepwell.alarm.AlarmTable
import practice.kotlin.com.sleepwell.recycler.CommentItem
import practice.kotlin.com.sleepwell.recycler.RecyclerItem

class commuList {
    companion object{
        var mList = arrayListOf<RecyclerItem>()
        var cList = arrayListOf<CommentItem>()
        var aList = arrayListOf<AlarmItem>()

        var likeOrRecent : Int = 1
        var position  = 0

        var isRere : Boolean = false // 댓글 : false(Default), 대댓글 : true

        // 댓글,대댓글 양식
        var idx : Int = -1
        var rid : Int = 0


    }
}