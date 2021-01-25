package movieapi.persistence.entity;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Star.class)
public class Star_ {
	 public static volatile SingularAttribute<Star, Integer> id;
	 public static volatile SingularAttribute<Star, String> name;
}
