package eu.chessout.v2.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import eu.chessout.v2.R

class MyAdapter(
    var arrayList: ArrayList<DashboardModel>
) : RecyclerView.Adapter<MyAdapter.ItemHolder>() {

    fun updateArrayList(newArrayList: List<DashboardModel>) {
        arrayList.clear()
        arrayList.addAll(newArrayList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.grid_layout_list_item, parent, false)
        return ItemHolder(itemHolder)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {

        var dashboardModel: DashboardModel = arrayList.get(position)

        holder.imageView.setImageResource(dashboardModel.iconsChar!!)
        holder.textView.text = dashboardModel.iconString

        holder.textView.setOnClickListener {
            performAction(dashboardModel.iconString)
        }
        holder.imageView.setOnClickListener {
            performAction(dashboardModel.iconString)
        }
    }

    fun performAction(iconString: String?) {
        // Toast.makeText(context, iconString, Toast.LENGTH_LONG).show()
    }

    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView = itemView.findViewById<ImageView>(R.id.icon_image)
        var textView = itemView.findViewById<TextView>(R.id.icon_text)
    }
}