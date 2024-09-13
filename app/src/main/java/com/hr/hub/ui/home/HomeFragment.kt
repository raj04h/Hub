package com.hr.hub.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hr.hub.databinding.FragmentHomeBinding
import com.hr.hub.ui.dashboard.Updates.MarketActivity
import com.hr.hub.ui.dashboard.Updates.NewsActivity
import com.hr.hub.ui.dashboard.Updates.SportActivity
import com.hr.hub.ui.dashboard.Updates.TechActivity
import com.hr.hub.ui.dashboard.calculation.BMIActivity
import com.hr.hub.ui.dashboard.calculation.CalciActivity
import com.hr.hub.ui.dashboard.calculation.CurrencyActivity
import com.hr.hub.ui.dashboard.calculation.MorecalciActivity
import com.hr.hub.ui.dashboard.calculation.StockcalActivity
import com.hr.hub.ui.dashboard.calculation.TimecalActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        // Set up buttons and their click listeners
        binding.buttonNews.setOnClickListener {
            startActivity(Intent(requireContext(), NewsActivity::class.java))
        }

        binding.buttonMarketIndex.setOnClickListener {
            startActivity(Intent(requireContext(), MarketActivity::class.java))
        }

        binding.buttonSports.setOnClickListener {
            startActivity(Intent(requireContext(), SportActivity::class.java))
        }

        binding.buttonTechnology.setOnClickListener {
            startActivity(Intent(requireContext(), TechActivity::class.java))
        }

        binding.buttonCalculator.setOnClickListener {
            startActivity(Intent(requireContext(), CalciActivity::class.java))
        }

        binding.buttonBmiCalculator.setOnClickListener {
            startActivity(Intent(requireContext(), BMIActivity::class.java))
        }

        binding.buttonCurrencyConverter.setOnClickListener {
            startActivity(Intent(requireContext(), CurrencyActivity::class.java))
        }

        binding.buttonTimeCalculator.setOnClickListener {
            startActivity(Intent(requireContext(), TimecalActivity::class.java))
        }

        binding.buttonStockCalculator.setOnClickListener {
            startActivity(Intent(requireContext(), StockcalActivity::class.java))
        }

        binding.buttonCalimore.setOnClickListener {
            startActivity(Intent(requireContext(), MorecalciActivity::class.java))
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
