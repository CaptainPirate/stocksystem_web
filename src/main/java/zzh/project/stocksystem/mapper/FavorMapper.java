package zzh.project.stocksystem.mapper;

import static zzh.project.stocksystem.mapper.DBInfo.Table.Favor.*;

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

import zzh.project.stocksystem.domain.Favor;

@Repository
public interface FavorMapper {
	@Insert("INSERT INTO " + TABLE_NAME + "(" 
			+ STOCK_CODE + ","  + USER_ID
			+ ")"
			+ "VALUES( #{f.stockCode}, #{f.userId} )")
	@Options(useGeneratedKeys = false)
	@SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="f._id", before=false, resultType=Long.class)
	public int save(@Param("f") Favor favor);
	

	@Delete("DELETE FROM " + TABLE_NAME + " WHERE " + ID + "  "
			+ "= #{_id}")
	public int delete(@Param("_id") long id);

	@Update("UPDATE " + TABLE_NAME + " SET " 
			+ STOCK_CODE + " = #{f.stockCode} , " 
			+ USER_ID + " = #{f.userId}  "
			+ "WHERE " + ID + " = #{f._id}")
	public int update(@Param("f") Favor favor);

	@Select("SELECT * FROM " + TABLE_NAME + " WHERE " + ID + " = #{_id}")
	@Results({
		@Result(property= "stockCode", column = STOCK_CODE)
	})
	public Favor get(@Param("_id") long id);

	@Select("SELECT * FROM " + TABLE_NAME)
	@Results({
		@Result(property= "stockCode", column = STOCK_CODE)
	})
	public List<Favor> findAll();

	@Select("SELECT * FROM " + TABLE_NAME + " WHERE " + USER_ID + " = #{userId}")
	@Results({
		@Result(property= "stockCode", column = STOCK_CODE)
	})
	public List<Favor> findByUserId(@Param("userId") Long userId);
	
	@Select("SELECT * FROM " + TABLE_NAME + " WHERE " + USER_ID + " = #{userId} and " + STOCK_CODE + " = #{code}")
	@Results({
		@Result(property= "stockCode", column = STOCK_CODE)
	})
	public Favor findByUserIdAndStockCode(@Param("userId") Long userId, @Param("code") String stockCode);
}
