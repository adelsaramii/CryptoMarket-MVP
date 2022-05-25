package com.AdelSarami.Market.features.coin.view

import com.robinhood.spark.SparkAdapter
import com.AdelSarami.Market.model.dataModel.ChartData

class ChartAdapter(
    private val historicalData :List<ChartData.Data>) :SparkAdapter()  {

    override fun getCount(): Int {
        return historicalData.size
    }

    override fun getItem(index: Int): ChartData.Data {
        return historicalData[index]
    }

    override fun getY(index: Int): Float {
        return historicalData[index].close.toFloat()
    }

}