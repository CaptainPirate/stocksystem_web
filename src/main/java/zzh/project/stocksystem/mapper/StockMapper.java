package zzh.project.stocksystem.mapper;

import static zzh.project.stocksystem.mapper.DBInfo.Table.Stock.*;

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

import zzh.project.stocksystem.domain.Stock;

@Repository
public interface StockMapper {
	@Insert("INSERT INTO " + TABLE_NAME + "(" 
			+ STOCK_CODE + ","  + USER_ID + "," + TOTAL
			+ ")"
			+ "VALUES( #{s.stockCode}, #{s.userId}, #{s.total} )")
	@Options(useGeneratedKeys = false)
	@SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="s._id", before=false, resultType=Long.class)
	public int save(@Param("s") Stock stock);
	

	@Delete("DELETE FROM " + TABLE_NAME + " WHERE " + ID + "  "
			+ "= #{_id}")
	public int delete(@Param("_id") long id);

	@Update("UPDATE " + TABLE_NAME + " SET " 
			+ STOCK_CODE + " = #{s.stockCode} , " 
			+ USER_ID + " = #{s.userId} ,  "
			+ TOTAL + " = #{s.total}  "
			+ "WHERE " + ID + " = #{f._id}")
	public int update(@Param("s") Stock stock);

	@Select("SELECT * FROM " + TABLE_NAME + " WHERE " + ID + " = #{_id}")
	@Results({
		@Result(property= "stockCode", column = STOCK_CODE),
		@Result(property= "userId", column = USER_ID)
	})
	public Stock get(@Param("_id") long id);

	@Select("SELECT * FROM " + TABLE_NAME)
	@Results({
		@Result(property= "stockCode", column = STOCK_CODE),
		@Result(property= "userId", column = USER_ID)
	})
	public List<Stock> findAll();

	@Select("SELECT * FROM " + TABLE_NAME + " WHERE " + USER_ID + " = #{userId}")
	@Results({
		@Result(property= "stockCode", column = STOCK_CODE),
		@Result(property= "userId", column = USER_ID)
	})
	public List<Stock> findByUserId(@Param("userId") Long userId);
	
	@Select("SELECT * FROM " + TABLE_NAME + " WHERE " + USER_ID + " = #{userId} and " + STOCK_CODE + " = #{code}")
	@Results({
		@Result(property= "stockCode", column = STOCK_CODE),
		@Result(property= "userId", column = USER_ID)
	})
	public Stock findByUserIdAndStockCode(@Param("userId") Long userId, @Param("code") String stockCode);
}
