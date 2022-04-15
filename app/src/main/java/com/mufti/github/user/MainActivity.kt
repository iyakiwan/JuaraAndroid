package com.mufti.github.user

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mufti.github.user.adapter.ListUserAdapter
import com.mufti.github.user.data.User
import com.mufti.github.user.databinding.ActivityMainBinding
import com.mufti.github.user.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private val list = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvGithubUsers.setHasFixedSize(true)

        mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[MainViewModel::class.java]

        observeViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.menu_search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                mainViewModel.searchUser(query)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_setting -> {
                startActivity(Intent(this, SettingActivity::class.java))
                true
            }
            R.id.menu_favorite -> {
                startActivity(Intent(this, FavoriteActivity::class.java))
                true
            }
            else -> true
        }
    }

    private fun observeViewModel() {
        mainViewModel.isLoading.observe(this, {
            showNotFound(false)
            showLoading(it)
        })
        mainViewModel.listUser.observe(this, {
            if (it.count() > 0) {
                list.clear()
                list.addAll(it)
                showNotFound(false)
            } else {
                list.clear()
                showNotFound(true)
            }
            showRecyclerList()
        })
        mainViewModel.errorMessage.observe(this, {
            if (!it.equals("")) {
                showToast("onFailure: $it")
            }
        })
        mainViewModel.totalCount.observe(this, {
            if (it > 0) {
                showToast("Total Result $it")
            }
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

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.lavGithubUsers.visibility = View.VISIBLE
        } else {
            binding.lavGithubUsers.visibility = View.GONE
        }
    }

    private fun showNotFound(isNotFound: Boolean) {
        if (isNotFound) {
            binding.lavNotFound.visibility = View.VISIBLE
        } else {
            binding.lavNotFound.visibility = View.GONE
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}