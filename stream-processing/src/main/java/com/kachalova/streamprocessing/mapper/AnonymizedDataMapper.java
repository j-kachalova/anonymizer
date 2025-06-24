package com.kachalova.streamprocessing.mapper;

import com.kachalova.streamprocessing.dto.AnonymizedDataDto;
import com.kachalova.streamprocessing.entity.AnonymizedData;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AnonymizedDataMapper {
    AnonymizedDataMapper INSTANCE = Mappers.getMapper(AnonymizedDataMapper.class);

    AnonymizedDataDto toDto(AnonymizedData entity);

    AnonymizedData toEntity(AnonymizedDataDto dto);
}
