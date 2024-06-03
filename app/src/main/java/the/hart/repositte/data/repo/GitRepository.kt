package the.hart.repositte.data.repo

import the.hart.repositte.data.api.GitHubService
import the.hart.repositte.data.api.model.GitHubSearchResultEntity
import the.hart.repositte.data.api.model.GitHubIssueEntity
import the.hart.repositte.data.api.model.toDomain
import the.hart.repositte.data.api.response.NetworkResult
import the.hart.repositte.data.api.response.handleApi
import the.hart.repositte.domain.model.RepositoryDomain
import javax.inject.Inject

class GitRepository @Inject constructor(
    private val gitHubApi: GitHubService
) {
    suspend fun getRepoSearchResultPage(query: String, page: Int = 1): GitHubSearchResultEntity {
        return gitHubApi.getRepoSearchPage(query, page)
    }

    suspend fun getRepository(owner: String, name: String): NetworkResult<RepositoryDomain> {
        return handleApi (
            execute = { gitHubApi.getRepo(owner, name) },
            convert = { it.toDomain() },
        )
    }

    suspend fun getRepositoryIssues(owner: String, name: String, page: Int = 1): List<GitHubIssueEntity> {
        return gitHubApi.getIssues(owner, name)
    }
}