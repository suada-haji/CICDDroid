package com.suadahaji.retirementcalculater

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppCenter.start(application, "dfd0d16a-4633-4cc9-ba3a-f006a4a69d83",
                Analytics::class.java, Crashes::class.java)

        calculateButton.setOnClickListener {
//            Crashes.generateTestCrash()
           try {
               val interestRate = interestEditText.text.toString().toFloat()
               val currentAge = ageEditText.text.toString().toInt()
               val retirementAge = retirementEditText.text.toString().toInt()
               val monthly = retirementEditText.text.toString().toFloat()
               val current = retirementEditText.text.toString().toFloat()

               val properties: HashMap<String, String> = HashMap()
               properties.put("interest_rate", interestRate.toString())
               properties.put("current_age", currentAge.toString())
               properties.put("retirement_age", retirementAge.toString())
               properties.put("monthly_savings", monthly.toString())
               properties.put("current_savings", current.toString())

               if (interestRate <= 0) {
                   Analytics.trackEvent("wrong_interest_rate", properties)
               }

               if (retirementAge <= currentAge) {
                   Analytics.trackEvent("wrong_age", properties)
               }
           }  catch (ex: Exception) {
               Analytics.trackEvent(ex.message)
           }
        }
    }
}
