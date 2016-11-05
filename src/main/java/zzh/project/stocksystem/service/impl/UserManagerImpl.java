package zzh.project.stocksystem.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zzh.project.stocksystem.ErrorCode;
import zzh.project.stocksystem.domain.Account;
import zzh.project.stocksystem.domain.Favor;
import zzh.project.stocksystem.domain.User;
import zzh.project.stocksystem.exception.StockSystemException;
import zzh.project.stocksystem.mapper.AccountMapper;
import zzh.project.stocksystem.mapper.FavorMapper;
import zzh.project.stocksystem.mapper.UserMapper;
import zzh.project.stocksystem.service.UserManager;
import zzh.project.stocksystem.util.BeanConvert;
import zzh.project.stocksystem.vo.AccountBean;
import zzh.project.stocksystem.vo.FavorBean;
import zzh.project.stocksystem.vo.UserBean;

@Service
@Transactional
public class UserManagerImpl implements UserManager {
	private final Logger logger = LoggerFactory.getLogger(UserManagerImpl.class);

	@Autowired
	private UserMapper userMapper;
	@Autowired
	private FavorMapper favorMapper;
	@Autowired
	private AccountMapper cardMapper;

	@Override
	public boolean register(UserBean user) {
		try {
			userMapper.save(BeanConvert.convert(user));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
		return true;
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
		Account card = cardMapper.findByUserId(userId);
		if (card != null) {
			if (card.getUserId().equals(userId)) {
				if (card.getPassword().equals(password)) {
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
		Account card = cardMapper.findByCardNum(cardBean.carNum);
		if (card != null) {
			throw new StockSystemException("该支付账号已被绑定", ErrorCode.ALREADY_EXISTS);
		}
		card = cardMapper.findByUserId(userId);
		if (card != null) {
			throw new StockSystemException("已绑定支付账号", ErrorCode.ALREADY_EXISTS);
		}
		User user = userMapper.get(userId);
		if (user != null) {
			card = new Account();
			card.setCardNum(cardBean.carNum);
			card.setIdNum(cardBean.idNum);
			card.setRealName(cardBean.realName);
			card.setPassword(cardBean.password);
			card.setUserId(userId);
			cardMapper.save(card);
		} else {
			throw new StockSystemException("该用户不存在", ErrorCode.NOT_EXITS);
		}
	}

	@Override
	public AccountBean getAccountInfo(Long userId) {
		Account card = cardMapper.findByUserId(userId);
		if (card != null) {
			AccountBean bean = new AccountBean();
			bean.carNum = card.getCardNum();
			bean.idNum = card.getIdNum();
			bean.realName = card.getRealName();
			System.out.println(bean.carNum);
			return bean;
		}
		return null;
	}
}
