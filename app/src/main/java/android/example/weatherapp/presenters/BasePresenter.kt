package android.example.weatherapp.presenters

import moxy.MvpPresenter
import moxy.MvpView

abstract class BasePresenter<T : MvpView> : MvpPresenter<T>() {

    abstract fun enable()
}