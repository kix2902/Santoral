package es.kix2902.santoral.activities

import android.content.ActivityNotFoundException
import android.content.Intent
import android.gesture.GestureLibraries
import android.gesture.GestureOverlayView
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
import es.kix2902.santoral.databinding.ActivityMainBinding
import es.kix2902.santoral.helpers.VerticalDivider
import es.kix2902.santoral.presenters.MainPresenter
import es.kix2902.santoral.px
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var presenter: MainPresenter

    private lateinit var binding: ActivityMainBinding

    private lateinit var adapter: SaintsAdapter

    private val gestureLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures)

    override fun onCreate(savedInstanceState: Bundle?) {
        Locale.setDefault(Locale("es", "ES"))
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        presenter = MainPresenter(this)

        adapter = SaintsAdapter(this) { item: Model.Saint ->
            val builder = CustomTabsIntent.Builder()
            builder.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))

            try {
                builder.build().launchUrl(this, Uri.parse(item.url))
            } catch (ex: ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(item.url)))
            }
        }

        binding.recyclerSaints.addItemDecoration(VerticalDivider(this))
        binding.recyclerSaints.adapter = adapter

        gestureLibrary.load()

        MobileAds.initialize(this)
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)

        presenter.loadSaints()
    }

    override fun onResume() {
        if (!adapter.isShowingNameResult()) {
            presenter.loadSwipeDateTracePreference()
        }

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
            val container = LinearLayout(this@MainActivity)
            container.orientation = LinearLayout.VERTICAL

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            params.setMargins(16.px, 0, 16.px, 0)

            val input = EditText(this@MainActivity)
            input.layoutParams = params
            input.setLines(1)
            input.maxLines = 1

            container.addView(input, params)

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
        binding.gestureView.isGestureVisible = enabled
    }

    fun showLoading() {
        binding.loadingBar.visibility = View.VISIBLE
    }

    fun hideLoading() {
        binding.loadingBar.visibility = View.GONE
    }

    fun clearList() {
        adapter.clearItems()
    }

    fun showSaints(saints: List<Model.Saint>, name: String? = null) {
        adapter.addItems(saints, name != null)

        if (name != null) {
            supportActionBar?.subtitle = name
            supportActionBar?.setHomeButtonEnabled(true)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            binding.gestureView.removeAllOnGesturePerformedListeners()
            changeSwipeTraceVisibility(false)

        } else {
            binding.gestureView.removeAllOnGesturePerformedListeners()
            binding.gestureView.addOnGesturePerformedListener(gestureListener)
            presenter.loadSwipeDateTracePreference()
        }
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

    fun showNameFeastNoResult(name: String) {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.no_date_for_name, name))
            .setNegativeButton(R.string.close, null)
            .show()
    }

    val gestureListener = GestureOverlayView.OnGesturePerformedListener { _, gesture ->
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
}
