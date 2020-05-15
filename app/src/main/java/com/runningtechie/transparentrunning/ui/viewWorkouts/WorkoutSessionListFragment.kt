package com.runningtechie.transparentrunning.ui.viewWorkouts

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
import com.runningtechie.transparentrunning.model.WorkoutSession

class WorkoutSessionListFragment : Fragment() {
    private lateinit var workoutSessionListViewModel: WorkoutSessionListViewModel
    private lateinit var workoutSessionRecyclerView: RecyclerView
    private var adapter: WorkoutSessionAdapter? = WorkoutSessionAdapter(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = WorkoutSessionViewModelFactory()
        workoutSessionListViewModel =
            ViewModelProvider(this@WorkoutSessionListFragment, factory).get(WorkoutSessionListViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_workout_session_list, container, false)

        workoutSessionRecyclerView = view.findViewById(R.id.workout_session_recycler_view) as RecyclerView
        workoutSessionRecyclerView.layoutManager = LinearLayoutManager(context)
        workoutSessionRecyclerView.adapter = adapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        workoutSessionListViewModel.workoutListLiveData.observe(
            viewLifecycleOwner,
            Observer { workoutSessions ->
                workoutSessions?.let {
                    updateUI(workoutSessions)
                }
            }
        )
    }

    private fun updateUI(workoutSessions: List<WorkoutSession>) {
        adapter = WorkoutSessionAdapter(workoutSessions)
        workoutSessionRecyclerView.adapter = adapter
    }

    companion object {
        fun newInstance() = WorkoutSessionListFragment()
    }

    private inner class WorkoutSessionHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        private lateinit var workoutSession: WorkoutSession

        private val titleTextView: TextView = itemView.findViewById(R.id.workout_session_title)
        private val dateTextView: TextView = itemView.findViewById(R.id.workout_session_date)
        private val durationTextView: TextView = itemView.findViewById(R.id.workout_session_duration)
        private val distanceTextView: TextView = itemView.findViewById(R.id.workout_session_distance)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(workoutSession: WorkoutSession) {
            this.workoutSession = workoutSession
            titleTextView.text = workoutSession.id.toString()
            dateTextView.text = workoutSession.date.toString()
            durationTextView.text = workoutSession.duration.toString()
            distanceTextView.text = workoutSession.distance.toString()
        }

        override fun onClick(v: View?) {

        }

    }

    private inner class WorkoutSessionAdapter(var workoutSessions: List<WorkoutSession>) : RecyclerView.Adapter<WorkoutSessionHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutSessionHolder {
            val view = layoutInflater.inflate(R.layout.list_item_workout_session, parent, false)
            return WorkoutSessionHolder(view)
        }

        override fun getItemCount() = workoutSessions.size


        override fun onBindViewHolder(holder: WorkoutSessionHolder, position: Int) {
            val workoutSession = workoutSessions[position]
            holder.bind(workoutSession)
        }

    }
}