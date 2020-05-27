package com.runningtechie.transparentrunning.ui.viewLocationPoint

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.runningtechie.transparentrunning.R
import com.runningtechie.transparentrunning.model.LocationPoint
import kotlinx.android.synthetic.main.fragment_recycler_view_list.*
import kotlinx.android.synthetic.main.list_item_location_point.*

class LocationPointListFragment : Fragment() {

    private lateinit var locationPointListViewModel: LocationPointListViewModel
    private var adapter: LocationPointAdapter? = LocationPointAdapter(emptyList())


    companion object {
        const val ARG_WORKOUT_SESSION_ID = "ARG_WORKOUT_SESSION_ID"
        fun newInstance(workoutSessionId: Long?): LocationPointListFragment {
            val args = Bundle().apply {
                putSerializable(ARG_WORKOUT_SESSION_ID, workoutSessionId)
            }
            return LocationPointListFragment().apply {
                arguments = args
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val workoutSessionId = arguments?.getSerializable(ARG_WORKOUT_SESSION_ID) as Long
        val factory = LocationPointViewModelFactory(workoutSessionId)
        locationPointListViewModel =
            ViewModelProvider(this@LocationPointListFragment, factory).get(LocationPointListViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_recycler_view_list, container, false)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        locationPointListViewModel.locationPointListLiveData.observe(
            viewLifecycleOwner,
            Observer { locationPoints->
                locationPoints?.let {
                    updateUI(locationPoints)
                }
            }
        )
    }

    private fun updateUI(locationPoints: List<LocationPoint>) {
        adapter = LocationPointAdapter(locationPoints)
        recyclerView.adapter = adapter
    }

    private inner class LocationPointHolder(view: View) : RecyclerView.ViewHolder(view) {
        private lateinit var locationPoint: LocationPoint

        fun bind(locationPoint: LocationPoint) {
            this.locationPoint = locationPoint
            locationPointIdTextView.text = locationPoint.id.toString()
            workoutSessionIdTextView.text = locationPoint.sessionId.toString()
            locationPointTimeTextView.text = locationPoint.time.toString()
            locationPointElapsedTimeTextView.text = locationPoint.elapsedTime.toString()
            locationPointLatitudeTextView.text = locationPoint.latitude.toString()
            locationPointLongitudeTextView.text = locationPoint.longitude.toString()
            locationPointAltitudeTextView.text = locationPoint.altitude.toString()
            locationPointSpeedTextView.text = locationPoint.speed.toString()
            locationPointElapsedDistanceTextView.text = locationPoint.elapsedDistance.toString()
        }
    }

    private inner class LocationPointAdapter(var locationPoints: List<LocationPoint>) :
        RecyclerView.Adapter<LocationPointHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationPointHolder {
            val view = layoutInflater.inflate(R.layout.list_item_location_point, parent, false)
            return LocationPointHolder(view)
        }

        override fun getItemCount() = locationPoints.size

        override fun onBindViewHolder(holder: LocationPointHolder, position: Int) {
            val locationPoint = locationPoints[position]
            holder.bind(locationPoint)
        }

    }
}
