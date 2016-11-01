package zzh.project.stocksystem.interceptor;

import java.util.Collections;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.google.gson.Gson;

import zzh.project.stocksystem.Const;
import zzh.project.stocksystem.ErrorCode;
import zzh.project.stocksystem.domain.User;
import zzh.project.stocksystem.mapper.UserMapper;
import zzh.project.stocksystem.util.LruLinkedHashMap;
import zzh.project.stocksystem.vo.BasicResponse;

/**
 * 简单的access_token校验
 */
@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {
	private static class AccessTokenMapper {
		public long userId;
		public long expiresIn;
	}

	private final Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);

	// 一级缓存
	private final Map<String, AccessTokenMapper> ACCESS_TOKEN_CACHE = Collections
			.synchronizedMap(new LruLinkedHashMap<String, AccessTokenMapper>(50));

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private Gson gson;

	public AuthInterceptor() {
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		logger.debug("preHandle " + request.getRequestURI());
		/*
		 * 从request param中读取access_token进行匹配， 匹配规则遵循 内存 -> 数据
		 * 匹配成功则在请求参数中新增一个用户唯一标记
		 */
		if (request.getAttribute(Const.REQUEST_CONVERT_USER_KEY) != null) {
			return true;
		}
		// 获取request参数中的access_token
		String accessToken = (String) request.getParameter("access_token");
		if (accessToken != null) {
			// 内存缓存
			AccessTokenMapper cacheMapper = ACCESS_TOKEN_CACHE.get(accessToken);
			// 存在于缓存
			if (cacheMapper != null) {
				if (cacheMapper.expiresIn > System.currentTimeMillis()) {
					ACCESS_TOKEN_CACHE.remove(accessToken);
					ACCESS_TOKEN_CACHE.put(accessToken, cacheMapper);
					request.setAttribute(Const.REQUEST_CONVERT_USER_KEY, cacheMapper.userId);
					return true;
				} else {
					ACCESS_TOKEN_CACHE.remove(accessToken);
				}
			} else {
				User user = userMapper.findByAccessToken(accessToken);
				if (user != null && user.getExpiresIn() > System.currentTimeMillis()) {
					AccessTokenMapper tokenMapper = new AccessTokenMapper();
					tokenMapper.expiresIn = user.getExpiresIn();
					tokenMapper.userId = user.get_id();
					ACCESS_TOKEN_CACHE.put(accessToken, tokenMapper);
					request.setAttribute(Const.REQUEST_CONVERT_USER_KEY, user.get_id());
					return true;
				}
			}
		}
		BasicResponse basicResponse = new BasicResponse();
		basicResponse.errcode = ErrorCode.ACCESS_TOKEN_EXPIRES;
		basicResponse.errmsg = "access_token is expires";
		logger.debug("access_token is expires -> " + accessToken);
		response.getWriter().print(gson.toJson(basicResponse));
		return false;
	}

}