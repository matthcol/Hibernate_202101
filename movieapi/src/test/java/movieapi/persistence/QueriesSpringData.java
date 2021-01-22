package movieapi.persistence;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
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

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class QueriesSpringData {

	@Autowired
	MovieRepository movieRepository; 
	
	@Autowired
	StarRepository starRepository; 
	
	@ParameterizedTest
	@ValueSource(ints = {2379713, 0})
	void testMovieById(int id) {
		var data = movieRepository.findById(id);
		System.out.println(data);
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"Spectre", "The Man Who Knew Too Much", "Dummy"} )
	void testMovieByTitle(String title) { 
		var data = movieRepository.findByTitle(title);
		System.out.println(data);
	}
	
	@Test
	void testMovieByTitleAndYearGreater() {
		String title = "The Man Who Knew Too Much";
		short year = 1950;
		var data = movieRepository.findByTitleAndYearGreaterThan(title, year);
		System.out.println(data);
	}
	
	@Test
	void testMovieByTitlePart() {
		String find = "Man";
		var data = movieRepository.findByTitleContaining(find)
				.map(Movie::getTitle)
				.collect(Collectors.joining(", "));
		System.out.println(data);
	}

	@Test
	void testStarByBirthyear() {
		int year = 1930;
		var data = starRepository.findByBirthdateYear(year);
		System.out.println(data);
	}
	
	@Test
	void testStarByBirthyearNull() {
		var data = starRepository.findByBirthdateIsNull();
		System.out.println("Star without birthdate: " + data.size());
		data.stream()
			.findFirst()
			.ifPresent(s -> System.out.println("First star: " + s));
	}
//	
//	@Test
//	void testMovieLast2years() {
//		// TODO year ambigu
//		var data = movieRepository.createQuery(
//				"select m from Movie m where m.year > extract(year from current_date)-2", 
//				Movie.class)
//			.getResultList();
//		System.out.println(data);
//	}
//	
	@ParameterizedTest
	@ValueSource(strings = {"Tarantino", "tarantino", "Quentin", "quentin"})
	void testStarByPartNameCaseInsensitive(String partName) {
		var data = starRepository.findByNameContainingIgnoreCase(partName);
		System.out.println(data);
	}
	
	@Test
	void testMovieByDirectorName() {
		String directorName = "Alfred Hitchcock";
		movieRepository.findByDirectorNameEndingWithIgnoreCase(directorName)
			.forEach(System.out::println);
	}
}

