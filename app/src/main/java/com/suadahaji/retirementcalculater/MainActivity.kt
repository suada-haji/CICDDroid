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

        AppCenter.start(application, "8ceb9e11-db30-4574-bf74-a3f7bd735c2f",
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
               resultTextView.text = "At the current rate of $interestRate%, saving \$$monthly a month you will have \$X by $retirementAge."
           }  catch (ex: Exception) {
               Analytics.trackEvent(ex.message)
           }
        }
    }
}