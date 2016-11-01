package zzh.project.stocksystem.mapper;

import static zzh.project.stocksystem.mapper.DBInfo.Table.Card.*;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import zzh.project.stocksystem.domain.Card;

@Repository
public interface CardMapper {
	@Insert("INSERT INTO " + TABLE_NAME + "(" 
			+ CARD_NUMBER + "," + REAL_NAME + "," + ID_NUM + "," + USER_ID
			+ ")"
			+ "VALUES( #{c.cardNum}, #{c.realName}, #{c.idNum}, #{c.userId} )")
	@Options(useGeneratedKeys = false)
	@SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="c._id", before=false, resultType=Long.class)
	public int save(@Param("c") Card card);
	

	@Delete("DELETE FROM " + TABLE_NAME + " WHERE " + ID + "  "
			+ "= #{_id}")
	public int delete(@Param("_id") long id);

	@Update("UPDATE " + TABLE_NAME + " SET " 
			+ CARD_NUMBER + " = #{c.cardNum} , " 
			+ REAL_NAME + " = #{c.realName} , "
			+ ID_NUM + " = #{c.idNum} , "
			+ USER_ID + " = #{c.userId}  "
			+ "WHERE " + ID + " = #{c._id}")
	public int update(@Param("c") Card card);

	@Select("SELECT * FROM " + TABLE_NAME + " WHERE " + ID + " = #{_id}")
	public Card get(@Param("_id") long id);

	@Select("SELECT * FROM " + TABLE_NAME)
	public List<Card> findAll();

	@Select("SELECT * FROM " + TABLE_NAME + " WHERE " + USER_ID + " = #{userId}")
	public List<Card> findByUserId(@Param("userId") String userId);
}
