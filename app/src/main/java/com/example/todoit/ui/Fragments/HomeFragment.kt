package com.example.todoit.ui.Fragments

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import com.example.todoit.Model.Notes
import com.example.todoit.R
import com.example.todoit.ViewModel.NotesViewModel
import com.example.todoit.databinding.FragmentHomeBinding
import com.example.todoit.ui.Adapter.NotesAdapter
import java.util.Locale

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    val viewModel: NotesViewModel by viewModels()
    var oldMyNotes = arrayListOf<Notes>()
    lateinit var adapter: NotesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        // Set up menu using MenuHost API
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.search_menu, menu)

                val item = menu.findItem(R.id.app_bar_search)
                val searchView = item.actionView as SearchView
                searchView.queryHint = "Enter Notes Here..."
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(p0: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(p0: String?): Boolean {
                        NotesFiltering(p0)
                        return true
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.about -> {
                        Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment2_to_aboutFragment)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        // RecyclerView and ViewModel setup
        viewModel.getNotes().observe(viewLifecycleOwner) { notesList ->
            if (notesList.isNullOrEmpty()) {
                binding.rcvAllNotes.visibility = View.GONE
                binding.tvNoNotes.visibility = View.VISIBLE
            } else {
                binding.rcvAllNotes.visibility = View.VISIBLE
                binding.tvNoNotes.visibility = View.GONE
                binding.rcvAllNotes.layoutManager = GridLayoutManager(requireContext(), 2)
                oldMyNotes = notesList as ArrayList<Notes>
                adapter = NotesAdapter(requireContext(), notesList)
                binding.rcvAllNotes.adapter = adapter
            }
        }

        // Filter all notes
        binding.filterAll.setOnClickListener {
            viewModel.getNotes().observe(viewLifecycleOwner) { notesList ->
                handleNoteDisplay(notesList)
            }
        }

        // Filter high priority notes
        binding.filterHigh.setOnClickListener {
            viewModel.getHighNotes().observe(viewLifecycleOwner) { notesList ->
                handleNoteDisplay(notesList)
            }
        }

        // Filter medium priority notes
        binding.filterMedium.setOnClickListener {
            viewModel.getMediumNotes().observe(viewLifecycleOwner) { notesList ->
                handleNoteDisplay(notesList)
            }
        }

        // Filter low priority notes
        binding.filterLow.setOnClickListener {
            viewModel.getLowNotes().observe(viewLifecycleOwner) { notesList ->
                handleNoteDisplay(notesList)
            }
        }

        // Add new notes
        binding.btnAddNotes.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_homeFragment2_to_createNotesFragment2)
        }

        return binding.root
    }

    // Filtering notes based on search input
    private fun NotesFiltering(p0: String?) {
        val newFilteredList = arrayListOf<Notes>()
        for (i in oldMyNotes) {
            if (i.title.lowercase(Locale.getDefault()).contains(p0?.lowercase(Locale.getDefault())!!) ||
                i.subTitle.lowercase(Locale.getDefault()).contains(p0.lowercase(Locale.getDefault()))
            ) {
                newFilteredList.add(i)
            }
        }
        adapter.filtering(newFilteredList)
    }

    // Handle the visibility of RecyclerView and No Data TextView
    private fun handleNoteDisplay(notesList: List<Notes>) {
        if (notesList.isNullOrEmpty()) {
            binding.rcvAllNotes.visibility = View.GONE
            binding.tvNoNotes.visibility = View.VISIBLE
        } else {
            binding.rcvAllNotes.visibility = View.VISIBLE
            binding.tvNoNotes.visibility = View.GONE
            oldMyNotes = notesList as ArrayList<Notes>
            adapter = NotesAdapter(requireContext(), notesList)
            binding.rcvAllNotes.adapter = adapter
        }
    }
}

