package practice.kotlin.com.sleepwell

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import practice.kotlin.com.sleepwell.fragment.CommuFrag


class LoadingActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        ClickEvents().startLoading()
        startLoading()
    }

    private fun startLoading() {
        val handler = Handler()
        handler.postDelayed(Runnable { finish() }, 2000)
    }
}