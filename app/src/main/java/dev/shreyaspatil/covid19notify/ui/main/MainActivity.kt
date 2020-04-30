package dev.shreyaspatil.covid19notify.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.MergeAdapter
import androidx.work.*
import dev.shreyaspatil.covid19notify.R
import dev.shreyaspatil.covid19notify.databinding.ActivityMainBinding
import dev.shreyaspatil.covid19notify.model.Details
import dev.shreyaspatil.covid19notify.ui.main.adapter.StateAdapter
import dev.shreyaspatil.covid19notify.ui.main.adapter.TotalAdapter
import dev.shreyaspatil.covid19notify.ui.state.StateDistrictActivity
import dev.shreyaspatil.covid19notify.utils.State
import dev.shreyaspatil.covid19notify.utils.getPeriod
import dev.shreyaspatil.covid19notify.worker.NotificationWorker
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModel()

    private val mTotalAdapter = TotalAdapter()
    private val mStateAdapter = StateAdapter()
    private val adapter = MergeAdapter(mTotalAdapter, mStateAdapter)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Init Toolbar
        setSupportActionBar(binding.toolbar)

        // Set adapter to the RecyclerView
        binding.recycler.adapter = adapter

        initData()
        initWorker()

        binding.swipeRefreshLayout.setOnRefreshListener {
            loadData()
        }
        //Listener for the passing the data to the state activity
        mStateAdapter.clickListener = this::navigateToStateDistrictScreen
    }

    private fun initData() {
        viewModel.covidLiveData.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> binding.swipeRefreshLayout.isRefreshing = true
                is State.Error -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    Toast.makeText(applicationContext, state.message, Toast.LENGTH_LONG).show()
                }
                is State.Success -> {

                    val list = state.data.stateWiseDetails
                    mTotalAdapter.submitList(list.subList(0, 1))
                    mStateAdapter.submitList(list.subList(1, list.size - 1))
                    //Toolbar Updated time
                    binding.textLastUpdatedView.text = this.getString(
                        R.string.text_last_updated,
                        getPeriod(
                            SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                                .parse(list[0].lastUpdatedTime)
                        )
                    )
                    binding.swipeRefreshLayout.isRefreshing = false

                }
            }
        })

        loadData()
    }

    private fun navigateToStateDistrictScreen(details: Details) {
        Intent(this, StateDistrictActivity::class.java).also {
            it.putExtra("StateDetails", details)
            startActivity(it)
        }
    }

    private fun loadData() {
        viewModel.getData()
    }

    private fun initWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val notificationWorkRequest =
            PeriodicWorkRequestBuilder<NotificationWorker>(1, TimeUnit.HOURS)
                .setConstraints(constraints)
                .build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            JOB_TAG,
            ExistingPeriodicWorkPolicy.KEEP,
            notificationWorkRequest
        )
    }

    companion object {
        const val JOB_TAG = "notificationWorkTag"
    }
}
