package com.mufti.github.user.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mufti.github.user.DetailActivity
import com.mufti.github.user.adapter.ListUserAdapter
import com.mufti.github.user.data.User
import com.mufti.github.user.databinding.ActivityMainBinding
import com.mufti.github.user.viewmodel.FollowViewModel

class FollowingFragment : Fragment() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var followViewModel: FollowViewModel
    private val list = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        followViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[FollowViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ActivityMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvGithubUsers.setHasFixedSize(true)

        val user =
            requireActivity().intent.getParcelableExtra<User>(DetailActivity.INTENT_USER) as User
        followViewModel.setFollowing(user.username.toString())
        observeViewModel()
    }

    private fun observeViewModel() {
        followViewModel.isLoading.observe(viewLifecycleOwner, {
            showNotFound(false)
            showLoading(it)
        })
        followViewModel.listUser.observe(viewLifecycleOwner, {
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
        followViewModel.errorMessage.observe(viewLifecycleOwner, {
            if (!it.equals("")) {
                showToast("onFailure: $it")
            }
        })
    }

    private fun showRecyclerList() {
        val layoutManager = LinearLayoutManager(context)
        binding.rvGithubUsers.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        binding.rvGithubUsers.addItemDecoration(itemDecoration)

        val listUserAdapter = ListUserAdapter(list)
        binding.rvGithubUsers.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                showToast("Following: ${data.username}")
            }
        })
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
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}