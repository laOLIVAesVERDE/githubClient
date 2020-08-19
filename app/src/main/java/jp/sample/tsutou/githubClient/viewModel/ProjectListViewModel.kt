package jp.sample.tsutou.githubClient.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope

import jp.sample.tsutou.githubClient.R
import jp.sample.tsutou.githubClient.service.model.Project
import jp.sample.tsutou.githubClient.service.repository.ProjectRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class ProjectListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ProjectRepository.instance
    var projectListLiveData: MutableLiveData<List<Project>> = MutableLiveData()

    init {
        loadProjectList()
    }

    private fun loadProjectList() = viewModelScope.launch { //onCleared() のタイミングでキャンセルされる
        try {
            val response = repository.getProjectList(getApplication<Application>().getString(R.string.github_user_name))
            if (response.isSuccessful) {
                projectListLiveData.postValue(response.body()) //データを取得したら、LiveDataを更新
            }
        } catch (e: Exception) {
            e.stackTrace
        }
    }
}
