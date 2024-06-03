package the.hart.repositte.ui.screens.details.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import the.hart.repositte.data.api.response.NetworkResult
import the.hart.repositte.data.repo.GitRepository
import the.hart.repositte.domain.model.IssueDomain
import the.hart.repositte.domain.usecase.GetIssuesPaginatedUseCase
import the.hart.repositte.domain.usecase.GetRepositoryUseCase
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val gitRepository: GitRepository,
    private val getRepositoryUseCase: GetRepositoryUseCase,
    private val getIssuesUseCase: GetIssuesPaginatedUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailsScreenUiState())
    val uiState = _uiState.asStateFlow()

    /**
     * Set up [uiState]
     */
    fun loadData(
        ownerName: String,
        repoName: String,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = getRepositoryUseCase.invoke(ownerName, repoName)

            if (response is NetworkResult.Success) {
                val repository = response.data

                val pager = getIssuesUseCase.invoke(ownerName, repoName, repository.issueCount)
                    .cachedIn(viewModelScope)

                _uiState.update {
                    it.copy(
                        isLoadingError = false,
                        ownerAvatar = repository.authorAvatar,
                        ownerName = repository.authorName,
                        repoName = repository.repoName,
                        repoDescription = repository.repoDescription ?: "",
                        issues = pager,
                    )
                }
            } else {
                _uiState.update {
                    DetailsScreenUiState(isLoadingError = true)
                }
            }
        }
    }
}

data class DetailsScreenUiState(
    val isLoadingError: Boolean = false,
    val ownerAvatar: String = "",
    val ownerName: String = "",
    val repoName: String = "",
    val repoDescription: String = "",
    val issues: Flow<PagingData<IssueDomain>>? = null,
)