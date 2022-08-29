package com.desertskyrangers.flightdeck.core.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

	@Test
	void testName() {
		User user = new User().firstName( "Uma" ).lastName( "Uvalde" );
		assertThat( user.name() ).isEqualTo( "Uma Uvalde" );
	}

	@Test
	void testNameWithPreferredName() {
		User user = new User().firstName( "Uma" ).lastName( "Uvalde" ).preferredName( "Alice" );
		assertThat( user.name() ).isEqualTo( "Alice" );
	}

	@Test
	void testNameWithBlankPreferredName() {
		User user = new User().firstName( "Uma" ).lastName( "Uvalde" ).preferredName( "" );
		assertThat( user.name() ).isEqualTo( "Uma Uvalde" );
	}

	@Test
	void testNameWithCallSign() {
		User user = new User().firstName( "Uma" ).lastName( "Uvalde" ).callSign( "Unicorn" );
		assertThat( user.name() ).isEqualTo( "Uma \"Unicorn\" Uvalde" );
	}

	@Test
	void testNameWithPreferredNameAndCallSign() {
		User user = new User().firstName( "Uma" ).lastName( "Uvalde" ).preferredName( "Alice" ).callSign( "Unicorn" );
		assertThat( user.name() ).isEqualTo( "Alice" );
	}

	@Test
	void testSortOrder() {

//| Steve     | Booker     |                        | Buzz       |
//| NULL      | NULL       | NULL                   | NULL       |
//| Glen      | Varga      |                        | F.E.L.I.X. |
//| Mark      | Soderquist | @Merlin                | Merlin     |
//| NULL      | Unlisted   | Unlisted               | NULL       |
//| Mark      | Soderquist | Mark Soderquist (ecco) | NULL       |
//| NULL      | NULL       | NULL                   | NULL       |
//| Noah      | Booker     |                        | Turbo      |
//| Hudson    | Brammer    | Hudson                 |            |
//| Blaine    | Forbush    | Blaine                 | NULL       |
//| Micah     | Flowers    |                        | Amazon     |

			User steve = new User().firstName( "Steve" ).lastName( "Brooks" ).callSign( "Banjo" );
			User micah = new User().firstName( "Micah" ).lastName( "Franklin" ).callSign( "Spark" );
		User mark = new User().firstName( "Mark" ).lastName( "Sanders" ).callSign( "Rocker" );
		User glen = new User().firstName( "Glen" ).lastName( "Vanderbilt" ).callSign( "Nash" );
		User noah = new User().firstName( "Noah" ).lastName( "Brooks" ).callSign( "Stream" );
		User hudson = new User().firstName( "Hudson" ).lastName( "Benson" ).callSign( "" ).preferredName("Hudson");
		User blaine = new User().firstName( "Blaine" ).lastName( "Friend" ).callSign( "" ).preferredName("Blaine");

		List<User> users = List.of( steve, glen, mark, noah, hudson, blaine, micah );
		List<User> sorted = users.stream().sorted().toList();
		assertThat( sorted ).containsExactly( blaine, glen, hudson, mark, micah, noah, steve );

		mark.preferredName("@Rocker");
		sorted = users.stream().sorted().toList();
		assertThat( sorted ).containsExactly( mark, blaine, glen, hudson, micah, noah, steve );
	}

}
