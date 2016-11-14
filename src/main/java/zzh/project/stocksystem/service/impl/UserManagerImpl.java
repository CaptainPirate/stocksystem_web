package zzh.project.stocksystem.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;
import zzh.project.stocksystem.ErrorCode;
import zzh.project.stocksystem.domain.Account;
import zzh.project.stocksystem.domain.Favor;
import zzh.project.stocksystem.domain.Stock;
import zzh.project.stocksystem.domain.Trade;
import zzh.project.stocksystem.domain.User;
import zzh.project.stocksystem.exception.StockSystemException;
import zzh.project.stocksystem.mapper.AccountMapper;
import zzh.project.stocksystem.mapper.FavorMapper;
import zzh.project.stocksystem.mapper.StockMapper;
import zzh.project.stocksystem.mapper.TradeMapper;
import zzh.project.stocksystem.mapper.UserMapper;
import zzh.project.stocksystem.service.UserManager;
import zzh.project.stocksystem.util.BeanConvert;
import zzh.project.stocksystem.vo.AccountBean;
import zzh.project.stocksystem.vo.FavorBean;
import zzh.project.stocksystem.vo.StockBean;
import zzh.project.stocksystem.vo.TradeBean;
import zzh.project.stocksystem.vo.UserBean;

@Service
@Transactional(rollbackFor = Throwable.class)
public class UserManagerImpl implements UserManager {
	private final Logger logger = LoggerFactory.getLogger(UserManagerImpl.class);

	@Autowired
	private UserMapper userMapper;
	@Autowired
	private FavorMapper favorMapper;
	@Autowired
	private AccountMapper accountMapper;
	@Autowired
	private TradeMapper tradeMapper;
	@Autowired
	private StockMapper stockMapper;
	@Autowired
	JPushClient jPushClient;

	private Timer timer = new Timer(true);
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public void register(UserBean user) throws StockSystemException {
		User localUser = userMapper.findByUsername(user.username);
		if (localUser != null) {
			throw new StockSystemException("用户名已存在", ErrorCode.ALREADY_EXISTS);
		}
		localUser = userMapper.findByNick(user.nick);
		if (localUser != null) {
			throw new StockSystemException("该昵称已被注册", ErrorCode.ALREADY_EXISTS);
		}
		localUser = userMapper.findByEmail(user.email);
		if (localUser != null) {
			throw new StockSystemException("该邮箱已被使用", ErrorCode.ALREADY_EXISTS);
		}
		userMapper.save(BeanConvert.convert(user));
	}

	@Override
	public boolean validLogin(String username, String password) {
		User user = userMapper.findByUsername(username);
		if (user != null && user.getPassword().equals(password)) {
			return true;
		}
		return false;
	}

	@Override
	public void activeAccessToken(String username, String accessToken, long expiresIn) {
		User user = userMapper.findByUsername(username);
		if (user != null) {
			user.setAccessToken(accessToken);
			user.setExpiresIn(expiresIn);
			userMapper.update(user);
		}
	}

	@Override
	public void favor(Long userId, String gid) {
		Favor favor = favorMapper.findByUserIdAndStockCode(userId, gid);
		if (favor == null) {
			// TODO .. 校验股票是否存在
			favor = new Favor();
			favor.setStockCode(gid);
			favor.setUserId(userId);
			favorMapper.save(favor);
		}
	}

	@Override
	public void cancelFavor(Long userId, String gid) {
		Favor favor = favorMapper.findByUserIdAndStockCode(userId, gid);
		if (favor != null) {
			favorMapper.delete(favor.get_id());
		}
	}

	@Override
	public List<FavorBean> listFavor(Long userId) {
		List<Favor> favors = favorMapper.findByUserId(userId);
		List<FavorBean> result = new ArrayList<>(favors.size());
		for (Favor favor : favors) {
			FavorBean bean = new FavorBean();
			bean.gid = favor.getStockCode();
			result.add(bean);
		}
		return result;
	}

	@Override
	public UserBean getUserInfo(Long userId) {
		User user = userMapper.get(userId);
		if (user != null) {
			UserBean bean = new UserBean();
			bean.username = user.getUsername();
			bean.email = user.getEmail();
			bean.nick = user.getNick();
			bean.balance = user.getBalance();
			return bean;
		}
		return null;
	}

	@Override
	public void recharge(Long userId, String carNum, String password, float money) throws StockSystemException {
		Account account = accountMapper.findByUserId(userId);
		if (account != null) {
			if (account.getUserId().equals(userId)) {
				if (account.getPassword().equals(password)) {
					User user = userMapper.get(userId);
					user.setBalance(user.getBalance() + money);
					userMapper.update(user);
				} else {
					throw new StockSystemException("支付密码错误", ErrorCode.PASS_WRONG);
				}
			} else {
				throw new StockSystemException("该支付账号已被绑定", ErrorCode.ALREADY_EXISTS);
			}
		} else {
			throw new StockSystemException("该支付账号不存在", ErrorCode.NOT_EXITS);
		}
	}

	@Override
	public void bindAccount(Long userId, AccountBean cardBean) throws StockSystemException {
		Account account = accountMapper.findByCardNum(cardBean.cardNum);
		if (account != null) {
			throw new StockSystemException("该支付账号已被绑定", ErrorCode.ALREADY_EXISTS);
		}
		account = accountMapper.findByUserId(userId);
		if (account != null) {
			throw new StockSystemException("已绑定支付账号", ErrorCode.ALREADY_EXISTS);
		}
		User user = userMapper.get(userId);
		if (user != null) {
			account = new Account();
			account.setCardNum(cardBean.cardNum);
			account.setIdNum(cardBean.idNum);
			account.setRealName(cardBean.realName);
			account.setPassword(cardBean.password);
			account.setUserId(userId);
			accountMapper.save(account);
		} else {
			throw new StockSystemException("该用户不存在", ErrorCode.NOT_EXITS);
		}
	}

