package practice.kotlin.com.sleepwell.recycler

class RecyclerItem : Comparable<RecyclerItem>{
    var id : Int? = null
    var linkUri : String? = null
    var iconUri : String? = null
    var titleStr : String? = null
    var writer : String?  = null
    var channelStr : String? = null
    var likeNumber : Int = 0
    var commentNumber : Int = 0

    override fun compareTo(other: RecyclerItem): Int {
        if (this.likeNumber < other.likeNumber) {
            return 1
        } else if (this.likeNumber > other.likeNumber) {
            return -1
        } else if(this.id!! < other.id!!){
            return -1
        } else if(this.id!! > other.id!!){
            return 1
        }
        return 0
    }
}