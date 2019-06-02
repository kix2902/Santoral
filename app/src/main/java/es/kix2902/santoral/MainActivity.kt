package es.kix2902.santoral

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MobileAds.initialize(this, getString(R.string.admob_app_id))

        val adView = findViewById<View>(R.id.ad_view) as AdView
        val adRequest = AdRequest.Builder()
            .addTestDevice(getString(R.string.test_device))
            .build()
        adView.loadAd(adRequest)
    }
}
