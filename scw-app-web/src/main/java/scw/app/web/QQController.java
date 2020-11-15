package scw.app.web;

import java.util.Map;

import scw.app.enums.SexType;
import scw.app.user.enums.AccountType;
import scw.app.user.model.UserAttributeModel;
import scw.app.user.pojo.User;
import scw.app.user.security.LoginManager;
import scw.app.user.security.LoginRequired;
import scw.app.user.service.UserService;
import scw.beans.annotation.Autowired;
import scw.core.utils.StringUtils;
import scw.http.HttpMethod;
import scw.http.server.ServerHttpRequest;
import scw.http.server.ServerHttpResponse;
import scw.mvc.annotation.Controller;
import scw.oauth2.AccessToken;
import scw.result.DataResult;
import scw.result.Result;
import scw.result.ResultFactory;
import scw.tencent.qq.connect.QQ;
import scw.tencent.qq.connect.QQRequest;
import scw.tencent.qq.connect.UserInfoResponse;

@Controller(value = "qq", methods = { HttpMethod.GET, HttpMethod.POST })
@scw.mvc.annotation.FactoryResult
public class QQController {
	private QQ qq;
	private UserService userService;
	@Autowired
	private LoginManager loginManager;
	@Autowired
	private ResultFactory resultFactory;
	@Autowired
	private UserControllerService userControllerService;

	public QQController(UserService userService, QQ qq) {
		this.userService = userService;
		this.qq = qq;
	}

	@Controller(value = "login")
	public Result login(String openid, String accessToken, ServerHttpRequest request, ServerHttpResponse response) {
		if (StringUtils.isEmpty(openid, accessToken)) {
			return resultFactory.parameterError();
		}

		User user = userService.getUserByAccount(AccountType.QQ_OPENID, openid);
		if (user == null) {
			UserInfoResponse userinfo = qq.getUserinfo(new QQRequest(accessToken, openid));
			UserAttributeModel userAttributeModel = new UserAttributeModel();
			userAttributeModel.setSex(SexType.forDescribe(userinfo.getGender()));
			userAttributeModel.setHeadImg(userinfo.getfigureUrlQQ1());
			userAttributeModel.setNickname(userinfo.getNickname());
			DataResult<User> dataResult = userService.register(AccountType.QQ_OPENID, openid, null, userAttributeModel);
			if (dataResult.isError()) {
				return dataResult;
			}

			user = dataResult.getData();
		}
		Map<String, Object> infoMap = userControllerService.login(user, request, response);
		return resultFactory.success(infoMap);
	}

	@Controller(value = "web_login")
	public Result webLogin(String code, String redirect_uri, ServerHttpRequest request, ServerHttpResponse response) {
		if (StringUtils.isEmpty(code, redirect_uri)) {
			return resultFactory.parameterError();
		}

		AccessToken accessToken = qq.getAccessToken(redirect_uri, code);
		String openid = qq.getOpenid(accessToken.getAccessToken().getToken());
		return login(openid, accessToken.getAccessToken().getToken(), request, response);
	}

	@LoginRequired
	@Controller(value = "bind")
	public Result bind(long uid, String openid, String accessToken) {
		if (StringUtils.isEmpty(openid, accessToken)) {
			return resultFactory.parameterError();
		}

		User user = userService.getUserByAccount(AccountType.QQ_OPENID, openid);
		if (user == null) {
			return resultFactory.error("用户不存在");
		}
		
		DataResult<User> dataResult = userService.bind(uid, AccountType.QQ_OPENID, openid);
		if(!dataResult.isSuccess()){
			return dataResult;
		}

		UserInfoResponse userinfo = qq.getUserinfo(new QQRequest(accessToken, openid));
		UserAttributeModel userAttributeModel = new UserAttributeModel();
		userAttributeModel.setSex(SexType.forDescribe(userinfo.getGender()));
		userAttributeModel.setHeadImg(userinfo.getfigureUrlQQ1());
		userAttributeModel.setNickname(userinfo.getNickname());
		return userService.updateUserAttribute(uid, userAttributeModel);
	}

	@Controller(value = "web_bind")
	@LoginRequired
	public Result webBind(long uid, String code, String redirect_uri) {
		if (StringUtils.isEmpty(code, redirect_uri)) {
			return resultFactory.parameterError();
		}

		AccessToken accessToken = qq.getAccessToken(redirect_uri, code);
		String openid = qq.getOpenid(accessToken.getAccessToken().getToken());
		return bind(uid, openid, accessToken.getAccessToken().getToken());
	}
}
