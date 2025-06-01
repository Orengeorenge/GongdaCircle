package com.gongda.gongdacircle.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gongda.gongdacircle.dto.PostDTO;
import com.gongda.gongdacircle.entity.Post;
import com.gongda.gongdacircle.vo.PostVO;

/**
 * 帖子服务接口
 *
 * @author GongdaCircle
 * @since 2024-01-01
 */
public interface PostService extends IService<Post> {
    
    /**
     * 发布帖子
     * @param postDTO 帖子信息
     * @param userId 发布用户ID
     * @return 发布结果
     */
    boolean publishPost(PostDTO postDTO, Long userId);
    
    /**
     * 根据ID获取帖子详情
     * @param id 帖子ID
     * @return 帖子详情
     */
    PostVO getPostById(Long id);
    
    /**
     * 分页查询帖子列表
     * @param page 页码
     * @param size 每页数量
     * @param keyword 搜索关键词
     * @param userId 用户ID（可选，查询指定用户的帖子）
     * @param type 帖子类型（可选）
     * @return 帖子分页数据
     */
    Page<PostVO> getPostList(Integer page, Integer size, String keyword, Long userId, Integer type);
    
    /**
     * 更新帖子
     * @param id 帖子ID
     * @param postDTO 帖子信息
     * @param userId 当前用户ID
     * @return 更新结果
     */
    boolean updatePost(Long id, PostDTO postDTO, Long userId);
    
    /**
     * 删除帖子
     * @param id 帖子ID
     * @param userId 当前用户ID
     * @return 删除结果
     */
    boolean deletePost(Long id, Long userId);
    
    /**
     * 点赞/取消点赞帖子
     * @param id 帖子ID
     * @param userId 用户ID
     * @return 操作结果
     */
    boolean likePost(Long id, Long userId);
    
    /**
     * 收藏/取消收藏帖子
     * @param id 帖子ID
     * @param userId 用户ID
     * @return 操作结果
     */
    boolean collectPost(Long id, Long userId);
    
    /**
     * 增加帖子浏览量
     * @param id 帖子ID
     */
    void increaseViewCount(Long id);
}