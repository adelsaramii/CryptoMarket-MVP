package com.AdelSarami.Market.features.coin.presenter

import com.AdelSarami.Market.model.ApiManager
import com.AdelSarami.Market.model.Assets
import com.AdelSarami.Market.model.dataModel.ChartData
import com.AdelSarami.Market.model.dataModel.CoinAboutItem
import com.AdelSarami.Market.model.dataModel.CoinsData


class CoinPresenter(
    private val apiManager: ApiManager
    , private val assets: Assets
) : CoinContract.Presenter {

    var mainView: CoinContract.View? = null

    override fun onAttach(view: CoinContract.View) {
        mainView = view
    }

    override fun onDetach() {
        mainView = null
    }

    override fun getChartData(data: CoinsData.Data, period: String) {

        apiManager.getChartData(
            data.coinInfo.name,
            period,
            object : ApiManager.ApiCallback<List<ChartData.Data>> {

                override fun onSuccess(data_chart: List<ChartData.Data>) {
                    mainView!!.setChart(data, data_chart)
                }

                override fun onError(errorMessage: String) {
                    mainView!!.failedChart()
                }

            })

    }

    override fun getStatistics(data: CoinsData.Data) {
        mainView!!.setStatistics(data)
    }

    override fun getAbout(data: CoinAboutItem) {
        mainView!!.setAbout(data)
    }

    override fun getAssets(coinName: String) {
        assets.getAboutDataFromAssets()
        if (assets.aboutDataMap[coinName] == null){
            val helperData = CoinAboutItem()
            mainView!!.setInitAboutUi(helperData)
        }else{
            mainView!!.setInitAboutUi(assets.aboutDataMap[coinName]!!)
        }
    }

}