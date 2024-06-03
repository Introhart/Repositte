package the.hart.repositte.domain.usecase

import the.hart.repositte.data.api.response.NetworkResult
import the.hart.repositte.data.repo.GitRepository
import the.hart.repositte.domain.model.RepositoryDomain
import javax.inject.Inject

class GetRepositoryUseCase @Inject constructor (
    private val gitRepository: GitRepository
) {
    suspend operator fun invoke(owner: String, name: String): NetworkResult<RepositoryDomain> {
        return gitRepository.getRepository(owner, name)
    }
}