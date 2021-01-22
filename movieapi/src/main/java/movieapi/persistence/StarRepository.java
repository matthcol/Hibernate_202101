package movieapi.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StarRepository extends JpaRepository<Star, Integer> {
	List<Star> findByNameContainingIgnoreCase(String partName);
	List<Star> findByBirthdateIsNull();
	
	@Query("select s from Star s where extract(year from s.birthdate) = :year")
	List<Star> findByBirthdateYear(int year);
}
