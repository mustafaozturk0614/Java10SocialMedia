package com.bilgeadam.mapper;

import com.bilgeadam.dto.response.UserProfileFindAllResponseDto;
import com.bilgeadam.rabbitmq.model.RegisterElasticModel;
import com.bilgeadam.repository.entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IElasticMapper {
    IElasticMapper INSTANCE = Mappers.getMapper(IElasticMapper.class);

    List<UserProfile> toUserProfiles(List<UserProfileFindAllResponseDto> dtos);

    UserProfile toUserProfile(RegisterElasticModel model);
}
