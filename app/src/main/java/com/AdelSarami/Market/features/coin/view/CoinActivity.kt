package com.AdelSarami.Market.features.coin.view

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.AdelSarami.Market.R
import com.AdelSarami.Market.databinding.ActivityCoinBinding
import com.AdelSarami.Market.features.coin.presenter.CoinContract
import com.AdelSarami.Market.features.coin.presenter.CoinPresenter
import com.AdelSarami.Market.model.*
import com.AdelSarami.Market.model.dataModel.ChartData
import com.AdelSarami.Market.model.dataModel.CoinAboutItem
import com.AdelSarami.Market.model.dataModel.CoinsData
import com.AdelSarami.Market.utils.*
import com.AdelSarami.Market.utils.snack

class CoinActivity : AppCompatActivity(), CoinContract.View {
    lateinit var binding: ActivityCoinBinding
    lateinit var dataThisCoin: CoinsData.Data
    lateinit var dataThisCoinAbout: CoinAboutItem
    val apiManager = ApiManager()
    val assets = Assets(this)
    private var presenter: CoinContract.Presenter = CoinPresenter(apiManager, assets)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter.onAttach(this)

        getBundle()
        initUi()

    }

    private fun getBundle() {
        val fromIntent = intent.getBundleExtra("bundle")!!
        dataThisCoin = fromIntent.getParcelable<CoinsData.Data>("bundle1")!!

        if (fromIntent.getString("bundle2") != null) {
            presenter.getAssets(fromIntent.getString("bundle2").toString())
        }

    }

    private fun initUi() {

        initChartUi()
        initStatistics()

    }

    @SuppressLint("SetTextI18n")
    private fun initChartUi() {

        var period: String = HOUR
        presenter.getChartData(dataThisCoin, period)

        binding.layoutChart.radioGroupMain.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_12h -> {
                    period = HOUR
                }
                R.id.radio_1d -> {
                    period = HOURS24
                }
                R.id.radio_1w -> {
                    period = WEEK
                }
                R.id.radio_1m -> {
                    period = MONTH
                }
                R.id.radio_3m -> {
                    period = MONTH3
                }
                R.id.radio_1y -> {
                    period = YEAR
                }
                R.id.radio_all -> {
                    period = ALL
                }
            }
            presenter.getChartData(dataThisCoin, period)
        }

    }

    private fun initStatistics() {
        presenter.getStatistics(dataThisCoin)
    }

    override fun setInitAboutUi(data: CoinAboutItem) {

        presenter.getAbout(data)

    }

    @SuppressLint("SetTextI18n")
    override fun setChart(data: CoinsData.Data, data_chart: List<ChartData.Data>) {

        binding.layoutToolbar.toolbar.title = data.coinInfo.fullName

        val chartAdapter = ChartAdapter(data_chart)
        binding.layoutChart.sparkviewMain.adapter = chartAdapter

        binding.layoutChart.txtChartPrice.text = data.dISPLAY.uSD.pRICE
        binding.layoutChart.txtChartChange1.text = " " + data.dISPLAY.uSD.cHANGE24HOUR

        if (data.rAW.uSD.cHANGEPCT24HOUR.toString().length > 4) {
            binding.layoutChart.txtChartChange2.text =
                data.rAW.uSD.cHANGEPCT24HOUR.toString().substring(0, 5) + "%"
        } else {
            binding.layoutChart.txtChartChange2.text = data.rAW.uSD.cHANGEPCT24HOUR.toString() + "%"
        }

        val taghir = data.rAW.uSD.cHANGEPCT24HOUR
        if (taghir > 0) {

            binding.layoutChart.txtChartChange2.setTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.colorGain
                )
            )

            binding.layoutChart.txtChartUpdown.setTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.colorGain
                )
            )

            binding.layoutChart.txtChartUpdown.text = "▲"

            binding.layoutChart.sparkviewMain.lineColor = ContextCompat.getColor(
                binding.root.context,
                R.color.colorGain
            )

        } else if (taghir < 0) {

            binding.layoutChart.txtChartChange2.setTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.colorLoss
                )
            )

            binding.layoutChart.txtChartUpdown.setTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.colorLoss
                )
            )

            binding.layoutChart.txtChartUpdown.text = "▼"

            binding.layoutChart.sparkviewMain.lineColor = ContextCompat.getColor(
                binding.root.context,
                R.color.colorLoss
            )


        } else {
            binding.layoutChart.txtChartChange2.setTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.tertiaryTextColor
                )
            )

            binding.layoutChart.txtChartUpdown.setTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.tertiaryTextColor
                )
            )

            binding.layoutChart.txtChartUpdown.text = "▼"

            binding.layoutChart.sparkviewMain.lineColor = ContextCompat.getColor(
                binding.root.context,
                R.color.tertiaryTextColor
            )
        }

        binding.layoutChart.sparkviewMain.setScrubListener {

            // show price kamel
            if (it == null) {
                binding.layoutChart.txtChartPrice.text = data.dISPLAY.uSD.pRICE
            } else {
                // show price this dot
                binding.layoutChart.txtChartPrice.text =
                    "$ " + (it as ChartData.Data).close.toString()
            }

        }

    }

    override fun failedChart() {
        binding.root.snack("Check your internet connection")
    }

    override fun setStatistics(data: CoinsData.Data) {
        binding.layoutStatistics.tvOpenAmount.text = data.dISPLAY.uSD.oPEN24HOUR
        binding.layoutStatistics.tvTodaysHighAmount.text = data.dISPLAY.uSD.hIGH24HOUR
        binding.layoutStatistics.tvTodayLowAmount.text = data.dISPLAY.uSD.lOW24HOUR
        binding.layoutStatistics.tvChangeTodayAmount.text = data.dISPLAY.uSD.cHANGE24HOUR
        binding.layoutStatistics.tvAlgorithm.text = data.coinInfo.algorithm
        binding.layoutStatistics.tvTotalVolume.text = data.dISPLAY.uSD.tOTALVOLUME24H
        binding.layoutStatistics.tvAvgMarketCapAmount.text = data.dISPLAY.uSD.mKTCAP
        binding.layoutStatistics.tvSupplyNumber.text = data.dISPLAY.uSD.sUPPLY
    }

    @SuppressLint("SetTextI18n")
    override fun setAbout(data: CoinAboutItem) {

        if (data.coinWebsite.equals("") || data.coinWebsite == null) {
            data.coinWebsite = "no-data"
            binding.layoutAbout.txtWebsite.text = "no-data"
        } else {
            binding.layoutAbout.txtWebsite.text = data.coinWebsite
        }
        if (data.coinGithub.equals("") || data.coinGithub.equals(null)) {
            data.coinGithub = "no-data"
            binding.layoutAbout.txtGithub.text = "no-data"
        } else {
            binding.layoutAbout.txtGithub.text = data.coinGithub
        }
        if (data.coinReddit.equals("") || data.coinReddit.equals(null)) {
            data.coinReddit = "no-data"
            binding.layoutAbout.txtReddit.text = "no-data"
        } else {
            binding.layoutAbout.txtReddit.text = data.coinReddit
        }
        if (data.coinTwitter.equals("") || data.coinTwitter == null || data.coinTwitter.equals("no-data")) {
            data.coinTwitter = "no-data"
            binding.layoutAbout.txtTwitter.text = "no-data"
        } else {
            binding.layoutAbout.txtTwitter.text = "@" + data.coinTwitter
        }
        if (data.coinDesc.equals("") || data.coinDesc == null) {
            data.coinDesc = "no-data"
            binding.layoutAbout.txtAboutCoin.text = "no-data"
        } else {
            binding.layoutAbout.txtAboutCoin.text = data.coinDesc
        }

        binding.layoutAbout.txtWebsite.setOnClickListener {
            openWebsiteDataCoin(data.coinWebsite!!)
        }
        binding.layoutAbout.txtGithub.setOnClickListener {
            openWebsiteDataCoin(data.coinGithub!!)
        }
        binding.layoutAbout.txtReddit.setOnClickListener {
            openWebsiteDataCoin(data.coinReddit!!)
        }
        binding.layoutAbout.txtTwitter.setOnClickListener {
            openWebsiteDataCoin(BASE_URL_TWITTER + data.coinTwitter!!)
        }

    }

    private fun openWebsiteDataCoin(url: String) {

        if (url.equals("no-data") || url.equals(BASE_URL_TWITTER + "no-data")) {
            //do nothing
        } else {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }

    }

}