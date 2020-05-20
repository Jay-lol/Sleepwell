package practice.kotlin.com.sleepwell.sleepAndCommu


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_sleep.view.*
import practice.kotlin.com.sleepwell.ClickEvents
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

        view.cal_now.setOnClickListener(this)
        view.cal_Specific.setOnClickListener(this)
        view.cal_future.setOnClickListener(this)


        return view
    }

    override fun onClick(v: View) {
        ClickEvents().cal(v, requireActivity(), null)
    }
}