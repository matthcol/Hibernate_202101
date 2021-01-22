package movieapi.persistence;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import movieapi.persistence.entity.Movie;
import movieapi.persistence.entity.Star;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class QueriesJPQL {

	@Autowired
	EntityManager entityManager;
	
	
	@ParameterizedTest
	@ValueSource(ints = {2379713, 0})
	void testMovieById(int id) {
		var data = entityManager.find(Movie.class, id);
		System.out.println(data);
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"Spectre", "The Man Who Knew Too Much", "Dummy"} )
	void testMovieByTitle(String title) {
		// TypedQuery<Movie> query = 
		var data = entityManager.createQuery(
				"select m from Movie m where m.title = :title", Movie.class)
			.setParameter("title", title)
			.getResultList();  // List<Movie>
			// .getSingleResult(); // Movie if only one (NoResultException or NonUniqueResultException)
		System.out.println(data);
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"Spectre", "The Man Who Knew Too Much", "Dummy"} )
	void testMovieByTitleNamedQuery(String title) {
		var data = entityManager.createNamedQuery(
				"get_movie_by_title", Movie.class)
			.setParameter("title", title)
			.getResultList();  // List<Movie>
			// .getSingleResult(); // Movie if only one (NoResultException or NonUniqueResultException)
		System.out.println(data);
	}
	
	
	@Test
	void testMovieByTitleAndYearGreater() {
		String title = "The Man Who Knew Too Much";
		short year = 1950;
		var data = entityManager.createQuery(
				"select m from Movie m where m.title = :title and m.year > :year")
				.setParameter("title", title)
				.setParameter("year", year)
				.getResultList();
		System.out.println(data);
	}
	
	@Test
	void testMovieByTitlePart() {
		String find = "Man";
		String title = "%" + find + "%";
		var data = entityManager.createQuery(
				"select m from Movie m where m.title like :title", Movie.class)
				.setParameter("title", title)
				.getResultStream()
				.filter(m -> m.getYear() >= 2000)
				.map(Movie::getTitle)
				//.collect(Collectors.toList()); // list with order from origin
				// .collect(Collectors.toCollection(ArrayList::new));
				.collect(Collectors.toCollection(TreeSet::new)); // String order
				//.forEach(t -> System.out.println("Title: " + t));
		System.out.println(data);
	}

	@Test
	void testStarByBirthyear() {
		int year = 1930;
		var data = entityManager.createQuery(
				"select s from Star s where extract(year from s.birthdate) = :year", Star.class)
			.setParameter("year", year)
			.getResultStream()
			.limit(10)
			.collect(Collectors.toCollection(()->new TreeSet<>(
					Comparator.comparing(Star::getName))));
		System.out.println(data);
	}
	
	@Test
	void testMovieLast2years() {
		// NB: YEAR is not ambiguous with year
		var data = entityManager.createQuery(
				"select m from Movie m where m.year > EXTRACT(YEAR from CURRENT_DATE)-2", 
				Movie.class)
			.getResultList();
		System.out.println(data);
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"Tarantino", "tarantino", "Quentin", "quentin"})
	void testStarByPartNameCaseInsensitive(String find) {
		String name = "%" + find.toLowerCase() + "%";
		var data = entityManager.createQuery(
				"select s from Star s where LOWER(s.name) like :name", 
				Star.class)
			.setParameter("name", name)
			.getResultList();
		System.out.println(data);
	}
	
	@Test
	void testMovieByDirectorName() {
		String directorName = "Alfred Hitchcock";
		var data = entityManager.createQuery(
		//		"select m from Movie m where m.director.name = :name",
				"select m from Movie m join m.director s where s.name = :name", 
				Movie.class)
			.setParameter("name", directorName)
			.getResultList();
		System.out.println(data);		
	}
	
	@Test
	void testYearsDirector() {
		String directorName = "Alfred Hitchcock";
		var data = entityManager.createQuery(
				"select distinct m.year from Movie m join m.director s where s.name = :name order by m.year desc",
				Short.class)
			.setParameter("name", directorName)
			.getResultList();
		System.out.println(data);
		
	}
	
	@Test
	void testTitleYearDirectorName() {
		entityManager.createQuery(
				"select m.title, m.year, s.name from Movie m join m.director s",
				Object[].class)
			.getResultStream()
			.limit(10)
			// .forEach(row->System.out.println(Arrays.toString(row)));
			.forEach(row->System.out.println(
					((String) row[0]).toUpperCase() +","+ row[1] +","+ row[2]));
	}
	
	@Test
	void testStatsByDirector() {
		long min_nb_movies = 30L;
		entityManager.createQuery(
				"select s.name, count(m.id), min(m.year) from Movie m join m.director s group by s having count(m.id) > :min_nb_movie",
				Object[].class)
			.setParameter("min_nb_movie", min_nb_movies)
			.getResultStream()
			.limit(10)
			.forEach(row->System.out.println(Arrays.toString(row)));
	}
}

