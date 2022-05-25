package com.AdelSarami.Market.features.coin.presenter

import com.AdelSarami.Market.model.dataModel.ChartData
import com.AdelSarami.Market.model.dataModel.CoinAboutItem
import com.AdelSarami.Market.model.dataModel.CoinsData
import com.AdelSarami.Market.utils.BasePresenter

interface CoinContract {

    interface Presenter : BasePresenter<View> {

        fun getChartData(data: CoinsData.Data, period: String)
        fun getStatistics(data: CoinsData.Data)
        fun getAbout(data: CoinAboutItem)
        fun getAssets(coinName :String)

    }

    interface View {

        fun setChart(data: CoinsData.Data, data_chart: List<ChartData.Data>)
        fun failedChart()
        fun setStatistics(data: CoinsData.Data)
        fun setAbout(data: CoinAboutItem)
        fun setInitAboutUi(data: CoinAboutItem)

    }

}