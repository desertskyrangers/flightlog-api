package com.desertskyrangers.flightdeck.adapter.store.entity.mapper;

import com.desertskyrangers.flightdeck.adapter.store.entity.AwardEntity;
import com.desertskyrangers.flightdeck.core.model.Actor;
import com.desertskyrangers.flightdeck.core.model.Award;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Qualifier;
import org.mapstruct.factory.Mappers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

@Mapper
public abstract class AwardEntityMapper {

	public static final AwardEntityMapper INSTANCE = Mappers.getMapper( AwardEntityMapper.class );

	private static final ObjectMapper MAPPER = new ObjectMapper();

	@Mapping( target = "recipient", source = "entity", qualifiedBy = RecipientQualifier.class )
	@Mapping( target = "presenter", source = "entity", qualifiedBy = PresenterQualifier.class )
	@Mapping( target = "earnedDate", expression = "java(new Date(entity.getEarnedDate()))" )
	public abstract Award toAward( AwardEntity entity );

	@RecipientQualifier
	Actor mapRecipient( AwardEntity entity ) {
		if( entity.getRecipientId() == null || entity.getRecipientType() == null ) return Actor.NONE;
		return new Actor().setId( entity.getRecipientId() ).setType( Actor.Type.valueOf( entity.getRecipientType() ) );
	}

	@PresenterQualifier
	Actor mapPresenter( AwardEntity entity ) {
		if( entity.getPresenterId() == null || entity.getPresenterType() == null ) return Actor.NONE;
		return new Actor().setId( entity.getPresenterId() ).setType( Actor.Type.valueOf( entity.getPresenterType() ) );
	}

	@Qualifier
	@Target( ElementType.METHOD )
	@Retention( RetentionPolicy.CLASS )
	@interface RecipientQualifier {}

	@Qualifier
	@Target( ElementType.METHOD )
	@Retention( RetentionPolicy.CLASS )
	@interface PresenterQualifier {}

	@Mapping( target = "recipientId", source = "award.recipient.id" )
	@Mapping( target = "recipientType", source = "award.recipient.type" )
	@Mapping( target = "presenterId", source = "award.presenter.id" )
	@Mapping( target = "presenterType", source = "award.presenter.type" )
	@Mapping( target = "earnedDate", source="award.earnedDate.time")
	public abstract AwardEntity toEntity( Award award );

	String toJson( Map<String, String> map ) throws JsonProcessingException {
		return MAPPER.writeValueAsString( map );
	}

	Map<String, String> toMap( String data ) throws JsonProcessingException {
		return MAPPER.readValue( data, new TypeReference<>() {} );
	}
}
