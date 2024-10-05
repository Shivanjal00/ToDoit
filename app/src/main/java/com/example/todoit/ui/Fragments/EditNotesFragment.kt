package com.example.todoit.ui.Fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import com.example.todoit.Model.Notes
import com.example.todoit.R
import com.example.todoit.ViewModel.NotesViewModel
import com.example.todoit.databinding.FragmentEditNotesBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.*

class EditNotesFragment : Fragment() {

    val oldNotes by navArgs<EditNotesFragmentArgs>()
    lateinit var binding: FragmentEditNotesBinding
    var priority: String = "1"
    val viewModel: NotesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
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
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val date = "$selectedDay/${selectedMonth + 1}/$selectedYear"  // Format: DD/MM/YYYY
            binding.edtDate.setText(date)
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun openTimePicker() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
            val time = String.format("%02d:%02d", selectedHour, selectedMinute) // Format: HH:mm
            binding.edtTime.setText(time)
        }, hour, minute, true)

        timePickerDialog.show()
    }

    private fun updateNotes(view: View) {
        val title = binding.edtTitle.text.toString()
        val subTitle = binding.edtSubtitle.text.toString()
        val notes = binding.edtNotes.text.toString()
        val date = binding.edtDate.text.toString() // Get the date from the EditText
        val time = binding.edtTime.text.toString() // Get the time from the EditText

        // Create updated note object
        val updatedNote = Notes(
            oldNotes.data.id,
            title = title,
            subTitle = subTitle,
            notes = notes,
            date = date, // Save the date
            time = time, // Save the time
            priority
        )

        viewModel.updateNotes(updatedNote)
        Toast.makeText(requireContext(), "Notes Updated Successfully", Toast.LENGTH_SHORT).show()

        // Navigate back to HomeFragment
        Navigation.findNavController(view).navigate(R.id.action_editNotesFragment2_to_homeFragment22)
    }

    // Function to show the delete confirmation dialog
    private fun showDeleteDialog() {
        val bottomSheet = BottomSheetDialog(requireContext(), R.style.BottomSheetStyle)
        bottomSheet.setContentView(R.layout.dialog_delete)

        val textViewYes = bottomSheet.findViewById<TextView>(R.id.dialog_yes)
        val textViewNo = bottomSheet.findViewById<TextView>(R.id.dialog_no)

        // If "Yes" is clicked, delete the note and navigate back to HomeFragment
        textViewYes?.setOnClickListener {
            viewModel.deleteNotes(oldNotes.data.id!!)
            bottomSheet.dismiss()

            // Navigate to HomeFragment after deletion
            Navigation.findNavController(requireView())
                .navigate(R.id.action_editNotesFragment2_to_homeFragment22)

            Toast.makeText(requireContext(), "Note Deleted", Toast.LENGTH_SHORT).show()
        }

        // If "No" is clicked, dismiss the dialog
        textViewNo?.setOnClickListener {
            bottomSheet.dismiss()
        }

        bottomSheet.show()
    }
}
