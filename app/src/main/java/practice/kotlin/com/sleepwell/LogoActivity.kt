package practice.kotlin.com.sleepwell

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class LogoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        movePage()
    }
    private fun movePage(){
        startActivity(Intent(this, MainActivity::class.java))
    }
}