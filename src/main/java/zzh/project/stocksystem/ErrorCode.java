package zzh.project.stocksystem;

/**
 * 错误代码 
 */
public final class ErrorCode {
	public static final int SUCCESS = 0; // 成功
	public static final int ACCESS_TOKEN_EXPIRES = 1001; // access_token 失效
	public static final int ARGUMENT_INVALID = 1002; // 参数非法
	public static final int ALREADY_EXISTS = 1003; // 已存在
	public static final int NOT_EXITS = 1004; // 不存在
	public static final int PASS_WRONG = 1005; // 密码错误
	public static final int BALANCE_NOT_ENOUGH = 1006; // 余额不足
}
