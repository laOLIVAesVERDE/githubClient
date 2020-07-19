package jp.sample.tsutou.githubClient.view.ui

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider

import jp.sample.tsutou.githubClient.R
import jp.sample.tsutou.githubClient.databinding.FragmentProjectDetailsBinding
import jp.sample.tsutou.githubClient.viewModel.ProjectViewModel

class ProjectFragment : Fragment() {

    companion object {
        private const val KEY_PROJECT_ID = "project_id"

        fun forProject(projectId: String) = ProjectFragment().apply {
            arguments = Bundle().apply { putString(KEY_PROJECT_ID, projectId) }
        }
    }

    private val projectId by lazy {
        requireNotNull(
                arguments?.getString(KEY_PROJECT_ID)
        ) {
            "projectId must not be null"
        }
    }

    private val viewModel by lazy {
        ViewModelProvider(this, ProjectViewModel.Factory(
                requireActivity().application, projectId
        )).get(ProjectViewModel::class.java)
    }

    private lateinit var binding: FragmentProjectDetailsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_project_details, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            projectViewModel = viewModel
            isLoading = true
        }

        viewModel.projectLiveData.observe(viewLifecycleOwner, Observer { project ->
            project?.let {
                binding.isLoading = false
                viewModel.setProject(it)
            }
        })
    }
}
