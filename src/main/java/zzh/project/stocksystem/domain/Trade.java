package zzh.project.stocksystem.domain;

import java.io.Serializable;
import java.util.Date;

public class Trade implements Serializable {
	private static final long serialVersionUID = 2416697974083175672L;
	private Long _id; // 数据库主键
	private String stockCode;
	private String stockName;
	private Long userId;
	private int tradeType; // 0 卖出，1购入
	private int status; //  0 处理中，1 处理完毕
	private int amount;
	private float uPrice;
	private Date tradeIn;

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
	
	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public int getTradeType() {
		return tradeType;
	}

	public void setTradeType(int tradeType) {
		this.tradeType = tradeType;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public float getuPrice() {
		return uPrice;
	}

	public void setuPrice(float uPrice) {
		this.uPrice = uPrice;
	}

	public Date getTradeIn() {
		return tradeIn;
	}

	public void setTradeIn(Date tradeIn) {
		this.tradeIn = tradeIn;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_id == null) ? 0 : _id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Trade other = (Trade) obj;
		if (_id == null) {
			if (other._id != null)
				return false;
		} else if (!_id.equals(other._id))
			return false;
		return true;
	}
}
