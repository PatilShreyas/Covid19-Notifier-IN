package dev.shreyaspatil.covid19notify.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.shreyaspatil.covid19notify.R
import dev.shreyaspatil.covid19notify.databinding.ItemTotalBinding
import dev.shreyaspatil.covid19notify.model.Details
import dev.shreyaspatil.covid19notify.utils.getPeriod
import java.text.SimpleDateFormat

class TotalAdapter : ListAdapter<Details, TotalAdapter.TotalViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TotalViewHolder(
        ItemTotalBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: TotalViewHolder, position: Int) =
        holder.bind(getItem(position))


    class TotalViewHolder(private val binding: ItemTotalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(details: Details) {
            binding.textLastUpdatedView.text = itemView.context.getString(
                R.string.text_last_updated,
                getPeriod(
                    SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                        .parse(details.lastUpdatedTime)
                )
            )

            binding.textConfirmed.text = details.confirmed
            binding.textActive.text = details.active
            binding.textRecovered.text = details.recovered
            binding.textDeceased.text = details.deaths

            // New Confirmed
            details.deltaConfirmed.let {
                if (it == "0") {
                    binding.groupNewConfirmed.visibility = View.GONE
                } else {
                    binding.groupNewConfirmed.visibility = View.VISIBLE
                    binding.textNewConfirmed.text = details.deltaConfirmed
                }
            }

            // New Recovered
            details.deltaRecovered.let {
                if (it == "0") {
                    binding.groupNewRecovered.visibility = View.GONE
                } else {
                    binding.groupNewRecovered.visibility = View.VISIBLE
                    binding.textNewRecovered.text = details.deltaRecovered
                }
            }

            // New Deaths
            details.deltaDeaths.let {
                if (it == "0") {
                    binding.groupNewDeaths.visibility = View.GONE
                } else {
                    binding.groupNewDeaths.visibility = View.VISIBLE
                    binding.textNewDeaths.text = details.deltaDeaths
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Details>() {
            override fun areItemsTheSame(oldItem: Details, newItem: Details): Boolean =
                oldItem.state == newItem.state

            override fun areContentsTheSame(oldItem: Details, newItem: Details): Boolean =
                oldItem == newItem

        }
    }
}