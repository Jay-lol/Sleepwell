package practice.kotlin.com.sleepwell


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.recycler_item.view.*
import org.jetbrains.anko.browse


class RecyclerImageTextAdapter (val context: Context, mList : MutableList<RecyclerItem>) :
    RecyclerView.Adapter<RecyclerImageTextAdapter.mViewH>() {

    private var mData: MutableList<RecyclerItem>? = mList

    class mViewH(view: View) : RecyclerView.ViewHolder(view!!) {
        var friend_Profile = view.icon
        var friend_Name = view.title
        var friend_Status = view.desc

    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var icon : ImageView? = null
        var title : TextView? = null
        var desc : TextView? = null
        init {
            icon = itemView.findViewById(R.id.icon)
            title = itemView.findViewById(R.id.title)
            desc = itemView.findViewById(R.id.desc)
        }
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): mViewH {
        return mViewH(LayoutInflater.from(context).inflate(R.layout.recycler_item,parent,false))
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    override fun onBindViewHolder(holder: mViewH, position: Int) {

        val text: String? = mData?.get(position)?.iconUri

        Glide.with(context).load(text)
             .override(480,270)
             .centerCrop()
             .into(holder.friend_Profile)
        holder.friend_Name?.setText(mData?.get(position)?.titleStr)
        holder.friend_Status?.setText(mData?.get(position)?.descStr)

        holder.itemView.setOnClickListener{
            mData?.get(position)?.linkUri?.let { it1 -> context.browse(it1) }
        }

    }


    // getItemCount() - 전체 데이터 갯수 리턴.
    override fun getItemCount(): Int {
        return mData!!.size
    }
}