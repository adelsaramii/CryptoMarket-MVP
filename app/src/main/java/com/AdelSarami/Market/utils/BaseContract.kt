package com.AdelSarami.Market.utils

interface BasePresenter<T> {

    fun onAttach(view: T)
    fun onDetach()

}