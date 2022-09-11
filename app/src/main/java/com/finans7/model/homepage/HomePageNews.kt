package com.finans7.model.homepage

import com.finans7.model.categorynews.PostListModel

data class HomePageNews(
    val besliManset: List<PostListModel>,
    val anaManset: List<PostListModel>,
    val sagManset: List<PostListModel>,
    val haberBandı: List<PostListModel>,
    val ozelHaberler: List<PostListModel>,
    val gununManseti: List<PostListModel>,
    val araManset: List<PostListModel>,
    val yatayHaberler: List<PostListModel>,
    val kategoriHaberleri: List<PostListModel>,
)
