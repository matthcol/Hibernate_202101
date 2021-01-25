package movieapi.persistence;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
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
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatcher;
import org.springframework.data.domain.Sort;

import movieapi.persistence.entity.Movie;
import movieapi.persistence.entity.Star;
import movieapi.persistence.repository.MovieRepository;
import movieapi.persistence.repository.StarRepository;

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
	void testMovieByTitlePartSorted() {
		String find = "Man";
		movieRepository.findByTitleContainingOrderByYearDesc(find)
				.forEach(System.out::println);
	}

	@Test
	void testStarByBirthyear() {
		int year = 1930;
		var data = starRepository.findByBirthdateYear(year);
		System.out.println(data);
	}
	
	@Test
	void testStarByBirthyearAndSort() {
		int year = 1930;
		starRepository.findByBirthdateYearAndSort(year, Sort.by("name"))
			.forEach(System.out::println);
		starRepository.findByBirthdateYearAndSort(year, 
				Sort.by(Sort.Order.asc("birthdate"), Sort.Order.desc("name")))
			.forEach(System.out::println);
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
	
	@Test
	void testTitleYearDirectorName() {
		String directorName = "Alfred Hitchcock";
		movieRepository.findByDirectorName(directorName)
			.limit(10)
			//.filter(m -> m.getYear() == 2020)
//			.forEach(System.out::println);
			.forEach(m-> System.out.println(
					m.getTitleUpperCase() 
					+","+ m.getYear() ));
	}
	
	@Test
	void testMovieCount() {
		System.out.println("Nb movies: " + movieRepository.count());
	}
	
	@Test
	void testStarByExample() {
		Star star = new Star();
		star.setName("Steve McQueen");
		Example<Star> example = Example.of(star);
		var data = starRepository.findAll(example);
		System.out.println(data);
		//star.setBirthdate(LocalDate.of(1930, 3, 24));
		var data2 = starRepository.findAll(Example.of(star));
		System.out.println(data2);
		star.setName("mcqueen");
		//star.setBirthdate(LocalDate.of(1930, 8, 25));
		var data3 = starRepository.findAll(
				Example.of(star, 
						ExampleMatcher.matchingAny()
							.withIgnoreCase()
							.withMatcher("name", GenericPropertyMatcher::endsWith)));
		System.out.println(data3);
				
	}
	
	@Test
	void testMoviesByActor() {
		String name = "Daniel Craig";
		movieRepository.findByActorsNameIgnoreCase(name)
			.forEach(System.out::println);
	}
}

