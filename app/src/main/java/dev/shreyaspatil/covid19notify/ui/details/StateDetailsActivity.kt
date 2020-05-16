package dev.shreyaspatil.covid19notify.ui.details

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.MergeAdapter
import dev.shreyaspatil.covid19notify.R
import dev.shreyaspatil.covid19notify.databinding.ActivityStateDetailsBinding
import dev.shreyaspatil.covid19notify.model.Details
import dev.shreyaspatil.covid19notify.ui.adapter.TotalAdapter
import dev.shreyaspatil.covid19notify.ui.details.adapter.DistrictsAdapter
import dev.shreyaspatil.covid19notify.utils.State
import dev.shreyaspatil.covid19notify.utils.getPeriod
import dev.shreyaspatil.covid19notify.utils.toDateFormat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.viewmodel.ext.android.viewModel

@FlowPreview
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class StateDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStateDetailsBinding

    private val mStateTotalAdapter = TotalAdapter()
    private val mDistrictAdapter = DistrictsAdapter()

    private val adapter = MergeAdapter(mStateTotalAdapter, mDistrictAdapter)

    private val viewModel: StateDetailsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStateDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        initData()

    }

    private fun getStateDetails(): Details? = intent.getParcelableExtra(KEY_STATE_DETAILS)

    private fun initViews() {
        setSupportActionBar(binding.appBarlayout.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.recyclerState.adapter = adapter

        val details: Details? = getStateDetails()

        details?.let {
            mStateTotalAdapter.submitList(listOf(it))

            supportActionBar?.title = it.state
            supportActionBar?.subtitle = getString(
                R.string.text_last_updated,
                getPeriod(
                    it.lastUpdatedTime.toDateFormat()
                )
            )
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            loadData(getStateDetails())
        }
    }

    private fun initData() {
        viewModel.stateCovidLiveDataDetails.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> {
                    binding.swipeRefreshLayout.isRefreshing = true
                }
                is State.Success -> {
                    val list = state.data.districtData
                    list.sortedByDescending { it.confirmed }.let { districtList ->
                        mDistrictAdapter.submitList(districtList)
                    }

                    binding.swipeRefreshLayout.isRefreshing = false

                }
                is State.Error -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    Toast.makeText(applicationContext, state.message, Toast.LENGTH_LONG).show()
                }

            }
        })

        if (viewModel.stateCovidLiveDataDetails.value !is State.Success) {
            loadData(getStateDetails())
        }
    }

    private fun loadData(details: Details?) {
        details?.state?.let {
            viewModel.getDistrictData(it)
        }
    }

    companion object {
        const val KEY_STATE_DETAILS = "key_state_details"
    }
}
