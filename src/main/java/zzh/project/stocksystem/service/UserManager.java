package zzh.project.stocksystem.service;

import java.util.List;

import zzh.project.stocksystem.exception.StockSystemException;
import zzh.project.stocksystem.vo.AccountBean;
import zzh.project.stocksystem.vo.FavorBean;
import zzh.project.stocksystem.vo.StockBean;
import zzh.project.stocksystem.vo.TradeBean;
import zzh.project.stocksystem.vo.UserBean;

public interface UserManager {

	/**
	 * 校验登陆
	 */
	public boolean validLogin(String username, String password);

	/**
	 * 注册
	 */
	public boolean register(UserBean user);

	/**
	 * 关注
	 */
	public void favor(Long userId, String gid);
	
	/**
	 * 取消关注
	 */
	public void cancelFavor(Long userId, String gid);

	/**
	 * 激活access_token
	 */
	public void activeAccessToken(String username, String accessToken, long expiresIn);
	
	/**
	 * 获取用户关注列表
	 */
	public List<FavorBean> listFavor(Long userId);
	
	/**
	 * 获取个人信息
	 */
	public UserBean getUserInfo(Long userId);
	
	/**
	 * 充值
	 */
	public void recharge(Long userId, String carNum, String password, float money) throws StockSystemException;
	
	/**
	 * 绑定卡片 
	 */
	public void bindAccount(Long userId, AccountBean account) throws StockSystemException;
	
	/**
	 * 获取用户绑定支付账号 
	 */
	public AccountBean getAccountInfo(Long userId);
	
	/**
	 * 获取交易记录
	 */
	public List<TradeBean> listTrade(Long userId);
	
	/**
	 * 购入
	 */
	public void buy(Long userId, String gid, String name, float uPrice, int amount) throws StockSystemException;
	
	/**
	 * 卖出
	 */
	public void sell(Long userId, String gid, String name, float uPrice, int amount) throws StockSystemException;
	
	/**
	 * 列出持有股票
	 */
	public List<StockBean> listStock(Long userId);
}
