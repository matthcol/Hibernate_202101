package movieapi.persistence;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import movieapi.persistence.entity.Movie;
import movieapi.persistence.entity.Star;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class MovieAssociations {
	@Autowired
	EntityManager entityManager;
	
	@Test
	void testCreateMovieWithDirector() {
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

	@Test
	void testReadMovieWithActors() {
		int id = 2379713;
		var movieOpt = Optional.ofNullable(entityManager.find(Movie.class, id));
		movieOpt.ifPresent(m->System.out.println(
				m
				+ " ; director: " + m.getDirector() 
				+ " ; actors: " +m.getActors()
		));
	}
	
	@Test
	void testCreateMovieWithActors() {
		// scenario 1 : insert new Movie + new Star
		Movie movie = new Movie("No Time To Die", (short) 2021, (short) 163);
		Star star = new Star("Cary Joji Fukunaga", LocalDate.of(1977,7,10));
		entityManager.persist(movie);
		entityManager.persist(star);
		entityManager.flush();
		System.out.println(movie);
		int idMovie = movie.getId();
		int idCary = star.getId(); 
		entityManager.clear();
		// scenario 2 : add actors on empty list
		Movie movie2 = entityManager.find(Movie.class, idMovie);
		Star daniel = entityManager.find(Star.class, 185819);
		Star ralph = entityManager.find(Star.class, 146);
		movie2.setActors(List.of(daniel, ralph));
		entityManager.flush();
		System.out.println(movie2.getActors());
		entityManager.clear();
		// scenario 3 : add actors on previous actor list
		Movie movie3 = entityManager.find(Movie.class, idMovie);
		System.out.println(movie3 + " ; actors: " + movie3.getActors());
		Star naomie = entityManager.find(Star.class, 365140);
		movie3.getActors().add(naomie);
		entityManager.flush(); // delete + 3 inserts
		System.out.println(movie3.getActors());
		entityManager.clear();
		// scenario 4 : set director
		Movie movie4 = entityManager.find(Movie.class, idMovie);
		Star cary = entityManager.find(Star.class, idCary);
		movie4.setDirector(cary);
		entityManager.flush();
	}
	
}
