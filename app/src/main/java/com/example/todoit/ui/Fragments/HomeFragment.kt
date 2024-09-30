package com.example.todoit.ui.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.example.todoit.R
import com.example.todoit.ViewModel.NotesViewModel
import com.example.todoit.databinding.FragmentHomeBinding
import com.example.todoit.ui.Adapter.NotesAdapter

class HomeFragment : Fragment() {

    lateinit var binding : FragmentHomeBinding
    val viewModel: NotesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        viewModel.getNotes().observe(viewLifecycleOwner, {notesList ->
            binding.rcvAllNotes.layoutManager = GridLayoutManager(requireContext(),2)

            binding.rcvAllNotes.adapter = NotesAdapter(requireContext(),notesList)

        })

        binding.btnAddNotes.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_homeFragment2_to_createNotesFragment2)
        }
        return binding.root
    }
}