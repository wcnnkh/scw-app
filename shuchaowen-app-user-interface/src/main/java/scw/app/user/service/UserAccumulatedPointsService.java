package scw.app.user.service;

import scw.app.user.pojo.UserAccumulatedPoints;
import scw.result.Result;
import scw.tcc.annotation.Tcc;
import scw.tcc.annotation.TccStage;
import scw.tcc.annotation.TryResult;

/**
 * 用户积分服务
 * @author shuchaowen
 *
 */
public interface UserAccumulatedPointsService {
	UserAccumulatedPoints getUserAccumulatedPoints(long uid);
	
	/**
	 * 修改积分
	 * @param uid
	 * @param change
	 * @param describe
	 * @return 修改失败返回空，修改成功返回日志id
	 */
	@Tcc(cancel="cancelChange")
	String change(long uid, int group, int change, String describe);
	
	/**
	 * 取消修改
	 * @param logId
	 * @return
	 */
	@TccStage
	Result cancelChange(@TryResult String logId);
	
	//Pagination<UserAccumulatedPointsLog> getPagination(long uid, int group, long beginTime, long endTime, int page, int limit);
}
