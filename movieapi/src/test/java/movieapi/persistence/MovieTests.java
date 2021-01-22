package movieapi.persistence;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import movieapi.persistence.entity.Movie;

@DataJpaTest // by default on H2 DB
@AutoConfigureTestDatabase(replace = Replace.NONE) // même base+config application
class MovieTests {
	
	@Autowired	// configuré + injecté par Spring
	EntityManager entityManager; // gestionnaire de cache hibernate

	@ParameterizedTest
	@NullSource
	@ValueSource(shorts = {1,181,600})
	void testCreateWithOptionalDuration(Short duration) {
		// given
		String title = "Avengers: Endgame";
		short year = 2019;
		Movie movie = new Movie(title, year, duration);
		System.out.println(movie);
		// when
		entityManager.persist(movie); // save obejct in DB and retrieve Id
		entityManager.flush(); // force synchro cache hibernate/db
		System.out.println(movie);
		// then
		assertNotNull(movie.getId());
	}
	
	@Test
	void testCreateWithLongTitle() {
		// given
		String title = "Night of the Day of the Dawn of the Son of the Bride of the Return of the Revenge of the Terror of the Attack of the Evil Mutant Hellbound Flesh Eating Crawling Alien Zombified Subhumanoid Living Dead, Part 5";
		short year = 2011;
		Movie movie = new Movie(title, year, null);
		System.out.println(movie);
		// when
		entityManager.persist(movie); // save obejct in DB and retrieve Id
		System.out.println(movie);
		// then
		assertNotNull(movie.getId());
	}

	@Test
	void testCreateWithNoTitle() {
		// given
		String title = null;
		short year = 2011;
		Movie movie = new Movie(title, year, null);
		// System.out.println(movie);
		// when + then
		assertThrows(PersistenceException.class, 
				() -> entityManager.persist(movie)); // try to save wrong object
	}
	
	@Test
	void readOneMovie() {
		int id = 56869; // id of existing movie (not a real unit test)
		var movie = entityManager.find(Movie.class, id);
		System.out.println(movie);
		System.out.println("Director: " + movie.getDirector());
	}
	
	@Test
	void readNotExistingMovie() {
		int id = 0; // id of no movie
		var movie = entityManager.find(Movie.class, id);
		System.out.println(movie);
		assertNull(movie);
	}
	
	@Test
	void readAll() {
		var movies = entityManager.createQuery("From Movie", Movie.class).getResultList();
		System.out.println("Movies read: " + movies.size());
		movies.stream()
			.limit(10)
			.map(Movie::getTitle)
			.forEach(System.out::println);
	}
	
	@Test
	void testUpdate() {
		int id = 56869; // id of existing movie (not a real unit test)
		var movie = entityManager.find(Movie.class, id);
		// update object already fetched in hibernate cache
		movie.setPosterUri("https://m.media-amazon.com/images/M/MV5BMTAxNDA1ODc5MDleQTJeQWpwZ15BbWU4MDg2MDA4OTEx._V1_UX182_CR0,0,182,268_AL_.jpg");
		entityManager.flush(); // generate update
	}
	
	@Test
	void testDelete() {
		int id = 56869; // id of existing movie (not a real unit test)
		var movie = entityManager.find(Movie.class, id);
		// delete object from cache/db
		entityManager.remove(movie);
		entityManager.flush(); // generate delete
	}
}
