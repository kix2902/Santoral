package es.kix2902.santoral.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import es.kix2902.santoral.R
import es.kix2902.santoral.presenters.MainPresenter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val presenter = MainPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        supportActionBar?.run {
            setDisplayShowHomeEnabled(true)
            setIcon(R.mipmap.ic_launcher)
        }

        MobileAds.initialize(this, getString(R.string.admob_app_id))

        val adRequest = AdRequest.Builder()
            .addTestDevice(getString(R.string.test_device))
            .build()
        adView.loadAd(adRequest)

        presenter.loadDay()
    }

    public fun showMessage(cause: Int) {
        Toast.makeText(this, cause, Toast.LENGTH_SHORT).show()
    }

}
