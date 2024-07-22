package sparta.nbcamp.wachu.domain.member.dto

data class ProfileResponse (
    val profileUrl:String?
){
    companion object{
        fun from(profileUrl: String): ProfileResponse{
            return ProfileResponse(
                profileUrl = profileUrl
            )
        }
    }
}