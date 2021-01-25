package movieapi.persistence.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "stars")
public class Star {
	private Integer id;
	private String name;
	private LocalDate birthdate;
	
	private List<Movie> directedMovies;
	private List<Movie> playedMovies;
	
	public Star() {
		directedMovies = new ArrayList<>();
		playedMovies = new ArrayList<>();
	}
	
	public Star(String name, LocalDate birthdate) {
		this();
		this.name = name;
		this.birthdate = birthdate;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	//@Column(name="id_star")
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public LocalDate getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(LocalDate birthdate) {
		this.birthdate = birthdate;
	}
	
	@OneToMany(mappedBy = "director") // lazy
	public List<Movie> getDirectedMovies() {
		return directedMovies;
	}

	public void setDirectedMovies(List<Movie> directedMovies) {
		this.directedMovies = directedMovies;
	}

	@ManyToMany(mappedBy = "actors") // lazy
	public List<Movie> getPlayedMovies() {
		return playedMovies;
	}

	public void setPlayedMovies(List<Movie> playedMovies) {
		this.playedMovies = playedMovies;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		return builder.append(name)
				.append("(")
				.append(birthdate)
				.append(")#")
				.append(id)
				.toString();
	}
}
