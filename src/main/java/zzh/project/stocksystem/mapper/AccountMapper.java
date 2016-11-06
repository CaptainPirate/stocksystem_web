package zzh.project.stocksystem.mapper;

import static zzh.project.stocksystem.mapper.DBInfo.Table.Account.*;

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

import zzh.project.stocksystem.domain.Account;

@Repository
public interface AccountMapper {
	@Insert("INSERT INTO " + TABLE_NAME + "(" 
			+ CARD_NUMBER + "," + REAL_NAME + "," + ID_NUM + "," + USER_ID + "," + PASSWORD
			+ ")"
			+ "VALUES( #{c.cardNum}, #{c.realName}, #{c.idNum}, #{c.userId}, #{c.password} )")
	@Options(useGeneratedKeys = false)
	@SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="c._id", before=false, resultType=Long.class)
	public int save(@Param("c") Account card);
	

	@Delete("DELETE FROM " + TABLE_NAME + " WHERE " + ID + "  "
			+ "= #{_id}")
	public int delete(@Param("_id") long id);

	@Update("UPDATE " + TABLE_NAME + " SET " 
			+ CARD_NUMBER + " = #{c.cardNum} , " 
			+ REAL_NAME + " = #{c.realName} , "
			+ ID_NUM + " = #{c.idNum} , "
			+ PASSWORD + " = #{c.password} , "
			+ USER_ID + " = #{c.userId}  "
			+ "WHERE " + ID + " = #{c._id}")
	public int update(@Param("c") Account card);

	@Select("SELECT * FROM " + TABLE_NAME + " WHERE " + ID + " = #{_id}")
	@Results({
		@Result(property="cardNum", column = CARD_NUMBER),
		@Result(property="realName", column = REAL_NAME), 
		@Result(property="idNum", column = ID_NUM),
		@Result(property="userId", column = USER_ID)
	})
	public Account get(@Param("_id") long id);

	@Select("SELECT * FROM " + TABLE_NAME)
	@Results({
		@Result(property="cardNum", column = CARD_NUMBER),
		@Result(property="realName", column = REAL_NAME), 
		@Result(property="idNum", column = ID_NUM),
		@Result(property="userId", column = USER_ID)
	})
	public List<Account> findAll();

	@Select("SELECT * FROM " + TABLE_NAME + " WHERE " + USER_ID + " = #{userId}")
	@Results({
		@Result(property="cardNum", column = CARD_NUMBER),
		@Result(property="realName", column = REAL_NAME), 
		@Result(property="idNum", column = ID_NUM),
		@Result(property="userId", column = USER_ID)
	})
	public Account findByUserId(@Param("userId") Long userId);
	
	@Select("SELECT * FROM " + TABLE_NAME + " WHERE " + CARD_NUMBER + " = #{cardNum}")
	@Results({
		@Result(property="cardNum", column = CARD_NUMBER),
		@Result(property="realName", column = REAL_NAME), 
		@Result(property="idNum", column = ID_NUM),
		@Result(property="userId", column = USER_ID)
	})
	public Account findByCardNum(@Param("cardNum") String cardNum);
}
