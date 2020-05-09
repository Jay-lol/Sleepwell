package practice.kotlin.com.sleepwell.sleepAndCommu

import android.app.Activity
import android.app.Dialog
import kotlinx.android.synthetic.main.submit_dialog.*
import okhttp3.internal.Util
import org.jetbrains.anko.toast
import practice.kotlin.com.sleepwell.ClickEvents
import practice.kotlin.com.sleepwell.R
import practice.kotlin.com.sleepwell.Utils
import practice.kotlin.com.sleepwell.Validator


class CustomDialog(private val context : Activity){

    // 호출할 다이얼로그 함수를 정의한다.
    fun callFunction() {

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        val dlg = Dialog(context)

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.submit_dialog)

        // 커스텀 다이얼로그를 노출한다.
        dlg.show()

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        dlg.okButton.setOnClickListener{
            if(dlg.linkSubmit.text.toString() == ""){
                context.toast("링크는 반드시 입력하셔야합니다!")
            } else if(!Validator().isUrl(dlg.linkSubmit.text.toString())){
                context.toast("잘못된 링크 형식입니다!!")
            } else{
                Utils(context).getMetaDataFromUrl(dlg.linkSubmit.text.toString())
                ClickEvents().cal(it , context, dlg)
                dlg.dismiss()
            }
        }
        dlg.cancelButton.setOnClickListener{

            dlg.dismiss()
        }


//        okButton.setOnClickListener(object : OnClickListener() {
//            fun onClick(view: View?) {
//                // '확인' 버튼 클릭시 메인 액티비티에서 설정한 main_label에
//                // 커스텀 다이얼로그에서 입력한 메시지를 대입한다.
//
//
//                // 커스텀 다이얼로그를 종료한다.
//                dlg.dismiss()
//            }
//        })
//        cancelButton.setOnClickListener(object : OnClickListener() {
//            fun onClick(view: View?) {
//                Toast.makeText(context, "취소 했습니다.", Toast.LENGTH_SHORT).show()
//
//                // 커스텀 다이얼로그를 종료한다.
//                dlg.dismiss()
//            }
//        })
    }

}