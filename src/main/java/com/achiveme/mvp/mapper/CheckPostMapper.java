package com.achiveme.mvp.mapper;

import com.achiveme.mvp.dto.checkPost.CheckPostResponseDTO;
import com.achiveme.mvp.entity.CheckPost;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel="spring")
public interface CheckPostMapper {
    @Mapping(source="challenge.id", target = "challengeId")
    CheckPostResponseDTO checkPostToCheckPostDTO(CheckPost checkPost);
}
