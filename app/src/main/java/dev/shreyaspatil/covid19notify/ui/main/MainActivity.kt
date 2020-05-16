package dev.shreyaspatil.covid19notify.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.MergeAdapter
import androidx.work.*
import com.google.android.material.snackbar.Snackbar
import dev.shreyaspatil.covid19notify.R
import dev.shreyaspatil.covid19notify.databinding.ActivityMainBinding
import dev.shreyaspatil.covid19notify.model.Details
import dev.shreyaspatil.covid19notify.ui.adapter.TotalAdapter
import dev.shreyaspatil.covid19notify.ui.details.StateDetailsActivity
import dev.shreyaspatil.covid19notify.ui.main.adapter.ItemAdapter
import dev.shreyaspatil.covid19notify.utils.*
import dev.shreyaspatil.covid19notify.worker.NotificationWorker
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

@FlowPreview
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModel()

    private val mTotalAdapter = TotalAdapter()
    private val mStateAdapter = ItemAdapter(this::navigateToStateDetailsActivity)
    private val adapter = MergeAdapter(mTotalAdapter, mStateAdapter)

    // Useful when back navigation is pressed.
    private var backPressedTime = 0L
    private val backSnackbar by lazy {
        Snackbar.make(binding.root, BACK_PRESSED_MESSAGE, Snackbar.LENGTH_SHORT)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        initData()
        initWorker()
    }

    private fun initViews() {
        setSupportActionBar(binding.appBarlayout.toolbar)

        binding.recycler.adapter = adapter

        binding.swipeRefreshLayout.setOnRefreshListener {
            loadData()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_uimode -> {
                val uiMode = if (isDarkTheme()) {
                    AppCompatDelegate.MODE_NIGHT_NO
                } else {
                    AppCompatDelegate.MODE_NIGHT_YES
                }
                applyTheme(uiMode)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

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
                    binding.swipeRefreshLayout.isRefreshing = false

                    val list = state.data.stateWiseDetails
                    mTotalAdapter.submitList(list.subList(0, 1))
                    mStateAdapter.submitList(list.subList(1, list.size - 1))

                    // Set Last Updated Time
                    supportActionBar?.subtitle = getString(
                        R.string.text_last_updated,
                        getPeriod(
                            list[0].lastUpdatedTime.toDateFormat()
                        )
                    )
                }
            }
        })

        if (viewModel.covidLiveData.value !is State.Success) {
            loadData()
        }
    }

    private fun loadData() {
        viewModel.getData()
    }

    private fun navigateToStateDetailsActivity(details: Details) {
        startActivity(Intent(this, StateDetailsActivity::class.java).apply {
            putExtra(StateDetailsActivity.KEY_STATE_DETAILS, details)
        })
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

    override fun onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backSnackbar.dismiss()
            super.onBackPressed()
            return
        } else {
            backSnackbar.show()
        }
        backPressedTime = System.currentTimeMillis()
    }

    companion object {
        const val JOB_TAG = "notificationWorkTag"
        const val BACK_PRESSED_MESSAGE = "Press back again to exit"
    }
}
