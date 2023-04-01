package com.desertskyrangers.flightdeck.adapter.store.entity.mapper;

import com.desertskyrangers.flightdeck.adapter.store.entity.AwardEntity;
import com.desertskyrangers.flightdeck.core.model.Actor;
import com.desertskyrangers.flightdeck.core.model.Award;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AwardEntityMapperTest {

	@Test
	void toAward() {
		UUID flightId = UUID.randomUUID();
		AwardEntity entity = new AwardEntity();
		entity.setId( UUID.randomUUID() );
		entity.setRecipientId( UUID.randomUUID() );
		entity.setRecipientType( Actor.Type.USER.name() );
		entity.setPresenterId( Actor.NONE.getId() );
		entity.setPresenterType( Actor.NONE.getType().name() );
		entity.setEarnedDate( new Date().getTime() );
		entity.setDescription( "Just for testing purposes" );
		entity.setData( "{\"flightId\":\"" + flightId + "\"}" );

		Award award = AwardEntityMapper.INSTANCE.toAward( entity );

		assertThat( award.getId() ).isEqualTo( entity.getId() );
		assertThat( award.getRecipient().getId() ).isEqualTo( entity.getRecipientId() );
		assertThat( award.getRecipient().getType().name() ).isEqualTo( entity.getRecipientType() );
		assertThat( award.getRecipient() ).isNotEqualTo( Actor.NONE );
		assertThat( award.getPresenter() ).isEqualTo( Actor.NONE );
		assertThat( award.getEarnedDate() ).isEqualTo( new Date( entity.getEarnedDate() ) );
		assertThat( award.getDescription() ).isEqualTo( entity.getDescription() );
		assertThat( award.get( "flightId" ) ).isEqualTo( flightId.toString() );

		// Ensure that values can still be added to the data map
		award.put( "testKey", "testValue" );
	}

	@Test
	void toAwardEntity() {
		Award award = new Award();
		award.setId( UUID.randomUUID() );
		award.setType( Award.Type.MEDAL );
		award.setRecipient( new Actor().setId( UUID.randomUUID() ).setType( Actor.Type.USER ) );
		award.setPresenter( new Actor().setId( UUID.randomUUID() ).setType( Actor.Type.ORG ) );
		award.setEarnedDate( new Date() );
		award.setDescription( "Just for testing purposes" );
		award.put( "flightId", UUID.randomUUID().toString() );

		AwardEntity entity = AwardEntityMapper.INSTANCE.toEntity( award );

		assertThat( entity.getId() ).isEqualTo( award.getId() );
		assertThat( entity.getRecipientType() ).isEqualTo( award.getRecipient().getType().name() );
		assertThat( entity.getRecipientId() ).isEqualTo( award.getRecipient().getId() );
		assertThat( entity.getPresenterType() ).isEqualTo( award.getPresenter().getType().name() );
		assertThat( entity.getPresenterId() ).isEqualTo( award.getPresenter().getId() );
		assertThat( entity.getEarnedDate() ).isEqualTo( award.getEarnedDate().getTime() );
		assertThat( entity.getDescription() ).isEqualTo( award.getDescription() );
		assertThat( entity.getData() ).isEqualTo( "{\"flightId\":\"" + award.get( "flightId" ) + "\"}" );
	}

}
