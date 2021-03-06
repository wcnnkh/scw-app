package scw.app.address.model;

import java.io.Serializable;

import scw.mapper.MapperUtils;
import scw.sql.orm.annotation.Column;

public class AddressModel implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;
	private String name;
	@Column(comment = "经度")
	private float longitude;
	@Column(comment = "纬度")
	private float latitude;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
	
	@Override
	public String toString() {
		return MapperUtils.getMapper().getFields(AddressModel.class).getValueMap(this).toString();
	}
}
