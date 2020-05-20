package practice.kotlin.com.sleepwell.sleepAndCommu


import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main.fragment_commu.view.*
import kotlinx.android.synthetic.main.fragment_commu.view.swipe_layout
import practice.kotlin.com.sleepwell.ClickEvents
import practice.kotlin.com.sleepwell.R
import practice.kotlin.com.sleepwell.clickBoard
import practice.kotlin.com.sleepwell.recycler.RecyclerImageTextAdapter
import practice.kotlin.com.sleepwell.statics.JsonString
import practice.kotlin.com.sleepwell.statics.commuList
import practice.kotlin.com.sleepwell.statics.commuList.Companion.mList


/**
 * A simple [Fragment] subclass.
 */
class CommuFrag : Fragment(), clickBoard , SwipeRefreshLayout.OnRefreshListener{
    var name = "등록"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        var view  = inflater.inflate(R.layout.fragment_commu, container, false)
        view.text_commu.text = name

//        if(false)
//            // 새로고침 누르면 CommuLoading().refreshList() 호출
//        view.refreshButton.setOnClickListener(this)

        view.text_commu.setOnClickListener{
            view.hideKeyboard()
            CustomDialog(requireActivity()).callFunction(view.recyclerView)
        }

        var cAdapter = RecyclerImageTextAdapter(requireActivity(), mList, this)

        view.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        view.recyclerView.adapter = cAdapter

        view.recyclerView.addItemDecoration(BottomOffsetDecoration(0, cAdapter))

        view.swipe_layout.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )
        view.swipe_layout.setOnRefreshListener(this)

        return view
    }

    override fun sendBoard(idx: Int, bool : Boolean?) {
        CustomDialog(requireActivity()).requestDeleteFireBoard(idx , view!!.recyclerView)
    }

    override fun sendDeIdx(idx: Long?, position : Int) {

    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun  refreshList() {
        val handler = Handler()
        handler.postDelayed({ view!!.recyclerView.adapter!!.notifyDataSetChanged() }, 2000)
    }

    override fun onRefresh() {
        JsonString.cnt = 0
        ClickEvents().startThumnailLoading(JsonString.cnt, view?.recyclerView, null)
        refreshList()
        view?.swipe_layout?.isRefreshing = false
    }

    class BottomOffsetDecoration(private val mBottomOffset: Int, val adapter: RecyclerImageTextAdapter) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)
            val dataSize = state.itemCount
            var position = parent.getChildAdapterPosition(view)
            if (dataSize > 0 && position == dataSize - 1) {
                outRect.set(0, 0, 0, mBottomOffset)
                commuList.position += 1
                ClickEvents().startThumnailLoading(JsonString.cnt, view.recyclerView, null)
            } else {
                outRect.set(0, 0, 0, 0)
            }
        }

    }

}
