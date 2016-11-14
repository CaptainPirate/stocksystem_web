package zzh.project.stocksystem.mapper;

import static zzh.project.stocksystem.mapper.DBInfo.Table.User.*;

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

import zzh.project.stocksystem.domain.User;

@Repository
public interface UserMapper {
	@Insert("INSERT INTO " + TABLE_NAME + "(" 
			+ USERNAME + "," + PASSWORD + "," + NICK + "," + EMAIL + "," + BALANCE  + "," + ACCESS_TOKEN  + "," + EXPIRES_IN
			+ ")"
			+ "VALUES( #{user.username}, #{user.password}, #{user.nick}, #{user.email}, #{user.balance}, #{user.accessToken}, #{user.expiresIn} )")
	@Options(useGeneratedKeys = false)
	@SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="user._id", before=false, resultType=Long.class)
	public int save(@Param("user") User user);

	@Delete("DELETE FROM " + TABLE_NAME + " WHERE " + ID + "  "
			+ "= #{_id}")
	public int delete(@Param("_id") long id);

	@Update("UPDATE " + TABLE_NAME + " SET " 
			+ USERNAME + " = #{user.username} , " 
			+ PASSWORD + " = #{user.password} , "
			+ NICK + " = #{user.nick} , "
			+ EMAIL + " = #{user.email} , "
			+ BALANCE + " = #{user.balance} , "
			+ ACCESS_TOKEN + " = #{user.accessToken} , "
			+ EXPIRES_IN + " = #{user.expiresIn} "
			+ "WHERE " + ID + " = #{user._id}")
	public int update(@Param("user") User user);

	@Select("SELECT * FROM " + TABLE_NAME + " WHERE " + ID + " = #{_id}")
	@Results({
		@Result(property="accessToken", column = ACCESS_TOKEN),
		@Result(property="expiresIn", column = EXPIRES_IN), 
	})
	public User get(@Param("_id") long id);

	@Select("SELECT * FROM " + TABLE_NAME)
	@Results({
		@Result(property="accessToken", column = ACCESS_TOKEN),
		@Result(property="expiresIn", column = EXPIRES_IN), 
	})
	public List<User> findAll();

	@Select("SELECT * FROM " + TABLE_NAME + " WHERE " + USERNAME + " = #{username}")
	@Results({
		@Result(property="accessToken", column = ACCESS_TOKEN),
		@Result(property="expiresIn", column = EXPIRES_IN), 
	})
	public User findByUsername(@Param("username") String username);
	
	@Select("SELECT * FROM " + TABLE_NAME + " WHERE " + NICK+ " = #{nick}")
	@Results({
		@Result(property="accessToken", column = ACCESS_TOKEN),
		@Result(property="expiresIn", column = EXPIRES_IN), 
	})
	public User findByNick(@Param("nick") String nick);
	
	@Select("SELECT * FROM " + TABLE_NAME + " WHERE " + EMAIL+ " = #{email}")
	@Results({
		@Result(property="accessToken", column = ACCESS_TOKEN),
		@Result(property="expiresIn", column = EXPIRES_IN), 
	})
	public User findByEmail(@Param("email") String email);
	
	@Select("SELECT * FROM " + TABLE_NAME + " WHERE " + ACCESS_TOKEN + " = #{accessToken}")
	@Results({
		@Result(property="accessToken", column = ACCESS_TOKEN),
		@Result(property="expiresIn", column = EXPIRES_IN), 
	})
	public User findByAccessToken(@Param("accessToken") String accessToken);
}