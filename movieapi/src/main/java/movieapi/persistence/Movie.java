package movieapi.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

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
	


	public Movie(String title, Short year, Short duration) {
		super();
		this.title = title;
		this.year = year;
		this.duration = duration;
	}
	
	public Movie() {
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

	@ManyToOne
	@JoinColumn(name = "id_director")
	public Star getDirector() {
		return director;
	}

	public void setDirector(Star director) {
		this.director = director;
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