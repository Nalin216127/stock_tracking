package com.example.task_02

import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.SearchView.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.task_02.data.SmartPhone
import com.example.task_02.ViewModel.SmartViewModel
import com.example.task_02.databinding.StockbuyBinding
import com.example.task_02.smart.PriceAdapter
import com.example.task_02.smart.SmartAdapter
import org.json.JSONArray
import org.json.JSONException
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.util.Collections.addAll


class MainActivity : AppCompatActivity() {

    private val binding: StockbuyBinding by lazy { StockbuyBinding.inflate(layoutInflater) }
    private var listIm=ArrayList<SmartPhone>()
    private var listDede= ArrayList<SmartPhone>()
    private var listFilter= ArrayList<SmartPhone>()

    private var listPrice=ArrayList<String>()
    private val imitemAdapter by lazy {  SmartAdapter(listFilter, onUpdate) }
    lateinit var searchView: SearchView
    lateinit var mSmartViewModel: SmartViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        mSmartViewModel = ViewModelProvider(this).get(SmartViewModel::class.java)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        addItemsFromJSON(com.example.task_02.R.raw.smartphones)

        addPriceItemsFromJSON()
        Log.d("list",listPrice.size.toString())

//
        Log.d("SmartList",listIm.toString())

        listIm.forEach {it->
            val smartPhone = SmartPhone(it.productName,it.productCode,it.price,it.quantity)
            mSmartViewModel.addSmartPhone(smartPhone)
        }


        mSmartViewModel.getAllSmartPhone.observe(this) {
            /* it.forEach {it->
                 val smartPhone = SmartPhone(it.productName,it.productCode,it.price,it.quantity)
                 listDede.add(smartPhone)
             }*/
            listDede.clear()
            listDede.addAll(it)

            listFilter.clear()
            listFilter.addAll(it)

            if(currentSelectionPos!=-1){
                pricecat(currentSelectionPos)
            }else
            imitemAdapter.notifyDataSetChanged()

        }

//        imitemAdapter= SmartAdapter(listDede,onUpdate)

        binding.phoneRv.layoutManager= LinearLayoutManager(applicationContext)

        binding.phoneRv.adapter = imitemAdapter


        val priceItemAdapter= PriceAdapter(listPrice)
        binding.priceRv.layoutManager= LinearLayoutManager(applicationContext,LinearLayoutManager.HORIZONTAL,false)

        binding.priceRv.adapter = priceItemAdapter

        binding.submitButton.setOnClickListener{
//            listIm.clear()
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Do you want to submit ?")
            builder.setTitle("Submit !!")
            builder.setCancelable(false)
            builder.setPositiveButton("Yes"){
                dialog,which->submit()
//                Toast.makeText(this, "Successfully submitted!", Toast.LENGTH_SHORT).show()
            }
            builder.setNegativeButton("No"){
                dialog,which->dialog.cancel()
            }

            val alertDialog = builder.create()
            alertDialog.show()
//            Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show()
//            addItemsFromJSON(com.example.task_02.R.raw.price1)
//            imitemAdapter.notifyDataSetChanged()
        }


        binding.btnAdd.setOnClickListener {
            addAll(listFilter)
            imitemAdapter.notifyDataSetChanged()

        }

        searchView = findViewById(R.id.search)

//        searchView.setBackgroundResource(R.drawable.border)
//        searchView.queryHint = "Write here to search";


        searchView.setOnQueryTextListener(object : OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(msg: String): Boolean {
                Log.d("search","changed ")
                filter(msg)
                imitemAdapter.notifyDataSetChanged()
                return false
            }
        })

