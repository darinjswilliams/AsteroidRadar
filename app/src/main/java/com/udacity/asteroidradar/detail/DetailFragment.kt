package com.udacity.asteroidradar.detail


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentDetailBinding
import timber.log.Timber

class DetailFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val application = requireNotNull(activity).application
        val asteroid = DetailFragmentArgs.fromBundle(requireArguments()).selectedAsteroid

        val viewModelFactory = DetailViewModelFactory(asteroid, application)

        Timber.i("Display Detail Information")

        binding.asteroid =  ViewModelProviders.of(
            this, viewModelFactory).get(DetailViewModel::class.java)

        binding.helpButton.setOnClickListener {
            displayAstronomicalUnitExplanationDialog()
        }


        return binding.root
    }

    private fun displayAstronomicalUnitExplanationDialog() {
        val builder = AlertDialog.Builder(requireActivity())
            .setMessage(getString(R.string.astronomica_unit_explanation))
            .setPositiveButton(android.R.string.ok, null)
        builder.create().show()
    }
}
