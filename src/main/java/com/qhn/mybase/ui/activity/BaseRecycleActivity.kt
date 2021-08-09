package com.qhn.mybase.ui.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.qhn.mybase.R
import com.qhn.mybase.databinding.ActivityRecyclerviewBinding

import com.qhn.mybase.network.ListStatus
import com.qhn.mybase.network.Status
import com.qhn.mybase.network.observeEx
import com.qhn.mybase.network.resultNotNull

import com.qhn.mybase.ui.viewmodel.BaseRecyclerViewViewModel
import com.qhn.mybase.ui.recyclerview.BaseAdapter
import com.qhn.mybase.utils.extend.toast

abstract class BaseRecycleActivity<T> : BaseActivity<ActivityRecyclerviewBinding>() {
    val adapter by lazy { BaseAdapter() }
    open var hasSwipeRefresh = true
    open var hasFootItem = true
    abstract val viewModel : BaseRecyclerViewViewModel<T>
    var listStatus = ListStatus.Content


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyclerview)
        initView()

        contentView.recyclerView.adapter = adapter
        contentView.recyclerView.layoutManager = LinearLayoutManager(this)
        listState = ListStatus.Content
        obsData()
        onReload()

    }

    open fun initView() {
        adapter.hasFootItem = false
        if (hasSwipeRefresh) {
            contentView.swipeRefreshLayout.setOnRefreshListener {
                onSwipeRefresh()
            }
        }
        adapter.setLoadMoreListener {
            if (listStatus == ListStatus.Content) {
                viewModel .paramsLiveData.value = Pair(viewModel.list.size / viewModel.limit + 1, viewModel.limit)
                listStatus = ListStatus.LoadMore
            } else {
                adapter.setLoadingFailed()
                toast("正在执行其他操作请稍后再试")
            }
        }

    }

    open fun obsData() {
        viewModel.obsData()
                .resultNotNull()
                .observeEx(this) {
                    when (it.status) {
                        Status.Content -> {
                            if (listStatus == ListStatus.LoadMore) {
                                viewModel.list.addAll(it.data!!)
                            } else {
                                viewModel.list.clear()
                                adapter.newItems.clear()
                                viewModel.list.addAll(it.data!!)
                                contentView.swipeRefreshLayout.isRefreshing = false
                            }
                            if (it.data.size < viewModel.limit)
                                adapter.setLoadingNoMore()
                            else
                                adapter.setLoadingSuccess()
                            applyData(it.data)
                            adapter.notifyDataSetChanged()
                            showContent()
                            listStatus = ListStatus.Content

                        }
                        Status.Loading -> {
                            showLoading()
                        }
                        Status.Error -> {
                            when (listStatus) {
                                ListStatus.LoadMore -> {
//                                    if (it.error?.code == ErrorCode.EMPTY) {
//                                        adapter.setLoadingNoMore()
//                                    } else {
//                                        adapter.setLoadingFailed()
//                                    }
                                    adapter.setLoadingFailed()
                                }
                                ListStatus.Content -> {
//                                    it.error?.message
                                    showError()
                                }
                                ListStatus.Refreshing -> {
                                    contentView.swipeRefreshLayout.isRefreshing = false
                                    showError()
                                }
                            }
                            listStatus = ListStatus.Content
                        }
                    }

                }

    }

    abstract fun applyData(data: List<T>)


    open fun onSwipeRefresh() {
        onReload()
    }

    override fun onReload() {
        viewModel.paramsLiveData.value = Pair(1, viewModel.limit)
    }
}