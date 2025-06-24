package com.kachalova.streamprocessing.mapper;

import com.kachalova.streamprocessing.dto.OriginalDataDto;
import com.kachalova.streamprocessing.entity.OriginalData;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OriginalDataMapper {
    OriginalDataMapper INSTANCE = Mappers.getMapper(OriginalDataMapper.class);

    OriginalDataDto toDto(OriginalData entity);

    OriginalData toEntity(OriginalDataDto dto);
}
