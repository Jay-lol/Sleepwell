package practice.kotlin.com.sleepwell

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class PageAdapter(myFm : FragmentManager) : FragmentStatePagerAdapter(myFm){

    private var myfragments : ArrayList<Fragment> = ArrayList()

    //해당 포지션의 fragment 반환
    override fun getItem(position: Int): Fragment = myfragments[position]
    // return myfragments[position]

    // 프레그먼트의 갯수 반환
    override fun getCount(): Int = myfragments.size

    // 내가 원하는 fragment를 추가
    fun addItems(fragment : Fragment){
        myfragments.add(fragment)
    }

}