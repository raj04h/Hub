package com.hr.hub.ui.dashboard.Productivity

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.hr.hub.R
import com.hr.hub.ui.dashboard.Productivity.dataProductivity.ReminderReceiver
import java.util.Calendar

class ReminderActivity : AppCompatActivity() {

    private lateinit var alarmManager: AlarmManager
    private lateinit var timePicker: TimePicker
    private lateinit var alarmLabel: EditText
    private lateinit var saveBtn: Button
    private lateinit var choseBtn: Button
    private lateinit var ringtonePickerLauncher: ActivityResultLauncher<Intent>

    private var selectedRingtoneUri: Uri? = null

    companion object {
        const val ALARM_REQ_CODE: Int = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder)

        initializeViews()
        initializeRingtonePicker()

        saveBtn.setOnClickListener {
            saveReminder()
        }

        choseBtn.setOnClickListener {
            selectRingtone()
        }
    }

    private fun initializeViews() {
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        timePicker = findViewById(R.id.TP_intime)
        alarmLabel = findViewById(R.id.et_alarmlebel)
        saveBtn = findViewById(R.id.btn_alarmset)
        choseBtn = findViewById(R.id.btn_soundChoose)
    }

    private fun saveReminder() {
        val hour = timePicker.hour
        val min = timePicker.minute
        val label = alarmLabel.text.toString()

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, min)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val intent = Intent(this, ReminderReceiver::class.java).apply {
            putExtra("Alarm_Label", label)
            putExtra("Alarm_Ringtone_Uri", selectedRingtoneUri?.toString())
        }

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            ALARM_REQ_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            Toast.makeText(this, "Reminder set successfully for $label", Toast.LENGTH_SHORT).show()
        } catch (e: SecurityException) {
            Toast.makeText(this, "Permission error: ${e.message}", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Error setting reminder: ${e.message}", Toast.LENGTH_SHORT).show()
        }

        val repeatDays = getSelectedDays()

        if (repeatDays.isNotEmpty()) {
            repeatDays.forEach { day ->
                val repeatCalendar = calendar.clone() as Calendar
                repeatCalendar.set(Calendar.DAY_OF_WEEK, day)

                if (repeatCalendar.timeInMillis <= System.currentTimeMillis()) {
                    repeatCalendar.add(Calendar.WEEK_OF_YEAR, 1)
                }

                val repeatIntent = PendingIntent.getBroadcast(
                    this,
                    ALARM_REQ_CODE + day,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    repeatCalendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY * 7,
                    repeatIntent
                )
            }
        }
    }

    private fun getSelectedDays(): List<Int> {
        val repeatDays = mutableListOf<Int>()

        val cbSun = findViewById<CheckBox>(R.id.cb_sun)
        val cbMon = findViewById<CheckBox>(R.id.cb_Mon)
        val cbTue = findViewById<CheckBox>(R.id.cb_Tue)
        val cbWed = findViewById<CheckBox>(R.id.cb_Wed)
        val cbThu = findViewById<CheckBox>(R.id.cb_Thu)
        val cbFri = findViewById<CheckBox>(R.id.cb_Fri)
        val cbSat = findViewById<CheckBox>(R.id.cb_Sat)

        if (cbSun.isChecked) repeatDays.add(Calendar.SUNDAY)
        if (cbMon.isChecked) repeatDays.add(Calendar.MONDAY)
        if (cbTue.isChecked) repeatDays.add(Calendar.TUESDAY)
        if (cbWed.isChecked) repeatDays.add(Calendar.WEDNESDAY)
        if (cbThu.isChecked) repeatDays.add(Calendar.THURSDAY)
        if (cbFri.isChecked) repeatDays.add(Calendar.FRIDAY)
        if (cbSat.isChecked) repeatDays.add(Calendar.SATURDAY)

        return repeatDays
    }

    private fun initializeRingtonePicker() {
        ringtonePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                selectedRingtoneUri = result.data?.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
                Toast.makeText(this, "Ringtone selected: $selectedRingtoneUri", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No Ringtone selected", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun selectRingtone() {
        val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER).apply {
            putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM)
            putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Alarm Song")
            putExtra(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI, null as Uri?)
        }

        ringtonePickerLauncher.launch(intent)
    }
}
