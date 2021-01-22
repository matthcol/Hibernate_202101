package movieapi.persistence;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class MovieAssociations {
	@Autowired
	EntityManager entityManager;
	
	@Test
	void test() {
		String title = "Avengers: Endgame";
		short year = 2019;
		short duration = 181;
		Movie movie = new Movie(title, year, duration);
		String name = "Joe Russo";
		LocalDate birthdate = LocalDate.of(1971, 7, 8);
		Star star = new Star(name, birthdate);
		movie.setDirector(star);
		entityManager.persist(star);
		entityManager.persist(movie);
		entityManager.flush();
	}

}
