package com.example.lokerandroid.ui.main.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.lokerandroid.R
import com.example.lokerandroid.databinding.ItemJobBinding
import com.example.lokerandroid.data.model.LowonganKerja
import com.example.lokerandroid.ui.main.fragment.ListFragmentDirections
import com.example.lokerandroid.ui.main.view.LokerClickListener
import kotlinx.android.synthetic.main.item_job.view.*

class LokerListAdapter(val lokerList: ArrayList<LowonganKerja>): RecyclerView.Adapter<LokerListAdapter.LokerViewHolder>(),
    LokerClickListener {

    fun updateLokerList(newLokerList: List<LowonganKerja>){
        lokerList.clear()
        lokerList.addAll(newLokerList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LokerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<ItemJobBinding>(inflater, R.layout.item_job, parent, false)
        return LokerViewHolder(
            view
        )
    }

    override fun getItemCount() = lokerList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: LokerViewHolder, position: Int) {
        holder.view.loker = lokerList[position]
        holder.view.listener = this
    }

    override fun onLokerClicked(v: View) {
        val uuid = v.lokerId.text.toString().toInt()
        val action =
            ListFragmentDirections.actionDetailFragment()
        action.jobUuid = uuid
        Navigation.findNavController(v).navigate(action)
    }

    class LokerViewHolder(var view: ItemJobBinding) : RecyclerView.ViewHolder(view.root)
}