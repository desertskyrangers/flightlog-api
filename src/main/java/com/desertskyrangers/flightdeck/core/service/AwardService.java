package com.desertskyrangers.flightdeck.core.service;

import com.desertskyrangers.flightdeck.adapter.store.entity.mapper.AwardEntityMapper;
import com.desertskyrangers.flightdeck.adapter.store.repo.AwardRepo;
import com.desertskyrangers.flightdeck.award.Awards;
import com.desertskyrangers.flightdeck.core.model.*;
import com.desertskyrangers.flightdeck.port.AwardServices;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AwardService implements AwardServices {

	//private final StateRetrieving stateRetrieving;

	private final AwardRepo awardRepo;

	public Page<Award> getAwards( UUID id, int page, int size ) {
		return awardRepo.findByRecipientId( id, PageRequest.of( page, size, Sort.Direction.DESC, "earnedDate" ) ).map( AwardEntityMapper.INSTANCE::toAward );
	}

	public void checkForAwards( Flight flight ) {
		Awards.getAwardTypes().stream().forEach( t -> {
			// Does the flight meet the requirements for this award type
			if( t.meetsRequirements( flight ) ) {
				// Create the award
				System.err.println( "Award requirements met type=" + t.getClass().getSimpleName() );

				Award award = new Award().setId( UUID.randomUUID() );
				award.setRecipient( new Actor( flight.pilot() ) );
				award.setEarnedDate( new Date() );

				// FIXME What about a link back to the award type?

				// FIXME This might really belong to the award type
				// Or, this can be used to enhance the award description
				award.setDescription( "" );
				// FIXME This might really belong to the award type
				award.setType( Award.Type.BADGE );

				award.put( "flightId", flight.id().toString() );

				awardRepo.save( AwardEntityMapper.INSTANCE.toEntity( award ) );
			}
		} );
	}

	public void checkForAwards( User user ) {
		// We want to determine if a user is eligible for one or more awards
		// - Check for records
		// - Check for flight awards
		// - Check for special awards

		// Create awards
	}

	public void checkForAwards( Group group ) {
		// We want to determine if a group is eligible for one or more awards
		// - Check for flight awards
		// - Check for special awards

		// Create awards
	}

	public void createAward( Award award ) {
		//
	}

}
