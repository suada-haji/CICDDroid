package com.suadahaji.retirementcalculater

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import com.microsoft.appcenter.utils.async.AppCenterConsumer
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import kotlin.math.pow


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppCenter.start(application, "dfd0d16a-4633-4cc9-ba3a-f006a4a69d83",
                Analytics::class.java, Crashes::class.java)

        val future = Crashes.hasCrashedInLastSession()
        future.thenAccept(AppCenterConsumer {
            if(it){
                Toast.makeText(this, "Oops! Sorry about that!", Toast.LENGTH_LONG).show()
            }
        })

        calculateButton.setOnClickListener {
             Crashes.generateTestCrash()
            try {
                val interestRate = interestEditText.text.toString().toFloat()
                val currentAge = ageEditText.text.toString().toInt()
                val retirementAge = retirementEditText.text.toString().toInt()
                val monthly = monthlySavingsEditText.text.toString().toFloat()
                val current = currentEditText.text.toString().toFloat()

                val properties: HashMap<String, String> = HashMap<String, String>()
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

                val futureSavings = calculateRetirement(interestRate, current, monthly, (retirementAge - currentAge) * 12)

                resultTextView.text = "At the current rate of $interestRate%, saving \$$monthly a month you will have \$${String.format("%f", futureSavings)} by $retirementAge."
            } catch (ex: Exception) {
                Analytics.trackEvent(ex.message)
            }
        }
    }

    fun calculateRetirement(interestRate: Float, currentSavings: Float, monthly: Float, numMonths: Int): Float {
        var futureSavings = currentSavings * (1 + (interestRate / 100 / 12)).pow(numMonths)

        for (i in 1..numMonths) {
            futureSavings += monthly * (1 + (interestRate / 100 / 12)).pow(i)
        }

        return futureSavings
    }
}
