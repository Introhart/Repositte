package the.hart.repositte.data.api.model

import com.google.gson.annotations.SerializedName
import the.hart.repositte.domain.model.RepositoryDomain

data class GitHubRepositoryEntity(
    @SerializedName("description")
    val description: String?,
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("has_issues")
    val hasIssues: Boolean,
    @SerializedName("id")
    val id: Int,
    @SerializedName("issues_url")
    val issuesUrl: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("open_issues")
    val openIssues: Int,
    @SerializedName("open_issues_count")
    val openIssuesCount: Int,
    @SerializedName("owner")
    val owner: GitHubOwnerEntity,
    @SerializedName("score")
    val score: Double,
    @SerializedName("url")
    val url: String,
)

fun GitHubRepositoryEntity.toDomain() = RepositoryDomain(
    authorAvatar = this.owner.avatarUrl,
    authorName = this.owner.login,
    repoName = this.name,
    repoDescription = this.description,
    issueCount = this.openIssuesCount,
)