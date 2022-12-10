package com.chrisojukwu.tallybookkeeping.ui.bookkeeping

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.chrisojukwu.tallybookkeeping.R
import com.chrisojukwu.tallybookkeeping.data.models.RecordHolder
import com.chrisojukwu.tallybookkeeping.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val sharedViewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            sharedViewmodel = sharedViewModel
        }

//        ViewCompat.setOnApplyWindowInsetsListener(binding.toolbarHomePage) { theView, windowInsets ->
//            val insets = windowInsets.getInsetsIgnoringVisibility(WindowInsetsCompat.Type.systemBars())
//            println(insets.top)
//            theView.updatePadding(top = insets.top)
//            WindowInsetsCompat.CONSUMED
//        }

        val adapter = IncomeExpenseAdapter(sharedViewModel.displayList.value!!) { entry -> onEntryClick(entry) }
        binding.recyclerViewRecords.adapter = adapter

        (activity as AppCompatActivity?)!!.supportActionBar?.hide()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cardViewMoneyIn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_editIncomeFragment)
        }

        binding.cardViewMoneyOut.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_editExpenseFragment)
        }

    }

    private fun onEntryClick(entry: RecordHolder) {

    }
}