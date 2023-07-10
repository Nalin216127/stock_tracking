package com.example.task_02.smart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.task_02.data.SmartPhone
import com.example.task_02.databinding.Item2Binding

class SmartAdapter(var list:List<SmartPhone>,  val listener:UpdateCartListener) :RecyclerView.Adapter<SmartAdapter.MyViewHolder>() {
    private var slist = emptyList<SmartPhone>()

    class MyViewHolder(var binding:Item2Binding):RecyclerView.ViewHolder(binding.root) {
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmartAdapter.MyViewHolder {
        val itemView = Item2Binding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(itemView)
    }



  /*  fun filterList(filterlist: ArrayList<SmartPhone>) {
        list.clear()
        list.addAll(filterlist)
        notifyDataSetChanged()
    }*/

    override fun onBindViewHolder(holder: SmartAdapter.MyViewHolder, position: Int) {
        val currentItem =list[position]

        holder.binding.apply {
            pName.text = currentItem.productName
            pCode.text = currentItem.productCode
            itemCount.text = currentItem.quantity.toString()
        }


        holder.binding.plus.setOnClickListener {
            var number = Integer.parseInt(holder.binding.itemCount.getText().toString())
            number++
            list[position].quantity+=1

            holder.binding.itemCount.setText(number.toString())
            listener.getQuantity(list[position])
        }


        holder.binding.minus.setOnClickListener {
            var number = Integer.parseInt(holder.binding.itemCount.getText().toString())
            if (number<=0){
                number = 0
                list[position].quantity=0
            }
            else{
                number--
                list[position].quantity-=1
            }
            holder.binding.itemCount.setText(number.toString())

            listener.getQuantity(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setData(smartPhone: List<SmartPhone>){
        this.slist =smartPhone
        notifyDataSetChanged()
    }

    interface UpdateCartListener{
        fun getQuantity(smartPhone: SmartPhone)
    }
}