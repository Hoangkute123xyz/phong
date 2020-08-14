package vn.hexagon.vietnhat.ui.home

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vn.hexagon.vietnhat.R
import vn.hexagon.vietnhat.data.model.banner.Banner


/**
 * Created by VuNBT on 8/16/2019.
 */
class InfinityPagerAdapter : RecyclerView.Adapter<ImageViewHolder>() {

    var listItem:List<Banner> = listOf()

    var isImageFitToScreen = false
    private var zoomOut = false


    lateinit var mContext :Context
    lateinit var mView :View

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        mContext = parent.context
        mView = parent.rootView
        return ImageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.pager_header_item, parent, false))
    }

    override fun getItemCount(): Int {
        return listItem.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        Glide.with(holder.img).load(listItem[position].img).into(holder.img)

        holder.img.setOnClickListener {
            val builder =
                AlertDialog.Builder(mContext)

            val viewGroup: ViewGroup = mView.findViewById(android.R.id.content)
            val dialogView: View = LayoutInflater.from(mContext).inflate(R.layout.aa_diaglog_image_full_screen, viewGroup, false)
            builder.setView(dialogView)
            val alertDialog = builder.create()
            val imageView_banner = dialogView.findViewById<ImageView>(R.id.imageView_banner)
            Glide.with(holder.img).load(listItem[position].img).into(imageView_banner)

            alertDialog.show()

        }
    }

    fun setItem(list: List<Banner>) {
        this.listItem = list
        notifyDataSetChanged()
    }
}

class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val img = view.findViewById<ImageView>(R.id.pager_image)


}