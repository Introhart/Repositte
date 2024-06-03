package the.hart.repositte.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import the.hart.repositte.data.repo.GitRepository
import the.hart.repositte.domain.model.IssueDomain

class IssuePagingSource(
    private val ownerName: String,
    private val repoName: String,
    private val openIssuesCount: Int,
    private val repository: GitRepository
) : PagingSource<Int, IssueDomain>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, IssueDomain> {
        return try {
            val currentPageNumber = params.key ?: 1
            val issues = repository.getRepositoryIssues(ownerName, repoName, currentPageNumber)

            val nextKey = when {
                (params.loadSize * (currentPageNumber + 1)) < openIssuesCount -> currentPageNumber + 1
                else -> null
            }

            LoadResult.Page(
                prevKey = null,
                nextKey = nextKey,
                data = issues.map { it.toDomain() }
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, IssueDomain>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}