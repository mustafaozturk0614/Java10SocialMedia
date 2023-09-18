package com.bilgeadam.mapper;



import com.bilgeadam.repository.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IPostMapper {
    IPostMapper INSTANCE = Mappers.getMapper(IPostMapper.class);

    Post toUserProfile(UserSaveRequestDto dto);
    Post toUserProfile(RegisterModel model);


    Post toUserProfile(UserProfileUpdateRequestDto dto);

   // @Mapping( source = "id",target = "userProfileId")
    UserProfileFindAllResponseDto toUserProfileFindAllResponseDto(Post userProfile);

    RegisterElasticModel toRegisterElasticModel(Post userProfile);


}
