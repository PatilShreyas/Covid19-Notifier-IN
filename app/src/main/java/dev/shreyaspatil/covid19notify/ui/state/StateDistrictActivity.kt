package dev.shreyaspatil.covid19notify.ui.state

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import dev.shreyaspatil.covid19notify.databinding.ActivityStateDistrictBinding
import dev.shreyaspatil.covid19notify.model.Details

class StateDistrictActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStateDistrictBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStateDistrictBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val details = intent.getParcelableExtra<Details>("StateDetails")

        binding.tvCheck.text = details?.toString()
        Log.d("Second Activity", "${details.toString()}")
    }
}
