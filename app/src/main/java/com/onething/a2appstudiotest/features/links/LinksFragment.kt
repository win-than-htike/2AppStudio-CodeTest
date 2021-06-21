package com.onething.a2appstudiotest.features.links

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.onething.a2appstudiotest.R
import com.onething.a2appstudiotest.databinding.FragmentLinksBinding
import com.onething.a2appstudiotest.features.links.dialog.AddLinksDialog
import com.onething.a2appstudiotest.utils.SwipeToDeleteCallback
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LinksFragment : Fragment() {

    private lateinit var binding: FragmentLinksBinding

    private val viewModel: LinksViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_links, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        setHasOptionsMenu(true)
        addSwipeDeleteToRecyclerView()
        binding.rvLinks.adapter = LinksListAdapter()
        viewModel.status.observe(viewLifecycleOwner, {
            val loadingSnackbar = Snackbar.make(binding.clRoot, getString(R.string.msg_adding_links), Snackbar.LENGTH_INDEFINITE)
            when (it) {
                LinksStatus.LOADING -> {
                    loadingSnackbar.show()
                }
                LinksStatus.DONE -> {
                    loadingSnackbar.dismiss()
                    Snackbar.make(binding.clRoot, getString(R.string.msg_done), Snackbar.LENGTH_SHORT).show()
                }
                LinksStatus.ERROR -> {
                    loadingSnackbar.dismiss()
                    Snackbar.make(binding.clRoot, getString(R.string.msg_error), Snackbar.LENGTH_SHORT).show()
                }
            }
        })

    }

    private fun addSwipeDeleteToRecyclerView() {
        val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.delete(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.rvLinks)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_add -> {
                showAddLinksDialog()
                true
            }
            R.id.menu_shuffle -> {
                viewModel.shuffle()
                true
            }
            R.id.menu_resort -> {
                viewModel.sort()
                true
            }
            R.id.menu_delete -> {
                multipleDelete()
                true
            }
            else ->
                super.onOptionsItemSelected(item)
        }
    }

    private fun multipleDelete() {
        val adapter = binding.rvLinks.adapter as LinksListAdapter
        val list = adapter.getSelectedItem()
        if (list.isNotEmpty()) {
            viewModel.multipleDelete(adapter.getSelectedItem())
            adapter.resetSelectState()
        } else {
            Snackbar.make(binding.clRoot, getString(R.string.msg_cannot_delete), Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun showAddLinksDialog() {
        val dialog = AddLinksDialog()
        dialog.setOnClickAddLink {
            viewModel.processLinks(it)
        }
        dialog.show(childFragmentManager, AddLinksDialog.TAG)
    }



}