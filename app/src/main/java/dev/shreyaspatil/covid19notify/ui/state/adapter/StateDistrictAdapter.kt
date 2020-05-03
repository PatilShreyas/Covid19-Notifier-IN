package dev.shreyaspatil.covid19notify.ui.state.adapter

import android.view.LayoutInflater
import android.view.View
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
            binding.textConfirmed.text = details.confirmed.toString()
            binding.textActive.text = details.active.toString()
            binding.textDeath.text = details.deceased.toString()
            binding.textRecovered.text = details.recovered.toString()

            // New Confirmed
            details.delta.confirmed.let {
                if (it == 0) {
                    binding.groupStateNewConfirm.visibility = View.GONE
                } else {
                    binding.groupStateNewConfirm.visibility = View.VISIBLE
                    binding.textDistrictNewConfirm.text = details.delta.confirmed.toString()
                }
            }
            // New Recovered
            details.delta.recovered.let {
                if (it == 0) {
                    binding.groupStateNewRecover.visibility = View.GONE
                } else {
                    binding.groupStateNewRecover.visibility = View.VISIBLE
                    binding.textDistrictNewRecover.text = details.delta.recovered.toString()
                }
            }
            // New Deaths
            details.delta.deceased.let {
                if (it == 0) {
                    binding.groupStateNewDeaths.visibility = View.GONE
                } else {
                    binding.groupStateNewDeaths.visibility = View.VISIBLE
                    binding.textDistrictNewDeath.text = details.delta.deceased.toString()
                }
            }
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