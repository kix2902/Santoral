package es.kix2902.santoral.ui.main

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import es.kix2902.santoral.R
import es.kix2902.santoral.data.Model
import es.kix2902.santoral.data.SantopediaApi
import es.kix2902.santoral.databinding.SaintsRowBinding
import es.kix2902.santoral.common.helpers.CircleTransform
import es.kix2902.santoral.toDisplayText

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        SaintsRowBinding.inflate(LayoutInflater.from(context), parent, false).root
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        SaintsRowBinding.bind(holder.itemView).apply {

            val item = items[position]

            if (isNameResult) {
                rowTitle.text = item.fullname
                rowSubtitle.text = item.date.toDisplayText()

            } else {
                rowTitle.text = item.name
                rowSubtitle.text = item.fullname
            }

            if (item.important == 1) {
                rowTitle.setTypeface(null, Typeface.BOLD)
                rowSubtitle.setTypeface(null, Typeface.BOLD)
            } else {
                rowTitle.setTypeface(null, Typeface.NORMAL)
                rowSubtitle.setTypeface(null, Typeface.NORMAL)
            }

            rowImage.imageAlpha = ALPHA_DEFAULT
            Picasso.get()
                .load("${SantopediaApi.SANTOPEDIA_API_URL}images/${item.id}.jpg")
                .fit()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .transform(CircleTransform())
                .into(rowImage, object : Callback {
                    override fun onSuccess() {
                        rowImage.imageAlpha = ALPHA_SAINT
                    }

                    override fun onError(e: Exception?) {
                    }
                })

            holder.itemView.setOnClickListener { listener(item) }
        }
    }

    fun isShowingNameResult() = isNameResult

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    companion object {
        const val ALPHA_SAINT = 255
        const val ALPHA_DEFAULT = 150
    }
}
