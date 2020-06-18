package scw.app.user.pojo;

import java.io.Serializable;

import scw.app.user.pojo.enums.UnionIdType;
import scw.sql.orm.annotation.PrimaryKey;
import scw.sql.orm.annotation.Table;

@Table
public class UnionId implements Serializable{
	private static final long serialVersionUID = 1L;
	@PrimaryKey
	private UnionIdType unionIdType;
	@PrimaryKey
	private String unionId;
	private long uid;
	public UnionIdType getUnionIdType() {
		return unionIdType;
	}
	public void setUnionIdType(UnionIdType unionIdType) {
		this.unionIdType = unionIdType;
	}
	public String getUnionId() {
		return unionId;
	}
	public void setUnionId(String unionId) {
		this.unionId = unionId;
	}
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
}
