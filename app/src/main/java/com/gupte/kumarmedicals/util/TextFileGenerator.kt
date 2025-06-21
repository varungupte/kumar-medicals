package com.gupte.kumarmedicals.util

import android.content.Context
import com.gupte.kumarmedicals.data.model.MedicalItem
import com.gupte.kumarmedicals.data.model.StoreInfo
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class TextFileGenerator(private val context: Context) {
    fun generateOrderTextFile(
        items: List<MedicalItem>, 
        totalAmount: Double,
        storeInfo: StoreInfo? = null
    ): File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val file = File(context.cacheDir, "medical_order_$timestamp.txt")
        
        val content = buildString {
            appendLine("MEDICAL ORDER")
            appendLine("Date: ${SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())}")
            
            // Add store information if available
            storeInfo?.let { store ->
                appendLine("Store: ${store.title}")
                appendLine("Email: ${store.emailId}")
                appendLine("Phone: ${store.phoneNumber}")
                appendLine("Address: ${store.address}")
                appendLine()
            }
            
            appendLine("=".repeat(50))
            appendLine()
            
            items.forEach { item ->
                appendLine("${item.name}")
                appendLine("  Size: ${item.size}")
                appendLine("  Quantity: ${item.quantity}")
                appendLine("  Price: ₹${item.price}")
                appendLine("  Total: ₹${item.price * item.quantity}")
                appendLine()
            }
            
            appendLine("=".repeat(50))
            appendLine("TOTAL AMOUNT: ₹$totalAmount")
        }
        
        file.writeText(content)
        return file
    }
} 