package movieapi.persistence;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "stars")
public class Star {
	private Integer id;
	private String name;
	private LocalDate birthdate;
	
	public Star() {
		super();
	}
	public Star(String name, LocalDate birthdate) {
		super();
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
