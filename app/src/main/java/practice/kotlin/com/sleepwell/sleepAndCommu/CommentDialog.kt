package practice.kotlin.com.sleepwell.sleepAndCommu

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_comment.*
import kotlinx.android.synthetic.main.comment_submit_dialog.*
import kotlinx.android.synthetic.main.submit_dialog.*
import kotlinx.android.synthetic.main.submit_dialog.cancelButton
import kotlinx.android.synthetic.main.submit_dialog.okButton
import org.jetbrains.anko.toast
import practice.kotlin.com.sleepwell.ClickEvents
import practice.kotlin.com.sleepwell.R
import practice.kotlin.com.sleepwell.statics.commuList.Companion.isRere


class CommentDialog(private val context : Activity) {

    // 대댓글 다이얼로그 함수를 정의한다.
    fun callFunction(
        contentUid: Int,
        rid: Int,
        replycontent: String,
        applicationContext: Context,
        recyclerComment: RecyclerView,
        imageView2: ImageView
    ) {

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        val dlg = Dialog(context)

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.comment_submit_dialog)
        dlg.askRere.setText("해당 댓글에 답글을 게시하시겠습니까??")

        // 커스텀 다이얼로그를 노출한다.
        dlg.show()

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        dlg.okButton.setOnClickListener {
            if (replycontent.length <= 10) {
                context.toast("단순한 댓글은 허용하지 않습니다!")
            } else {
                ClickEvents().sendReCommentF(contentUid, rid , replycontent , applicationContext, recyclerComment, imageView2 )
                dlg.dismiss()
            }
        }
        dlg.cancelButton.setOnClickListener {
            isRere = false
            this.context.comment.hint = "댓글"
            dlg.dismiss()
        }
    }

    // 댓글 다이얼로그 함수를 정의한다.
    fun callFunction(id: Int?, content : String ,context: Context?,
                     recycler: RecyclerView ,imageView: ImageView) {
        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        val dlg = Dialog(this.context)

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.comment_submit_dialog)
        dlg.askRere.setText("댓글을 게시하시겠습니까??")

        // 커스텀 다이얼로그를 노출한다.
        dlg.show()

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        dlg.okButton.setOnClickListener {
            if (content.length <= 10) {
                this.context.toast("단순한 댓글은 허용하지 않습니다!")
            } else {
                ClickEvents().sendCommentF(id ,content, context, recycler, imageView )
                dlg.dismiss()
            }
        }
        dlg.cancelButton.setOnClickListener {

            isRere = false
            this.context.comment.hint = "댓글"
            dlg.dismiss()
        }


    }
}