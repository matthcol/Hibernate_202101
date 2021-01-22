package movieapi.persistence.projection;

public interface TitleYear {
	String getTitle();
	Short getYear();
	
	default String getTitleUpperCase() {
		return getTitle().toUpperCase();
	}
}
