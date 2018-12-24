package com.jayyin.developer.demo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.jayyin.developer.yinnavtab.R
import com.jayyin.developer.yinnavtab.views.YinNavTab
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
    }

    private fun initViews() {

        //点击事件
        tab.setOnClickListener(object : YinNavTab.OnClickListener {
            override fun onClick(view: View, position: Int) {
                Toast.makeText(this@MainActivity, "clicked tab " + position, Toast.LENGTH_SHORT).show()
            }
        })

        tab2.setOnClickListener(object : YinNavTab.OnClickListener {
            override fun onClick(view: View, position: Int) {
                Toast.makeText(this@MainActivity, "clicked tab " + position, Toast.LENGTH_SHORT).show()
            }
        })

        //滑动事件
        tab2.setOnScrollerListener(object : YinNavTab.OnScrollerListener {
            override fun onScroller(view: View, position: Int) {
                Toast.makeText(this@MainActivity, "scrolle to tab " + position, Toast.LENGTH_SHORT).show()

            }

        })

        tab3.setOnClickListener(object : YinNavTab.OnClickListener {
            override fun onClick(view: View, position: Int) {
                Toast.makeText(this@MainActivity, "clicked tab " + position, Toast.LENGTH_SHORT).show()
            }

        })

        tab3.setOnScrollerListener(object : YinNavTab.OnScrollerListener {
            override fun onScroller(view: View, position: Int) {
                Toast.makeText(this@MainActivity, "scrolle to tab " + position, Toast.LENGTH_SHORT).show()

            }

        })
    }
}
