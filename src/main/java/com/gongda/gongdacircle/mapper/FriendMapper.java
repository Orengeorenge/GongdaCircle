package com.gongda.gongdacircle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gongda.gongdacircle.entity.Friend;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 好友关系Mapper接口
 *
 * @author GongdaCircle
 * @since 2024-01-01
 */
@Mapper
public interface FriendMapper extends BaseMapper<Friend> {
    
    /**
     * 查询两个用户之间的好友关系
     * @param userId1 用户1ID
     * @param userId2 用户2ID
     * @return 好友关系
     */
    @Select("SELECT * FROM friend WHERE " +
            "((user_id = #{userId1} AND friend_id = #{userId2}) OR " +
            "(user_id = #{userId2} AND friend_id = #{userId1})) " +
            "AND deleted = 0 LIMIT 1")
    Friend selectFriendship(@Param("userId1") Long userId1, @Param("userId2") Long userId2);
    
    /**
     * 检查是否已申请好友（单向）
     * @param userId 申请者ID
     * @param friendId 被申请者ID
     * @return 好友申请记录
     */
    @Select("SELECT * FROM friend WHERE user_id = #{userId} AND friend_id = #{friendId} " +
            "AND status IN (0, 1) AND deleted = 0 LIMIT 1")
    Friend selectPendingOrApprovedApplication(@Param("userId") Long userId, @Param("friendId") Long friendId);
    
    /**
     * 统计待处理的好友申请数量
     * @param userId 用户ID
     * @return 待处理申请数量
     */
    @Select("SELECT COUNT(*) FROM friend WHERE friend_id = #{userId} AND status = 0 AND deleted = 0")
    int countPendingRequests(@Param("userId") Long userId);
} 