package com.bilgeadam.mapper;



import com.bilgeadam.dto.request.UserProfileUpdateRequestDto;
import com.bilgeadam.dto.request.UserSaveRequestDto;
import com.bilgeadam.dto.response.UserProfileFindAllResponseDto;
import com.bilgeadam.rabbitmq.model.RegisterElasticModel;
import com.bilgeadam.rabbitmq.model.RegisterModel;
import com.bilgeadam.repository.entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IUserMapper {
    IUserMapper INSTANCE = Mappers.getMapper(IUserMapper.class);

    UserProfile toUserProfile(UserSaveRequestDto dto);
    UserProfile toUserProfile(RegisterModel model);


    UserProfile toUserProfile(UserProfileUpdateRequestDto dto);

   // @Mapping( source = "id",target = "userProfileId")
    UserProfileFindAllResponseDto toUserProfileFindAllResponseDto(UserProfile userProfile);

    RegisterElasticModel toRegisterElasticModel(UserProfile userProfile);


}
