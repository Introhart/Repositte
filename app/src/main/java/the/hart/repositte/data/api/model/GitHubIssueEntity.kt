package the.hart.repositte.data.api.model

import com.google.gson.annotations.SerializedName
import the.hart.repositte.domain.model.IssueDomain

data class GitHubIssueEntity(
    @SerializedName("body")
    val body: String?,
    @SerializedName("comments")
    val comments: Int,
    @SerializedName("id")
    val id: Long,
    @SerializedName("number")
    val number: Int,
    @SerializedName("repository_url")
    val repositoryUrl: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("user")
    val user: GitHubOwnerEntity
) {
    fun toDomain() = IssueDomain(
        id = this.id,
        title = this.title,
        body = this.body ?: "",
        repositoryUrl = this.repositoryUrl,
    )
}