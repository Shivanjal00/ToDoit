package com.example.todoit.ui.Fragments

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.todoit.Model.Notes
import com.example.todoit.R
import com.example.todoit.TaskReminderReceiver
import com.example.todoit.ViewModel.NotesViewModel
import com.example.todoit.databinding.FragmentCreateNotesBinding
import java.util.*
import android.provider.Settings


class CreateNotesFragment : Fragment() {

    lateinit var binding: FragmentCreateNotesBinding
    var priority: String = "1"

    val viewModel: NotesViewModel by viewModels()
    private var calendar = Calendar.getInstance() // To store selected date and time

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentCreateNotesBinding.inflate(layoutInflater, container, false)

        binding.pGreen.setImageResource(R.drawable.baseline_done_24)

        binding.pGreen.setOnClickListener {
            priority = "1"
            binding.pGreen.setImageResource(R.drawable.baseline_done_24)
            binding.pRed.setImageResource(0)
            binding.pyellow.setImageResource(0)
        }

        binding.pyellow.setOnClickListener {
            priority = "2"
            binding.pyellow.setImageResource(R.drawable.baseline_done_24)
            binding.pRed.setImageResource(0)
            binding.pGreen.setImageResource(0)
        }

        binding.pRed.setOnClickListener {
            priority = "3"
            binding.pRed.setImageResource(R.drawable.baseline_done_24)
            binding.pyellow.setImageResource(0)
            binding.pGreen.setImageResource(0)
        }

        binding.editDate.setOnClickListener {
            openDatePicker()
        }
        binding.editTime.setOnClickListener {
            openTimePicker()
        }

        binding.btnSaveNotes.setOnClickListener {
            createNotes(it)
        }

        return binding.root
    }

    // Open DatePickerDialog to select date
    private fun openDatePicker() {
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                // Format the selected date and set it to the EditText
                val selectedDate = String.format("%02d/%02d/%d", dayOfMonth, monthOfYear + 1, year)
                binding.editDate.setText(selectedDate)
            },
            currentYear,
            currentMonth,
            currentDay
        )
        datePickerDialog.show()
    }

    // Open TimePickerDialog to select time
    private fun openTimePicker() {
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)

                // Format the selected time and set it to the EditText
                val selectedTime = String.format("%02d:%02d", hourOfDay, minute)
                binding.editTime.setText(selectedTime)
            },
            currentHour,
            currentMinute,
            true
        )
        timePickerDialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun scheduleNotification(title: String, description: String, timeInMillis: Long) {
        // Adjust the calendar to set seconds to zero
        calendar.set(Calendar.SECOND, 0)

        val adjustedTimeInMillis = calendar.timeInMillis // Use the adjusted calendar

        try {
            val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager

            // Check if the app can schedule exact alarms
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (!alarmManager.canScheduleExactAlarms()) {
                    showExactAlarmPermissionDialog()
                    return
                }
            }

            val intent = Intent(requireContext(), TaskReminderReceiver::class.java).apply {
                putExtra("taskTitle", title)
                putExtra("taskDescription", description)
            }

            val pendingIntent = PendingIntent.getBroadcast(
                requireContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            // Schedule the alarm
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, adjustedTimeInMillis, pendingIntent)
        } catch (e: SecurityException) {
            Toast.makeText(requireContext(), "Permission required to set exact alarms", Toast.LENGTH_SHORT).show()
        }
    }


    private fun showExactAlarmPermissionDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Exact Alarm Permission")
            .setMessage("This app requires permission to schedule exact alarms. Please allow this permission in settings.")
            .setPositiveButton("Go to Settings") { _, _ ->
                // Navigate to app settings to manually allow permission
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", requireContext().packageName, null)
                }
                startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun createNotes(it: View?) {
        val title = binding.editTitle.text.toString()
        val subTitle = binding.editSubtitle.text.toString()
        val notes = binding.editNotes.text.toString()

        // Set seconds to zero for the calendar instance
        calendar.set(Calendar.SECOND, 0)

        // Get the time in milliseconds with seconds set to zero
        val taskTimeInMillis = calendar.timeInMillis

        // Schedule the notification for the selected time
        scheduleNotification(title, subTitle, taskTimeInMillis)

        val d = Date()
        val notesDate: CharSequence = DateFormat.format("MMMM d, yyyy", d.time)

        // Format the selected time
        val selectedTime = String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE))

        // Create the Notes object with the new date and time fields
        val data = Notes(
            null,
            title = title,
            subTitle = subTitle,
            notes = notes,
            date = notesDate.toString(),
            time = selectedTime, // Save the selected time here
            priority = priority
        )

        // Add notes to the ViewModel
        viewModel.addNotes(data)

        // Show a success message
        Toast.makeText(requireContext(), "Notes saved successfully", Toast.LENGTH_SHORT).show()

        // Navigate back to the home fragment
        Navigation.findNavController(it!!).navigate(R.id.action_createNotesFragment2_to_homeFragment22)
    }


}
