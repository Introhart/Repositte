package the.hart.repositte.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import the.hart.repositte.data.repo.GitRepository
import the.hart.repositte.data.paging.IssuePagingSource
import the.hart.repositte.domain.model.IssueDomain
import javax.inject.Inject

class GetIssuesPaginatedUseCase @Inject constructor(
    private val repository: GitRepository
) {
    fun invoke(
        owner: String,
        name: String,
        issueCount: Int
    ): Flow<PagingData<IssueDomain>> {
        return Pager(
            config = PagingConfig(pageSize = 30),
            initialKey = 1,
            pagingSourceFactory = {
                IssuePagingSource(
                    ownerName = owner,
                    repoName = name,
                    openIssuesCount = issueCount,
                    repository = repository
                )
            }
        ).flow
    }
}