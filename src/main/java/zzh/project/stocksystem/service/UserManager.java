package zzh.project.stocksystem.service;

import java.util.List;

import zzh.project.stocksystem.vo.FavorBean;
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
}
