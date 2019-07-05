package es.kix2902.santoral.activities

import android.content.Intent
import android.gesture.GestureLibraries
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import es.kix2902.santoral.R
import es.kix2902.santoral.adapters.SaintsAdapter
import es.kix2902.santoral.data.Model
import es.kix2902.santoral.helpers.VerticalDivider
import es.kix2902.santoral.presenters.MainPresenter
import es.kix2902.santoral.px
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var presenter: MainPresenter

    private lateinit var adapter: SaintsAdapter

    private val gestureLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        presenter = MainPresenter(this)

        adapter = SaintsAdapter(ArrayList(), this) { item: Model.Saint ->
            val builder = CustomTabsIntent.Builder()
            builder.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))

            builder.build().launchUrl(this, Uri.parse(item.url))
        }

        recyclerSaints.addItemDecoration(VerticalDivider(this))
        recyclerSaints.adapter = adapter

        gestureLibrary.load()
        gestureView.addOnGesturePerformedListener { _, gesture ->
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

        MobileAds.initialize(this, getString(R.string.admob_app_id))

        val adRequest = AdRequest.Builder()
            .addTestDevice(getString(R.string.test_device))
            .build()
        adView.loadAd(adRequest)

        presenter.loadSaints()
    }

    override fun onResume() {
        presenter.loadSwipeDateTracePreference()

        super.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_activity, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            presenter.today()
            true
        }

        R.id.select_date -> {
            val calendar = presenter.getCalendar()
            val dpd = DatePickerDialog.newInstance(
                { _, year, month, day ->
                    presenter.setDate(year, month, day)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            dpd.dismissOnPause(true)
            dpd.show(supportFragmentManager, "DatePickerDialog")
            true
        }

        R.id.search_name -> {
            val container = LinearLayout(this@MainActivity);
            container.orientation = LinearLayout.VERTICAL;

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            params.setMargins(16.px, 0, 16.px, 0)

            val input = EditText(this@MainActivity)
            input.layoutParams = params
            input.setLines(1)
            input.maxLines = 1

            container.addView(input, params);

            AlertDialog.Builder(this)
                .setTitle(R.string.dialog_search_name_title)
                .setView(container)
                .setPositiveButton(R.string.dialog_search_positive) { _, _ ->
                    presenter.searchName(input.text.toString())
                }
                .setNegativeButton(R.string.dialog_search_negative, null)
                .show()
            true
        }

        R.id.settings -> {
            startActivity(Intent(this, SettingsActivity::class.java))
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    fun changeSwipeTraceVisibility(enabled: Boolean) {
        gestureView.isGestureVisible = enabled
    }

    fun showLoading() {
        loadingBar.visibility = View.VISIBLE
    }

    fun hideLoading() {
        loadingBar.visibility = View.GONE
    }

    fun clearList() {
        adapter.clearItems()
    }

    fun showSaints(saints: List<Model.Saint>) {
        adapter.addItems(saints)
    }

    fun showMessage(cause: Int) {
        Toast.makeText(this, cause, Toast.LENGTH_LONG).show()
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

    fun showNameFeastResult(name: String, month: String, date: String) {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.feast_for_name, name, date, month))
            .setPositiveButton("Mostrar fecha") { _, _ -> presenter.setDateFeast() }
            .setNegativeButton(R.string.close, null)
            .show()
    }

    fun showNameFeastNoResult(name: String) {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.no_date_for_name, name))
            .setNegativeButton(R.string.close, null)
            .show()
    }

}
