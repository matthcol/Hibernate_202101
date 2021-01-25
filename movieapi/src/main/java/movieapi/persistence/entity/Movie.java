package movieapi.persistence.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

// @NamedQueries( if several NamedQuery
@NamedQuery(
		name = "get_movie_by_title", 
		query = "select m from Movie m where m.title = :title")
@Entity
@Table(name = "movies")
public class Movie {

	// add id for tracking data in DB
	private Integer id;
	
	private String title;
	private Short year;
	private Short duration;
	private String posterUri;
	
	private Star director;
	
	private List<Star> actors;
	
	public Movie(String title, Short year, Short duration) {
		this();
		this.title = title;
		this.year = year;
		this.duration = duration;
	}
	
	public Movie() {
		// only for insert use cases RAM => DB
		actors = new ArrayList<>();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	//@Column(name="id_movie")
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(length = 300, nullable = false)
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Column(nullable = false)
	public Short getYear() {
		return year;
	}
	public void setYear(Short year) {
		this.year = year;
	}
	
	@Column(nullable = true)
	public Short getDuration() {
		return duration;
	}
	
	public void setDuration(Short duration) {
		this.duration = duration;
	}
	
	@Column(length=300, nullable = true)
	public String getPosterUri() {
		return posterUri;
	}

	public void setPosterUri(String posterUri) {
		this.posterUri = posterUri;
	}

	@ManyToOne //(fetch=FetchType.LAZY)	// fetch = FetchType.Eager by default
	@JoinColumn(name = "id_director", nullable = true)
	public Star getDirector() {
		return director;
	}

	public void setDirector(Star director) {
		this.director = director;
	}
	
	@ManyToMany //(fetch = FetchType.EAGER) // fetch = Lazy
	@JoinTable(
			name="play",
			joinColumns = @JoinColumn(name="id_movie"),
			inverseJoinColumns = @JoinColumn(name="id_actor"))
	public List<Star> getActors() {
		return actors;
	}

	public void setActors(List<Star> actors) {
		this.actors = actors;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(title);
		return builder.append("(")
				.append(year)
				.append(",")
				.append(duration)
				.append(")#")
				.append(id)
				.toString();
	}
	
}
