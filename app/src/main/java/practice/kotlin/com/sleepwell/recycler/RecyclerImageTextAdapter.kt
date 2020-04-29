package practice.kotlin.com.sleepwell.recycler


import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.recycler_item.view.*
import org.jetbrains.anko.browse
import org.jetbrains.anko.toast
import practice.kotlin.com.sleepwell.ClickEvents
import practice.kotlin.com.sleepwell.R

const val LIKE = 0
const val DISLIKE = 1

class RecyclerImageTextAdapter(val context: Context, mList: MutableList<RecyclerItem>) :
    RecyclerView.Adapter<RecyclerImageTextAdapter.mViewH>() {

    private var mData: MutableList<RecyclerItem>? = mList

    class mViewH(view: View) : RecyclerView.ViewHolder(view!!) {
        var icon = view.icon
        var title = view.title
        var channelName = view.channelName
        var likeNumber = view.likeNumber
        var likeButton = view.likeButton
        var dislikeButton = view.dislikeButton
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var icon: ImageView? = null
        var title: TextView? = null
        var channelName: TextView? = null
        var likeNumber: TextView? = null

        init {
            icon = itemView.findViewById(R.id.icon)
            title = itemView.findViewById(R.id.title)
            channelName = itemView.findViewById(R.id.channelName)
            likeNumber = itemView.findViewById(R.id.likeNumber)
        }
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
        var check = 0

        Glide.with(context).load(text)
            .override(480, 270)
            .centerCrop()
            .into(holder.icon)
        holder.title?.setText(mData?.get(position)?.titleStr)
        holder.channelName?.setText(mData?.get(position)?.channelStr)
        holder.likeNumber?.setText(mData?.get(position)?.likeNumber.toString())

        holder.itemView.setOnClickListener {
            mData?.get(position)?.linkUri?.let { it1 -> context.browse(it1) }
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

    // getItemCount() - 전체 데이터 갯수 리턴.
    override fun getItemCount(): Int {
        if (mData!!.size > 50)
            return 50
        else
            return mData!!.size
    }
}