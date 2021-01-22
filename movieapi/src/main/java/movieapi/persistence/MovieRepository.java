package movieapi.persistence;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;

// gramatical rules here:
// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation
public interface MovieRepository extends JpaRepository<Movie, Integer>{
	List<Movie> findByTitle(String title);
	List<Movie> findByTitleAndYearGreaterThan(String title, short year);
	// Set<Movie> findByTitleContaining(String title);
	Stream<Movie> findByTitleContaining(String title);
	Stream<Movie> findByDirectorNameEndingWithIgnoreCase(String name);
}