//        binding.priceRv.setOnClickListener {
//            listIm.clear()
//            addItemsFromJSON(com.example.task_02.R.raw.price1)
//            imitemAdapter.notifyDataSetChanged()
//        }


        binding.btnDown.setOnClickListener {
            binding.priceRv.visibility = VISIBLE
            binding.phoneRv.visibility = VISIBLE
            binding.btnUp.visibility  = VISIBLE
            binding.btnDown.visibility = GONE
        }


        binding.btnUp.setOnClickListener {
            binding.priceRv.visibility = GONE
            binding.phoneRv.visibility = GONE
            binding.btnDown.visibility = VISIBLE
            binding.btnUp.visibility  = GONE
        }

        priceItemAdapter.setClickListener(onClicked)

    }

    private fun submit() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Successfully Submitted!!")
        builder.setPositiveButton("OK"){
                dialog,which->dialog.dismiss()
//                Toast.makeText(this, "Successfully submitted!", Toast.LENGTH_SHORT).show()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private val onClicked = object : PriceAdapter.OnItemClickListener{
        override fun onClicked(position: Int) {
            pricecat(position)
        }
    }

    private val onUpdate = object : SmartAdapter.UpdateCartListener{
        override fun getQuantity(smartPhone: SmartPhone) {
            mSmartViewModel.updateSmartPhone(smartPhone)
        }
    }

    var searchText=""
    private fun filter(text: String) {
        searchText=text
        listFilter.clear()
        listDede.filter {
          return@filter commonFilter(it,text)
        }.let {
            listFilter.addAll(it)
        }
//        return addAll(listFilter)
        imitemAdapter.notifyDataSetChanged()

    }

    private fun addAll(addList:ArrayList<SmartPhone>) {
        addList.forEach{
            it.quantity+=1
            mSmartViewModel.updateSmartPhone(it)
        }
    }

    private fun commonFilter(it: SmartPhone, text: String): Boolean {
        if(currentSelectionPos==-1)
            return it.productName.toLowerCase().contains(text.toLowerCase())
        else{
            return (it.productName.toLowerCase().contains(text.toLowerCase()) && priceCategoryFilter(it,currentSelectionPos))
        }
    }


    var currentSelectionPos = -1
    fun pricecat(p:Int){
        currentSelectionPos=p
            listFilter.clear()
        listDede.filter {
//           return@filter priceCategoryFilter(it,p)
           return@filter commonFilter(it,searchText)

        }.let {
            listFilter.addAll(it)
        }
            imitemAdapter.notifyDataSetChanged()
        }

    private fun priceCategoryFilter(it: SmartPhone, p: Int): Boolean {
        if(p==0)
            return it.price<1000000

        if(p==1)
            return it.price >= 80000

        if(p==2)
            return  it.price in 70001..79999

        if(p==3)
            return  it.price in 60001..69999

        if(p==4)
            return  it.price in 50001..59999

        if(p==5)
            return  it.price in 40001..49999

        if(p==6)
            return it.price in 30001..39999

        else
            return it.price<=3000
    }


    private fun addItemsFromJSON(ids:Int) {
        try {
            val jsonDataString = readJSONDataFromFile(ids)
            val jsonArray = JSONArray(jsonDataString)
            for (i in 0 until jsonArray.length()) {
                val itemObj = jsonArray.getJSONObject(i)


                val productName=itemObj.getString("productName")
                val productCode=itemObj.getString("productCode")
                val price=itemObj.getInt("price")

                val im=SmartPhone(productName,productCode,price)

                listIm.add(im)
            }
        } catch (e: JSONException) {
            Log.d("Taf", "addItemsFromJSON: ", e)
        } catch (e: IOException) {
            Log.d("taf", "addItemsFromJSON: ", e)
        }
    }



    private fun addPriceItemsFromJSON() {
        try {
            val jsonDataString = readJSONDataFromFile(com.example.task_02.R.raw.price)
            val jsonArray = JSONArray(jsonDataString)
            for (i in 0 until jsonArray.length()) {
                val itemObj = jsonArray.getJSONObject(i)


                val price=itemObj.getString("price")


                listPrice.add(price)
            }
        } catch (e: JSONException) {
            Log.d("Taf", "addItemsFromJSON: ", e)
        } catch (e: IOException) {
            Log.d("taf", "addItemsFromJSON: ", e)
        }
    }


    @Throws(IOException::class)
    private fun readJSONDataFromFile(id:Int): String {
        var inputStream: InputStream? = null
        val builder = StringBuilder()
        try {
            var jsonString: String? = null

            inputStream = resources.openRawResource(id)

            val bufferedReader = BufferedReader(
                InputStreamReader(inputStream, "UTF-8")
            )
            while (bufferedReader.readLine().also { jsonString = it } != null) {
                builder.append(jsonString)
            }
        } finally {
            inputStream?.close()
        }
        return String(builder)
    }




}


