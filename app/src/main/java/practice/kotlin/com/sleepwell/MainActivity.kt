package practice.kotlin.com.sleepwell

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.custom_tab1.view.*
import org.jetbrains.anko.startActivity
import practice.kotlin.com.sleepwell.fragment.AlarmFrag
import practice.kotlin.com.sleepwell.fragment.CommuFrag
import practice.kotlin.com.sleepwell.fragment.SleepFrag


class MainActivity : AppCompatActivity() {

    private lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mContext = applicationContext

        startActivity<LoadingActivity>()

        // 모든 버튼 클릭관리

        val sleepFragment = SleepFrag()
        sleepFragment.name = "첫번째 창"
        val alarmFragment = AlarmFrag()
        val commuFragment = CommuFrag()

        val adapter = PageAdapter(supportFragmentManager) // PageAdapter 생성후 PageAdapter에 추가!
        adapter.addItems(sleepFragment)
        adapter.addItems(alarmFragment)
        adapter.addItems(commuFragment)

        // 중요!
        main_viewPager.setAdapter(adapter) // 뷰페이저에 adapter 장착
        main_tablayout.setupWithViewPager(main_viewPager) // 탭레이아웃과 뷰페이저를 연동


        main_tablayout.getTabAt(0)?.setCustomView(createView("취침시간"))
        main_tablayout.getTabAt(1)?.setCustomView(createView("알람"))
        main_tablayout.getTabAt(2)?.setCustomView(createView("커뮤니티"))

    }


    private fun createView(tabName: String): View {
        var tabView = LayoutInflater.from(mContext).inflate(R.layout.custom_tab1, null)
        // custom tab1의 tab_text의 text를 tabname으로
        tabView.tab_text.text = tabName
        when (tabName) {
            "취침시간" -> {
                tabView.tab_logo.setImageResource(android.R.drawable.ic_menu_day)
                return tabView
            }
            "알람" -> {
                tabView.tab_logo.setImageResource(android.R.drawable.ic_menu_agenda)
                return tabView
            }
            "커뮤니티" -> {
                tabView.tab_logo.setImageResource(android.R.drawable.ic_menu_call)
                return tabView
            }
            else -> {
                return tabView
            }
        }

    }
}
