package zzh.project.stocksystem.mapper;

public class DBInfo {
	public static final class Table {
		// 用户表
		public static final class User {
			public static final String TABLE_NAME = "t_user";
			public static final String ID = "_id";
			public static final String USERNAME = "username";
			public static final String PASSWORD = "password";
			public static final String NICK = "nick";
			public static final String EMAIL = "email";
			public static final String BALANCE = "balance";
			public static final String ACCESS_TOKEN = "access_token";
			public static final String EXPIRES_IN = "expires_in";
		}

		// 银行卡表
		public static final class Card {
			public static final String TABLE_NAME = "t_card";
			public static final String ID = "_id";
			public static final String CARD_NUMBER = "card_numbar";
			public static final String REAL_NAME = "real_name";
			public static final String ID_NUM = "id_number";
			public static final String USER_ID = "user_id";
		}

		// 自选表
		public static final class Favor {
			public static final String TABLE_NAME = "t_favor";
			public static final String ID = "_id";
			public static final String STOCK_CODE = "stock_code";
			public static final String USER_ID = "user_id";
		}

		// 交易表
		public static final class Trade {
			public static final String TABLE_NAME = "t_trade";
			public static final String ID = "_id";
			public static final String STOCK_CODE = "stock_code";
			public static final String USER_ID = "user_id";
			public static final String TRADE_TYPE = "trade_type";
			public static final String STATUS = "status";
			public static final String TRADE_IN = "trade_in";
		}
	}
}
