package practice.kotlin.com.sleepwell.fragment



import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_commu.*
import kotlinx.android.synthetic.main.fragment_commu.view.*
import practice.kotlin.com.sleepwell.*


/**
 * A simple [Fragment] subclass.
 */
class CommuFrag : Fragment(),View.OnClickListener {
    var name = "등록"

    private var mAdapter: RecyclerImageTextAdapter? = null

    var mList = arrayListOf<RecyclerItem>()
    lateinit var recyclerView1 : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_commu, container, false)
        view.text_commu.text = name


        if(JsonString.cnt == 0) {
            addItem(
                JsonString.getServerString[0],
                JsonString.getTitleString[0], JsonString.getWriterString[0]
            )
            // 두 번째 아이템 추가.
            addItem(
                JsonString.getServerString[1],
                JsonString.getTitleString[1], JsonString.getWriterString[1]
            )
            JsonString.cnt++
        }

        recyclerView1 = view.findViewById(R.id.recyclerView) as RecyclerView
        recyclerView1.layoutManager = LinearLayoutManager(requireContext())
        recyclerView1.adapter = RecyclerImageTextAdapter(requireContext(), mList)

        view.text_commu.setOnClickListener(this)

        return view
    }


    override fun onClick(v: View) {
        ClickEvents().cal(v,requireActivity())
    }

    fun addItem(icon: String, title: String, desc: String) {

        var item : RecyclerItem? = RecyclerItem()

        item?.iconUri = icon
        item?.titleStr = title
        item?.descStr = desc

        mList.add(item!!)
    }
}
