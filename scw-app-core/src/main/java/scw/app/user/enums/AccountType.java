package scw.app.user.enums;

import scw.app.user.pojo.User;
import scw.mapper.Field;
import scw.mapper.FieldFeature;
import scw.mapper.MapperUtils;

public enum AccountType {
	USERNAME("username"), 
	PHONE("phone"),
	EMAIL("email"), 
	;
	
	private final String fieldName;
	
	AccountType(String fieldName){
		this.fieldName = fieldName;
	}
	
	public String getFieldName() {
		return fieldName;
	}

	public Field getField(){
		return MapperUtils.getMapper().getFields(User.class).accept(FieldFeature.EXISTING_GETTER_FIELD).find(fieldName, String.class);
	}

	public String getAccount(User user) {
		return (String) getField().getGetter().get(user);
	}

	public void setAccount(User user, String account) {
		getField().getSetter().set(user, account);
	}
}
