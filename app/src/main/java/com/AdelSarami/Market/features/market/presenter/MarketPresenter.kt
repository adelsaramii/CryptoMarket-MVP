package com.AdelSarami.Market.features.market.presenter

import com.AdelSarami.Market.model.ApiManager
import com.AdelSarami.Market.model.dataModel.CoinsData

class MarketPresenter(
    private val apiManager: ApiManager = ApiManager()
) : MarketContract.Presenter {

    var mainView: MarketContract.View? = null

    override fun onAttach(view: MarketContract.View) {
        mainView = view
        getNewsFromApi()
        getTopCoinsFromApi()
    }

    override fun refresh() {
        getNewsFromApi()
        getTopCoinsFromApi()
    }

    override fun onDetach() {
        mainView = null
    }

    private fun getNewsFromApi() {

        apiManager.getNews(object : ApiManager.ApiCallback<ArrayList<Pair<String, String>>> {

            override fun onSuccess(data: ArrayList<Pair<String, String>>) {
                mainView!!.setNews(data)
            }

            override fun onError(errorMessage: String) {
                mainView!!.failedNews()
            }

        })

    }

    private fun getTopCoinsFromApi() {

        apiManager.getCoinsList(object : ApiManager.ApiCallback<List<CoinsData.Data>> {

            override fun onSuccess(data: List<CoinsData.Data>) {
                mainView!!.setCoinList(data)
            }

            override fun onError(errorMessage: String) {
                mainView!!.failedCoinList()
            }

        })

    }

}