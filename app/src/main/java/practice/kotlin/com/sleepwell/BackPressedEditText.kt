package practice.kotlin.com.sleepwell

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import android.widget.EditText
import practice.kotlin.com.sleepwell.statics.commuList.Companion.isRere


class BackPressedEditText(context: Context?,  attributeSet : AttributeSet?) :
    androidx.appcompat.widget.AppCompatEditText(context, attributeSet) {

    override fun onKeyPreIme(keyCode: Int, event: KeyEvent): Boolean {

        if(keyCode== KeyEvent.KEYCODE_BACK){
            isRere = false
            hint = "댓글"
            clearFocus()
            setText("")
            Log.d("키보드 뒤로가기", " 코딩 몬스터")
        }

        return false
    }

}