package the.hart.repositte.domain.model

data class IssueDomain(
    val id: Long,
    val title: String,
    val body: String?,
    val repositoryUrl: String,
)
