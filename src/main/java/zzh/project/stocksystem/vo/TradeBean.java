package zzh.project.stocksystem.vo;

public class TradeBean {
	public String stockCode;
	public String stockName;
	public String uPrice;
	public int amount;
	public int status; // 0 处理中，1 处理完毕
	public String type; // "buy", "sell"
    public String date;
}
