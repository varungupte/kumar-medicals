// Handles the search UI, user interactions, and displays search results using RecyclerView
package com.gupte.kumarmedicals.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.gupte.kumarmedicals.databinding.FragmentMedicalBinding
import com.gupte.kumarmedicals.util.TextFileGenerator
import com.gupte.kumarmedicals.util.ViewExtensions.hide
import com.gupte.kumarmedicals.util.ViewExtensions.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {

    @Inject
    lateinit var medicalAdapter: MedicalItemsAdapter

    private val viewModel: MainViewModel by viewModels()
    private var _binding: FragmentMedicalBinding? = null
    private val binding get() = _binding!!
    private var textFileGenerator: TextFileGenerator? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMedicalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textFileGenerator = TextFileGenerator(requireContext())
        setupRecyclerView()
        setupObservers()
        setupButtons()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = medicalAdapter
        }

        medicalAdapter.onQuantityChanged = { itemName, quantity ->
            viewModel.updateQuantity(itemName, quantity)
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.items.collect { items ->
                medicalAdapter.submitList(items)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.storeInfo.collect { storeInfo ->
                storeInfo?.let { store ->
                    binding.storeTitle.text = store.title
                    binding.storeEmail.text = "Email: ${store.emailId}"
                    binding.storePhone.text = "Phone: ${store.phoneNumber}"
                    binding.storeAddress.text = "Address: ${store.address}"
                    binding.storeInfoCard.visibility = View.VISIBLE
                } ?: run {
                    binding.storeInfoCard.visibility = View.GONE
                }
            }
        }

        // Observe order items to control button state
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.items.collect { items ->
                val hasOrderItems = items.any { it.quantity > 0 }
                binding.generatePdfButton.isEnabled = hasOrderItems
                
                // Update button appearance based on state
                if (hasOrderItems) {
                    binding.generatePdfButton.alpha = 1.0f
                } else {
                    binding.generatePdfButton.alpha = 0.5f
                }
            }
        }

        viewModel.searchState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UIState.Loading -> {
                    binding.progressBar.show()
                    binding.storeInfoCard.visibility = View.GONE
                }
                is UIState.Success -> {
                    binding.progressBar.hide()
                }
                is UIState.Failure -> {
                    binding.progressBar.hide()
                    Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                }
                is UIState.Idle -> {
                    binding.progressBar.hide()
                }
            }
        }
    }

    private fun setupButtons() {
        binding.generatePdfButton.setOnClickListener {
            // Clear focus from any EditText to trigger the onFocusChange listener
            // and save the last entered quantity before generating the order.
            activity?.currentFocus?.clearFocus()
            
            generateAndShareTextFile()
        }
    }

    private fun generateAndShareTextFile() {
        val orderItems = viewModel.getOrderItems()
        val totalAmount = viewModel.getTotalAmount()
        val storeInfo = viewModel.storeInfo.value
        val textFile = textFileGenerator?.generateOrderTextFile(orderItems, totalAmount, storeInfo)
        
        if (textFile == null) {
            Toast.makeText(context, "Error generating text file", Toast.LENGTH_SHORT).show()
            return
        }

        val uri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.provider",
            textFile
        )

        val storeEmail = viewModel.getStoreEmail()
        val storeName = viewModel.getStoreName()
        
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_SUBJECT, "Medical Order from ${storeName ?: "Kumar Medicals"}")
            putExtra(Intent.EXTRA_TEXT, "Please find the attached medical order.")
            
            // Add email recipient if available
            storeEmail?.let { email ->
                putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            }
        }

        startActivity(Intent.createChooser(intent, "Share Order"))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        textFileGenerator = null
    }
}
