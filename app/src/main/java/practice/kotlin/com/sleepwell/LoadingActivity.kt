package practice.kotlin.com.sleepwell

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import practice.kotlin.com.sleepwell.statics.JsonString


class LoadingActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        ClickEvents().StartThumnailLoading(JsonString.cnt)
        startLoading()
    }

    private fun startLoading() {
        val handler = Handler()
        handler.postDelayed(Runnable { finish() }, 2000)
    }
}