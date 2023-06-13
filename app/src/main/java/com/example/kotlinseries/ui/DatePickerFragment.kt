package com.example.kotlinseries.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinseries.Injection
import com.example.kotlinseries.viewmodels.KotlinSeriersViewModel
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.Calendar
import kotlin.math.absoluteValue


class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {
    //viewmodel instance
    var returnedAge: String? = null
    private val viewModel: KotlinSeriersViewModel by lazy {
        val activity = requireNotNull(this.requireActivity()) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProvider(
            this, Injection.provideViewModelFactory(
                context = activity,
                owner = this,
            )
        )
            .get(KotlinSeriersViewModel::class.java)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)+1

        // Create a new instance of DatePickerDialog and return it
        val dialogFragment = DatePickerDialog(requireContext(), this, year, month, day)
        dialogFragment.datePicker.maxDate = c.timeInMillis
        return dialogFragment

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

        val date = dayOfMonth.toString() + "-" + month.toString() + "-" + year.toString()
        returnedAge = calculateAgeFromDate(date)

        //viewModel.getInputToCalculateAge(dayOfMonth,month,year)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun calculateAgeFromDate(dob: String): String {
        val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("d-M-yyyy")
        val date = LocalDate.now()
        val dateNow: String = date.format(dateFormatter)
        //val dateOfBIrth = LocalDate.parse(dob)
        //val stringDateOfBIrth:String = dateOfBIrth.format(dateFormatter)
        val from = LocalDate.parse(dob, dateFormatter)
        val currenDate = LocalDate.parse(dateNow, dateFormatter)
        val period = Period.between(from, currenDate)

        val years = period.years

        //Calculating months remaining until next Birth date
        val nextBirthDate = from.withYear(currenDate.year)
        val remainingDays = when {
            nextBirthDate.isBefore(currenDate) -> Period.between(
                currenDate,
                nextBirthDate.plusYears(1)
            ).toTotalMonths()

            else -> Period.between(currenDate, nextBirthDate).toTotalMonths()
        }
        val months = period.months
        val days = period.days

        Log.d("days",days.toString())
        Log.d("months",months.toString())

        val result =
            "You current age is ${years.toString()} and months remaining until your next birth date is ${remainingDays.toString()}"

        return result
    }

}