package com.runningtechie.transparentrunning.ui.viewWorkouts

import android.content.Context
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
import kotlinx.android.synthetic.main.fragment_recycler_view_list.*
import kotlinx.android.synthetic.main.list_item_workout_session.*

class WorkoutSessionListFragment : Fragment() {
    interface Callbacks {
        fun onWorkoutSelected(workoutSessionId: Long?)
    }

    private var callbacks: Callbacks? = null

    private lateinit var workoutSessionListViewModel: WorkoutSessionListViewModel
    private var adapter: WorkoutSessionAdapter? = WorkoutSessionAdapter(emptyList())

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = WorkoutSessionViewModelFactory()
        workoutSessionListViewModel =
            ViewModelProvider(this@WorkoutSessionListFragment, factory).get(WorkoutSessionListViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_recycler_view_list, container, false)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
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

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    private fun updateUI(workoutSessions: List<WorkoutSession>) {
        adapter = WorkoutSessionAdapter(workoutSessions)
        recyclerView.adapter = adapter
    }

    companion object {
        fun newInstance() = WorkoutSessionListFragment()
    }

    private inner class WorkoutSessionHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        private lateinit var workoutSession: WorkoutSession

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(workoutSession: WorkoutSession) {
            this.workoutSession = workoutSession
            workoutSessionIdTextView.text = workoutSession.id.toString()
            workoutSessionTitleTextView.text = workoutSession.title.toString()
            workoutSessionDateTextView.text = workoutSession.date.toString()
            workoutSessionDurationTextView.text = workoutSession.duration.toString()
            workoutSessionDistanceTextView.text = workoutSession.distance.toString()
        }

        override fun onClick(v: View?) {
            callbacks?.onWorkoutSelected(workoutSession.id)
        }

    }

    private inner class WorkoutSessionAdapter(var workoutSessions: List<WorkoutSession>) :
        RecyclerView.Adapter<WorkoutSessionHolder>() {
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
