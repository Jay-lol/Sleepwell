package practice.kotlin.com.sleepwell

import android.util.Log

interface clickBoard {
    fun sendBoard(id : Int, bool: Boolean?, position: Int, idx : Int)
    fun sendDeIdx(idx : Long? , position : Int)
    fun changeData(idx: Long, position : Int) {}
}