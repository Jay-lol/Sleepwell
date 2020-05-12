package practice.kotlin.com.sleepwell.recycler

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recycler_comment.view.*
import practice.kotlin.com.sleepwell.R
import practice.kotlin.com.sleepwell.onItemClick


class CommentRecycler(val context: Context, cList: ArrayList<CommentItem>, listener : onItemClick) :
    RecyclerView.Adapter<CommentRecycler.cViewH>(){

    private var cData: ArrayList<CommentItem> = cList
    val mCallback = listener

    class cViewH(view: View) : RecyclerView.ViewHolder(view) {
        var rid : Int? = null
        var profile = view.profile
        var userId = view.userId
        var replycontent = view.replyContent
        var reply = view.ifRere
        var rereply = view.rereply
        var back = view.comment_main
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): cViewH {
        val a = parent.getContext()
        return cViewH(
            LayoutInflater.from(
                a
            ).inflate(R.layout.recycler_comment, parent, false)
        )
    }

    override fun onBindViewHolder(holder: cViewH, position: Int) {
            holder.profile.setImageResource(R.drawable.commu_tab_logo)

            holder.rid = cData[position].commuId

            holder.userId.text = cData[position].cWriter
            holder.replycontent.text = cData[position].replycontent

            if (cData[position].isRere) {
                holder.back.setBackgroundColor(Color.parseColor("#FF787878"))
                holder.reply.layoutParams.width = 50

                holder.rereply.visibility = View.INVISIBLE
                holder.reply.requestLayout()
            } else {
                holder.back.setBackgroundResource(R.drawable.custom_ripple_effect)

                holder.reply.layoutParams.width = 0
                holder.reply.layoutParams.height = 0

                holder.rereply.visibility = View.VISIBLE
                holder.reply.requestLayout()
            }

            setupButton(holder, position)

    }

    private fun setupButton(holder: cViewH, position: Int) {
        holder.rereply.setOnClickListener {
//            holder.back.setBackgroundColor(Color.parseColor("#FFAA0000"))
            mCallback.onClick(holder)
        }
    }

    override fun getItemCount(): Int {
        return cData.size
    }


}

