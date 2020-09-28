package scw.app.user.model;

import java.io.Serializable;
import java.sql.Date;
import java.util.Calendar;

import scw.app.enums.SexType;
import scw.core.utils.StringUtils;
import scw.mapper.MapperUtils;
import scw.util.CalendarUtils;

public class UserAttributeModel implements Serializable {
	private static final long serialVersionUID = 1L;
	private Date birthday;
	private SexType sex;
	private int age;
	// 创建age时候用的时间
	private long createAgeTime;

	private String nickname;
	private String headImg;

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public SexType getSex() {
		return sex;
	}

	public void setSex(SexType sex) {
		this.sex = sex;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	public long getCreateAgeTime() {
		return createAgeTime;
	}

	public void setCreateAgeTime(long createAgeTime) {
		this.createAgeTime = createAgeTime;
	}

	/**
	 * 获取实际年龄
	 * 
	 * @return
	 */
	public int getActualAge() {
		if (birthday != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(birthday);
			return CalendarUtils.getDayBeginCalendar(System.currentTimeMillis()).get(Calendar.YEAR)
					- calendar.get(Calendar.YEAR);
		} else {
			if (createAgeTime <= 0) {
				return age;
			}

			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(createAgeTime);
			return CalendarUtils.getDayBeginCalendar(System.currentTimeMillis()).get(Calendar.YEAR)
					- calendar.get(Calendar.YEAR) + age;
		}
	}

	@Override
	public String toString() {
		return MapperUtils.getMapper().toString(this);
	}

	public void writeTo(UserAttributeModel userAttributeModel) {
		if (birthday != null) {
			userAttributeModel.setBirthday(birthday);
		}

		if (sex != null) {
			userAttributeModel.setSex(sex);
		}

		if (age != 0 && age != userAttributeModel.getAge()) {
			userAttributeModel.setAge(age);
		}

		if (StringUtils.isNotEmpty(nickname)) {
			userAttributeModel.setNickname(nickname);
		}

		if (StringUtils.isNotEmpty(headImg)) {
			userAttributeModel.setHeadImg(headImg);
		}

		if (createAgeTime != 0) {
			userAttributeModel.setCreateAgeTime(createAgeTime);
		}
	}
}
