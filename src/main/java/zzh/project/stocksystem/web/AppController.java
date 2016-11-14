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
import zzh.project.stocksystem.exception.StockSystemException;
import zzh.project.stocksystem.service.UserManager;
import zzh.project.stocksystem.util.StringUtil;
import zzh.project.stocksystem.vo.BasicResponse;
import zzh.project.stocksystem.vo.AccountBean;
import zzh.project.stocksystem.vo.FavorBean;
import zzh.project.stocksystem.vo.StockBean;
import zzh.project.stocksystem.vo.TradeBean;
import zzh.project.stocksystem.vo.UserBean;

/**
 * 简单的业务接口暴露，没用作后端校验
 */

@Controller
@RequestMapping("/app")
public class AppController {
	private final Logger logger = LoggerFactory.getLogger(AppController.class);
	@Autowired
	private UserManager userManager;
	@Autowired
	private Gson gson;

	@RequestMapping(value = "/login", produces = { "application/json;charset=UTF-8" })
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
				// 生成一个access_token
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

	@RequestMapping(value = "/register", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public BasicResponse register(@RequestBody UserBean user) {
		logger.debug("register req " + user);
		BasicResponse response = new BasicResponse();
		response.errcode = ErrorCode.SUCCESS;
		if (user != null) {
			logger.debug("register info ->" + gson.toJson(user));
			try {
				userManager.register(user);
			} catch (StockSystemException e) {
				response.errcode = e.getErrorCode();
				response.errmsg = e.getMessage();
			}
		}
		return response;
	}

	@RequestMapping(value = "/favor", produces = { "application/json;charset=UTF-8" })
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

	@RequestMapping(value = "/unFavor", produces = { "application/json;charset=UTF-8" })
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

	@RequestMapping(value = "/listFavor", produces = { "application/json;charset=UTF-8" })
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

	@RequestMapping(value = "/getInfo", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String getInfo(HttpServletRequest request) {
		long userId = (long) request.getAttribute(Const.REQUEST_CONVERT_USER_KEY);
		BasicResponse response = new BasicResponse();
		UserBean bean = userManager.getUserInfo(userId);
		response.errcode = ErrorCode.SUCCESS;
		response.data = bean;
		logger.debug("getInfo resp " + response);
		return gson.toJson(response);
	}

	@RequestMapping(value = "/recharge", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String recharge(HttpServletRequest request, @RequestBody String body) {
		long userId = (long) request.getAttribute(Const.REQUEST_CONVERT_USER_KEY);
		JsonObject json = new JsonParser().parse(body).getAsJsonObject();
		String cardNum = json.get("cardNum").getAsString();
		String password = json.get("password").getAsString();
		float money = json.get("money").getAsFloat();
		BasicResponse response = new BasicResponse();
		response.errcode = ErrorCode.SUCCESS;
		try {
			userManager.recharge(userId, cardNum, password, money);
		} catch (StockSystemException e) {
			response.errcode = e.getErrorCode();
			response.errmsg = e.getMessage();
		}
		logger.debug("recharge resp " + response);
		return gson.toJson(response);
	}

	@RequestMapping(value = "/bind", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String bindAccount(HttpServletRequest request, @RequestBody AccountBean accountBean) {
		long userId = (long) request.getAttribute(Const.REQUEST_CONVERT_USER_KEY);
		BasicResponse response = new BasicResponse();
		response.errcode = ErrorCode.SUCCESS;
		try {
			userManager.bindAccount(userId, accountBean);
		} catch (StockSystemException e) {
			response.errcode = e.getErrorCode();
			response.errmsg = e.getMessage();
		}
		logger.debug("bindAccount resp " + response);
		return gson.toJson(response);
	}

	@RequestMapping(value = "/getAccount", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String getAccount(HttpServletRequest request) {
		long userId = (long) request.getAttribute(Const.REQUEST_CONVERT_USER_KEY);
		BasicResponse response = new BasicResponse();
		response.errcode = ErrorCode.SUCCESS;
		AccountBean bean = userManager.getAccountInfo(userId);
		if (bean == null) {
			response.errcode = ErrorCode.NOT_EXITS;
			response.errmsg = "尚未绑定";
		} else {
			response.data = bean;
		}
		logger.debug("getAccount resp " + response);
		return gson.toJson(response);
	}

	@RequestMapping(value = "/listTrade", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String listTrade(HttpServletRequest request) {
		long userId = (long) request.getAttribute(Const.REQUEST_CONVERT_USER_KEY);
		BasicResponse response = new BasicResponse();
		response.errcode = ErrorCode.SUCCESS;
		List<TradeBean> beans = userManager.listTrade(userId);
		response.data = beans;
		logger.debug("listTrade resp " + response);
		return gson.toJson(response);
	}

	@RequestMapping(value = "/buy", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String buy(HttpServletRequest request, @RequestBody String body) {
		long userId = (long) request.getAttribute(Const.REQUEST_CONVERT_USER_KEY);
		JsonObject json = new JsonParser().parse(body).getAsJsonObject();
		String gid = json.get("gid").getAsString();
		String name = json.get("name").getAsString();
		float uPrice = json.get("uPrice").getAsFloat();
		int amount = json.get("amount").getAsInt();
		BasicResponse response = new BasicResponse();
		response.errcode = ErrorCode.SUCCESS;
		try {
			userManager.buy(userId, gid, name, uPrice, amount);
		} catch (StockSystemException e) {
			response.errcode = e.getErrorCode();
			response.errmsg = e.getMessage();
		}
		logger.debug("buy resp " + response);
		return gson.toJson(response);
	}

	@RequestMapping(value = "/sell", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String sell(HttpServletRequest request, @RequestBody String body) {
		long userId = (long) request.getAttribute(Const.REQUEST_CONVERT_USER_KEY);
		JsonObject json = new JsonParser().parse(body).getAsJsonObject();
		String gid = json.get("gid").getAsString();
		String name = json.get("name").getAsString();
		float uPrice = json.get("uPrice").getAsFloat();
		int amount = json.get("amount").getAsInt();
		BasicResponse response = new BasicResponse();
		response.errcode = ErrorCode.SUCCESS;
		try {
			userManager.sell(userId, gid, name, uPrice, amount);
		} catch (StockSystemException e) {
			response.errcode = e.getErrorCode();
			response.errmsg = e.getMessage();
		}
		logger.debug("sell resp " + response);
		return gson.toJson(response);
	}

	@RequestMapping(value = "/listStock", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String listStock(HttpServletRequest request) {
		long userId = (long) request.getAttribute(Const.REQUEST_CONVERT_USER_KEY);
		BasicResponse response = new BasicResponse();
		response.errcode = ErrorCode.SUCCESS;
		List<StockBean> beans = userManager.listStock(userId);
		response.data = beans;
		logger.debug("listStock resp " + response);
		return gson.toJson(response);
	}

	@RequestMapping(value = "/check", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String check() {
		BasicResponse response = new BasicResponse();
		response.errcode = ErrorCode.SUCCESS;
		return gson.toJson(response);
	}
};