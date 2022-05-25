package com.AdelSarami.Market.model

import android.content.Context
import com.AdelSarami.Market.model.dataModel.CoinAboutData
import com.AdelSarami.Market.model.dataModel.CoinAboutItem
import com.google.gson.Gson

class Assets(val context: Context) {

    lateinit var aboutDataMap: MutableMap<String, CoinAboutItem>

    fun getAboutDataFromAssets() {

        val fileInString = context.assets
            .open("currencyinfo.json")
            .bufferedReader()
            .use { it.readText() }

        aboutDataMap = mutableMapOf<String, CoinAboutItem>()

        val gson = Gson()
        val dataAboutAll = gson.fromJson(fileInString, CoinAboutData::class.java)

        dataAboutAll.forEach {
            aboutDataMap[it.currencyName] = CoinAboutItem(
                it.info.web,
                it.info.github,
                it.info.twt,
                it.info.desc,
                it.info.reddit
            )
        }

    }

}
