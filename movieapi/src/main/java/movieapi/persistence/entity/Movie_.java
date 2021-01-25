package movieapi.persistence.entity;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Movie.class)
public class Movie_ {
	 public static volatile SingularAttribute<Movie, Integer> id;
	 public static volatile SingularAttribute<Movie, String> title;
	 public static volatile SingularAttribute<Movie, Short> year;
	 public static volatile SingularAttribute<Movie, Star> director;
	 public static volatile ListAttribute<Movie, Star> actors;
}
