package practice.kotlin.com.sleepwell

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View.OnTouchListener
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_comment.*
import practice.kotlin.com.sleepwell.recycler.CommentRecycler
import practice.kotlin.com.sleepwell.sleepAndCommu.CommentDialog
import practice.kotlin.com.sleepwell.statics.commuList
import practice.kotlin.com.sleepwell.statics.commuList.Companion.cList
import practice.kotlin.com.sleepwell.statics.commuList.Companion.isRere


class CommentActivity  : AppCompatActivity(), onItemClick {
    var contentUid : Int = 0

    // (취소(키보드를 사라지게 만들때)를 누를때,현재 상태를 알기 위해서 )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

        contentUid = intent.getIntExtra("contentUid", 0)
        commuList.idx = contentUid

//        this.window.setSoftInputMode(
//
//            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)


        recyclerComment.setOnTouchListener(OnTouchListener { v, event ->
            val imm =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(comment.getWindowToken(), 0)
            Log.d("", "화면밖클릭")
            isRere = false
            comment.hint = "댓글"
            false
        })

        findViewById<EditText>(R.id.comment).setOnEditorActionListener { v, actionId, event ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEND -> {
                    sendMessage()
                    true
                }
                else -> false
            }
        }


        ClickEvents().StartCommentLoading(contentUid, imageView2, recyclerComment)

        recyclerComment.adapter = CommentRecycler(this, cList, this)
        recyclerComment.layoutManager = LinearLayoutManager(this)
        recyclerComment.itemAnimator = DefaultItemAnimator()
//        (recyclerComment.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(0, 20)

        setUpButton()


    }

    private fun sendMessage() {
        Log.d("condent ", "$contentUid")


        if(isRere){ // 대댓글이면

            applicationContext.hideKeyboard()


            CommentDialog(this).callFunction(contentUid,commuList.rid, comment.text.toString(),
                applicationContext,recyclerComment,imageView2)
            comment.clearFocus()
            comment.setText("")
            isRere = false
            comment.hint = "댓글"
            Log.d("zzzzz","대댓글제출")
        }
        else {
            applicationContext.hideKeyboard()

            CommentDialog(this).callFunction(contentUid, comment.text.toString(),
                applicationContext,recyclerComment,imageView2)


            comment.clearFocus()
            comment.setText("")
            isRere = false
            comment.hint = "댓글"
            Log.d("hhh", "댓글제출")
        }
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when(keyCode) {

            KeyEvent.KEYCODE_BACK -> Log.d("wp", "dsadsad")
        }

        return super.onKeyDown(keyCode, event)
    }

    private fun setUpButton() { // 게시

        sendCommentButton.setOnClickListener{
            sendMessage()
        }
    }

    override fun onClick(holder: CommentRecycler.cViewH) {  // 대댓글클릭시
        applicationContext.showKeyboard()
        commuList.rid = holder.rid!!
        Log.d("대댓글클릭","대댓글")
        isRere = true
        comment.hint = "RE: 댓글"
    }

    override fun onResume() {
        if(isRere)
            comment.hint = "RE: 댓글"
        else
            comment.hint = "댓글"
        super.onResume()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        cList.clear()
    }

    fun Context.hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val a = getWindow().getDecorView().getRootView().getWindowToken()
        imm.hideSoftInputFromWindow(a, 0)
    }

    fun Context.showKeyboard() {
        comment.requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED ,InputMethodManager.HIDE_IMPLICIT_ONLY)
    }




}