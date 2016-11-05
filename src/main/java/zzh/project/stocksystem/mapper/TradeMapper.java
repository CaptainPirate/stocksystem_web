package zzh.project.stocksystem.mapper;

import static zzh.project.stocksystem.mapper.DBInfo.Table.Trade.*;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import zzh.project.stocksystem.domain.Trade;

@Repository
public interface TradeMapper {
	@Insert("INSERT INTO " + TABLE_NAME + "(" 
			+ STOCK_CODE + ","  + USER_ID + "," + TRADE_TYPE + "," + STATUS + "," + TRADE_IN 
			+ ")"
			+ "VALUES( #{t.stockCode}, #{t.userId}, #{t.tradeType}, #{t.status}, #{t.tradeIn} )")
	@Options(useGeneratedKeys = false)
	@SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="t._id", before=false, resultType=Long.class)
	public int save(@Param("t") Trade trade);
	

	@Delete("DELETE FROM " + TABLE_NAME + " WHERE " + ID + "  "
			+ "= #{_id}")
	public int delete(@Param("_id") long id);

	@Update("UPDATE " + TABLE_NAME + " SET " 
			+ STOCK_CODE + " = #{f.stockCode} , " 
			+ USER_ID + " = #{f.userId} , "
			+ TRADE_TYPE + " = #{f.tradeType} ,  "
			+ STATUS + " = #{f.status} , "
			+ TRADE_IN + " = #{f.tradeIn}  "
			+ "WHERE " + ID + " = #{f._id}")
	public int update(@Param("f") Trade trade);

	@Select("SELECT * FROM " + TABLE_NAME + " WHERE " + ID + " = #{_id}")
	public Trade get(@Param("_id") long id);

	@Select("SELECT * FROM " + TABLE_NAME)
	public List<Trade> findAll();

	@Select("SELECT * FROM " + TABLE_NAME + " WHERE " + USER_ID + " = #{userId}")
	public List<Trade> findByUserId(@Param("userId") Long userId);
}
