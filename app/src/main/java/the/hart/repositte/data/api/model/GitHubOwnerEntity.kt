package the.hart.repositte.data.api.model


import com.google.gson.annotations.SerializedName

data class GitHubOwnerEntity(
    @SerializedName("avatar_url")
    val avatarUrl: String,
    @SerializedName("events_url")
    val eventsUrl: String,
    @SerializedName("gravatar_id")
    val gravatarId: String,
    @SerializedName("html_url")
    val htmlUrl: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("login")
    val login: String,
    @SerializedName("node_id")
    val nodeId: String,
    @SerializedName("repos_url")
    val reposUrl: String,
    @SerializedName("url")
    val url: String
)