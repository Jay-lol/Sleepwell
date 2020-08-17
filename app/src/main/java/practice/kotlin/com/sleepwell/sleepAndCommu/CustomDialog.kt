package practice.kotlin.com.sleepwell.sleepAndCommu

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.delete_fire.*
import kotlinx.android.synthetic.main.fragment_commu.*
import kotlinx.android.synthetic.main.submit_dialog.*
import org.jetbrains.anko.toast
import practice.kotlin.com.sleepwell.ClickEvents
import practice.kotlin.com.sleepwell.R
import practice.kotlin.com.sleepwell.Utils
import practice.kotlin.com.sleepwell.Validator
import practice.kotlin.com.sleepwell.statics.JsonString.Companion.macAddress


class CustomDialog(private val context : Activity){

    // 호출할 다이얼로그 함수를 정의한다.
    fun callFunction(recycler: RecyclerView) {
        var password = macAddress.substring(0, macAddress.indexOf(":"))
        password += "1919"
        var index = macAddress.lastIndexOf(":")
        var secondindex = macAddress.substring(0, index)

        var writer = "익명" + macAddress.substring(index+1,
            macAddress.length) + secondindex.substring(secondindex.lastIndexOf(":")+1, secondindex.length)

        var writerTitle = "null@title"

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        val dlg = Dialog(context)


        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.submit_dialog)



        // 커스텀 다이얼로그를 노출한다.
        dlg.show()
        dlg.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        dlg.sendSubmit.setOnClickListener{
            if(dlg.linkSubmit.text.toString() == ""){
                context.toast("링크는 반드시 입력하셔야합니다!")
            } else if(!Validator().isUrl(dlg.linkSubmit.text.toString())){
                context.toast("잘못된 링크 형식입니다!!")
            } else{

                if(!TextUtils.isEmpty(dlg.passwordSubmitBoard.text.toString()))
                    password = dlg.passwordSubmitBoard.text.toString()
                if(!TextUtils.isEmpty(dlg.writerSubmit.text.toString()))
                    writer = dlg.writerSubmit.text.toString()
                if(!TextUtils.isEmpty(dlg.titleSubmit.text.toString()))
                    writerTitle = dlg.titleSubmit.text.toString()

                Utils(context).getMetaDataFromUrl(writer, password, writerTitle ,dlg.linkSubmit.text.toString(), context
                ,recycler)
                context.progressBar3.visibility = View.VISIBLE
                context.window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                dlg.dismiss()
            }
        }
        dlg.closeSubmit.setOnClickListener{

            dlg.dismiss()
        }

    }



    // 게시글 신고삭제
    fun requestDeleteFireBoard(id: Int, recycler: RecyclerView, position : Int, idx : Int){
        var password = macAddress.substring(0, macAddress.indexOf(":"))
        password += "1919"

        val dlg = Dialog(this.context)

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.delete_fire)

        // 커스텀 다이얼로그를 노출한다.
        dlg.show()
        dlg.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))



        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        dlg.deleteButton.setOnClickListener {   // 삭제
            Log.d("ss","삭제")

            if(!TextUtils.isEmpty(dlg.deleteCommentPassword.text.toString()))
                password = dlg.deleteCommentPassword.text.toString()
            context.progressBar3.visibility=View.VISIBLE
            context.window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            ClickEvents().deleteBoard(id, password, context, recycler, position, idx)
            dlg.dismiss()

        }
        dlg.fireButton.setOnClickListener {// 신고
            Log.d("ss","신고")
//            ClickEvents().fireReComment(id, context)
            ClickEvents().fireBoard(id, context)
            dlg.dismiss()
        }

    }



}