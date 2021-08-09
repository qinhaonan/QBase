package com.qhn.mybase.ui.activity

import android.annotation.SuppressLint

import android.graphics.Typeface
import android.os.Build
import android.os.Bundle

import android.util.TypedValue
import android.view.*
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.qhn.mybase.R
import com.qhn.mybase.network.ListStatus
import com.qhn.mybase.utils.extend.dp
import org.greenrobot.eventbus.EventBus

@SuppressLint("Registered")
open class BaseActivity<T : ViewDataBinding> : AppCompatActivity() {
    val TAG: String = this::class.java.simpleName


    open var hasToolbar = true  //是否需要toolbar
    open var hasStatusBar = true //状态栏是否存在
    open var isStatusBarTransparent = false //状态栏透明
    open var isFitsSystemWindows = false //是否在状态栏处预览空间
    open var isRegisterEventBus = false // 是否注册EventBus
    open var listState = ListStatus.Content

    //各种viewStub
    private val baseView by lazy { LayoutInflater.from(this).inflate(R.layout.activity_base, null, false) }
    private val errorView by lazy { baseView.findViewById<ViewStub>(R.id.vs_error) }
    private val loadingView by lazy { baseView.findViewById<ViewStub>(R.id.vs_loading) }
    private val needLoginView by lazy { baseView.findViewById<ViewStub>(R.id.vs_need_login) }
    private val noDataView by lazy { baseView.findViewById<ViewStub>(R.id.vs_no_data) }
    private val vsToolbar by lazy { baseView.findViewById<ViewStub>(R.id.vs_toolbar) }
    val toolbar by lazy { if (hasToolbar) vsToolbar.inflate() as Toolbar else null }
    val titleView by lazy { TextView(this) }    //为了让toolbar居中,创建的TextView

    private var currentView: View? = null //若为null 则说明展示的是contentView
    lateinit var contentView: T
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isRegisterEventBus) {
            EventBus.getDefault().register(this)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (!hasStatusBar) {
                window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)//状态栏隐藏
            } else {
                if (isStatusBarTransparent) {
                    window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)// 状态栏透明,
//                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//                    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//                    window.statusBarColor = Color.TRANSPARENT
                }
            }
        }
        initBaseView()
    }

    @SuppressLint("InflateParams")
    override fun setContentView(layoutResID: Int) {
        val frameLayout = baseView.findViewById<FrameLayout>(R.id.frameLayout)
        val layoutInflater = LayoutInflater.from(this)
        contentView = DataBindingUtil.inflate(layoutInflater, layoutResID, frameLayout, false)
        frameLayout.addView(contentView.root, 0)
        baseView.fitsSystemWindows = isFitsSystemWindows
        if (isFitsSystemWindows) window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary)
        if (hasToolbar)
            setToolbar(toolbar)
        window.setContentView(baseView)
    }

    /**
     *  设置toolbar 样式
     */
    open fun initToolbar() {
        toolbar?.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
        toolbar?.setNavigationIcon(R.drawable.ic_arrow_back_toolbar_24dp)
        toolbar?.setNavigationOnClickListener {
            finish()
        }
    }

    /**
     *  toolbar title 居中
     */
    private fun setToolbar(toolbar: Toolbar?) {
        titleView.setTextColor(ContextCompat.getColor(this, R.color.white))
        titleView.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.textHuge))
        val layoutParams = Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT, Gravity.CENTER)
        titleView.layoutParams = layoutParams
        toolbar?.addView(titleView)
        initToolbar()
    }

    //errorView 点击事件 调用onreload 当其他view没有调用inflate时,执行下面的方法需要显式给currentView赋值
    private fun initBaseView() {
        errorView.inflate().findViewById<Button>(R.id.btn_reload).setOnClickListener { onReload() }
        currentView = errorView
    }

    //显示不同状态下的页面
    fun showError() {
        showView(errorView)
    }

    fun showLoading() {
        showView(loadingView)
    }

    fun showNoData() {
        showView(noDataView)
    }

    fun showNeedLogin() {
        showView(noDataView)
    }

    fun showContent() {
        currentView?.visibility = View.GONE
    }

    private fun showView(view: View) {
        if (currentView == view)
            return
        else {
            currentView?.visibility = View.GONE
            view.visibility = View.VISIBLE
            currentView = view
        }
    }

    open fun onReload() {}

    override fun onDestroy() {
        super.onDestroy()
        if (isRegisterEventBus) {
            EventBus.getDefault().unregister(this)
        }
    }
}