package com.qhn.mybase.ui.fragment


import android.graphics.Typeface
import android.os.Bundle

import android.util.TypedValue
import android.view.*
import android.widget.Button
import android.widget.TextView
import android.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.qhn.mybase.R
import com.qhn.mybase.network.ListStatus

abstract class BaseFragment<T : ViewDataBinding> : Fragment() {
    val TAG: String = this::class.java.simpleName
    open var hasToolbar = true
    open var listState = ListStatus.Content
    abstract var layoutResID: Int
    //各种viewStub
    private var baseView: View? = null
    private val errorView by lazy { baseView?.findViewById<ViewStub>(R.id.vs_error) }
    private val loadingView by lazy { baseView?.findViewById<ViewStub>(R.id.vs_loading) }
    private val needLoginView by lazy { baseView?.findViewById<ViewStub>(R.id.vs_need_login) }
    private val noDataView by lazy { baseView?.findViewById<ViewStub>(R.id.vs_no_data) }
    private val vsToolbar by lazy { baseView?.findViewById<ViewStub>(R.id.vs_toolbar) }
    val toolbar by lazy { if (hasToolbar && vsToolbar != null) vsToolbar!!.inflate() as Toolbar else null }
    val titleView by lazy { TextView(context) }    //为了让toolbar居中,创建的TextView

    private var currentView: View? = null //若为null 则说明展示的是contentView
    lateinit var contentView: T
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        contentView = DataBindingUtil.inflate(layoutInflater, layoutResID, container, false)
        baseView = contentView.root
        setToolbar(toolbar)
        loadView()
        return baseView
    }

    open fun loadView() {}

    /**
     *  设置toolbar 样式
     */
    open fun initToolbar() {
        toolbar?.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorPrimary))
        toolbar?.setNavigationIcon(R.drawable.ic_arrow_back_toolbar_24dp)
        toolbar?.setNavigationOnClickListener {
        }
    }

    /**
     *  toolbar title 居中
     */
    private fun setToolbar(toolbar: Toolbar?) {
        titleView.setTextColor(ContextCompat.getColor(context!!, R.color.white))
        titleView.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.textHuge))
        val layoutParams = Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT, Gravity.CENTER)
        titleView.layoutParams = layoutParams
        toolbar?.addView(titleView)
        initToolbar()
    }

    //errorView 点击事件 调用onreload 当其他view没有调用inflate时,执行下面的方法需要显式给currentView赋值
    private fun initView() {
        errorView?.inflate()?.findViewById<Button>(R.id.btn_reload)?.setOnClickListener { onReload() }
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

    private fun showView(view: View?) {
        if (currentView == view)
            return
        else {
            currentView?.visibility = View.GONE
            view?.visibility = View.VISIBLE
            currentView = view
        }
    }

    open fun onReload() {}
}