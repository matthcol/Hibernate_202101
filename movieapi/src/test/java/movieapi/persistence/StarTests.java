package movieapi.persistence;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import movieapi.persistence.entity.Star;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class StarTests {

	@Autowired
	EntityManager entityManager;
	
	@Test
	void test() {
		String name = "Steve McQueen";
		LocalDate birthdate = LocalDate.of(1930, 3, 24);
		Star star = new Star(name, birthdate);
		entityManager.persist(star);
		assertNotNull(star.getId());
	}

}
