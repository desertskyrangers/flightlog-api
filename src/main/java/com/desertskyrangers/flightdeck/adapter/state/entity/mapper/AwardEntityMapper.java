package com.desertskyrangers.flightdeck.adapter.state.entity.mapper;

import com.desertskyrangers.flightdeck.adapter.state.entity.AwardEntity;
import com.desertskyrangers.flightdeck.core.model.Award;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AwardEntityMapper {

	AwardEntityMapper INSTANCE = Mappers.getMapper(AwardEntityMapper.class);

	Award toAward( AwardEntity entity );

}
