package com.hr.hub.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hr.hub.R
import com.hr.hub.databinding.FragmentDashboardBinding
import com.hr.hub.ui.dashboard.Navigation.JourneyActivity
import com.hr.hub.ui.dashboard.Navigation.MapViewActivity
import com.hr.hub.ui.dashboard.Navigation.MoreNavigation
import com.hr.hub.ui.dashboard.Navigation.WeatherActivity
import com.hr.hub.ui.dashboard.Productivity.ClockActivity
import com.hr.hub.ui.dashboard.Productivity.NotesActivity
import com.hr.hub.ui.dashboard.Productivity.ReminderActivity
import com.hr.hub.ui.dashboard.Productivity.TOdoActivity
import com.hr.hub.ui.dashboard.Professional.HealthActivity
import com.hr.hub.ui.dashboard.Professional.MoreProfessional
import com.hr.hub.ui.dashboard.Professional.PdfreaderActivity
import com.hr.hub.ui.dashboard.Professional.ScannerActivity
import com.hr.hub.ui.dashboard.Professional.VoicerecorderActivity
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

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view=inflater.inflate(R.layout.fragment_dashboard,container,false)

        val binding=FragmentDashboardBinding.bind(view)

        val btnnews=binding.buttonNews
        btnnews.setOnClickListener {
            val newsEntry = Intent(requireContext(), NewsActivity::class.java)
            startActivity(newsEntry)
        }

        val btnmarket=binding.buttonMarketIndex
        btnmarket.setOnClickListener {
            val marketEntry = Intent(requireContext(),MarketActivity::class.java)
            startActivity(marketEntry)
        }

        val btnsport=binding.buttonSports

        btnsport.setOnClickListener {
            val sportEntry =Intent(requireContext(),SportActivity::class.java)
            startActivity(sportEntry)
        }

        val btntech=binding.buttonTechnology
        btntech.setOnClickListener {
            val techEntry =Intent(requireContext(), TechActivity::class.java)
            startActivity(techEntry)
        }
        val btncalci=binding.buttonCalculator
        btncalci.setOnClickListener {
            val techEntry =Intent(requireContext(), CalciActivity::class.java)
            startActivity(techEntry)
        }
        val btnbmi=binding.buttonBmiCalculator
        btnbmi.setOnClickListener {
            val techEntry =Intent(requireContext(), BMIActivity::class.java)
            startActivity(techEntry)
        }
        val btncurrency=binding.buttonCurrencyConverter
        btncurrency.setOnClickListener {
            val techEntry =Intent(requireContext(), CurrencyActivity::class.java)
            startActivity(techEntry)
        }
        val btntimecal=binding.buttonTimeCalculator
        btntimecal.setOnClickListener {
            val techEntry =Intent(requireContext(), TimecalActivity::class.java)
            startActivity(techEntry)
        }
        val btnstock=binding.buttonStockCalculator
        btnstock.setOnClickListener {
            val techEntry =Intent(requireContext(), StockcalActivity::class.java)
            startActivity(techEntry)
        }
        val btncalculator=binding.buttonCalimore
        btncalculator.setOnClickListener{
            val techEntry=Intent(requireContext(),MorecalciActivity::class.java)
            startActivity(techEntry)
        }
        val btnscanner=binding.buttonDocScanner
        btnscanner.setOnClickListener {
            val techEntry =Intent(requireContext(), ScannerActivity::class.java)
            startActivity(techEntry)
        }
        val btnvoice=binding.buttonVoiceRecorder
        btnvoice.setOnClickListener {
            val techEntry =Intent(requireContext(), VoicerecorderActivity::class.java)
            startActivity(techEntry)
        }
        val btnpdf=binding.buttonPdfReader
        btnpdf.setOnClickListener {
            val techEntry =Intent(requireContext(), PdfreaderActivity::class.java)
            startActivity(techEntry)
        }
        val btnhealth=binding.buttonHealthData
        btnhealth.setOnClickListener {
            val techEntry =Intent(requireContext(), HealthActivity::class.java)
            startActivity(techEntry)
        }
        val btnprofesional=binding.buttonProfessionalMore
        btnprofesional.setOnClickListener {
            val techEntry =Intent(requireContext(), MoreProfessional::class.java)
            startActivity(techEntry)
        }
        val btnweather=binding.buttonWeather
        btnweather.setOnClickListener {
            val techEntry =Intent(requireContext(), WeatherActivity::class.java)
            startActivity(techEntry)
        }
        val btnmapview=binding.buttonMapView
        btnmapview.setOnClickListener {
            val techEntry =Intent(requireContext(), MapViewActivity::class.java)
            startActivity(techEntry)
        }
        val btnjourney=binding.buttonTravelingList
        btnjourney.setOnClickListener {
            val techEntry =Intent(requireContext(), JourneyActivity::class.java)
            startActivity(techEntry)
        }
        val btnnavigation=binding.buttonNavigationMore
        btnnavigation.setOnClickListener {
            val techEntry =Intent(requireContext(), MoreNavigation::class.java)
            startActivity(techEntry)
        }
        val btnclock=binding.buttonCalendarPlanner
        btnclock.setOnClickListener {
            val techEntry =Intent(requireContext(), ClockActivity::class.java)
            startActivity(techEntry)
        }
        val btntodo=binding.buttonTodoList
        btntodo.setOnClickListener {
            val techEntry =Intent(requireContext(), TOdoActivity::class.java)
            startActivity(techEntry)
        }
        val btnnotes=binding.buttonNotes
        btnnotes.setOnClickListener {
            val techEntry =Intent(requireContext(), NotesActivity::class.java)
            startActivity(techEntry)
        }
        val btnreminder=binding.buttonTaskReminder
        btnreminder.setOnClickListener {
            val techEntry =Intent(requireContext(), ReminderActivity::class.java)
            startActivity(techEntry)
        }

        return view
        }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}