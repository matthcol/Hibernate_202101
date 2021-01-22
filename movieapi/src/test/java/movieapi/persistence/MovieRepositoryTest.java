package movieapi.persistence;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import movieapi.persistence.entity.Movie;
import movieapi.persistence.repository.MovieRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class MovieRepositoryTest {

	@Autowired
	MovieRepository movieRepository;
	
	@Test
	void testCreate() {
		Movie movie = new Movie("Avengers: Endgame", (short) 2019, null);
		movieRepository.saveAndFlush(movie);
		assertNotNull(movie.getId());
	}
	
	@Test
	void testReadExistingMovie() {
		int id = 56869;
		Optional<Movie> movieOpt = movieRepository.findById(id);
		movieOpt.ifPresent(System.out::println);
	}
	
	@Test
	void testReadNonExistingMovie() {
		int id = 0;
		Optional<Movie> movieOpt = movieRepository.findById(id);
		assertTrue(movieOpt.isEmpty());
	}
	
	@Test
	void testReadAll() {
		var movies = movieRepository.findAll();
		movies.stream()
			.limit(10)
			.forEach(System.out::println);
	}

}