	@Override
	public AccountBean getAccountInfo(Long userId) {
		Account account = accountMapper.findByUserId(userId);
		if (account != null) {
			AccountBean bean = new AccountBean();
			bean.cardNum = account.getCardNum();
			bean.idNum = account.getIdNum();
			bean.realName = account.getRealName();
			System.out.println(bean.cardNum);
			return bean;
		}
		return null;
	}

	@Override
	public List<TradeBean> listTrade(Long userId) {
		List<Trade> trades = tradeMapper.findByUserId(userId);
		List<TradeBean> beans = new ArrayList<>(trades.size());
		for (Trade trade : trades) {
			TradeBean bean = new TradeBean();
			bean.stockCode = trade.getStockCode();
			bean.stockName = trade.getStockName();
			bean.type = trade.getTradeType() == 0 ? "sell" : "buy";
			bean.status = trade.getStatus();
			bean.uPrice = trade.getuPrice() + "";
			bean.amount = trade.getAmount();
			bean.date = dateFormat.format(trade.getTradeIn());
			beans.add(bean);
		}
		return beans;
	}

	@Override
	public void buy(Long userId, String gid, String name, float uPrice, int amount) throws StockSystemException {
		// 购入后会优先从余额中扣除，但不会直接转入持有股票，需要后台审核
		User user = userMapper.get(userId);
		if (user != null) {
			if (user.getBalance() < uPrice * amount) {
				throw new StockSystemException("余额不足", ErrorCode.BALANCE_NOT_ENOUGH);
			}
			user.setBalance(user.getBalance() - uPrice * amount);
			userMapper.update(user);

			Trade trade = new Trade();
			trade.setStockCode(gid);
			trade.setStockName(name);
			trade.setUserId(userId);
			trade.setTradeType(1);
			trade.setStatus(0);
			trade.setAmount(amount);
			trade.setuPrice(uPrice);
			trade.setTradeIn(new Date());
			tradeMapper.save(trade);

			// 这里模拟30秒后自动审核
			autoVerified(trade.get_id(), 1000 * 30);
		}
	}

	@Override
	public void sell(Long userId, String gid, String name, float uPrice, int amount) throws StockSystemException {
		// 卖出后，从持有股票中移除相应数量，但余额不会马上转入，需要先审核
		User user = userMapper.get(userId);
		if (user != null) {
			Stock stock = stockMapper.findByUserIdAndStockCode(userId, gid);
			if (stock == null || stock.getTotal() < amount) {
				throw new StockSystemException("持有股票不足", ErrorCode.NOT_EXITS);
			} else {
				stock.setTotal(stock.getTotal() - amount);
				if (stock.getTotal() == 0) {
					stockMapper.delete(stock.get_id());
				} else {
					stockMapper.update(stock);
				}
			}

			Trade trade = new Trade();
			trade.setStockCode(gid);
			trade.setStockName(name);
			trade.setUserId(userId);
			trade.setTradeType(0);
			trade.setStatus(0);
			trade.setAmount(amount);
			trade.setuPrice(uPrice);
			trade.setTradeIn(new Date());
			tradeMapper.save(trade);

			// 这里模拟30秒后自动审核
			autoVerified(trade.get_id(), 1000 * 30);
		}
	}

	private void autoVerified(long tradeId, long duration) {
		// 这里模拟30秒后自动审核
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				verified(tradeId);
			}
		};

		try {
			timer.schedule(task, duration);
		} catch (IllegalStateException e) {
			timer = new Timer(true);
			timer.schedule(task, duration);
		}
	}

	private void verified(Long tradeId) {
		Trade trade = tradeMapper.get(tradeId);
		if (trade != null) {
			User user = userMapper.get(trade.getUserId());
			if (trade.getTradeType() == 0) { // 卖出
				user.setBalance(user.getBalance() + trade.getuPrice() * trade.getAmount());
				userMapper.update(user);
			}

			Stock stock = stockMapper.findByUserIdAndStockCode(trade.getUserId(), trade.getStockCode());
			if (trade.getTradeType() == 1) {
				if (stock != null) {
					stock.setTotal(stock.getTotal() + trade.getAmount());
					stockMapper.update(stock);
				} else {
					stock = new Stock();
					stock.setStockCode(trade.getStockCode());
					stock.setTotal(trade.getAmount());
					stock.setUserId(trade.getUserId());
					stockMapper.save(stock);
				}
			}
			trade.setStatus(1);
			tradeMapper.update(trade);

			// 向设备发起一个推送
			try {
				StringBuilder msg = new StringBuilder("您");
				msg.append("于" + dateFormat.format(trade.getTradeIn())).append(trade.getTradeType() == 1 ? "购买" : "抛出")
						.append("的 " + trade.getStockName() + " 已受理。");

				PushPayload payload = PushPayload.newBuilder().setPlatform(Platform.android())
						.setAudience(Audience.alias(user.getUsername()))
						.setNotification(Notification.android(msg.toString(), "后端推送", null)).build();
				PushResult result = jPushClient.sendPush(payload);
				logger.debug("push result - " + result);
			} catch (APIConnectionException e) {
				e.printStackTrace();
			} catch (APIRequestException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public List<StockBean> listStock(Long userId) {
		List<Stock> stocks = stockMapper.findByUserId(userId);
		List<StockBean> beans = new ArrayList<>(stocks.size());
		for (Stock stock : stocks) {
			StockBean bean = new StockBean();
			bean.gid = stock.getStockCode();
			bean.total = stock.getTotal();
			beans.add(bean);
		}
		return beans;
	}
}
