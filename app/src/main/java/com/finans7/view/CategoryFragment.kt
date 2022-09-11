package com.finans7.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.finans7.R
import com.finans7.adapter.CategoriesAdapter
import com.finans7.databinding.CategoryFragmentBinding
import com.finans7.model.category.CategoryModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*
import kotlin.collections.ArrayList

class CategoryFragment(val categories: List<CategoryModel>, val vV: View) : BottomSheetDialogFragment() {
    private lateinit var v: CategoryFragmentBinding
    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var navDirections: NavDirections

    private lateinit var categoryList: ArrayList<CategoryModel>
    private lateinit var searchedCategoryList: ArrayList<CategoryModel>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = CategoryFragmentBinding.inflate(inflater)
        return v.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryList = convertCategoryFromList(categories)

        v.categoryFragmentRecyclerView.setHasFixedSize(true)
        v.categoryFragmentRecyclerView.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        categoriesAdapter = CategoriesAdapter(categoryList)
        v.categoryFragmentRecyclerView.adapter = categoriesAdapter

        categoriesAdapter.setOnCategoryItemClickListener(object : CategoriesAdapter.CategoryItemClickListener{
            override fun onItemClick(categoryModel: CategoryModel) {
                closeThisFragment()
                goToNewsByCategoryPage(categoryModel)
            }
        })

        v.categoryFragmentEditSearch.addTextChangedListener {
            if (it.toString().isNotEmpty()){
                searchedCategoryList = getCategoryListBySearch(it.toString(), categoryList)
                categoriesAdapter.loadData(searchedCategoryList)
            } else
                categoriesAdapter.loadData(categoryList)
        }

        v.categoryFragmentImgClose.setOnClickListener {
            closeThisFragment()
        }
    }

    private fun convertCategoryFromList(categories: List<CategoryModel>) : ArrayList<CategoryModel> {
        categoryList = ArrayList()

        for (category in categories)
            categoryList.add(category)

        return categoryList
    }

    private fun getCategoryListBySearch(searchValue: String, categoryList: ArrayList<CategoryModel>) : ArrayList<CategoryModel> {
        val categories: ArrayList<CategoryModel> = ArrayList()

        for (category in categoryList){
            if (category.NAME.lowercase(Locale.getDefault()).contains(searchValue.lowercase(Locale.getDefault())))
                categories.add(category)
        }

        return categories
    }

    private fun closeThisFragment(){
        dismiss()
    }

    private fun goToNewsByCategoryPage(categoryData: CategoryModel){
        navDirections = MainFragmentDirections.actionMainFragmentToNewsByCategoryFragment(categoryData, null)
        Navigation.findNavController(vV).navigate(navDirections)
    }
}