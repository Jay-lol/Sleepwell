package practice.kotlin.com.sleepwell.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_commu.view.*
import practice.kotlin.com.sleepwell.ClickEvents
import practice.kotlin.com.sleepwell.R

/**
 * A simple [Fragment] subclass.
 */
class CommuFrag : Fragment(),View.OnClickListener {
    var name = "등록"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_commu, container, false)
        view.text_commu.text = name

        view.text_commu.setOnClickListener(this)

        return view
    }

    override fun onClick(v: View) {
        ClickEvents().cal(v,requireActivity())
    }
}
