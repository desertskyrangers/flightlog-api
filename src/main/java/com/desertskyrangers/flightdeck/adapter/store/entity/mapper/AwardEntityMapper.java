package com.desertskyrangers.flightdeck.adapter.store.entity.mapper;

import com.desertskyrangers.flightdeck.adapter.store.entity.AwardEntity;
import com.desertskyrangers.flightdeck.core.model.Award;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AwardEntityMapper {

	AwardEntityMapper INSTANCE = Mappers.getMapper(AwardEntityMapper.class);

	Award toAward( AwardEntity entity );

}
