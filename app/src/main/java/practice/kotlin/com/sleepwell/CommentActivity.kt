package practice.kotlin.com.sleepwell

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import kotlinx.android.synthetic.main.activity_comment.*
import org.jetbrains.anko.toast
import practice.kotlin.com.sleepwell.recycler.CommentRecycler
import practice.kotlin.com.sleepwell.sleepAndCommu.CommentDialog
import practice.kotlin.com.sleepwell.statics.commuList
import practice.kotlin.com.sleepwell.statics.commuList.Companion.cList
import practice.kotlin.com.sleepwell.statics.commuList.Companion.isRere


class CommentActivity  : YouTubeBaseActivity(), onItemClick {
    var contentUid : Int = 0
    var videoId : String = ""
    var isFullScreen = false
    lateinit var youtubeplayer : YouTubePlayer

    // (취소(키보드를 사라지게 만들때)를 누를때,현재 상태를 알기 위해서 )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

        var  config : Configuration = getResources().getConfiguration()


        val linearLayoutManagerWrapepr = LinearLayoutManagerWrapper(this, LinearLayoutManager.VERTICAL, false)

        contentUid = intent.getIntExtra("contentUid", 0)
        commuList.idx = contentUid
        videoId = intent.getStringExtra("videoId")
        Log.d("CommentActivity contentUid",contentUid.toString())


        playVideo(videoId, you_tube_player_view)

        if(config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerComment.setOnTouchListener { v, event ->
                val imm =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(comment.getWindowToken(), 0)
                Log.d("", "화면밖클릭")
                comment.clearFocus()
                comment.setText("")
                isRere = false
                comment.hint = "댓글"
                false
            }

            findViewById<EditText>(R.id.comment).setOnEditorActionListener { v, actionId, event ->
                return@setOnEditorActionListener when (actionId) {
                    EditorInfo.IME_ACTION_SEND -> {
                        sendMessage()
                        true
                    }
                    else -> false
                }
            }


            ClickEvents().startCommentLoading(contentUid, imageView2, recyclerComment)

            recyclerComment.adapter = CommentRecycler(applicationContext, cList, this)
            recyclerComment.layoutManager = linearLayoutManagerWrapepr
            recyclerComment.itemAnimator = DefaultItemAnimator()

            setUpButton()

        }

    }

    private fun sendMessage() {
        Log.d("condent ", "$contentUid")


        if(isRere){ // 대댓글이면

            applicationContext.hideKeyboard()
            // 전역변수 rid 댓글의 아이디를 넘겨주는게 핵심
            CommentDialog(this).requestSendReComment(contentUid, commuList.rid, comment.text.toString(),
                applicationContext,recyclerComment,imageView2)
            comment.clearFocus()
            comment.setText("")
            isRere = false
            comment.hint = "댓글"
            Log.d("zzzzz","대댓글제출")
        }
        else {
            applicationContext.hideKeyboard()

            CommentDialog(this).requestSendComment(contentUid, comment.text.toString(),
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
        commuList.rid = holder.rid
        Log.d("대댓글클릭","대댓글")
        isRere = true
        comment.hint = "RE: 댓글"
    }


    override fun onClick(like: String){
        toast(like + "!!")
    }

    override fun onClick(delete :  String , rid : Int){
        commuList.rid = rid
        if(delete == "comment") {
            Log.d("댓글삭제신고 클릭", "클릭")
            CommentDialog(this).requestDeleteComment(
                commuList.idx, commuList.rid, applicationContext,
                recyclerComment, imageView2
            )
         } else{
            Log.d("대댓글삭제신고 클릭", "클릭")
            CommentDialog(this).requestRereDeleteDialog(
                commuList.idx, commuList.rid, applicationContext,
                recyclerComment, imageView2, "DELTE"
            )
        }

    }


    override fun onBackPressed() {
        if (isFullScreen){
            youtubeplayer.setFullscreen(false);
        } else {
            super.onBackPressed()
            cList.clear()
        }
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


    class LinearLayoutManagerWrapper(context: Context, orientation: Int, reverseLayout: Boolean)
        : LinearLayoutManager(context, orientation, reverseLayout) {

        override fun supportsPredictiveItemAnimations(): Boolean { return false } }

    fun playVideo(videoId: String?, youTubePlayerView: YouTubePlayerView) {
        //initialize youtube player view
        youTubePlayerView.initialize("develop",
            object : YouTubePlayer.OnInitializedListener {
                override fun onInitializationSuccess(
                    provider: YouTubePlayer.Provider,
                    youTubePlayer: YouTubePlayer, b: Boolean) {

                    youtubeplayer = youTubePlayer

                    youTubePlayer.cueVideo(videoId)

                    youTubePlayer.setPlayerStateChangeListener(object : YouTubePlayer.PlayerStateChangeListener {
                        override fun onAdStarted() {}
                        override fun onLoading() {}
                        override fun onVideoStarted() {}
                        override fun onVideoEnded() {}
                        override fun onError(p0: YouTubePlayer.ErrorReason) {}
                        override fun onLoaded(videoId: String) { youTubePlayer.play() }

                 })


                    youTubePlayer.setOnFullscreenListener { b ->

                        isFullScreen = b

                    }
                }



                override fun onInitializationFailure(
                    provider: YouTubePlayer.Provider,
                    youTubeInitializationResult: YouTubeInitializationResult
                ) {
                }

            })
    }

}