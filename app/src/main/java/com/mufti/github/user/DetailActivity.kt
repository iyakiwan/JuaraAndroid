package com.mufti.github.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.mufti.github.user.adapter.SectionsPagerAdapter
import com.mufti.github.user.data.User
import com.mufti.github.user.databinding.ActivityDetailBinding
import com.mufti.github.user.room.Github
import com.mufti.github.user.util.ViewModelFactoryRoom
import com.mufti.github.user.viewmodel.DetailViewModel

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel
    private var user: User? = null
    private var github: Github? = null
    private var isFavorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = intent.getParcelableExtra<User>(INTENT_USER) as User

        detailViewModel = obtainViewModel(this@DetailActivity)
        detailViewModel.getDetailUser(user?.username.toString())

        observeViewModel()
        tabLayoutSetting()
        checkRoom()
        clickFavorite()
    }

    private fun obtainViewModel(activity: AppCompatActivity): DetailViewModel {
        val factory = ViewModelFactoryRoom.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[DetailViewModel::class.java]
    }

    private fun observeViewModel() {
        detailViewModel.isLoading.observe(this, {
            showLoading(it)
        })
        detailViewModel.detailUser.observe(this, {
            if (it != null) {
                title = it.username
                Glide.with(this)
                    .load(it.avatar)
                    .circleCrop()
                    .into(binding.ivAvatar)
                binding.tvName.text = it.name
                binding.tvCompany.text = it.company
                binding.tvLocation.text = it.location
                binding.tvValueRepository.text = it.repository.toString()
                binding.tvValueFollowers.text = it.followers.toString()
                binding.tvValueFollowing.text = it.following.toString()
            }
        })
        detailViewModel.errorMessage.observe(this, {
            if (!it.equals("")) {
                Toast.makeText(this, "onFailure: $it", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun tabLayoutSetting() {
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        binding.vpGithubDetail.adapter = sectionsPagerAdapter
        TabLayoutMediator(binding.tabsGithubDetail, binding.vpGithubDetail) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
    }

    private fun checkRoom() {
        detailViewModel.getGithubByUsername(user?.username.toString()).observe(this, {
            isFavorite = it != null
            github = it
            setFavorite()
        })
    }

    private fun clickFavorite() {
        binding.fabFavorite.setOnClickListener {
            if (isFavorite) {
                github?.let { data -> detailViewModel.deleteFromRoom(data) }
                showToast("Success Delete Data")
                isFavorite = false
            } else {
                val data = Github()
                data.let {
                    it.avatar = user?.avatar
                    it.type = user?.type
                    it.username = user?.username
                }
                detailViewModel.insertToRoom(data)
                github = data
                showToast("Success Insert Data")
                isFavorite = true
            }
            setFavorite()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.lavGithubDetail.visibility = View.VISIBLE
        } else {
            binding.lavGithubDetail.visibility = View.GONE
        }
    }

    private fun setFavorite() {
        if (isFavorite) {
            binding.fabFavorite.setImageResource(R.drawable.ic_favorite)
        } else {
            binding.fabFavorite.setImageResource(R.drawable.ic_favorite_border)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val INTENT_USER = "intent_user"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tx_followers,
            R.string.tx_following
        )
    }
}