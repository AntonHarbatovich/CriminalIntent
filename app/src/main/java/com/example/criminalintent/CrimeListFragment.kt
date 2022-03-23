package com.example.criminalintent

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class CrimeListFragment : Fragment() {

    private lateinit var crimeRecyclerView: RecyclerView
    private var adapter: CrimeAdapter? = null

    private val crimeListViewModel: CrimeListViewModel by lazy {
        ViewModelProvider(this).get(CrimeListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Total crimes: ${crimeListViewModel.crimes.size}")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)

        crimeRecyclerView = view.findViewById(R.id.crime_recycler_view) as RecyclerView
        crimeRecyclerView.layoutManager = LinearLayoutManager(context)

        updateUI()

        return view
    }

    private fun updateUI() {
        val crimes = crimeListViewModel.crimes
        adapter = CrimeAdapter(crimes)
        crimeRecyclerView.adapter = adapter
    }

    private inner class CrimeHolder(view: View) :
        RecyclerView.ViewHolder(view), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        private lateinit var crime: Crime

        private val titleTextView: TextView = itemView.findViewById(R.id.crime_title)
        private val dateTextView: TextView = itemView.findViewById(R.id.crime_date)

        fun bind(crime: Crime) {
            this.crime = crime
            if (crime.requiresPolice) {
                bindRequiresPolice()
            } else {
                bindSimple()
            }
        }

        private fun bindSimple() {
            titleTextView.text = this.crime.title
            dateTextView.text = this.crime.date.toString()
        }

        private fun bindRequiresPolice() {
            val buttonRequiresPolice: Button = itemView.findViewById(R.id.crime_button_requires_police)
            titleTextView.text = this.crime.title
            dateTextView.text = this.crime.date.toString()
            buttonRequiresPolice.setOnClickListener {
                Toast.makeText(context, "There is a call to the police", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onClick(p0: View?) {
            Toast.makeText(context, "${crime.title} pressed!", Toast.LENGTH_SHORT).show()
        }

    }

    private inner class CrimeAdapter(var crimes: List<Crime>) :
        RecyclerView.Adapter<CrimeHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
            val layout = when (viewType) {
                CRIME_SIMPLE -> R.layout.list_item_crime
                CRIME_REQUIRES_POLICE -> R.layout.list_item_crime_requires_police
                else -> throw IllegalArgumentException("Invalid type")
            }
            val view = layoutInflater.inflate(layout, parent, false)
            return CrimeHolder(view)
        }

        override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
            val crime = crimes[position]
            holder.bind(crime)
        }

        override fun getItemCount(): Int = crimes.size
        override fun getItemViewType(position: Int): Int {
            val crime = crimes[position]
            return when (crime.requiresPolice) {
                true -> CRIME_REQUIRES_POLICE
                else -> CRIME_SIMPLE
            }
        }
    }

    companion object {
        private const val TAG = "CrimeListFragment"

        private const val CRIME_SIMPLE = 0
        private const val CRIME_REQUIRES_POLICE = 1

        fun newInstance(): CrimeListFragment {
            return CrimeListFragment()
        }
    }
}