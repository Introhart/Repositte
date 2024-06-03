package the.hart.repositte.domain.model

data class RepositoryDomain(
    val authorAvatar: String,
    val authorName: String,
    val repoName: String,
    val repoDescription: String?,
    val issueCount: Int,
)