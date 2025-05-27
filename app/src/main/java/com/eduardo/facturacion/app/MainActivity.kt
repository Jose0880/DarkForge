package com.eduardo.facturacion.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.eduardo.facturacion.app.databinding.ActivityMainBinding
import com.eduardo.facturacion.app.model.Invoice
import com.eduardo.facturacion.app.model.Product
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val products = mutableListOf<Product>()
    private var invoiceNumber = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        binding.buttonAddProduct.setOnClickListener {
            addProduct()
        }

        binding.buttonGenerateInvoice.setOnClickListener {
            generateInvoice()
        }

        binding.buttonPrintInvoice.setOnClickListener {
            printInvoice()
        }
    }

    private fun addProduct() {
        val productName = binding.editTextProductName.text.toString()
        val priceText = binding.editTextProductPrice.text.toString()
        val quantityText = binding.editTextProductQuantity.text.toString()

        if (productName.isNotEmpty() && priceText.isNotEmpty() && quantityText.isNotEmpty()) {
            try {
                val price = priceText.toDouble()
                val quantity = quantityText.toInt()
                
                val product = Product(productName, price, quantity)
                products.add(product)
                
                // Clear input fields
                binding.editTextProductName.text.clear()
                binding.editTextProductPrice.text.clear()
                binding.editTextProductQuantity.text.clear()
                
                updateProductList()
            } catch (e: NumberFormatException) {
                // Handle invalid number format
            }
        } else {
            // Show error message
        }
    }

    private fun updateProductList() {
        val stringBuilder = StringBuilder()
        var total = 0.0
        
        for (product in products) {
            val subtotal = product.price * product.quantity
            stringBuilder.append("${product.name} - ${product.quantity} x $${product.price} = $${subtotal}\n")
            total += subtotal
        }
        
        stringBuilder.append("\n${getString(R.string.total, String.format("$%.2f", total))}")
        binding.textViewProductList.text = stringBuilder.toString()
    }

    private fun generateInvoice() {
        val clientName = binding.editTextClientName.text.toString()
        
        if (clientName.isNotEmpty() && products.isNotEmpty()) {
            val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
            val invoice = Invoice(invoiceNumber, clientName, currentDate, ArrayList(products))
            
            // Display invoice details
            val invoiceText = "${getString(R.string.invoice_number, invoice.number)}\n" +
                    "${getString(R.string.date, invoice.date)}\n" +
                    "Cliente: ${invoice.clientName}\n\n" +
                    "Productos:\n" +
                    binding.textViewProductList.text
            
            binding.textViewInvoice.text = invoiceText
            binding.textViewInvoice.visibility = android.view.View.VISIBLE
            
            invoiceNumber++
        } else {
            // Show error message
        }
    }

    private fun printInvoice() {
        // In a real app, this would connect to a printer or generate a PDF
        // For now, we'll just show a message
        if (binding.textViewInvoice.visibility == android.view.View.VISIBLE) {
            // Simulate printing
            // In a real app, you would implement printing functionality here
        }
    }
}