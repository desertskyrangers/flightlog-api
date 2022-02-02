package com.desertskyrangers.flightdeck.adapter.state.repo;

import com.desertskyrangers.flightdeck.adapter.state.entity.GroupEntity;
import com.desertskyrangers.flightdeck.adapter.state.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface GroupRepo extends JpaRepository<GroupEntity, UUID> {

	//List<GroupEntity> findAllByOwner_Id( UUID id );

	//Integer countByOwner_Id( UUID id );

	//List<GroupEntity> findAllByMembersNotIn( Set<UserEntity> members );

	// Works as expected
	//List<GroupEntity> findAllByOwnerIsNot( UserEntity owner );

	//List<GroupEntity> findAllByMembersNotContaining( Set<UserEntity> members );

}
