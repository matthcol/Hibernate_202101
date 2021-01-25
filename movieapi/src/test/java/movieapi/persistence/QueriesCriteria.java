package movieapi.persistence;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import movieapi.persistence.entity.Movie;
import movieapi.persistence.entity.Movie_;
import movieapi.persistence.entity.Star;
import movieapi.persistence.entity.Star_;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class QueriesCriteria {
	
	@Autowired
	EntityManager entityManager;

	@Test
	void testMovieTitleYear() {
		var criteriaBuilder = entityManager.getCriteriaBuilder();
		var criteriaMovie = criteriaBuilder.createQuery(Movie.class);
		var root = criteriaMovie.from(Movie.class);
		criteriaMovie.select(root);
		criteriaMovie.where(
			criteriaBuilder.and(
				criteriaBuilder.equal(root.get(Movie_.title), "The Man Who Knew Too Much"),
				criteriaBuilder.greaterThan(root.get(Movie_.year), (short) 1950)));
		entityManager.createQuery(criteriaMovie)
			.getResultStream()
			.forEach(System.out::println);
	}
	
	@Test
	void testMovieDirector() {
		var criteriaBuilder = entityManager.getCriteriaBuilder();
		var criteriaMovie = criteriaBuilder.createQuery(Movie.class);
		var rootMovie = criteriaMovie.from(Movie.class);
		var movieJoin = rootMovie.join(Movie_.director);
		criteriaMovie.where(
				criteriaBuilder.equal(movieJoin.get(Star_.name), "Clint Eastwood"));		
		entityManager.createQuery(criteriaMovie)
			.getResultStream()
			.forEach(System.out::println);
	}

	@Test
	void testMovieActors() {
		var criteriaBuilder = entityManager.getCriteriaBuilder();
		var criteriaMovie = criteriaBuilder.createQuery(Movie.class);
		var rootMovie = criteriaMovie.from(Movie.class);
		var movieJoin = rootMovie.join(Movie_.actors);
		criteriaMovie.where(
				criteriaBuilder.equal(movieJoin.get(Star_.name), "Clint Eastwood"));		
		entityManager.createQuery(criteriaMovie)
			.getResultStream()
			.forEach(System.out::println);
	}
		
}
