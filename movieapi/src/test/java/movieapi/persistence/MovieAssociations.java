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
		entityManager.clear();
		// scenario 5 : read movie again with default fetching
		Movie movie5 = entityManager.find(Movie.class, idMovie);
		System.out.println(movie5); // eager sur director et lazy sur actors
		System.out.println(movie5.getDirector()); // already fetched
		System.out.println(movie5.getActors()); // fetch actors here
		entityManager.clear();
		// scenario 6 : read movie again with dynamic fetching (JPQL)
		Movie movie6 = entityManager.createQuery(
				"select m from Movie m left join fetch m.director left join fetch m.actors where m.id =:id",
				Movie.class)
				.setParameter("id", idMovie)
				.getSingleResult();
		System.out.println(movie6); // eager sur director et actors
		System.out.println(movie6.getDirector()); // already fetched
		System.out.println(movie6.getActors()); // already fetched
		entityManager.clear();
		// scenario 7 : read movie again with dynamic fetching (entityGraph)
		Movie movie7 = entityManager.find(Movie.class, idMovie,
				Collections.singletonMap(
						"javax.persistence.fetchgraph",
						entityManager.getEntityGraph( "movie.actors" )
					));
		System.out.println("SC7: " + movie7); // eager sur director et actors
		System.out.println(movie7.getDirector()); // already fetched
		System.out.println(movie7.getActors()); // already fetched
		entityManager.clear();
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
