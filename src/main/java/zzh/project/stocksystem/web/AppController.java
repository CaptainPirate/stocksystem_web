package zzh.project.stocksystem.web;

import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import zzh.project.stocksystem.Const;
import zzh.project.stocksystem.ErrorCode;
import zzh.project.stocksystem.service.UserManager;
import zzh.project.stocksystem.util.StringUtil;
import zzh.project.stocksystem.vo.BasicResponse;
import zzh.project.stocksystem.vo.FavorBean;
import zzh.project.stocksystem.vo.UserBean;

@Controller
@RequestMapping("/app")
public class AppController {
	private final Logger logger = LoggerFactory.getLogger(AppController.class);
	@Autowired
	private UserManager userManager;
	@Autowired
	private Gson gson;

	@RequestMapping(value = "/login")
	@ResponseBody
	public String login(@RequestBody String body) {
		logger.debug("login req " + body);
		JsonObject json = new JsonParser().parse(body).getAsJsonObject();
		String username = json.get("username").getAsString();
		String password = json.get("password").getAsString();
		BasicResponse response = new BasicResponse();
		if (username != null && password != null) {
			boolean validResult = userManager.validLogin(username, password);
			if (validResult) {
				String accessToken = StringUtil.getRandomString(15);
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
				long expiresIn = calendar.getTimeInMillis();
				userManager.activeAccessToken(username, accessToken, expiresIn);
				JsonObject accessTokenJson = new JsonObject();
				accessTokenJson.addProperty("access_token", accessToken);
				accessTokenJson.addProperty("expires", expiresIn);
				response.data = accessTokenJson;
			} else {
				response.errcode = ErrorCode.PASS_WRONG;
				response.errmsg = "pass wrong";
			}
		} else {
			response.errcode = ErrorCode.ARGUMENT_INVALID;
			response.errmsg = "invalid argument";
		}
		logger.debug("register resp " + response);
		return gson.toJson(response);
	}

	@RequestMapping("/register")
	@ResponseBody
	public BasicResponse register(@RequestBody UserBean user) {
		logger.debug("register req " + user);
		BasicResponse response = new BasicResponse();
		if (user != null) {
			logger.debug("register info ->" + gson.toJson(user));
			boolean result = userManager.register(user);
			if (!result) {
				response.errcode = ErrorCode.USERNAME_ALREADY_EXISTS;
				response.errmsg = "user is already exists";
			}
		}
		return response;
	}

	@RequestMapping(value = "/favor")
	@ResponseBody
	public String favor(HttpServletRequest request, @RequestBody String body) {
		logger.debug("favor req " + body);
		long userId = (long) request.getAttribute(Const.REQUEST_CONVERT_USER_KEY);
		JsonObject json = new JsonParser().parse(body).getAsJsonObject();
		String gid = json.get("gid").getAsString();
		BasicResponse response = new BasicResponse();
		userManager.favor(userId, gid);
		response.errcode = ErrorCode.SUCCESS;
		logger.debug("register resp " + response);
		return gson.toJson(response);
	}

	@RequestMapping(value = "/unfavor")
	@ResponseBody
	public String unfavor(HttpServletRequest request, @RequestBody String body) {
		logger.debug("unfavor req " + body);
		long userId = (long) request.getAttribute(Const.REQUEST_CONVERT_USER_KEY);
		JsonObject json = new JsonParser().parse(body).getAsJsonObject();
		String gid = json.get("gid").getAsString();
		BasicResponse response = new BasicResponse();
		userManager.cancelFavor(userId, gid);
		response.errcode = ErrorCode.SUCCESS;
		logger.debug("register resp " + response);
		return gson.toJson(response);
	}

	@RequestMapping(value = "/listfavor")
	@ResponseBody
	public String listFavor(HttpServletRequest request, @RequestBody String body) {
		logger.debug("unfavor req " + body);
		long userId = (long) request.getAttribute(Const.REQUEST_CONVERT_USER_KEY);
		BasicResponse response = new BasicResponse();
		List<FavorBean> favorBeans = userManager.listFavor(userId);
		response.errcode = ErrorCode.SUCCESS;
		response.data = favorBeans;
		logger.debug("listFavor resp " + response);
		return gson.toJson(response);
	}

	@RequestMapping(value = "/check")
	@ResponseBody
	public String check() {
		BasicResponse response = new BasicResponse();
		response.errcode = ErrorCode.SUCCESS;
		return gson.toJson(response);
	}
};