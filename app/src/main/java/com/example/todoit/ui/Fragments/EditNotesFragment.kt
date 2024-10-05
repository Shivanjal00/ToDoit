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
import android.provider.Settings
import android.text.format.DateFormat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.todoit.Model.Notes
import com.example.todoit.R
import com.example.todoit.TaskReminderReceiver
import com.example.todoit.ViewModel.NotesViewModel
import com.example.todoit.databinding.FragmentEditNotesBinding
import java.util.*

class EditNotesFragment : Fragment() {

    val oldNotes by navArgs<EditNotesFragmentArgs>()
    lateinit var binding: FragmentEditNotesBinding
    var priority: String = "1"

    val viewModel: NotesViewModel by viewModels()
    private var calendar = Calendar.getInstance() // To store selected date and time

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentEditNotesBinding.inflate(layoutInflater, container, false)

        // Set up menu using MenuHost API
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.delete_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menu_delete -> {
                        showDeleteDialog()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        // Populate fields with old data
        binding.edtTitle.setText(oldNotes.data.title)
        binding.edtSubtitle.setText(oldNotes.data.subTitle)
        binding.edtNotes.setText(oldNotes.data.notes)
        binding.edtDate.setText(oldNotes.data.date)  // Populate the date field
        binding.edtTime.setText(oldNotes.data.time)  // Populate the time field

        // Set priority according to the existing data
        when (oldNotes.data.priority) {
            "1" -> {
                priority = "1"
                binding.pGreen.setImageResource(R.drawable.baseline_done_24)
            }
            "2" -> {
                priority = "2"
                binding.pYellow.setImageResource(R.drawable.baseline_done_24)
            }
            "3" -> {
                priority = "3"
                binding.pRed.setImageResource(R.drawable.baseline_done_24)
            }
        }

        // Set priority selection click listeners
        binding.pGreen.setOnClickListener {
            priority = "1"
            binding.pGreen.setImageResource(R.drawable.baseline_done_24)
            binding.pRed.setImageResource(0)
            binding.pYellow.setImageResource(0)
        }
        binding.pYellow.setOnClickListener {
            priority = "2"
            binding.pYellow.setImageResource(R.drawable.baseline_done_24)
            binding.pRed.setImageResource(0)
            binding.pGreen.setImageResource(0)
        }
        binding.pRed.setOnClickListener {
            priority = "3"
            binding.pRed.setImageResource(R.drawable.baseline_done_24)
            binding.pYellow.setImageResource(0)
            binding.pGreen.setImageResource(0)
        }

        // Date and time picker listeners
        binding.edtDate.setOnClickListener { openDatePicker() }
        binding.edtTime.setOnClickListener { openTimePicker() }

        // Save updated note
        binding.btnEditSaveNotes.setOnClickListener {
            updateNotes(it)
        }

        return binding.root
    }

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
                binding.edtDate.setText(selectedDate)
            },
            currentYear,
            currentMonth,
            currentDay
        )
        datePickerDialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun openTimePicker() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
            val time = String.format("%02d:%02d", selectedHour, selectedMinute) // Format the selected time
            binding.edtTime.setText(time)

            // Set the selected time to the calendar object
            calendar.set(Calendar.HOUR_OF_DAY, selectedHour)
            calendar.set(Calendar.MINUTE, selectedMinute)
            calendar.set(Calendar.SECOND, 0) // Set the seconds to zero

            // Set the alarm
            setAlarm(calendar.timeInMillis)
        }, hour, minute, DateFormat.is24HourFormat(requireContext()))
        timePickerDialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun setAlarm(timeInMillis: Long) {
        // Create an intent for the alarm
        val intent = Intent(requireContext(), TaskReminderReceiver::class.java)

        // Create a pending intent
        val pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, intent, PendingIntent.FLAG_MUTABLE)

        // Create an alarm manager
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Set the alarm
        if (alarmManager.canScheduleExactAlarms()) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
        } else {
            // Request the permission
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
            startActivity(intent)
        }
    }

    private fun updateNotes(view: View) {
        val title = binding.edtTitle.text.toString()
        val subTitle = binding.edtSubtitle.text.toString()
        val notes = binding.edtNotes.text.toString()
        val date = binding.edtDate.text.toString()
        val time = binding.edtTime.text.toString()

        /*val calendar = Calendar.getInstance()
        val timeParts = time.split(":")
        calendar.set(Calendar.HOUR_OF_DAY, timeParts[0].toInt())
        calendar.set(Calendar.MINUTE, timeParts[1].toInt())
        calendar.set(Calendar.SECOND, 0) // Set the seconds to zero*/

        val updatedNotes = Notes(oldNotes.data.id, title, subTitle, notes, date, time, priority)
        viewModel.updateNotes(updatedNotes)

        Toast.makeText(requireContext(), "Notes updated successfully", Toast.LENGTH_SHORT).show()

        Navigation.findNavController(view).navigate(R.id.action_editNotesFragment2_to_homeFragment22)
    }

    private fun showDeleteDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Delete Note")
        builder.setMessage("Are you sure you want to delete this note?")
        builder.setPositiveButton("Yes") { _, _ ->
            oldNotes.data.id?.let { viewModel.deleteNotes(it) }
            Toast.makeText(requireContext(), "Note deleted successfully", Toast.LENGTH_SHORT).show()
            Navigation.findNavController(requireView()).navigate(R.id.action_editNotesFragment2_to_homeFragment22)
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.show()
    }
}