package zzh.project.stocksystem.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zzh.project.stocksystem.domain.Favor;
import zzh.project.stocksystem.domain.User;
import zzh.project.stocksystem.mapper.FavorMapper;
import zzh.project.stocksystem.mapper.UserMapper;
import zzh.project.stocksystem.service.UserManager;
import zzh.project.stocksystem.util.BeanConvert;
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
}
