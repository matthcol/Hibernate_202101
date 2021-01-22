package movieapi.persistence.repository;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import movieapi.persistence.entity.Star;

public interface StarRepository extends JpaRepository<Star, Integer> {
	List<Star> findByNameContainingIgnoreCase(String partName);
	List<Star> findByBirthdateIsNull();
	
	@Query("select s from Star s where extract(year from s.birthdate) = :year")
	List<Star> findByBirthdateYear(int year);
	
	@Query("select s from Star s where extract(year from s.birthdate) = :year")
	Stream<Star> findByBirthdateYearAndSort(int year, Sort sort);
}
