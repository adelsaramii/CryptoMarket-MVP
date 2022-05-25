package com.AdelSarami.Market.features.market.presenter

import com.AdelSarami.Market.model.dataModel.CoinsData
import com.AdelSarami.Market.utils.BasePresenter

interface MarketContract {

    interface Presenter : BasePresenter<View> {

        fun refresh()

    }

    interface View{

        fun setNews(data: ArrayList<Pair<String, String>>)
        fun failedNews()
        fun setCoinList(data :List<CoinsData.Data>)
        fun failedCoinList()

    }

}