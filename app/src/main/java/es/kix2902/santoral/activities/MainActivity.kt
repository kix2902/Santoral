package es.kix2902.santoral.activities

import Model
import android.gesture.Gesture
import android.gesture.GestureLibraries
import android.gesture.GestureOverlayView
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import es.kix2902.santoral.R
import es.kix2902.santoral.adapters.SaintsAdapter
import es.kix2902.santoral.helpers.VerticalDivider
import es.kix2902.santoral.presenters.MainPresenter
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val presenter = MainPresenter(this)

    private lateinit var adapter: SaintsAdapter

    private val gestureLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        adapter = SaintsAdapter(ArrayList(), this) { item: Model.ApiResponse ->
            val builder = CustomTabsIntent.Builder()
            builder.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(this, Uri.parse(item.url))
        }

        recyclerSaints.addItemDecoration(VerticalDivider(this))
        recyclerSaints.adapter = adapter

        gestureLibrary.load()
        gestureView.addOnGesturePerformedListener(object : GestureOverlayView.OnGesturePerformedListener {
            override fun onGesturePerformed(overlay: GestureOverlayView?, gesture: Gesture?) {
                val predictions = gestureLibrary.recognize(gesture)

                if (predictions.size > 0 && predictions[0].score > 1.0) {
                    val result = predictions[0].name

                    if ("left".equals(result, ignoreCase = true)) {
                        presenter.nextDay()
                    } else if ("right".equals(result, ignoreCase = true)) {
                        presenter.previousDay()
                    }
                }
            }
        })

        MobileAds.initialize(this, getString(R.string.admob_app_id))

        val adRequest = AdRequest.Builder()
            .addTestDevice(getString(R.string.test_device))
            .build()
        adView.loadAd(adRequest)

        presenter.loadSaints()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            presenter.today()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    fun showLoading() {
        loadingBar.visibility = View.VISIBLE
        adapter.clearItems()
    }

    fun hideLoading() {
        loadingBar.visibility = View.GONE
    }

    fun showSaints(saints: List<Model.ApiResponse>) {
        adapter.addItems(saints)
    }

    fun showMessage(cause: Int) {
        Toast.makeText(this, cause, Toast.LENGTH_SHORT).show()
    }

    fun showDate(date: String, isToday: Boolean) {
        supportActionBar?.subtitle = date

        if (isToday) {
            supportActionBar?.setHomeButtonEnabled(false)
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
        } else {
            supportActionBar?.setHomeButtonEnabled(true)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

}
