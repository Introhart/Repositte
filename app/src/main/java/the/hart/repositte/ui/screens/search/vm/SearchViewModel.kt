package the.hart.repositte.ui.screens.search.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import the.hart.repositte.domain.model.RepositoryDomain
import the.hart.repositte.domain.usecase.GetRepositoryPaginatedUseCase
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getRepoPaginated: GetRepositoryPaginatedUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(RepoPreviewUiState())
    val uiState = _uiState.asStateFlow()

    fun search() {
        if (_uiState.value.query.isEmpty()) {
            return
        }

        val pager = getRepoPaginated.invoke(_uiState.value.query).cachedIn(viewModelScope)

        _uiState.update {
            _uiState.value.copy(pager = pager)
        }
    }

    fun onQueryChange(newQuery: String) {
        _uiState.update {
            _uiState.value.copy(query = newQuery)
        }
    }

    fun clearQuery() {
        _uiState.update {
            _uiState.value.copy(query = "")
        }
    }
}

data class RepoPreviewUiState(
    val query: String = "",
    val pager: Flow<PagingData<RepositoryDomain>>? = null,
)