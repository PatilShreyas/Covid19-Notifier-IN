package dev.shreyaspatil.covid19notify.ui.state

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.MergeAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.shreyaspatil.covid19notify.R
import dev.shreyaspatil.covid19notify.databinding.ActivityStateDistrictBinding
import dev.shreyaspatil.covid19notify.model.Details
import dev.shreyaspatil.covid19notify.model.DistrictData
import dev.shreyaspatil.covid19notify.ui.main.MainActivity.Companion.KEY_STATE_DETAILS
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

        //Getting details response from the previous activity
        val details: Details? = intent.getParcelableExtra<Details>(KEY_STATE_DETAILS)
        val detailList = mutableListOf<Details>()
        details?.let { detailList.add(it) }

        //Setting up the Adapter with merge adapter
        binding.recyclerState.adapter = adapter

        //Setting up the toolbar view with title and time from the above response
        binding.toolbarTitleState.text = details?.state
        binding.textLastUpdatedView.text = this.getString(
            R.string.text_last_updated,
            getPeriod(
                SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                    .parse(details?.lastUpdatedTime)
            )
        )

        loadData(details)

        mStateTotalAdapter.submitList(detailList)

        initData()

        binding.swipeRefreshLayout.setOnRefreshListener {
            loadData(details)
        }

        binding.ivGoBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun loadData(details: Details?) {
        details?.state?.let { stateViewModel.getDistrictData(it) }
    }

    private fun initData() {
        stateViewModel.stateCovidLiveData.observe(this, Observer {
            when (it) {
                is State.Success -> {
                    val list: List<DistrictData> = it.data.districtData
                    val sortedList = list.sortedByDescending {districtData ->
                        districtData.confirmed
                    }
                    mStateDistrictAdapter.submitList(sortedList)
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
        binding.swipeRefreshLayout.apply {
            setProgressBackgroundColorSchemeColor(
                ContextCompat.getColor(
                    this@StateDistrictActivity,
                    R.color.background
                )
            )
            setColorSchemeColors(
                ContextCompat.getColor(
                    this@StateDistrictActivity,
                    R.color.colorAccent
                )
            )
        }
    }
}
