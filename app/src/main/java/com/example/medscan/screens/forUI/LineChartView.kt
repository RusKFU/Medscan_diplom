package com.example.medscan.screens.forUI


import ads_mobile_sdk.h6
import android.content.Context
import android.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.example.medscan.database.entity.HealthEntry
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import android.view.ViewGroup
import androidx.compose.material3.Text
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.medscan.screens.AnalysisViewModel
import android.graphics.Color as AndroidColor


@Composable
fun HealthCharts(
                 navController: NavController,
                 viewModel: AnalysisViewModel = viewModel(factory = AnalysisViewModelFactory(
                     LocalContext.current)) //ERROR
    ) {
    val context = LocalContext.current
    val entries by viewModel.entries.collectAsState()
    if (entries.isEmpty()) {
        Text("Нет данных для отображения графиков", modifier = Modifier.padding(16.dp))
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            ChartSection(
                title = "Температура",
                entries = entries,
                extractor = { it.temperature },
                label = "Температура (°C)",
                color = AndroidColor.RED,
                context = context
            )
        }
        item {
            ChartSection(
                title = "Сахар",
                entries = entries,
                extractor = { it.bloodSugar },
                label = "Сахар (ммоль/л)",
                color = AndroidColor.MAGENTA,
                context = context
            )
        }
        item {
            ChartSection(
                title = "Вес",
                entries = entries,
                extractor = { it.weight },
                label = "Вес (кг)",
                color = AndroidColor.BLUE,
                context = context
            )
        }
        item {
            ChartSection(
                title = "Давление",
                entries = entries,
                extractor = { it.systolicPressure?.toFloat() },
                label = "Давление верхнее (мм рт.ст.)",
                color = AndroidColor.GREEN,
                context = context
            )
        }
    }
}

@Composable
fun ChartSection(
    title: String,
    entries: List<HealthEntry>,
    extractor: (HealthEntry) -> Float?,
    label: String,
    color: Int,
    context: Context
) {
    val dataPoints = entries.mapIndexedNotNull { index, item ->
        extractor(item)?.let { Entry(index.toFloat(), it) }
    }

    if (dataPoints.isEmpty()) return

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        AndroidView(
            factory = {
                createLineChart(context, dataPoints, label, color)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp),
            update = { chart ->
                chart.data?.notifyDataChanged()
                chart.notifyDataSetChanged()
                chart.invalidate()
            }
        )
    }
}

private fun createLineChart(
    context: Context,
    dataPoints: List<Entry>,
    label: String,
    color: Int
): LineChart {
    return LineChart(context).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            400
        )

        val dataSet = LineDataSet(dataPoints, label).apply {
            this.color = color
            valueTextColor = AndroidColor.BLACK
            lineWidth = 2f
            circleRadius = 4f
            setCircleColor(color)
            setDrawFilled(true)
            fillAlpha = 50
            fillColor = color
            mode = LineDataSet.Mode.LINEAR
        }

        this.data = LineData(dataSet)
        xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            granularity = 1f
            setDrawGridLines(false)
        }

        axisRight.isEnabled = false
        axisLeft.setDrawGridLines(true)
        description.isEnabled = false
        legend.isEnabled = false
        setTouchEnabled(true)
        setPinchZoom(true)
        animateX(500)
    }
}





@Composable
fun LineChartView(context: Context, label: String, entries: List<Entry>) {
    AndroidView(
        factory = {
            LineChart(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    600
                )
                description.isEnabled = false
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                axisRight.isEnabled = false
                setTouchEnabled(true)
            }
        },
        update = { chart ->
            val dataSet = LineDataSet(entries, label)
            dataSet.color = Color.BLUE
            dataSet.valueTextColor = Color.BLACK
            dataSet.lineWidth = 2f
            chart.data = LineData(dataSet)
            chart.invalidate()
        }
    )
}
