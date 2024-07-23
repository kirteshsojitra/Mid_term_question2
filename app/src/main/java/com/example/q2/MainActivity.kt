package com.example.question2

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var goalSpinner: Spinner
    private lateinit var goaActivityInput: EditText
    private lateinit var logActivityButton: Button
    private lateinit var progressRecyclerView: RecyclerView

    private val progressData = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        goalSpinner = findViewById(R.id.goalSpinner)
        goaActivityInput = findViewById(R.id.activityInput)
        logActivityButton = findViewById(R.id.logActivityButton)
        progressRecyclerView = findViewById(R.id.progressRecyclerView)

        // Setup Spinner
        ArrayAdapter.createFromResource(
            this,
            R.array.fitness_goals_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            goalSpinner.adapter = adapter
        }

        // Spinner onItemSelectedListener
        goalSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val item = parent.getItemAtPosition(position).toString()
                Toast.makeText(this@MainActivity, "Selected: $item", Toast.LENGTH_SHORT).show()
                goaActivityInput.text.clear()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Initialize RecyclerView
        progressRecyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = ProgressAdapter(progressData)
        progressRecyclerView.adapter = adapter


        logActivityButton.setOnClickListener {
            val activity = goalSpinner.getSelectedItem().toString();
            val activityValue = goaActivityInput.text.toString().toIntOrNull() ?: 0

            if (activity.isNotEmpty() && activityValue > 0) {
                val currentTime =
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
                if (activity == "Calories") {
                    val newActivity = "$currentTime - $activity burn: $activityValue"
                    progressData.add(newActivity)
                } else if (activity == "Steps") {
                    val newActivity = "$currentTime - $activity: $activityValue"
                    progressData.add(newActivity)
                } else if (activity == "Distance") {
                    val distanceValue = activityValue.toDouble()/1000
                    val newActivity = "$currentTime - $activity: $distanceValue km"
                    progressData.add(newActivity)
                }
                adapter.updateProgress(progressData)
                goaActivityInput.text.clear()

            } else {
                Toast.makeText(this, "Please enter any number", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}