package practice.kotlin.com.sleepwell.view

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import practice.kotlin.com.sleepwell.ClickEvents
import practice.kotlin.com.sleepwell.R
import practice.kotlin.com.sleepwell.statics.JsonString
import practice.kotlin.com.sleepwell.statics.commuList.Companion.mList
import practice.kotlin.com.sleepwell.statics.commuList.Companion.position


class SplashActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(Color.parseColor("#3B4CA8"))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }
        position = 0
        mList.clear()
        ClickEvents().startThumnailLoading(JsonString.cnt, null, null, null)
        startLoading()
    }

    private fun startLoading() {
        val handler = Handler()
        handler.postDelayed(Runnable { finish() }, 2000)
    }
}