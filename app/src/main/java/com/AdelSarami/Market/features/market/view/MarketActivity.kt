package com.AdelSarami.Market.features.market.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.AdelSarami.Market.databinding.ActivityMarketBinding
import com.google.gson.Gson
import com.AdelSarami.Market.model.ApiManager
import com.AdelSarami.Market.model.dataModel.CoinAboutData
import com.AdelSarami.Market.model.dataModel.CoinAboutItem
import com.AdelSarami.Market.model.dataModel.CoinsData
import com.AdelSarami.Market.features.coin.view.CoinActivity
import com.AdelSarami.Market.features.market.presenter.MarketContract
import com.AdelSarami.Market.features.market.presenter.MarketPresenter
import com.AdelSarami.Market.utils.snack

class MarketActivity : AppCompatActivity(), MarketAdapter.RecyclerCallback, MarketContract.View {

    lateinit var binding: ActivityMarketBinding
    lateinit var adapter: MarketAdapter
    val apiManager = ApiManager()
    private var presenter: MarketContract.Presenter = MarketPresenter(apiManager)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMarketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter.onAttach(this)

        binding.layoutWatchlist.btnShowMore.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.livecoinwatch.com/"))
            startActivity(intent)
        }
        binding.swipeRefreshMain.setOnRefreshListener {
            presenter.refresh()
        }
    }

    override fun onCoinItemClicked(dataCoin: CoinsData.Data) {
        val intent = Intent(this, CoinActivity::class.java)

        val bundle = Bundle()
        bundle.putParcelable("bundle1", dataCoin)
        bundle.putString("bundle2", dataCoin.coinInfo.name)

        intent.putExtra("bundle", bundle)
        startActivity(intent)
    }

    override fun setNews(data: ArrayList<Pair<String, String>>) {
        val randomAccess = (0..49).random()
        binding.layoutNews.txtNews.text = data[randomAccess].first
        binding.layoutNews.imgNews.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(data[randomAccess].second))
            startActivity(intent)
        }
        binding.layoutNews.newsLayout.setOnClickListener {
            setNews(data)
        }
    }

    override fun failedNews() {
        binding.root.snack("Check your internet connection")
        binding.swipeRefreshMain.isRefreshing = false
    }

    override fun setCoinList(data: List<CoinsData.Data>) {
        adapter = MarketAdapter(ArrayList(data), this)
        binding.layoutWatchlist.recyclerMain.adapter = adapter
        binding.layoutWatchlist.recyclerMain.layoutManager = LinearLayoutManager(this)
        binding.swipeRefreshMain.isRefreshing = false
    }

    override fun failedCoinList() {
        binding.root.snack("Check your internet connection")
        binding.swipeRefreshMain.isRefreshing = false
    }

}