package practice.kotlin.com.sleepwell.sleepAndCommu



import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_commu.view.*
import practice.kotlin.com.sleepwell.*
import practice.kotlin.com.sleepwell.recycler.RecyclerImageTextAdapter
import practice.kotlin.com.sleepwell.statics.commuList.Companion.mList


/**
 * A simple [Fragment] subclass.
 */
class CommuFrag : Fragment() {
    var name = "등록"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_commu, container, false)
        view.text_commu.text = name

//        if(false)
//            // 새로고침 누르면 CommuLoading().refreshList() 호출
//        view.refreshButton.setOnClickListener(this)

        setUpButton(view)

        view.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        view.recyclerView.adapter =
            RecyclerImageTextAdapter(requireContext(), mList)
//
//        view.text_commu.setOnClickListener(this)

        return view
    }


    private fun setUpButton(view : View){

        view.refreshButton.setOnClickListener{
            ClickEvents().cal(it, requireActivity(),null)
            refreshList()
        }

        view.text_commu.setOnClickListener{
            view.hideKeyboard()
            CustomDialog(requireActivity()).callFunction()
        }

    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
//    override fun onClick(v: View) {
////        ClickEvents().cal(v,requireActivity())
//        CustomDialog(requireContext()).callFunction()
//        refreshList()
//    }

    private fun  refreshList() {
        val handler = Handler()
        handler.postDelayed(Runnable { view!!.recyclerView.adapter!!.notifyDataSetChanged() }, 2000)
    }
}
