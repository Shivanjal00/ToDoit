package com.example.todoit.ui.Fragments

import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Note
import android.text.format.DateFormat
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.todoit.Model.Notes
import com.example.todoit.R
import com.example.todoit.ViewModel.NotesViewModel
import com.example.todoit.databinding.FragmentCreateNotesBinding
import java.util.*

class CreateNotesFragment : Fragment() {

    lateinit var binding: FragmentCreateNotesBinding
    var priority: String = "1"

    val viewModel: NotesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

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
            binding.pGreen.setImageResource(0)
            binding.pGreen.setImageResource(0)

        }

        binding.btnSaveNotes.setOnClickListener {
            createNotes(it)
        }


        return binding.root
    }

    private fun createNotes(it: View?) {

        val title = binding.editTitle.text.toString()
        val subTitle = binding.editSubtitle.text.toString()
        val notes = binding.editNotes.text.toString()

        val d = Date()
        val notesdate: CharSequence = DateFormat.format("MMMM d, yyyy ", d.time)

        val data = Notes(
            null,
            title = title,
            subTitle = subTitle,
            notes = notes,
            date = notesdate.toString(),
            priority
        )
        viewModel.addNotes(data)
        Toast.makeText(requireContext(), "Notes saved successfully", Toast.LENGTH_SHORT).show()

    }
}