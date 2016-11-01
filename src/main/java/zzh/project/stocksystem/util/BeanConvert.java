package zzh.project.stocksystem.util;

import zzh.project.stocksystem.domain.User;
import zzh.project.stocksystem.vo.UserBean;

public class BeanConvert {
	private BeanConvert() {

	}
	
	public static User convert(UserBean userBean) {
		User user = new User();
		user.setEmail(userBean.email);
		user.setNick(userBean.nick);
		user.setPassword(userBean.password);
		user.setUsername(userBean.username);
		return user;
	}
}
