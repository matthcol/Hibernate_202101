package movieapi.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="play")
public class Play {
	// id of association
	private Integer id;
	
	// associated entities
	private Movie movie;
	private Star actor;
	
	// association property
	private String role;
	
	public Play() {}
	
	public Play(Movie movie, Star actor, String role) {
		super();
		this.movie = movie;
		this.actor = actor;
		this.role = role;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne
		@JoinColumn(name="id_movie")
	public Movie getMovie() {
		return movie;
	}
	
	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	@ManyToOne
		@JoinColumn(name="id_actor")
	public Star getActor() {
		return actor;
	}

	public void setActor(Star actor) {
		this.actor = actor;
	}

	@Column(nullable = true)
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	
}
