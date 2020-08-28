package com.srinivas.enbdassessment.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.srinivas.enbdassessment.R
import com.srinivas.enbdassessment.data.db.entities.Hit
import com.srinivas.enbdassessment.databinding.ActivityDetailedBinding
import kotlinx.android.synthetic.main.activity_pixabay.*


class DetailedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bindingUtil:ActivityDetailedBinding=DataBindingUtil.setContentView(this,R.layout.activity_detailed)
        setSupportActionBar(activity_main_toolbar)

        val result: Hit? = intent.getSerializableExtra("image") as? Hit
        setTitle(result?.tags)
        bindingUtil.hit=result




    }
}