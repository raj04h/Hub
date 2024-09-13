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

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view=inflater.inflate(R.layout.fragment_dashboard,container,false)

        val binding=FragmentDashboardBinding.bind(view)


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