package practice.kotlin.com.sleepwell.sleepAndCommu


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_sleep.view.*
import org.jetbrains.anko.browse
import practice.kotlin.com.sleepwell.R

/**
 * A simple [Fragment] subclass.
 */
class SleepFrag : Fragment(), View.OnClickListener {
    var name = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_sleep, container, false)
        view.textView_sleep.text = name

        view.cal_now.setOnClickListener(this)
        view.cal_future.setOnClickListener(this)
        view.cal_past.setOnClickListener(this)


        return view
    }

    override fun onClick(v: View) {
        // 메인 클릭이벤트 처리
        activity?.browse("https://youtu.be/mtb78gxUU3E")
//       ClickEvents().cal(v, requireActivity())
    }
}
