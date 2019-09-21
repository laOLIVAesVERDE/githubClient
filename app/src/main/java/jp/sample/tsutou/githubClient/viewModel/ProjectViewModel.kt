package jp.sample.tsutou.githubClient.viewModel

import android.app.Application
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.*

import jp.sample.tsutou.githubClient.R
import jp.sample.tsutou.githubClient.service.model.Project
import jp.sample.tsutou.githubClient.service.repository.ProjectRepository
import kotlinx.coroutines.launch

class ProjectViewModel(
        private val myApplication: Application,
        private val mProjectID: String
) : AndroidViewModel(myApplication) {

    private val repository = ProjectRepository.instance
    val projectLiveData: MutableLiveData<Project> = MutableLiveData()

    var project = ObservableField<Project>()

    //ViewModel初期化時にロード
    init {
        loadProject()
    }

    private fun loadProject() {

        //viewModelScope->ViewModel.onCleared() のタイミングでキャンセルされる CoroutineScope
        viewModelScope.launch {

            try {
                val project = repository.getProjectDetails(myApplication.getString(R.string.github_user_name), mProjectID)
                if (project.isSuccessful) {
                    projectLiveData.postValue(project.body())
                }
            } catch (e: Exception) {
                Log.e("loadProject:Failed", e.stackTrace.toString())
            }
        }
    }

    fun setProject(project: Project) {
        this.project.set(project)
    }

    /**
     * IDの(DI)依存性注入Factory
     */
    class Factory(private val application: Application, private val projectID: String) : ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            return ProjectViewModel(application, projectID) as T
        }
    }
}
