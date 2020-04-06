package com.doubleb.covid19.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.doubleb.covid19.R
import com.doubleb.covid19.ui.view.ErrorView.Companion.UNDER_CONSTRUCTION
import com.doublebb.tracking.ScreenName
import com.doublebb.tracking.Tracking
import kotlinx.android.synthetic.main.fragment_news.*

class NewsFragment : Fragment() {
    companion object {
        const val TAG = "NewsFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_news, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Tracking.sendScreenView(this, ScreenName.NEWS)
        news_error_view
            .errorType(UNDER_CONSTRUCTION)
            .build()
    }
}