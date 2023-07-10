package com.example.task_02.smart

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.task_02.R
import com.example.task_02.databinding.AdapterPriceBinding

class PriceAdapter (listPrc: ArrayList<String>) : RecyclerView.Adapter<PriceAdapter.MyViewHolder>() {

    private var priceList = listPrc
    private var onClickListener:OnClickListener? = null
    var listener:OnItemClickListener? = null
    var selectedPrice = ""

    class MyViewHolder(var binding: AdapterPriceBinding): RecyclerView.ViewHolder(binding.root) {
    }

    fun setClickListener(listener1: OnItemClickListener){
        listener = listener1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PriceAdapter.MyViewHolder {
        val itemView = AdapterPriceBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(itemView)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: PriceAdapter.MyViewHolder, position: Int) {
        val currentItem =priceList[position]
        holder.binding.apply {
//            pName.text = currentItem.price.toString()
//            pCode.text = currentItem.productCode.toString()
                button3.text = currentItem.toString()
            (selectedPrice == currentItem).let{
                button3.isSelected = it
            }
            button3.setOnClickListener{
                Log.d("position",position.toString())
                listener!!.onClicked(position)
                selectedPrice = currentItem
                notifyDataSetChanged()
            }

        }

        holder.itemView.rootView.setOnClickListener {
            listener!!.onClicked(position)
        }


//        holder.button.setSelected(if (holder.button.isSelected()) true else false)


    }

    override fun getItemCount(): Int {
        return priceList.size
    }

    interface OnItemClickListener{
        fun onClicked(position: Int)
    }

}