package zzh.project.stocksystem.mapper;

import static zzh.project.stocksystem.mapper.DBInfo.Table.Trade.*;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import zzh.project.stocksystem.domain.Trade;

@Repository
public interface TradeMapper {
	@Insert("INSERT INTO " + TABLE_NAME + "(" 
			+ STOCK_CODE + "," + STOCK_NAME + ","  + USER_ID + "," + TRADE_TYPE + "," + STATUS + "," + AMOUNT + "," + UNIT_PRICE + "," + TRADE_IN 
			+ ")"
			+ "VALUES( #{t.stockCode}, #{t.stockName}, #{t.userId}, #{t.tradeType}, #{t.status}, #{t.amount}, #{t.uPrice}, #{t.tradeIn} )")
	@Options(useGeneratedKeys = false)
	@SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="t._id", before=false, resultType=Long.class)
	public int save(@Param("t") Trade trade);
	

	@Delete("DELETE FROM " + TABLE_NAME + " WHERE " + ID + "  "
			+ "= #{_id}")
	public int delete(@Param("_id") long id);

	@Update("UPDATE " + TABLE_NAME + " SET " 
			+ STOCK_CODE + " = #{t.stockCode} , " 
			+ STOCK_NAME + " = #{t.stockName} , " 
			+ USER_ID + " = #{t.userId} , "
			+ TRADE_TYPE + " = #{t.tradeType} ,  "
			+ STATUS + " = #{t.status} , "
			+ AMOUNT + " = #{t.amount} , "
			+ UNIT_PRICE + " = #{t.uPrice} , "
			+ TRADE_IN + " = #{t.tradeIn}  "
			+ "WHERE " + ID + " = #{t._id}")
	public int update(@Param("t") Trade trade);

	@Select("SELECT * FROM " + TABLE_NAME + " WHERE " + ID + " = #{_id}")
	@Results({
		@Result(property= "stockCode", column = STOCK_CODE),
		@Result(property= "stockName", column = STOCK_NAME),
		@Result(property= "userId", column = USER_ID),
		@Result(property= "tradeType", column = TRADE_TYPE),
		@Result(property= "uPrice", column = UNIT_PRICE),
		@Result(property= "tradeIn", column = TRADE_IN)
	})
	public Trade get(@Param("_id") long id);

	@Select("SELECT * FROM " + TABLE_NAME + " order by " + TRADE_IN + " desc")
	@Results({
		@Result(property= "stockCode", column = STOCK_CODE),
		@Result(property= "stockName", column = STOCK_NAME),
		@Result(property= "userId", column = USER_ID),
		@Result(property= "tradeType", column = TRADE_TYPE),
		@Result(property= "uPrice", column = UNIT_PRICE),
		@Result(property= "tradeIn", column = TRADE_IN)
	})
	public List<Trade> findAll();

	@Select("SELECT * FROM " + TABLE_NAME + " WHERE " + USER_ID + " = #{userId} order by " + TRADE_IN + " desc limit 30")
	@Results({
		@Result(property= "stockCode", column = STOCK_CODE),
		@Result(property= "stockName", column = STOCK_NAME),
		@Result(property= "userId", column = USER_ID),
		@Result(property= "tradeType", column = TRADE_TYPE),
		@Result(property= "uPrice", column = UNIT_PRICE),
		@Result(property= "tradeIn", column = TRADE_IN)
	})
	public List<Trade> findByUserId(@Param("userId") Long userId);
}
