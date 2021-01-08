package practice.kotlin.com.sleepwell.recycler


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recycler_item.view.*
import org.jetbrains.anko.toast
import practice.kotlin.com.sleepwell.ClickEvents
import practice.kotlin.com.sleepwell.R
import practice.kotlin.com.sleepwell.adapter.GlideApp
import practice.kotlin.com.sleepwell.clickBoard
import practice.kotlin.com.sleepwell.view.CommentActivity


class RecyclerImageTextAdapter(val context: Context, mList: ArrayList<RecyclerItem>, listener : clickBoard) :
    RecyclerView.Adapter<RecyclerImageTextAdapter.mViewH>(){

    private var mData: ArrayList<RecyclerItem>? = mList
    val mCallback = listener

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    class mViewH(view: View) : RecyclerView.ViewHolder(view!!) {
        var icon = view.icon
        var title = view.title
        var channelName = view.channelName
        var likeNumber = view.likeNumber
        var writer = view.writerName
        var likeButton = view.likeButton
        var dislikeButton = view.dislikeButton
        var commentNumber = view.commentNumber
    }


    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): mViewH {
        return mViewH(
            LayoutInflater.from(
                context
            ).inflate(R.layout.recycler_item, parent, false)
        )
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    override fun onBindViewHolder(holder: mViewH, position: Int) {
        val text: String? = mData?.get(position)?.iconUri

        GlideApp.with(context).load(text)
            .override(480, 270)
            .centerCrop()
            .into(holder.icon)
        holder.title?.text = mData?.get(position)?.titleStr
        holder.channelName?.text = mData?.get(position)?.channelStr
        holder.likeNumber?.text = mData?.get(position)?.likeNumber.toString()
        holder.writer?.text = mData?.get(position)?.writer.toString()
        holder.commentNumber?.text = mData?.get(position)?.commentNumber.toString()

        setupButton(holder, position)

    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    override fun getItemCount(): Int {
        if (mData!!.size > 50)
            return 50
        else
            return mData!!.size
    }

    private fun setupButton(holder: mViewH, position: Int) {
        var check = 0

        holder.itemView.setOnClickListener {
            var intent = Intent(it.context, CommentActivity::class.java)
            intent.putExtra("contentUid", mData?.get(position)?.id)
            intent.putExtra("videoId", mData?.get(position)?.linkUri?.substring(mData?.get(position)?.linkUri!!.lastIndexOf("/") + 1,
                mData?.get(position)?.linkUri!!.length))
            context.startActivity(intent)
        }

        holder.itemView.setOnLongClickListener{
            mCallback.sendBoard(mData?.get(position)?.id!!.toInt(), null,holder.adapterPosition , position)
            true
        }



        holder.likeButton.setOnClickListener {
            if (check == 0) {
                ClickEvents()
                    .sendLike(mData?.get(position)?.id, context, holder)
                check++
            } else {
                context.toast("이미 투표하셨습니다")
            }
        }

        holder.dislikeButton.setOnClickListener {
            if (check == 0) {
                ClickEvents()
                    .sendDislike(mData?.get(position)?.id, context, holder)
                check++
            } else {
                context.toast("이미 투표하셨습니다")
            }
        }


    }


    fun changeItem(new : ArrayList<RecyclerItem>){
        mData = new
    }

}