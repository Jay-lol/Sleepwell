package practice.kotlin.com.sleepwell.sleepAndCommu


import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SmoothScroller
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main.fragment_commu.*
import kotlinx.android.synthetic.main.fragment_commu.view.*
import practice.kotlin.com.sleepwell.ClickEvents
import practice.kotlin.com.sleepwell.R
import practice.kotlin.com.sleepwell.clickBoard
import practice.kotlin.com.sleepwell.recycler.RecyclerImageTextAdapter
import practice.kotlin.com.sleepwell.statics.JsonString
import practice.kotlin.com.sleepwell.statics.commuList
import practice.kotlin.com.sleepwell.statics.commuList.Companion.commuview
import practice.kotlin.com.sleepwell.statics.commuList.Companion.likeOrRecentOrHot
import practice.kotlin.com.sleepwell.statics.commuList.Companion.mList


/**
 * A simple [Fragment] subclass.
 */
class CommuFrag : Fragment(), clickBoard, SwipeRefreshLayout.OnRefreshListener {
    private var waitLoading = true
    private var wait = true
    private var isFabClicked: Boolean = false
    var name = "등록"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_commu, container, false)
//        view.text_commu.text = name
        commuview = view
        view.fabSub1.shrink()
        view.fabSub2.shrink()
        var smoothScroller: SmoothScroller = object : LinearSmoothScroller(context) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }
        }
        view.progressBar2.visibility = View.GONE
        var isRefresh: Boolean = false
//        if(false)
//            // 새로고침 누르면 CommuLoading().refreshList() 호출
//        view.refreshButton.setOnClickListener(this)

        view.fabMain.setOnClickListener {
            isFabClicked = !isFabClicked
            view.hideKeyboard()
            if (isFabClicked) {
                ObjectAnimator.ofFloat(fabMain, "rotation", 0f, 45f).setDuration(800).start()
                ObjectAnimator.ofFloat(fabSub1, "translationY", -200f).apply { start() }
                ObjectAnimator.ofFloat(fabSub2, "translationY", -400f).apply { start() }
            } else {
                ObjectAnimator.ofFloat(fabMain, "rotation", 90f).setDuration(800).start()
                ObjectAnimator.ofFloat(fabSub1, "translationY", 0f).apply { start() }
                ObjectAnimator.ofFloat(fabSub2, "translationY", 0f).apply { start() }
            }

        }

        view.fabSub2.setOnClickListener {
            CustomDialog(requireActivity()).callFunction(view.recyclerView)
        }

        view.fabSub1.setOnClickListener {
            if (view.progressBar2.visibility != View.VISIBLE && wait) {
                if (likeOrRecentOrHot == 0) {
                    likeOrRecentOrHot = 2
                    wait = false
                    Handler().postDelayed({
                        onRefresh()
                        view.fabSub1.setIconResource(R.drawable.ic_access_time_black_24dp)
                        wait = true
                    }, 500)
                } else if(likeOrRecentOrHot == 1) {
                    likeOrRecentOrHot = 0
                    wait = false
                    Handler().postDelayed({
                        onRefresh()
                        view.fabSub1.setIconResource(R.drawable.ic_whatshot_black_24dp)
                        wait = true
                    }, 500)
                } else {
                    likeOrRecentOrHot = 1
                    wait = false
                    Handler().postDelayed({
                        onRefresh()
                        view.fabSub1.setIconResource(R.drawable.ic_favorite_black_24dp)
                        wait = true
                    }, 500)
                }
            }
        }

        var cAdapter = RecyclerImageTextAdapter(requireActivity(), mList, this)
        val linearLayoutManager = LinearLayoutManagerWrapper(requireContext(), LinearLayout.VERTICAL, false)
        view.recyclerView.layoutManager = linearLayoutManager

        view.recyclerView.adapter = cAdapter
//
//        view.recyclerView.addItemDecoration(BottomOffsetDecoration(0, cAdapter))
//
        view.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastPosition =
                    (recyclerView.layoutManager as LinearLayoutManagerWrapper?)!!.findLastCompletelyVisibleItemPosition()
                val totalCount = cAdapter.itemCount - 1

                if (dy < -10 && !(lastPosition < 10) && !isRefresh) {
                    Log.d("dy값", dy.toString())
                    isRefresh = true
                    view.floatingActionButton.visibility = View.VISIBLE
                } else if (dy > 10) {
                    if (isRefresh)
                        isRefresh = !isRefresh
                    Log.d("dy값", dy.toString())
                    view.floatingActionButton.visibility = View.GONE
                }

                Log.d("안함", lastPosition.toString() + "\n" + totalCount.toString())

                if (lastPosition == totalCount && lastPosition != -1 && totalCount > 15) {
                    view.progressBar2.visibility = View.VISIBLE
                    if (waitLoading) {
                        waitLoading = false
                        Handler().postDelayed({
                            commuList.position += 1
                            ClickEvents().startThumnailLoading(JsonString.cnt, view.recyclerView, null, null)
                            waitLoading = true
                        }, 1500)
                    }

                }
            }
        })

        view.floatingActionButton.setOnClickListener {
            smoothScroller.targetPosition = 0
            linearLayoutManager.startSmoothScroll(smoothScroller)
            view.floatingActionButton.visibility = View.GONE
        }


        view.swipe_layout.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )
        view.swipe_layout.setOnRefreshListener(this)

        return view
    }

    override fun sendBoard(id: Int, bool: Boolean?, position: Int, idx: Int) {
        view?.recyclerView?.scrollToPosition(position)
        CustomDialog(requireActivity()).requestDeleteFireBoard(id, view!!.recyclerView, position, idx)
    }

    override fun sendDeIdx(idx: Long?, position: Int) {

    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun refreshList() {
        val handler = Handler()
        handler.postDelayed({ view!!.recyclerView.adapter!!.notifyDataSetChanged() }, 2000)
    }

    override fun onRefresh() {
        JsonString.cnt = 0
        mList.clear()
        view?.recyclerView?.adapter = RecyclerImageTextAdapter(requireContext(), mList, this)
        ClickEvents().startThumnailLoading(0, view?.recyclerView, null, null)
//        refreshList()
        view?.swipe_layout?.isRefreshing = false
    }

    class LinearLayoutManagerWrapper(context: Context, orientation: Int, reverseLayout: Boolean) :
        LinearLayoutManager(context, orientation, reverseLayout) {
        override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (e: IndexOutOfBoundsException) {
                Log.e("probe", "meet a IOOBE in RecyclerView");

            }

        }

        override fun supportsPredictiveItemAnimations(): Boolean {
            return false
        }
    }

    override fun onStop() {
        super.onStop()
        view?.progressBar2?.visibility = View.GONE
        view?.progressBar3?.visibility = View.GONE
    }
//    class BottomOffsetDecoration(private val mBottomOffset: Int, val adapter: RecyclerImageTextAdapter) : RecyclerView.ItemDecoration() {
//        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
//            super.getItemOffsets(outRect, view, parent, state)
//            val dataSize = state.itemCount
//            var position = parent.getChildAdapterPosition(view)
//            if (dataSize > 0 && position == dataSize - 1) {
//                outRect.set(0, 0, 0, mBottomOffset)
//                commuList.position += 1
//                ClickEvents().startThumnailLoading(JsonString.cnt, view.recyclerView, null)
//            } else {
//                outRect.set(0, 0, 0, 0)
//            }
//        }
//
//    }


}
