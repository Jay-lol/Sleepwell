package practice.kotlin.com.sleepwell

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import practice.kotlin.com.sleepwell.statics.commuList.Companion.isRere


class BackPressedEditText(context: Context,  attributeSet : AttributeSet?) :
    androidx.appcompat.widget.AppCompatEditText(context, attributeSet) {

    private val TAG: String = "로그"

    override fun onKeyPreIme(keyCode: Int, event: KeyEvent): Boolean {

        if(keyCode== KeyEvent.KEYCODE_BACK){
            isRere = false
            hint = "댓글"
            clearFocus()
            setText("")
            Log.d(TAG, "키보드 뒤로가기")
        }

        return false
    }

}