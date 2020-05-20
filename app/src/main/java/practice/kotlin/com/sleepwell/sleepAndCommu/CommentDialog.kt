package practice.kotlin.com.sleepwell.sleepAndCommu

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.TextUtils
import android.util.Log
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_comment.*
import kotlinx.android.synthetic.main.comment_submit_dialog.*
import kotlinx.android.synthetic.main.delete_fire.*
import org.jetbrains.anko.toast
import practice.kotlin.com.sleepwell.ClickEvents
import practice.kotlin.com.sleepwell.R
import practice.kotlin.com.sleepwell.statics.JsonString.Companion.macAddress
import practice.kotlin.com.sleepwell.statics.commuList.Companion.isRere


class CommentDialog(private val context: Activity) {

    // 댓글 다이얼로그 함수를 정의한다.
    fun requestSendComment(
        id: Int, content: String, context: Context?,
        recycler: RecyclerView, imageView: ImageView) {

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        val dlg = Dialog(this.context)
        var password = macAddress.substring(0, macAddress.indexOf(":"))

        var index = macAddress.lastIndexOf(":")
        var secondindex = macAddress.substring(0, index)

        var writer = "익명" + macAddress.substring(index+1,
            macAddress.length) + secondindex.substring(secondindex.lastIndexOf(":")+1, secondindex.length)
        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.comment_submit_dialog)
        dlg.askRere.setText("댓글을 게시하시겠습니까??")

        // 커스텀 다이얼로그를 노출한다.
        dlg.show()
        dlg.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        dlg.sendCommentSubmit.setOnClickListener {
            if (content.length <= 5) {
                this.context.toast("단순한 댓글은 허용하지 않습니다!")
            } else {
                if(!TextUtils.isEmpty(dlg.passwordSubmitComment.text.toString()))
                    password = dlg.passwordSubmitComment.text.toString()
                if(!TextUtils.isEmpty(dlg.writerSubmitComment.text.toString()))
                    writer = dlg.writerSubmitComment.text.toString()
                Log.d("다이얼로그","$id" + password + writer)
                ClickEvents().sendCommentF(id, writer, password, content, context, recycler, imageView)
                dlg.dismiss()
            }
        }
        dlg.closeCommentSubmit.setOnClickListener {

            isRere = false
            this.context.comment.hint = "댓글"
            dlg.dismiss()
        }
    }

    // 대댓글 다이얼로그 함수를 정의한다.
    fun requestSendReComment(
        contentUid: Int,
        rid: Int,
        replycontent: String,
        applicationContext: Context,
        recyclerComment: RecyclerView,
        imageView2: ImageView) {

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        val dlg = Dialog(context)
        var password = macAddress.substring(0, macAddress.indexOf(":"))

        var index = macAddress.lastIndexOf(":")
        var secondindex = macAddress.substring(0, index)

        var writer = "익명" + macAddress.substring(index+1,
            macAddress.length) + secondindex.substring(secondindex.lastIndexOf(":")+1, secondindex.length)

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.comment_submit_dialog)
        dlg.askRere.setText("해당 댓글에 답글을 게시하시겠습니까??")

        // 커스텀 다이얼로그를 노출한다.
        dlg.show()
        dlg.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        dlg.sendCommentSubmit.setOnClickListener {
            if (replycontent.length <= 5) {
                context.toast("단순한 댓글은 허용하지 않습니다!")
            } else {
                if(!TextUtils.isEmpty(dlg.passwordSubmitComment.text.toString()))
                    password = dlg.passwordSubmitComment.text.toString()
                if(!TextUtils.isEmpty(dlg.writerSubmitComment.text.toString()))
                    writer = dlg.writerSubmitComment.text.toString()

                ClickEvents().sendReCommentF(
                    contentUid,
                    rid, writer, password,
                    replycontent,
                    applicationContext,
                    recyclerComment,
                    imageView2
                )
                dlg.dismiss()
            }
        }
        dlg.closeCommentSubmit.setOnClickListener {
            isRere = false
            this.context.comment.hint = "댓글"
            dlg.dismiss()
        }
    }



    fun requestDeleteComment(
        contentUid: Int, id: Int, context: Context,
        recycler: RecyclerView, imageView: ImageView) {

        val dlg = Dialog(this.context)
        var password = macAddress.substring(0, macAddress.indexOf(":"))

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

            ClickEvents().deleteComment(contentUid, id , password, context ,
                recycler, imageView)

            dlg.dismiss()
            
        }
        dlg.fireButton.setOnClickListener {// 신고

            ClickEvents().fireComment(id, context)
            dlg.dismiss()
        }
    }

    fun requestRereDeleteDialog(contentUid: Int, id: Int, context: Context,
                                recycler: RecyclerView, imageView: ImageView, delete : String){
        val dlg = Dialog(this.context)
        var password = macAddress.substring(0, macAddress.indexOf(":"))
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
            ClickEvents().deleteReComment(contentUid, id , password, context ,
                recycler, imageView)

            dlg.dismiss()

        }
        dlg.fireButton.setOnClickListener {// 신고
            ClickEvents().fireReComment(id, context)
            dlg.dismiss()
        }

    }


}
