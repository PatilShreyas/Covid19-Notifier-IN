package dev.shreyaspatil.covid19notify.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.shreyaspatil.covid19notify.R
import dev.shreyaspatil.covid19notify.databinding.ItemStateBinding
import dev.shreyaspatil.covid19notify.model.Details
import dev.shreyaspatil.covid19notify.utils.getPeriod
import java.text.SimpleDateFormat

class StateAdapter : ListAdapter<Details, StateAdapter.StateViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = StateViewHolder(
        ItemStateBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: StateViewHolder, position: Int) =
        holder.bind(getItem(position))


    class StateViewHolder(private val binding: ItemStateBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(details: Details) {
            binding.textState.text = details.state
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
            binding.textDeath.text = details.deaths

            // New Confirmed
            details.deltaConfirmed.let {
                if (it == "0") {
                    binding.groupStateNewConfirm.visibility = View.GONE
                } else {
                    binding.groupStateNewConfirm.visibility = View.VISIBLE
                    binding.textStateNewConfirm.text = details.deltaConfirmed
                }
            }

            // New Recovered
            details.deltaRecovered.let {
                if (it == "0") {
                    binding.groupStateNewRecover.visibility = View.GONE
                } else {
                    binding.groupStateNewRecover.visibility = View.VISIBLE
                    binding.textStateNewRecover.text = details.deltaRecovered
                }
            }

            // New Deaths
            details.deltaDeaths.let {
                if (it == "0") {
                    binding.groupStateNewDeaths.visibility = View.GONE
                } else {
                    binding.groupStateNewDeaths.visibility = View.VISIBLE
                    binding.textStateNewDeath.text = details.deltaDeaths
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