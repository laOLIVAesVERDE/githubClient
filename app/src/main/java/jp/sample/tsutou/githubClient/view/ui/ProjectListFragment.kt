package jp.sample.tsutou.githubClient.view.ui

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import jp.sample.tsutou.githubClient.R
import jp.sample.tsutou.githubClient.databinding.FragmentProjectListBinding
import jp.sample.tsutou.githubClient.service.model.Project
import jp.sample.tsutou.githubClient.view.adapter.ProjectAdapter
import jp.sample.tsutou.githubClient.view.callback.ProjectClickCallback
import jp.sample.tsutou.githubClient.viewModel.ProjectListViewModel

const val TAG_OF_PROJECT_LIST_FRAGMENT = "ProjectListFragment"

class ProjectListFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProvider(this).get(ProjectListViewModel::class.java)
    }

    private lateinit var binding: FragmentProjectListBinding
    private val projectAdapter: ProjectAdapter = ProjectAdapter(object : ProjectClickCallback {
        override fun onClick(project: Project) {
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED) && activity is MainActivity) {
                (activity as MainActivity).show(project)
            }
        }
    })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_project_list, container, false) //dataBinding
        binding.apply {
            projectList.adapter = projectAdapter
            isLoading = true
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.projectListLiveData.observe(viewLifecycleOwner, Observer { projects ->
            projects?.let {
                binding.isLoading = false
                projectAdapter.setProjectList(it)
            }
            Log.d("ConfirmProjects", projects.toString())
        })
    }
}
