package the.hart.repositte.data.api.model


import com.google.gson.annotations.SerializedName

data class GitHubSearchResultEntity(
    @SerializedName("incomplete_results")
    val incompleteResults: Boolean,
    @SerializedName("items")
    val items: List<GitHubRepositoryEntity>,
    @SerializedName("total_count")
    val totalCount: Int
)