package scw.app.admin.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import scw.app.user.model.PermissionGroupInfo;
import scw.app.user.pojo.PermissionGroup;
import scw.app.user.pojo.PermissionGroupAction;
import scw.app.user.pojo.User;
import scw.app.user.security.LoginRequired;
import scw.app.user.service.PermissionGroupActionService;
import scw.app.user.service.PermissionGroupService;
import scw.app.user.service.UserService;
import scw.beans.annotation.Autowired;
import scw.context.result.Result;
import scw.context.result.ResultFactory;
import scw.core.utils.StringUtils;
import scw.http.HttpMethod;
import scw.mvc.annotation.ActionAuthority;
import scw.mvc.annotation.ActionAuthorityParent;
import scw.mvc.annotation.Controller;
import scw.mvc.page.Page;
import scw.mvc.page.PageFactory;
import scw.mvc.security.HttpActionAuthorityManager;
import scw.security.authority.http.HttpAuthority;
import scw.security.session.UserSession;

@LoginRequired
@ActionAuthorityParent(AdminUserController.class)
@Controller(value = "admin")
public class PermissionGroupController {
	private PermissionGroupService permissionGroupService;
	private PermissionGroupActionService permissionGroupActionService;
	@Autowired
	private ResultFactory resultFactory;
	private UserService userService;
	@Autowired
	private PageFactory pageFactory;
	@Autowired
	private HttpActionAuthorityManager httpActionAuthorityManager;

	public PermissionGroupController(PermissionGroupService permissionGroupService,
			PermissionGroupActionService permissionGroupActionService, UserService userService) {
		this.permissionGroupService = permissionGroupService;
		this.permissionGroupActionService = permissionGroupActionService;
		this.userService = userService;
	}

	@ActionAuthority(value = "权限组列表", menu = true)
	@Controller(value = "group_list")
	public Page group_list(UserSession<Long> requestUser, Integer parentId) {
		User user = userService.getUser(requestUser.getUid());
		int pid = (parentId == null || parentId == 0) ? user.getPermissionGroupId() : parentId;
		Page page = pageFactory.getPage("/admin/ftl/group_list.ftl");
		page.put("groupList", permissionGroupService.getSubList(pid, false));
		page.put("parentId", pid);
		page.put("parentGroup", permissionGroupService.getById(pid));
		page.put("isTop", user.getPermissionGroupId() == pid);// 是否是最上级了
		return page;
	}

	@ActionAuthority(value = "(查看或修改)权限界面")
	@Controller(value = "group_view")
	public Object group_view(int parentId, int id) {
		Page page = pageFactory.getPage("/admin/ftl/group_view.ftl");
		page.put("parentId", parentId);
		page.put("group", permissionGroupService.getById(id));
		return page;
	}

	@LoginRequired
	@Controller(value = "group_action_list")
	public Result action_list(int groupId, UserSession<Long> requestUser, int parentId) {
		List<HttpAuthority> httpAuthorities;
		if (parentId == 0 && userService.isSuperAdmin(requestUser.getUid())) {
			httpAuthorities = httpActionAuthorityManager.getAuthorityList(null);
		} else {
			User user = userService.getUser(requestUser.getUid());
			int id = parentId == 0 ? user.getPermissionGroupId() : parentId;
			List<PermissionGroupAction> actions = permissionGroupActionService.getActionList(id);
			httpAuthorities = new ArrayList<HttpAuthority>();
			for (PermissionGroupAction action : actions) {
				HttpAuthority httpAuthority = httpActionAuthorityManager.getAuthority(action.getActionId());
				if (httpAuthority == null) {
					continue;
				}

				httpAuthorities.add(httpAuthority);
			}
		}

		HashSet<String> selecteds = new HashSet<String>();
		List<PermissionGroupAction> actions = permissionGroupActionService.getActionList(groupId);
		for (PermissionGroupAction action : actions) {
			selecteds.add(action.getActionId());
		}

		List<Object> list = new ArrayList<Object>();
		for (HttpAuthority httpAuthority : httpAuthorities) {
			Map<String, Object> map = new HashMap<String, Object>(8);
			map.put("id", httpAuthority.getId());
			map.put("pId", httpAuthority.getParentId());
			map.put("name", httpAuthority.getName());
			map.put("checked", selecteds.contains(httpAuthority.getId()));
			list.add(map);
		}
		return resultFactory.success(list);
	}

	@ActionAuthority(value = "(添加/修改)权限")
	@Controller(value = "group_add_or_update", methods = HttpMethod.POST)
	public Result group_add_or_update(UserSession<Long> requestUser, int id, int parentId, String name, boolean disable,
			String ids) {
		PermissionGroupInfo info = new PermissionGroupInfo();
		info.setId(id);
		info.setDisable(disable);
		info.setAuthorityIds(StringUtils.splitList(String.class, ids, ",", true));
		info.setName(name);

		PermissionGroup group = permissionGroupService.getById(id);
		if (group == null) {
			info.setParentId(parentId);
		} else {
			info.setParentId(group.getParentId());
		}
		return permissionGroupService.createOrUpdate(info);
	}
}
