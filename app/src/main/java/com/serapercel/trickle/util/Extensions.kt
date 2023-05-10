package com.serapercel.trickle.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Pie
import com.anychart.enums.Align
import com.anychart.enums.LegendLayout
import com.anychart.enums.Orientation
import com.serapercel.trickle.R
import com.serapercel.trickle.data.entity.Account
import com.serapercel.trickle.data.entity.User
import dagger.hilt.android.internal.Contexts

// Sending a short toast message
fun Context.toastShort(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

// Sending a long toast message
fun Context.toastLong(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

// Remove punctuation marks from string
fun String.removePunctuation(): String {
    return this.replace("[\\p{Punct}]".toRegex(), "")
}

// Convert to Account
fun String.toAccount(): Account {
    val user = User(this.split(" ")[2], this.split(" ")[1])
    return Account(this.split(" ")[0], user)
}

// Fragment View
fun replaceFragment(requireActivity: FragmentActivity, container: Int, fragment: Fragment) {
    val manager = requireActivity.supportFragmentManager
    val fragmentTransaction = manager.beginTransaction()
    fragmentTransaction.replace(container, fragment)
    fragmentTransaction.commit()
}

fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner, object : Observer<T> {
        override fun onChanged(t: T) {
            removeObserver(this)
            observer.onChanged(t)
        }
    })
}

// Check Internet Connection
fun hasInternetConnection(context: Context): Boolean {
    val connectivityManager = Contexts.getApplication(context).getSystemService(
        Context.CONNECTIVITY_SERVICE
    ) as ConnectivityManager
    val activeNetwork = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
    return when {
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false
    }
}

// Response Handle
fun handleNeedsResponse(response: Boolean): NetworkResult<Boolean> {
    return when {
        response -> {
            NetworkResult.Success(data = response)
        }
        else -> {
            NetworkResult.Error("Add Needs Firebase Error!")
        }
    }
}

// Create pie chart
fun Context.createPieChart(
    chart: AnyChartView,
    title: String,
    labels: List<String>,
    datas: List<Float>
) {
    val backgroundColor = this.getColor(R.color.backgroundColor)
    val backgroundColorStr = "#" + Integer.toHexString(backgroundColor).substring(2)
    val textColor = this.getColor(R.color.primaryColor)
    val textColorStr = "#" + Integer.toHexString(textColor).substring(2)

    try {
        val pie: Pie = AnyChart.pie()
        val dataPieChart: MutableList<DataEntry> = mutableListOf()
        for (index in labels.indices) {
            dataPieChart.add(
                ValueDataEntry(
                    labels.elementAt(index),
                    datas.elementAt(index)
                )
            )
        }
        pie.data(dataPieChart)
        pie.title(title)
        pie.title().fontColor(textColorStr)
        pie.title().fontSize(20)
        pie.legend().align(Align.TOP)
        pie.legend().position(Orientation.RIGHT)
        pie.legend().itemsLayout(LegendLayout.VERTICAL)
        pie.background().fill(backgroundColorStr)
        chart.setChart(pie)
    }catch (excetpion: Exception){
        // TODO : Show Loading circle
    }

}