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
import es.kix2902.santoral.helpers.CircleTransform
import kotlinx.android.synthetic.main.saints_row.view.*

class SaintsAdapter(
    private val items: MutableList<Model.Saint>,
    private val context: Context,
    val listener: (Model.Saint) -> Unit
) : RecyclerView.Adapter<SaintsAdapter.ViewHolder>() {

    fun clearItems() {
        items.clear()
        notifyDataSetChanged()
    }

    fun addItems(items: List<Model.Saint>) {
        this.items.addAll(items)
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
        holder.personName.text = item.name
        holder.saintName.text = item.fullname

        holder.saintImage.setImageResource(R.mipmap.ic_launcher)
        holder.saintImage.imageAlpha = ALPHA_DEFAULT
        item.foto?.let {
            Picasso.get()
                .load(it)
                .fit()
                .centerInside()
                .error(R.mipmap.ic_launcher)
                .transform(CircleTransform())
                .into(holder.saintImage, object : Callback {
                    override fun onSuccess() {
                        holder.saintImage.imageAlpha = ALPHA_SAINT
                    }

                    override fun onError(e: Exception?) {
                        holder.saintImage.setImageResource(R.mipmap.ic_launcher)
                        holder.saintImage.imageAlpha = ALPHA_DEFAULT
                    }
                })
        }

        if (item.important == 1) {
            holder.saintName.setTypeface(null, Typeface.BOLD)
            holder.personName.setTypeface(null, Typeface.BOLD)
        } else {
            holder.saintName.setTypeface(null, Typeface.NORMAL)
            holder.personName.setTypeface(null, Typeface.NORMAL)
        }

        holder.itemView.setOnClickListener { listener(item) }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val personName: TextView = view.person_name
        val saintName: TextView = view.saint_name
        val saintImage: ImageView = view.saint_image
    }

    companion object {
        const val ALPHA_SAINT = 255
        const val ALPHA_DEFAULT = 150
    }
}