package practice.kotlin.com.sleepwell

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_tab1.view.*
import org.jetbrains.anko.*
import practice.kotlin.com.sleepwell.adapter.PageAdapter
import practice.kotlin.com.sleepwell.alarm.AlarmFrag
import practice.kotlin.com.sleepwell.sleepAndCommu.CommuFrag
import practice.kotlin.com.sleepwell.sleepAndCommu.SleepFrag
import practice.kotlin.com.sleepwell.statics.JsonString
import java.net.NetworkInterface
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var mContext: Context
    private val REQUEST_ACCESS_NETWORK_STATE = 1000
    private lateinit var macAddress : String
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

        val adapter =
            PageAdapter(supportFragmentManager) // PageAdapter 생성후 PageAdapter에 추가!
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

    override fun onResume() {
        super.onResume()
        permissionCheck(cancel = {
        }, ok = {
            macAddress = getMACAddress("wlan0")
        })
    }

    private fun permissionCheck(cancel: () -> Unit, ok: () -> Unit) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            //권한 거부일 경우
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_NETWORK_STATE)) {
                // 이전에 권한 한번 거부했을 경우
                cancel()
            } else {
                // 권한 요청
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.ACCESS_NETWORK_STATE),
                    REQUEST_ACCESS_NETWORK_STATE
                )
            }
        } else {
            ok()
        }
    }

    private fun sendMacAddress(){
        val wifi = getSystemService(Context.WIFI_SERVICE) as WifiManager
        val info = wifi.connectionInfo
        val macAddress = info.macAddress
        Log.i("Mac: %s", macAddress )
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            REQUEST_ACCESS_NETWORK_STATE -> {
                if((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)){
                    sendMacAddress()
                } else {
                    // 거부시
                    toast("권한 거부 됨")
                }
                return
            }
        }

    }
    fun getMACAddress(interfaceName: String?): String {
        try {
            val interfaces: List<NetworkInterface> =
                Collections.list(NetworkInterface.getNetworkInterfaces())
            for (intf in interfaces) {
                if (interfaceName != null) {
                    if (!intf.getName().equals(interfaceName, ignoreCase = true)) continue
                }
                val mac: ByteArray = intf.getHardwareAddress() ?: return ""
                val buf = StringBuilder()
                for (idx in mac.indices) buf.append(String.format("%02X:", mac[idx]))
                if (buf.length > 0) buf.deleteCharAt(buf.length - 1)
                Log.i("Mac %s", buf.toString())
                JsonString.macAddress = buf.toString()
                return buf.toString()
            }
        } catch (ex: Exception) {
        } // for now eat exceptions
        return ""
    }
}
