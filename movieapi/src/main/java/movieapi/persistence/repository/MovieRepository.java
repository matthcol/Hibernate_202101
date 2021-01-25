package movieapi.persistence.repository;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import movieapi.persistence.entity.Movie;
import movieapi.persistence.projection.TitleYear;

// gramatical rules here:
// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation
public interface MovieRepository extends JpaRepository<Movie, Integer>{
	List<Movie> findByTitle(String title);
	List<Movie> findByTitleAndYearGreaterThan(String title, short year);
	// Set<Movie> findByTitleContaining(String title);
	Stream<Movie> findByTitleContaining(String title);
	Stream<Movie> findByTitleContainingOrderByYearDesc(String title);
	Stream<Movie> findByDirectorNameEndingWithIgnoreCase(String name);
	
	//@Query("select m.title, m.year, s.name from Movie m join m.director s")
	Stream<TitleYear> findByDirectorName(String name);
	
	//Stream<Movie> findByActorsNameIgnoreCase(String name);
	Stream<Movie> findByPlaysActorNameIgnoreCase(String name);

}
