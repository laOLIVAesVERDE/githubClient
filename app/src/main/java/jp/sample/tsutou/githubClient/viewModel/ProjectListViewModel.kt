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

/**
 * List<Project>のrepositoryから送られてくるデータとuiイベントに責務を持つViewModel　
 */
class ProjectListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ProjectRepository.instance
    var projectListLiveData: MutableLiveData<List<Project>> = MutableLiveData()

    //ViewModel初期化時にロード
    init {
        loadProjectList()
    }

    private fun loadProjectList() {

        //viewModelScope->ViewModel.onCleared() のタイミングでキャンセルされる CoroutineScope
        viewModelScope.launch {
            try {
                val request = repository.getProjectList(getApplication<Application>().getString(R.string.github_user_name))
                if (request.isSuccessful) {
                    //データを取得したら、LiveDataを更新
                    projectListLiveData.postValue(request.body())
                }
            } catch (e: Exception) {
                e.stackTrace
            }
        }
    }
}
