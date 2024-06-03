package the.hart.repositte.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import the.hart.repositte.data.api.model.toDomain
import the.hart.repositte.data.repo.GitRepository
import the.hart.repositte.domain.model.RepositoryDomain

class RepositoryPagingSource(
    private val query: String,
    private val repo: GitRepository
) : PagingSource<Int, RepositoryDomain>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RepositoryDomain> {
        return try {
            val currentPageNumber = params.key ?: 1
            val entity = repo.getRepoSearchResultPage(query, currentPageNumber)

            val nextKey = when {
                (params.loadSize * (currentPageNumber + 1)) < entity.totalCount -> currentPageNumber + 1
                else -> null
            }

            LoadResult.Page(
                prevKey = null,
                nextKey = nextKey,
                data = entity.items.map { it.toDomain() }
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, RepositoryDomain>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}