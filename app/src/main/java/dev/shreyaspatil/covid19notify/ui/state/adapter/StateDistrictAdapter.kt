package dev.shreyaspatil.covid19notify.ui.state.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.shreyaspatil.covid19notify.databinding.ItemDistrictBinding
import dev.shreyaspatil.covid19notify.model.DistrictData

class StateDistrictAdapter :
    ListAdapter<DistrictData, StateDistrictAdapter.TotalViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TotalViewHolder(
        ItemDistrictBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: TotalViewHolder, position: Int) =
        holder.bind(getItem(position))


    class TotalViewHolder(private val binding: ItemDistrictBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(details: DistrictData) {
            binding.textDistrictName.text = details.district
            binding.textConfirmed.text =
                details.confirmed.toString()
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DistrictData>() {
            override fun areItemsTheSame(
                oldItem: DistrictData,
                newItem: DistrictData
            ): Boolean =
                oldItem.district == newItem.district

            override fun areContentsTheSame(
                oldItem: DistrictData,
                newItem: DistrictData
            ): Boolean =
                oldItem == newItem

        }
    }
}