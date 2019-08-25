package es.kix2902.santoral.adapters

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import es.kix2902.santoral.R
import es.kix2902.santoral.data.Model
import es.kix2902.santoral.data.SantopediaApi
import es.kix2902.santoral.helpers.CircleTransform
import es.kix2902.santoral.toDisplayText
import kotlinx.android.synthetic.main.saints_row.view.*

class SaintsAdapter(
    private val context: Context,
    val listener: (Model.Saint) -> Unit
) : RecyclerView.Adapter<SaintsAdapter.ViewHolder>() {

    private val items = ArrayList<Model.Saint>()
    private var isNameResult = false

    fun clearItems() {
        items.clear()
        notifyDataSetChanged()
    }

    fun addItems(items: List<Model.Saint>, isNameResult: Boolean) {
        this.items.addAll(items)
        this.isNameResult = isNameResult
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.saints_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        if (isNameResult) {
            holder.title.text = item.fullname
            holder.subtitle.text = item.date.toDisplayText()

        } else {
            holder.title.text = item.name
            holder.subtitle.text = item.fullname
        }

        if (item.important == 1) {
            holder.title.setTypeface(null, Typeface.BOLD)
            holder.subtitle.setTypeface(null, Typeface.BOLD)
        } else {
            holder.title.setTypeface(null, Typeface.NORMAL)
            holder.subtitle.setTypeface(null, Typeface.NORMAL)
        }

        holder.image.imageAlpha = ALPHA_DEFAULT
        Picasso.get()
            .load("${SantopediaApi.SANTOPEDIA_API_URL}images/${item.id}.jpg")
            .fit()
            .centerCrop()
            .placeholder(R.mipmap.ic_launcher)
            .transform(CircleTransform())
            .into(holder.image, object : Callback {
                override fun onSuccess() {
                    holder.image.imageAlpha = ALPHA_SAINT
                }

                override fun onError(e: Exception?) {
                }
            })

        holder.itemView.setOnClickListener { listener(item) }
    }

    fun isShowingNameResult() = isNameResult

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.row_title
        val subtitle: TextView = view.row_subtitle
        val image: ImageView = view.row_image
    }

    companion object {
        const val ALPHA_SAINT = 255
        const val ALPHA_DEFAULT = 150
    }
}