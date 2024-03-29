package com.idiotleon.tutorialretrofit2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var disposable: Disposable? = null

    private val wikiApiService by lazy {
        WikiApiService.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_search.setOnClickListener {
            if (et_search.text.toString().isNotEmpty()) {
                beginSearch(et_search.text.toString())
            }
        }
    }

    private fun beginSearch(searchString: String) {
        disposable = wikiApiService
            .hitCountCheck("query", "json", "search", searchString)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result -> tv_dashboard.text = "${result.query.searchinfo.totalhits} result found" },
                { error -> Toast.makeText(this, error.message, Toast.LENGTH_LONG).show() })
    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }
}
