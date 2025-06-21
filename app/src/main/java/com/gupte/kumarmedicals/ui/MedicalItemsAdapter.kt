package com.gupte.kumarmedicals.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gupte.kumarmedicals.data.model.MedicalItem
import com.gupte.kumarmedicals.databinding.ItemMedicalBinding
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class MedicalItemsAdapter @Inject constructor() : 
    ListAdapter<MedicalItem, MedicalItemsAdapter.ViewHolder>(DiffCallback()) {

    var onQuantityChanged: ((String, Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMedicalBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemMedicalBinding) : 
        RecyclerView.ViewHolder(binding.root) {
        
        private var currentItemName: String? = null
        
        fun bind(item: MedicalItem) {
            currentItemName = item.name
            
            binding.apply {
                itemName.text = item.name
                itemPrice.text = "₹${item.price}"
                itemSize.text = item.size
                
                // Show hint when quantity is 0, otherwise show the quantity
                if (item.quantity > 0) {
                    quantityInput.setText(item.quantity.toString())
                } else {
                    quantityInput.setText("")
                }
                
                // Simple focus change listener - update when user finishes editing
                quantityInput.setOnFocusChangeListener { _, hasFocus ->
                    if (!hasFocus) { // When user loses focus (finishes editing)
                        val quantity = quantityInput.text.toString().toIntOrNull() ?: 0
                        currentItemName?.let { itemName ->
                            onQuantityChanged?.invoke(itemName, quantity)
                        }
                    }
                }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<MedicalItem>() {
        override fun areItemsTheSame(oldItem: MedicalItem, newItem: MedicalItem) = 
            oldItem.name == newItem.name
        override fun areContentsTheSame(oldItem: MedicalItem, newItem: MedicalItem) = 
            oldItem == newItem
    }
} 