package zzh.project.stocksystem.domain;

import java.io.Serializable;

public class Stock implements Serializable {
	private static final long serialVersionUID = 85280390005470649L;
	private Long _id; // 数据库主键
	private String stockCode;
	private int total;
	private Long userId;

	public Long get_id() {
		return _id;
	}

	public void set_id(Long _id) {
		this._id = _id;
	}

	public String getStockCode() {
		return stockCode;
	}

	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
