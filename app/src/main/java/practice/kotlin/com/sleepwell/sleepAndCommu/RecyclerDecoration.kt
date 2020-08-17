package practice.kotlin.com.sleepwell.sleepAndCommu

import android.graphics.Rect
import android.view.View
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView

class RecyclerDecoration(divHeight : Int) : RecyclerView.ItemDecoration() {

    private val divHeight =divHeight

    override fun getItemOffsets(@NonNull outRect : Rect, @NonNull view : View, @NonNull parent : RecyclerView,
                                @NonNull state : RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state);
        if (parent.getChildAdapterPosition(view) != parent.getAdapter()!!.getItemCount() - 1)
            outRect.bottom = divHeight;

    }
}
