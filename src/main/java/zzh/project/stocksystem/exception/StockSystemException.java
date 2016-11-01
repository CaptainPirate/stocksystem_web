package zzh.project.stocksystem.exception;

/**
 * Service层业务异常 TODO 待处理
 */
public class StockSystemException extends Exception {
	private static final long serialVersionUID = -515967510963652282L;

	public StockSystemException(String msg) {
		super(msg);
	}

	public StockSystemException(String msg, Throwable clause) {
		super(msg, clause);
	}

	public int getErrorCode() {
		return 0;
	}
}
