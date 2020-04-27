package dev.shreyaspatil.covid19notify.ui.state

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.MergeAdapter
import dev.shreyaspatil.covid19notify.R
import dev.shreyaspatil.covid19notify.databinding.ActivityStateDistrictBinding
import dev.shreyaspatil.covid19notify.model.Details
import dev.shreyaspatil.covid19notify.model.DistrictData
import dev.shreyaspatil.covid19notify.ui.state.adapter.StateDistrictAdapter
import dev.shreyaspatil.covid19notify.ui.state.adapter.StateTotalAdapter
import dev.shreyaspatil.covid19notify.utils.State
import dev.shreyaspatil.covid19notify.utils.getPeriod
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class StateDistrictActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStateDistrictBinding

    private val mStateTotalAdapter = StateTotalAdapter()
    private val mStateDistrictAdapter = StateDistrictAdapter()

    private val adapter = MergeAdapter(mStateTotalAdapter, mStateDistrictAdapter)

    private val stateViewModel: StateViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStateDistrictBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val details: Details? = intent.getParcelableExtra<Details>("StateDetails")
        val detailtList = mutableListOf<Details>()
        details?.let { detailtList.add(it) }

        binding.recyclerState.adapter = adapter

        binding.toolbarTitleState.text = details?.state
        binding.textLastUpdatedView.text = this.getString(
            R.string.text_last_updated,
            getPeriod(
                SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                    .parse(details?.lastUpdatedTime)
            )
        )

        details?.state?.let { stateViewModel.getDistrictData(it) }

        mStateTotalAdapter.submitList(detailtList)

        initData()

        binding.swipeRefreshLayout.setOnRefreshListener {
            details?.state?.let { stateViewModel.getDistrictData(it) }
        }
    }


    private fun initData() {
        stateViewModel.stateCovidLiveData.observe(this, Observer {
            when (it) {
                is State.Success -> {
                    val list: List<DistrictData> = it.data.districtData
                    Log.d("SecondActivity", list.toString())
                    mStateDistrictAdapter.submitList(list)
                    binding.swipeRefreshLayout.isRefreshing = false

                }
                is State.Loading -> {
                    binding.swipeRefreshLayout.isRefreshing = true
                }
                is State.Error -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    Toast.makeText(applicationContext, it.message, Toast.LENGTH_LONG).show()
                }

            }
        })

    }
}
