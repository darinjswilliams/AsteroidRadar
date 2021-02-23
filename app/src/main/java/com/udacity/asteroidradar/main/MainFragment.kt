package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.api.AsteroidFilter
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import timber.log.Timber

class MainFragment : Fragment() {

    /**
     * One way to delay creation of the viewModel until an appropriate lifecycle method is to use
     * lazy. This requires that viewModel not be referenced before onViewCreated(), which we
     * do in this Fragment.
     */
    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }
        ViewModelProvider(
            this,
            MainViewModel.Factory(activity.application)
        ).get(MainViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMainBinding.inflate(inflater)

        //Allows Data binding to be Observed LiveData for any changes with the LifeCycle of this Fragment
        binding.lifecycleOwner = this

        //Give binding access to the MainViewModel
        binding.viewModel = viewModel

        //This will trigger livedata
        binding.asteroidRecycler.adapter = MainAsteroidAdapter(MainAsteroidAdapter.OnClickListener {
            viewModel.displayAsteroidDetails(it)
        })


        setHasOptionsMenu(true)
        Timber.i("OnCreateView mainFragment")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.navigateToSelectedProperty.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                Timber.i("Navigate to Detail Screen")
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.displayAsteroidDetailsCompleted()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.updateFilter(
            when (item.itemId) {
                R.id.show_saved_menu -> AsteroidFilter.SHOW_SAVE
                R.id.show_today_menu -> AsteroidFilter.SHOW_TODAY
                else -> AsteroidFilter.SHOW_WEEK
            }
        )
        return true
    }
}
