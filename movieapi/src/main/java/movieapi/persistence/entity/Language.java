package movieapi.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Language {
	private String name;
	private String isoCode2;
	
	public Language(String name, String isoCode2) {
		super();
		this.name = name;
		this.isoCode2 = isoCode2;
	}
	
	public Language() {}
	
	@Column(name="lang_name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name="lang_code2")
	public String getIsoCode2() {
		return isoCode2;
	}
	public void setIsoCode2(String isoCode2) {
		this.isoCode2 = isoCode2;
	}

}
