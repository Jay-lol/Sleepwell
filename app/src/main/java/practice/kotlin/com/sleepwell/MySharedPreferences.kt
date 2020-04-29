package practice.kotlin.com.sleepwell

import android.content.Context
import android.content.SharedPreferences

class MySharedPreferences(context: Context) {
    private val defaultInt = 0

    private val instances: SharedPreferences by lazy{
        context.getSharedPreferences("test", Context.MODE_PRIVATE)
    }
}