package com.mufti.github.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mufti.github.user.adapter.ListUserAdapter
import com.mufti.github.user.data.User
import com.mufti.github.user.databinding.ActivityMainBinding
import com.mufti.github.user.room.GithubMappingHelper
import com.mufti.github.user.util.ViewModelFactoryRoom
import com.mufti.github.user.viewmodel.FavoriteViewModel

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var favoriteViewModel: FavoriteViewModel
    private val list = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "Favorite Page"

        favoriteViewModel = obtainViewModel(this)
        binding.rvGithubUsers.setHasFixedSize(true)
    }

    override fun onResume() {
        super.onResume()
        observeViewModel()
    }

    private fun observeViewModel() {
        showLoading(true)
        favoriteViewModel.getAllGithub().observe(this, {
            if (it.count() > 0){
                list.clear()
                val listUser = GithubMappingHelper.mapCursorToListUser(it)
                list.addAll(listUser)
                showNotFound(false)
            } else {
                list.clear()
                showNotFound(true)
            }
            showLoading(false)
            showRecyclerList()
        })
    }

    private fun showRecyclerList() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvGithubUsers.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvGithubUsers.addItemDecoration(itemDecoration)

        val listUserAdapter = ListUserAdapter(list)
        binding.rvGithubUsers.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                intentToDetailActivity(data)
            }
        })
    }

    private fun intentToDetailActivity(data: User) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.INTENT_USER, data)
        startActivity(intent)
    }

    private fun showNotFound(isNotFound: Boolean) {
        if (isNotFound) {
            binding.lavNotFound.visibility = View.VISIBLE
        } else {
            binding.lavNotFound.visibility = View.GONE
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.lavGithubUsers.visibility = View.VISIBLE
        } else {
            binding.lavGithubUsers.visibility = View.GONE
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = ViewModelFactoryRoom.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteViewModel::class.java]
    }
}