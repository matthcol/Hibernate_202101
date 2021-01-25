package movieapi.persistence;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import movieapi.persistence.entity.ColorType;
import movieapi.persistence.entity.Language;
import movieapi.persistence.entity.Movie;
import movieapi.persistence.entity.Play;
import movieapi.persistence.entity.Star;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class MovieAssociations {
	@Autowired
	EntityManager entityManager;
	
	@Test
	@Rollback(false) // debug purpose
	void testCreateMovieWithDirector() {
		String title = "Avengers: Endgame";
		short year = 2019;
		short duration = 181;
		Movie movie = new Movie(title, year, duration);
		String name = "Joe Russo";
		LocalDate birthdate = LocalDate.of(1971, 7, 8);
		//Date birthdate = new Date(1971, 7, 8);
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
		Star daniel = new Star("Daniel Craig", null);
		Star ralph = new Star("Ralph Fiennes", null);
		entityManager.persist(daniel);
		entityManager.persist(ralph);
		entityManager.persist(movie);
		entityManager.flush();
		Play playDaniel = new Play(movie, daniel, "James Bond");
		Play playRalph = new Play(movie, ralph, "M.");
		var actors = List.of(playDaniel, playRalph);
		actors.forEach(entityManager::persist);
		movie.setPlays(actors);
		entityManager.flush();
		int idMovie = movie.getId();
		entityManager.clear();
		// Read Data again
		Movie movie2 = entityManager.find(Movie.class, idMovie);
		System.out.println("Casting " + movie + ":");
		movie2.getPlays().stream()
			.forEach(p-> System.out.println(p.getActor() + " : " + p.getRole()));
	}
	
	@Test
	void testCascade() {
		// 2 objects en RAM not in hibernate cache
		Movie movie = new Movie("No Time To Die", (short) 2021, (short) 163);
		Star star = new Star("Cary Joji Fukunaga", LocalDate.of(1977,7,10));
		movie.setDirector(star);
		// add movie in cache hibernate for persistance
		//entityManager.persist(star);
		entityManager.persist(movie); // and star with cascade
		entityManager.flush();
		System.out.println(movie);
		int idMovie = movie.getId();
		int idCary = star.getId(); 
		//entityManager.clear();
		
		entityManager.remove(movie);
		entityManager.flush();
	}
	
	@Test
	@Rollback(false) // for debug purpose
	void testCreateMovieAllFeatures() {
		// scenario 1 : insert new Movie + new Star
		Movie movie = new Movie("No Time To Die", (short) 2021, (short) 163);
		movie.setColor(ColorType.COLOR);
		Collections.addAll(movie.getGenres(), "Action", "Adventure", "Thriller");
		Language lang = new Language("English", "En");
		movie.setLanguage(lang);
		entityManager.persist(movie);
		entityManager.flush();
		int idMovie = movie.getId();
		// read previous data
		entityManager.clear();
		Movie movie2 = entityManager.find(Movie.class, idMovie);
		System.out.println(movie2 + ": " + movie2.getColor() 
				+ " ; genres: " + movie2.getGenres()
				+ " ; language: " + movie2.getLanguage().getName()
				+ "/" + movie2.getLanguage().getIsoCode2()
				+ " ; duration: " + movie2.getDurationHourMinute()
				+ " ; hours: " + movie2.getHours()
				);
		assertEquals(ColorType.COLOR, movie2.getColor());
		// read again via query on color
		entityManager.clear();
		var color = ColorType.COLOR;
		entityManager.createQuery(
				"select m from Movie m where color = :color",
				Movie.class)
			.setParameter("color", color)
			.getResultStream()
			.forEach(m -> System.out.println(m + " ; color: " + m.getColor()));
		// read again via query on genres
		var genre = "Thriller";
		entityManager.createQuery(
				"select m from Movie m where :genre member of m.genres",
				Movie.class)
			.setParameter("genre", genre)
			.getResultStream()
			.forEach(m -> System.out.println(m + " ; genres: " + m.getGenres()));
		// read again via query on language
		var lang_code2 = "En";
		entityManager.createQuery(
				"select m from Movie m where m.language.isoCode2 = :lang",
				Movie.class)
			.setParameter("lang", lang_code2)
			.getResultStream()
			.forEach(m -> System.out.println(m + " ; language: " + m.getLanguage().getName()));
	}
	
}
