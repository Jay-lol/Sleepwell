package practice.kotlin.com.sleepwell.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recycler_comment.view.*
import kotlinx.android.synthetic.main.recycler_item.view.*
import practice.kotlin.com.sleepwell.R

class CommentRecycler(val context: Context, cList: MutableList<CommentItem>) :
    RecyclerView.Adapter<CommentRecycler.cViewH>() {

    private var cData: MutableList<CommentItem>? = cList

    class cViewH(view: View) : RecyclerView.ViewHolder(view) {
        var profile = view.profile
        var userId = view.userId
        var replycontent = view.replyContent
        var reply = view.imageView3
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): cViewH {
        return cViewH(
            LayoutInflater.from(
                context
            ).inflate(R.layout.recycler_comment, parent, false)
        )
    }

    override fun onBindViewHolder(holder: cViewH, position: Int) {

        holder.profile.setImageResource(R.drawable.commu_tab_logo)
        holder.userId.text = cData?.get(position)?.cWriter
        holder.replycontent.text = cData?.get(position)?.replycontent

        if (position != 0 && position % 2 == 0) {
            holder.reply.setImageResource(R.drawable.ic_subdirectory_arrow_right_black_24dp)
            holder.reply.layoutParams.width = 50
            holder.reply.requestLayout()
        }

    }


    override fun getItemCount(): Int {
        return cData!!.size
    }
}