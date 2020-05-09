package practice.kotlin.com.sleepwell

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ContentView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_comment.*
import kotlinx.android.synthetic.main.fragment_commu.view.*
import practice.kotlin.com.sleepwell.recycler.CommentRecycler
import practice.kotlin.com.sleepwell.sleepAndCommu.CommuFrag
import practice.kotlin.com.sleepwell.statics.commuList.Companion.cList

class CommentActivity  : AppCompatActivity() {
    var contentUid : Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)
        contentUid = intent.getIntExtra("contentUid", 0)

        ClickEvents().StartCommentLoading(contentUid, imageView2, recyclerComment)

        recyclerComment.layoutManager = LinearLayoutManager(this)
        recyclerComment.adapter = CommentRecycler(applicationContext, cList)

        sendCommentButton.setOnClickListener{
            applicationContext.hideKeyboard()
            Log.d("condent ", "$contentUid")
            ClickEvents().sendCommentF(contentUid, comment.text.toString() ,applicationContext,recyclerComment, imageView2)
            comment.setText("")
            comment.clearFocus()
        }

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

}