package jp.sample.tsutou.githubClient.view.ui

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import jp.sample.tsutou.githubClient.R
import jp.sample.tsutou.githubClient.databinding.FragmentProjectDetailsBinding
import jp.sample.tsutou.githubClient.viewModel.ProjectViewModel

private const val KEY_PROJECT_ID = "project_id"

class ProjectFragment : Fragment() {

    private lateinit var binding: FragmentProjectDetailsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_project_details, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val projectID = arguments?.getString(KEY_PROJECT_ID)

        val factory = ProjectViewModel.Factory(
                requireActivity().application, projectID ?: ""
        )

        val viewModel = ViewModelProviders.of(this, factory).get(ProjectViewModel::class.java)

        binding.apply {
            projectViewModel = viewModel
            isLoading = true
        }

        observeViewModel(viewModel)

    }

    private fun observeViewModel(viewModel: ProjectViewModel) {
        viewModel.projectLiveData.observe(viewLifecycleOwner, Observer { project ->
            if (project != null) {

                binding.isLoading = false
                viewModel.setProject(project)
            }
        })
    }

    companion object {

        //idを詰め渡すFactory
        fun forProject(projectID: String): ProjectFragment {
            val fragment = ProjectFragment()
            val args = Bundle()

            args.putString(KEY_PROJECT_ID, projectID)
            fragment.arguments = args

            return fragment
        }
    }
}
