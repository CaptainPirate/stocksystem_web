package zzh.project.stocksystem.exception;

/**
 * Service层业务异常
 */
public class StockSystemException extends Exception {
	private static final long serialVersionUID = -515967510963652282L;
	private final int errCode;

	public StockSystemException(String msg, int errCode) {
		super(msg);
		this.errCode = errCode;
	}

	public int getErrorCode() {
		return errCode;
	}
}
