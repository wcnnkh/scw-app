package scw.app.admin.web;

import scw.app.admin.pojo.AdminRole;
import scw.app.admin.service.AdminRoleGroupActionService;
import scw.app.admin.service.AdminRoleService;
import scw.beans.annotation.Autowired;
import scw.core.instance.annotation.Configuration;
import scw.mvc.HttpChannel;
import scw.mvc.action.Action;
import scw.mvc.action.ActionFilter;
import scw.mvc.annotation.ActionAuthority;
import scw.mvc.security.HttpActionAuthorityManager;
import scw.result.ResultFactory;
import scw.security.authority.http.HttpAuthority;

@Configuration
public class AdminActionFilter implements ActionFilter {
	public static final String ROUTE_ATTR_NAME = "route";

	@Autowired
	private AdminRoleFactory adminRoleFactory;
	@Autowired
	private ResultFactory resultFactory;
	@Autowired
	private AdminRoleGroupActionService adminRoleGroupActionService;
	@Autowired
	private HttpActionAuthorityManager httpActionAuthorityManager;

	public Object doFilter(Action action, HttpChannel httpChannel) throws Throwable {
		AdminLoginRequired adminLoginRequired = action.getAnnotatedElement().getAnnotation(AdminLoginRequired.class);
		ActionAuthority actionAuthority = action.getMethodAnnotatedElement().getAnnotation(ActionAuthority.class);
		if (adminLoginRequired != null || actionAuthority != null) {
			AdminRole adminRole = adminRoleFactory.getAdminRole(httpChannel, action);
			if (adminRole == null) {
				return resultFactory.authorizationFailure();
			}

			if (adminRole.getUserName().equals(AdminRoleService.DEFAULT_ADMIN_NAME)) {
				return action.doAction(httpChannel);
			}

			if (actionAuthority != null) {
				HttpAuthority httpAuthority = httpActionAuthorityManager.getAuthority(action);
				if (httpAuthority == null) {
					httpChannel.getLogger().warn("not found autority: {}", action);
					return action.doAction(httpChannel);
				}
				// 权限判断
				if (adminRoleGroupActionService.check(adminRole.getGroupId(), httpAuthority.getId())) {
					return action.doAction(httpChannel);
				}

				return resultFactory.error("权限不足");
			}
		}
		return action.doAction(httpChannel);
	}

}
