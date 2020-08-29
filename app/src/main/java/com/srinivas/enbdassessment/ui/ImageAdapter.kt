package com.srinivas.enbdassessment.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.srinivas.enbdassessment.R
import com.srinivas.enbdassessment.data.db.entities.Hit
import com.srinivas.enbdassessment.databinding.InflateImageItemBinding


class ImageAdapter(var imageInterface: ImageInterface) :
    RecyclerView.Adapter<ImageAdapter.ImageHolder>() {


    inner class ImageHolder(val inflateImageItemBinding: InflateImageItemBinding) :
        RecyclerView.ViewHolder(inflateImageItemBinding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Hit>() {
        override fun areItemsTheSame(oldItem: Hit, newItem: Hit): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Hit, newItem: Hit): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)


    override fun getItemCount(): Int = differ.currentList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder =
        ImageHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.inflate_image_item,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.inflateImageItemBinding.hit = differ.currentList[position]
        holder.inflateImageItemBinding.imgView.setOnClickListener {
            imageInterface.onItemclick(
                holder.inflateImageItemBinding.imgView,
                differ.currentList[position]
            )
        }
    }


    interface ImageInterface {
        fun onItemclick(view: View, hit: Hit)
    }
}