package es.kix2902.santoral.adapters

import Model
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import es.kix2902.santoral.R
import kotlinx.android.synthetic.main.saints_row.view.*

class SaintsAdapter(val items: List<Model.ApiResponse>, val context: Context) :
    RecyclerView.Adapter<SaintsAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.saints_row, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.personName.text = item.name
        holder.saintName.text = item.fullname
        item.foto?.let {
            Picasso.get().load(it).into(holder.saintImage)
        }
        if (item.important == 1) {
            holder.saintName.setTypeface(null, Typeface.BOLD);
            holder.personName.setTypeface(null, Typeface.BOLD);
            holder.itemView.setBackgroundColor(Color.YELLOW)
        } else {
            holder.saintName.setTypeface(null, Typeface.NORMAL);
            holder.personName.setTypeface(null, Typeface.NORMAL);
            holder.itemView.setBackgroundResource(0)
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val personName = view.person_name
        val saintName = view.saint_name
        val saintImage = view.saint_image
    }

}