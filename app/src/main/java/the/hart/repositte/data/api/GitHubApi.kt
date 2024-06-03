package the.hart.repositte.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import the.hart.repositte.data.api.model.GitHubRepositoryEntity
import the.hart.repositte.data.api.model.GitHubIssueEntity
import the.hart.repositte.data.api.model.GitHubSearchResultEntity

interface GitHubService {

    /**
     * Get list of repositories by [query]
     * @param query search request
     */
    @GET("/search/repositories")
    suspend fun getRepoQuery(
        @Query("q") query: String
    ): GitHubSearchResultEntity

    /**
     * Get paginated list of [GitHub] repositories by [query]
     * @param query search request
     * @param page pagination page serial number
     * @param perPage pagination page max. elements number
     */
    @GET("/search/repositories")
    suspend fun getRepoSearchPage(
        @Query("q") query: String,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 30,
    ): GitHubSearchResultEntity

    /**
     * Get repository by [owner] and [name]
     * @param owner repository owner
     * @param name repository name
     */
    @GET("/repos/{owner}/{name}")
    suspend fun getRepo(
        @Path("owner") owner: String,
        @Path("name") name: String,
    ): Response<GitHubRepositoryEntity>

    /**
     * Get list of repository issues
     * @param owner repository owner
     * @param name repository name
     * @param page pagination page serial number
     * @param perPage pagination page max. elements number
     */
    @GET("/repos/{owner}/{name}/issues")
    suspend fun getIssues(
        @Path("owner") owner: String,
        @Path("name") name: String,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 30,
    ): List<GitHubIssueEntity>
}