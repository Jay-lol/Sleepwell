package practice.kotlin.com.sleepwell.recycler

import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recycler_comment.view.*
import org.jetbrains.anko.toast
import practice.kotlin.com.sleepwell.ClickEvents
import practice.kotlin.com.sleepwell.R
import practice.kotlin.com.sleepwell.onItemClick
import practice.kotlin.com.sleepwell.sleepAndCommu.CustomDialog


class CommentRecycler(val context: Context, cList: ArrayList<CommentItem>, listener: onItemClick) :
    RecyclerView.Adapter<CommentRecycler.cViewH>() {

    private var cData: ArrayList<CommentItem> = cList
    val mCallback = listener

    class cViewH(view: View) : RecyclerView.ViewHolder(view) {
        var rid : Int = -1
        var likeButton = view.commentLikeButton
        var userId = view.userId
        var replycontent = view.replyContent
        var reply = view.ifRere
        var rereply = view.rereply
        var back = view.comment_main
        var likecnt = view.LikeCount
        var deleteButton = view.deletButton
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
        holder.rid = cData[position].rid

        if (cData[position].likeNumber == "0")//좋아요 개수가 0이면
        {
            holder.likecnt.text = ""
        } else {
            holder.likecnt.text = cData[position].likeNumber
        }

        holder.userId.text = cData[position].cWriter
        holder.replycontent.text = cData[position].replycontent

        if (cData[position].isRere) {
            holder.back.setBackgroundColor(Color.parseColor("#000000"))       // FF787878 에서 수정
            holder.likeButton.setBackgroundResource(R.drawable.custom_ripple_effect_two)
            holder.reply.layoutParams.width = 50

            holder.rereply.visibility = View.INVISIBLE
            holder.reply.requestLayout()
        } else {
            holder.back.setBackgroundColor(Color.parseColor("#282828"))
            holder.likeButton.setBackgroundResource(R.drawable.custom_ripple_effect)
            holder.reply.layoutParams.width = 0
            holder.reply.layoutParams.height = 0

            holder.rereply.visibility = View.VISIBLE
            holder.reply.requestLayout()
        }

        setupButton(holder, position)

    }

    private fun setupButton(holder: cViewH, position: Int) {
        var check = 0
        holder.rereply.setOnClickListener {
            mCallback.onClick(holder)
        }

        holder.likeButton.setOnClickListener {


            if (check == 0) {
                // 댓글, 대댓글좋아요 작업. cData[position].isRere을 통해 댓글인지, 대댓글인지
                if(!cData[position].isRere)
                    ClickEvents().sendCommentLike(cData[position].rid, context, holder)

                else
                    ClickEvents().sendReCommentLike(cData[position].rrid, context, holder)
                check++
            } else {
                context.toast("이미 투표하셨습니다?")
            }

            mCallback.onClick("좋아요!!")

        }

        holder.deleteButton.setOnClickListener{
            if(!cData[position].isRere)
                mCallback.onClick("comment", cData[position].rid)

            else
                mCallback.onClick("reComment", cData[position].rrid)

        }


    }

    override fun getItemCount(): Int {
        return cData.size
    }


}

