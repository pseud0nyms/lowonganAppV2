package com.example.lokerandroid.ui.main.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.lokerandroid.R
import com.example.lokerandroid.ui.main.adapter.LokerListAdapter
import com.example.lokerandroid.ui.main.fragment.ListFragmentDirections
import com.example.lokerandroid.ui.main.viewmodel.LokerViewModel
import kotlinx.android.synthetic.main.fragment_list.*

/**
 * A simple [Fragment] subclass.
 */
class ListFragment : Fragment() {

    private lateinit var viewModel: LokerViewModel
    private val lokerListAdapter =
        LokerListAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(LokerViewModel::class.java)
        viewModel.refresh()

        lokerList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = lokerListAdapter
        }

        refreshLayout.setOnRefreshListener {
            lokerList.visibility = View.GONE
            listError.visibility = View.GONE
            loadingView.visibility = View.VISIBLE
            viewModel.refreshBypassCache()
            refreshLayout.isRefreshing = false
        }

        observeViewModel()
    }

    fun observeViewModel(){
        viewModel.loker.observe(viewLifecycleOwner, Observer { loker ->
            loker?.let {
                lokerList.visibility = View.VISIBLE
                lokerListAdapter.updateLokerList(loker)
            }
        })

        viewModel.lokerLoadError.observe(viewLifecycleOwner, Observer { isError ->
            isError?.let {
                listError.visibility = if (it) View.VISIBLE else View.GONE
            }
        })

        viewModel.loading.observe(viewLifecycleOwner, Observer { isLoading ->
            isLoading?.let {
                loadingView.visibility = if (it) View.VISIBLE else View.GONE
                if (it){
                    listError.visibility = View.GONE
                    lokerList.visibility = View.GONE
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.actionSetings -> {
                view?.let { Navigation.findNavController(it).navigate(ListFragmentDirections.actionSetings()) }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
