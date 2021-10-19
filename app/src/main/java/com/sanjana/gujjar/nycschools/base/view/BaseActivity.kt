package com.sanjana.gujjar.nycschools.base.view

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.res.ResourcesCompat
import com.sanjana.gujjar.nycschools.R
import com.sanjana.gujjar.nycschools.base.android.components.lifecycle.BaseActivityConfig
import com.sanjana.gujjar.nycschools.base.android.components.lifecycle.Tag
import com.sanjana.gujjar.nycschools.base.android.components.lifecycle.Tagged
import com.sanjana.gujjar.nycschools.base.baseEventModel.MessageEvent
import com.sanjana.gujjar.nycschools.databinding.BaseActivityBinding
import com.sanjana.gujjar.nycschools.utils.DialogUtils
import com.sanjana.gujjar.nycschools.utils.shouldShow

abstract class BaseActivity(TAG: String, val baseConfig: BaseActivityConfig = BaseActivityConfig(),): AppCompatActivity(), Tagged by Tag(TAG) {

    private var _binding: BaseActivityBinding? = null

    @VisibleForTesting
    protected val binding
        get() = _binding as BaseActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = BaseActivityBinding.inflate(layoutInflater)
        super.setContentView(binding.root)
        processBaseActivityConfig(savedInstanceState)
    }

    fun showToolbar(show: Boolean) {
        if (isFinishing) {
            return
        }
        binding.appbar.shouldShow(show)
    }


    fun showBackButton(showBackButton :Boolean){
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(showBackButton)
            if (showBackButton) {
                val icon = ResourcesCompat.getDrawable(resources, android.R.drawable.ic_menu_close_clear_cancel, null)?.mutate()
                it.setHomeAsUpIndicator(icon)
            } else {
                it.setHomeAsUpIndicator(null)
            }
        }
    }

    private fun processBaseActivityConfig(savedInstanceState: Bundle?) {

        if (baseConfig.isFullScreen) {
            binding.appbar.visibility = View.GONE
            binding.toolbarOverlay.visibility = View.GONE
            val layoutParams = binding.rootFragmentContainer.layoutParams as CoordinatorLayout.LayoutParams
            layoutParams.behavior = null
            window.apply {
                clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                statusBarColor = Color.TRANSPARENT
            }
        } else {
            baseConfig.toolbarOverlayFactory?.let { toolbarOverlayFactory ->
                binding.toolbarOverlay.addView(toolbarOverlayFactory(this, binding.toolbarOverlay))
            }
            setSupportActionBar(binding.toolbar)
        }

        val rootFragmentFactory = baseConfig.rootFragmentFactory
        if (savedInstanceState == null && rootFragmentFactory != null) {
            with(rootFragmentFactory.invoke()) {
                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.rootFragmentContainer, this, TAG)
                    .setPrimaryNavigationFragment(this)
                    .commit()
            }
        }
    }

    private fun showGenericAlertDialog(messageEvent: MessageEvent) {
        DialogUtils.showDialog(
            activity = this,
            title = messageEvent.title,
            message = messageEvent.message ?: "",
            isCancelable = false,
            listener = DialogInterface.OnClickListener { d, _ -> d.dismiss() }
        )
    }
}