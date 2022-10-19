package com.example.retroffit_api_call_app.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.retroffit_api_call_app.R
import com.example.retroffit_api_call_app.models.Animal

class AnimalListAdapter(var list: MutableList<Animal>): RecyclerView.Adapter<AnimalListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var name: TextView
        var latinName: TextView
        var animalType: TextView
        var activeTime:TextView
        var lengthMin: TextView
        var lengthMax: TextView
        var weightMin: TextView
        var weightMax: TextView
        var lifespan: TextView
        var habitat: TextView
        var diet: TextView
        var geoRange: TextView
        var id: TextView

        init {
            name = itemView.findViewById(R.id.name)
            latinName = itemView.findViewById(R.id.latin_name)
            animalType = itemView.findViewById(R.id.animal_type)
            activeTime = itemView.findViewById(R.id.active_time)
            lengthMin = itemView.findViewById(R.id.length_min)
            lengthMax = itemView.findViewById(R.id.length_max)
            weightMin = itemView.findViewById(R.id.weight_min)
            weightMax = itemView.findViewById(R.id.weight_max)
            lifespan = itemView.findViewById(R.id.lifespan)
            habitat = itemView.findViewById(R.id.habitat)
            diet = itemView.findViewById(R.id.diet)
            geoRange = itemView.findViewById(R.id.geo_range)
            id = itemView.findViewById(R.id.id)
            Log.d("ASALA", "Create")

            //make api call
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.animal_data_layout,parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataPosition = list[position]

        println(list[position])
        holder.name.text = ("Name: " + dataPosition.name) ?: "***"
        holder.latinName.text = ("Latin name: " + dataPosition.latinName)
        holder.animalType.text = "Anima Type: " + dataPosition.animalType ?: "***"
        holder.activeTime.text = "Active time: " + dataPosition.activeTime ?: "***"
        holder.lengthMin.text = "Length min: " + dataPosition.lengthMin ?: "***"
        holder.lengthMax.text = "Length max: " + dataPosition.lengthMax ?: "***"
        holder.weightMin.text = "Weight min: " + dataPosition.weightMin ?: "***"
        holder.weightMax.text = "Weight max: " + dataPosition.weightMax ?: "***"
        holder.lifespan.text = "Lifespan: " + dataPosition.lifespan ?: "***"
        holder.habitat.text = "Habitat: " + dataPosition.habitat ?: "***"
        holder.diet.text = "Diet: " + dataPosition.diet ?: "***"
        holder.geoRange.text = ("Get Range: " + dataPosition.geoRange) ?: "***"
        holder.id.text = ("ID: " + dataPosition.id) ?: "***"

        println(dataPosition.name + ", \n" +
                dataPosition.latinName + ", \n" +
                dataPosition.animalType + ", \n" +
                dataPosition.activeTime + ", \n" +
                dataPosition.lengthMin + ", \n" +
                dataPosition.lengthMax + ", \n" +
                dataPosition.weightMin + ", \n" +
                dataPosition.weightMax + ", \n" +
                dataPosition.lifespan + ", \n" +
                dataPosition.habitat + ", \n" +
                dataPosition.diet + ", \n" +
                dataPosition.geoRange + ", \n" +
                dataPosition.id + "\n "
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }
}