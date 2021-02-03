package scw.app.address.service;

import java.util.List;

import scw.app.address.model.UserAddressInfo;
import scw.app.address.model.UserAddressModel;
import scw.app.address.pojo.UserAddress;
import scw.context.result.DataResult;

/**
 * 用户地址管理
 * @author shuchaowen
 *
 */
public interface UserAddressService {
	UserAddress getById(long id);
	
	UserAddressInfo getUserAddressInfo(long id);

	/**
	 * 获取用户地址列表
	 * @param uid
	 * @return
	 */
	List<UserAddressInfo> getUserAddressList(long uid);

	/**
	 * 创建一个用户地址
	 * @param uid
	 * @param userAddressModel
	 * @return
	 */
	DataResult<UserAddressInfo> create(long uid, UserAddressModel userAddressModel);

	/**
	 * 此方法未检验uid
	 * @param id
	 * @param userAddressModel
	 * @return
	 */
	DataResult<UserAddressInfo> update(long id, UserAddressModel userAddressModel);
}
