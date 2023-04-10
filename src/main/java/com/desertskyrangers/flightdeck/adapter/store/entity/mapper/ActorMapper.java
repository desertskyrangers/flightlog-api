package com.desertskyrangers.flightdeck.adapter.store.entity.mapper;

import com.desertskyrangers.flightdeck.adapter.store.entity.AwardEntity;
import com.desertskyrangers.flightdeck.core.model.Actor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ActorMapper {

	@Mapping( target = "id", source = "entity.recipientId" )
	@Mapping( target = "type", source = "entity.recipientType" )
	Actor toActor( AwardEntity entity );

}
