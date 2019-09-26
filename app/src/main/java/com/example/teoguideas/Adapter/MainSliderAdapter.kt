package com.example.kotlincomicreader.Adapter

import com.example.kotlincomicreader.Model.Banner
import ss.com.bannerslider.adapters.SliderAdapter
import ss.com.bannerslider.viewholder.ImageSlideViewHolder

class MainSliderAdapter(private val bannerList: List<Banner>): SliderAdapter() {

    override fun onBindImageSlide(position: Int, imageSlideViewHolder: ImageSlideViewHolder?) {
        imageSlideViewHolder!!.bindImageSlide(bannerList[position].Link)
    }

    override fun getItemCount(): Int {
        return bannerList.size
    }

}