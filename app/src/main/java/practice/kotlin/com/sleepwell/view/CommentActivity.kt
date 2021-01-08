package practice.kotlin.com.sleepwell.view

import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import kotlinx.android.synthetic.main.activity_comment.*
import practice.kotlin.com.sleepwell.ClickEvents
import practice.kotlin.com.sleepwell.R
import practice.kotlin.com.sleepwell.onItemClick
import practice.kotlin.com.sleepwell.recycler.CommentRecycler
import practice.kotlin.com.sleepwell.sleepAndCommu.CommentDialog
import practice.kotlin.com.sleepwell.statics.JsonString.Companion.isCommentFirstLoading
import practice.kotlin.com.sleepwell.statics.commuList
import practice.kotlin.com.sleepwell.statics.commuList.Companion.cList
import practice.kotlin.com.sleepwell.statics.commuList.Companion.commentActivityCnt
import practice.kotlin.com.sleepwell.statics.commuList.Companion.commentPosition
import practice.kotlin.com.sleepwell.statics.commuList.Companion.commentView
import practice.kotlin.com.sleepwell.statics.commuList.Companion.isRere


class CommentActivity  : YouTubeBaseActivity(), onItemClick {
    var contentUid : Int = 0
    var videoId : String? = ""
    var isFullScreen = false
    var waitLoading = true
    lateinit var youtubeplayer : YouTubePlayer
    var isyoutubeinit :Boolean = false
    var view2 : View? = null
    var er = 0

    // (취소(키보드를 사라지게 만들때)를 누를때,현재 상태를 알기 위해서 )

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        commentActivityCnt += 1
        isCommentFirstLoading = true
        cList.clear()
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.activity_comment,null)
        commentView = view
        setContentView(view)

        view2 = window.decorView
        var  config : Configuration = getResources().getConfiguration()
        commentRecyclerProgress.visibility = View.GONE
        val linearLayoutManagerWrapepr = LinearLayoutManagerWrapper(this, LinearLayoutManager.VERTICAL, false)

        contentUid = intent.getIntExtra("contentUid", 0)
        commuList.idx = contentUid
        videoId = intent.getStringExtra("videoId")
        Log.d("CommentActivity contentUid",contentUid.toString())
        videoId?.let { Log.d("playvid url" , it) }
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


            ClickEvents().startCommentLoading(contentUid, imageView2, recyclerComment, commentRecyclerProgress, commentActivityCnt)

            var commentAdapter  = CommentRecycler(applicationContext, cList, this)
            recyclerComment.adapter = commentAdapter
            recyclerComment.layoutManager = linearLayoutManagerWrapepr
            recyclerComment.itemAnimator = DefaultItemAnimator()

            recyclerComment.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val lastPosition =
                        (recyclerComment.layoutManager as LinearLayoutManagerWrapper?)!!.findLastCompletelyVisibleItemPosition()
                    val totalCount = commentAdapter.itemCount - 1

//                    if (dy < -10 && !(lastPosition<10) && !isRefresh) {
//                        Log.d("dy값", dy.toString())
//                        isRefresh = true
//                        view.floatingActionButton.visibility = View.VISIBLE
//                    } else if(dy > 10){
//                        if(isRefresh)
//                            isRefresh = !isRefresh
//                        Log.d("dy값", dy.toString())
//                        view.floatingActionButton.visibility = View.GONE
//                    }


                    Log.d("안함", lastPosition.toString() + "\n" + totalCount.toString())

                    if (lastPosition == totalCount && lastPosition!=-1 && totalCount > 9) {
                        commentRecyclerProgress.visibility = View.VISIBLE
                        val a = commentActivityCnt
                        if(waitLoading) {
                            waitLoading = false
                            Handler().postDelayed({
                                commentPosition += 1
                                ClickEvents().startCommentLoading(contentUid, imageView2, recyclerComment ,commentRecyclerProgress, a)
                                waitLoading = true
                            }, 1500)
                        }

                    }
                }
            })

            setUpButton()

        }
//            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_NOSENSOR


    }


    //reconfigure display properties on screen rotation
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        //Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.d("뭔데","d")
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.d("뭔데","dd")
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

    override fun onClick(position: Int) {
        super.onClick(position)
        Log.d("dd", position.toString())
        recyclerComment.smoothScrollToPosition(position)
    }

    override fun onBackPressed() {
        if (isFullScreen){
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            youtubeplayer.setFullscreen(false)
        } else if(!isFullScreen && isyoutubeinit){
            super.onBackPressed()
            isCommentFirstLoading = false
            commentPosition = 0
            cList.clear()
        } else {
            er+=1
            if(er==10)
                super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isyoutubeinit = false
        isCommentFirstLoading = false
        commentPosition = 0
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


    class LinearLayoutManagerWrapper(context: Context, orientation: Int, reverseLayout: Boolean)
        : LinearLayoutManager(context, orientation, reverseLayout) {
        override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (e: IndexOutOfBoundsException) {
                Log.e("probe", "meet a IOOBE in RecyclerView");

            }

        }
        override fun supportsPredictiveItemAnimations(): Boolean { return false }
    }

    fun playVideo(videoId: String?, youTubePlayerView: YouTubePlayerView) {
        //initialize youtube player view
        try {
            youTubePlayerView.initialize("develop",
                object : YouTubePlayer.OnInitializedListener {
                    override fun onInitializationSuccess(
                        provider: YouTubePlayer.Provider,
                        youTubePlayer: YouTubePlayer, b: Boolean) {

                        youtubeplayer = youTubePlayer

                        youTubePlayer.cueVideo(videoId)
                        isyoutubeinit = true
                        youTubePlayer.setPlayerStateChangeListener(object : YouTubePlayer.PlayerStateChangeListener {
                            override fun onAdStarted() {}
                            override fun onLoading() {}
                            override fun onVideoStarted() {}
                            override fun onVideoEnded() {}
                            override fun onError(p0: YouTubePlayer.ErrorReason) {}
                            override fun onLoaded(videoId: String) {
                                youTubePlayer.play()
                            }

                        })

                        youTubePlayer.setOnFullscreenListener {
                            if (it) {
                                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                                isFullScreen = it
                            } else {
                                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                                isFullScreen = it
                            }

                        }
                    }


                    override fun onInitializationFailure(
                        provider: YouTubePlayer.Provider,
                        youTubeInitializationResult: YouTubeInitializationResult
                    ) {
                        isyoutubeinit = true
                        Log.d("youtube", "Fail $youTubeInitializationResult")
                    }

                })
        } catch (e: Exception){
            Log.d("youtube", e.toString())
        }
    }


}