package the.hart.repositte.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import the.hart.repositte.data.repo.GitRepository
import the.hart.repositte.data.paging.RepositoryPagingSource
import the.hart.repositte.domain.model.RepositoryDomain
import javax.inject.Inject

class GetRepositoryPaginatedUseCase @Inject constructor(
    private val gitRepository: GitRepository,
) {
    fun invoke(query: String): Flow<PagingData<RepositoryDomain>> {
        return Pager(
            config = PagingConfig(pageSize = 30),
            initialKey = 1,
            pagingSourceFactory = { RepositoryPagingSource(query, gitRepository) }
        ).flow
    }
}