package scw.app.web;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import scw.app.address.model.UserAddressInfo;
import scw.app.address.model.UserAddressModel;
import scw.app.address.pojo.Address;
import scw.app.address.pojo.UserAddress;
import scw.app.address.service.AddressService;
import scw.app.address.service.UserAddressService;
import scw.app.user.pojo.User;
import scw.app.user.security.LoginRequired;
import scw.app.user.service.UserService;
import scw.beans.annotation.Autowired;
import scw.context.result.Result;
import scw.context.result.ResultFactory;
import scw.http.HttpMethod;
import scw.mapper.MapperUtils;
import scw.mvc.annotation.Controller;
import scw.security.session.UserSession;

@Controller(value = "/user/address", methods = { HttpMethod.GET, HttpMethod.POST })
@LoginRequired
public class UserAddressController {
	private UserAddressService userAddressService;
	@Autowired
	private UserService userService;
	@Autowired
	private ResultFactory resultFactory;
	@Autowired
	private AddressService addressService;

	public UserAddressController(UserAddressService userAddressService) {
		this.userAddressService = userAddressService;
	}

	@Controller(value = "info")
	public Result info(UserSession<Long> requestUser, long id) {
		UserAddress userAddress = userAddressService.getById(id);
		if (userAddress == null) {
			return resultFactory.error("用户地址不存在");
		}

		List<Address> list = addressService.getParents(userAddress.getAddressId(), true);
		StringBuilder sb = new StringBuilder();
		for (Address address : list) {
			sb.append(address.getName());
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.putAll(MapperUtils.getMapper().getFieldValueMap(userAddress));
		map.put("address", sb.toString());
		return resultFactory.success(map);
	}

	@Controller(value = "list")
	public Result list(UserSession<Long> requestUser) {
		User user = userService.getUser(requestUser.getUid());
		if (user == null) {
			return resultFactory.error("用户不存在");
		}

		List<UserAddressInfo> list = userAddressService.getUserAddressList(user.getUid());
		if (list == null) {
			list = Collections.emptyList();
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("defaultAddressId", user.getDefaultAddressId());
		map.put("list", list);
		return resultFactory.success(map);
	}

	@Controller(value = "create")
	public Result create(UserSession<Long> requestUser, UserAddressModel userAddressModel) {
		return userAddressService.create(requestUser.getUid(), userAddressModel);
	}

	@Controller(value = "update")
	public Result update(long id, UserSession<Long> requestUser, UserAddressModel userAddressModel) {
		UserAddress userAddress = userAddressService.getById(id);
		if (userAddress == null) {
			return resultFactory.error("地址不存在");
		}

		if (userAddress.getUid() != requestUser.getUid()) {
			return resultFactory.error("非法请求，只能操作自己的地址");
		}

		return userAddressService.update(id, userAddressModel);
	}

	@Controller(value = "update_default_id")
	public Result updateDefaultAddressId(UserSession<Long> requestUser, long id) {
		UserAddress userAddress = userAddressService.getById(id);
		if (userAddress == null) {
			return resultFactory.error("地址不存在");
		}

		if (userAddress.getUid() != requestUser.getUid()) {
			return resultFactory.error("非法请求，只能操作自己的地址");
		}

		return userService.updateDefaultAddressId(requestUser.getUid(), id);
	}
}
