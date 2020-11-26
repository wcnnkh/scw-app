package scw.app.user.enums;

/**
 * 常见的类型
 * @author asus1
 *
 */
public enum UnionIdType {
	QQ_OPENID(1000),
	WX_OPENID(2000),
	WX_XCX_OPENID(3000),
	WX_UNIONID(4000),
	;
	private final int value;
	
	UnionIdType(int value){
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}

	public static UnionIdType forValue(int value){
		for(UnionIdType type : values()){
			if(type.value == value){
				return type;
			}
		}
		return null;
	}
}
