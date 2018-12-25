package com.jayyin.developer.demo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.jayyin.developer.yinnavtab.R
import com.jayyin.developer.yinnavtab.views.YinNavTab
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_yinnavtab_mode2.*

class YinNavTabMode2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yinnavtab_mode2)

        initViews()
    }

    private fun initViews() {

        tab2_1.setOnClickListener(object : YinNavTab.OnClickListener {
            override fun onClick(view: View, position: Int) {
                Toast.makeText(this@YinNavTabMode2Activity, "clicked tab " + position, Toast.LENGTH_SHORT).show()
            }
        })

        //滑动事件
        tab2_1.setOnScrollerListener(object : YinNavTab.OnScrollerListener {
            override fun onScroller(view: View, position: Int) {
                Toast.makeText(this@YinNavTabMode2Activity, "scrolle to tab " + position, Toast.LENGTH_SHORT).show()

            }
        })


        tab2_2.setOnClickListener(object : YinNavTab.OnClickListener {
            override fun onClick(view: View, position: Int) {
                Toast.makeText(this@YinNavTabMode2Activity, "clicked tab " + position, Toast.LENGTH_SHORT).show()
            }
        })

        //滑动事件
        tab2_2.setOnScrollerListener(object : YinNavTab.OnScrollerListener {
            override fun onScroller(view: View, position: Int) {
                Toast.makeText(this@YinNavTabMode2Activity, "scrolle to tab " + position, Toast.LENGTH_SHORT).show()

            }
        })
        tab2_3.setOnClickListener(object : YinNavTab.OnClickListener {
            override fun onClick(view: View, position: Int) {
                Toast.makeText(this@YinNavTabMode2Activity, "clicked tab " + position, Toast.LENGTH_SHORT).show()
            }
        })

        //滑动事件
        tab2_3.setOnScrollerListener(object : YinNavTab.OnScrollerListener {
            override fun onScroller(view: View, position: Int) {
                Toast.makeText(this@YinNavTabMode2Activity, "scrolle to tab " + position, Toast.LENGTH_SHORT).show()

            }
        })
        tab2_4.setOnClickListener(object : YinNavTab.OnClickListener {
            override fun onClick(view: View, position: Int) {
                Toast.makeText(this@YinNavTabMode2Activity, "clicked tab " + position, Toast.LENGTH_SHORT).show()
            }
        })

        //滑动事件
        tab2_4.setOnScrollerListener(object : YinNavTab.OnScrollerListener {
            override fun onScroller(view: View, position: Int) {
                Toast.makeText(this@YinNavTabMode2Activity, "scrolle to tab " + position, Toast.LENGTH_SHORT).show()

            }
        })
    }
}
