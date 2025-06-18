package com.gongda.gongdacircle.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gongda.gongdacircle.dto.PostDTO;
import com.gongda.gongdacircle.entity.Post;
import com.gongda.gongdacircle.entity.User;
import com.gongda.gongdacircle.mapper.PostMapper;
import com.gongda.gongdacircle.service.PostService;
import com.gongda.gongdacircle.service.UserService;
import com.gongda.gongdacircle.vo.PostVO;
import com.gongda.gongdacircle.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 帖子服务实现类
 *
 * @author GongdaCircle
 * @since 2024-01-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {
    
    private final UserService userService;
    
    @Override
    @Transactional
    public boolean publishPost(PostDTO postDTO, Long userId) {
        // 验证用户是否存在
        User user = userService.getById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 检查用户状态
        if (user.getStatus() == 0) {
            throw new RuntimeException("用户已被禁用，无法发布帖子");
        }
        
        // 创建帖子对象
        Post post = new Post();
        BeanUtil.copyProperties(postDTO, post);
        post.setUserId(userId);
        post.setStatus(1); // 设置为已发布状态
        
        // 初始化统计字段
        post.setLikeCount(0);
        post.setCommentCount(0);
        post.setCollectCount(0);
        post.setShareCount(0);
        post.setViewCount(0);
        post.setIsTop(0);
        post.setIsEssence(0);
        
        return save(post);
    }
    
    @Override
    public PostVO getPostById(Long id) {
        Post post = getById(id);
        if (post == null || post.getStatus() == 2) { // 2表示已删除
            return null;
        }
        
        // 增加浏览量
        increaseViewCount(id);
        
        return convertToVO(post);
    }
    
    @Override
    public Page<PostVO> getPostList(Integer page, Integer size, String keyword, Long userId, Integer type) {
        Page<Post> postPage = new Page<>(page, size);
        
        LambdaQueryWrapper<Post> queryWrapper = new LambdaQueryWrapper<>();
        
        // 只查询已发布的帖子
        queryWrapper.eq(Post::getStatus, 1);
        
        // 根据用户ID过滤
        if (userId != null) {
            queryWrapper.eq(Post::getUserId, userId);
        }
        
        // 根据帖子类型过滤
        if (type != null) {
            queryWrapper.eq(Post::getType, type);
        }
        
        // 关键词搜索
        if (StrUtil.isNotBlank(keyword)) {
            queryWrapper.and(wrapper -> wrapper
                .like(Post::getTitle, keyword)
                .or()
                .like(Post::getContent, keyword)
                .or()
                .like(Post::getTags, keyword)
            );
        }
        
        // 排序：置顶的帖子在前，然后按创建时间倒序
        queryWrapper.orderByDesc(Post::getIsTop)
                   .orderByDesc(Post::getCreateTime);
        
        Page<Post> result = page(postPage, queryWrapper);
        
        // 转换为VO
        Page<PostVO> voPage = new Page<>();
        BeanUtil.copyProperties(result, voPage, "records");
        
        List<PostVO> postVOList = result.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        
        voPage.setRecords(postVOList);
        return voPage;
    }
    
    @Override
    @Transactional
    public boolean updatePost(Long id, PostDTO postDTO, Long userId) {
        Post post = getById(id);
        if (post == null) {
            throw new RuntimeException("帖子不存在");
        }
        
        // 验证权限：只有帖子作者才能修改
        if (!post.getUserId().equals(userId)) {
            throw new RuntimeException("无权限修改此帖子");
        }
        
        // 更新帖子信息
        BeanUtil.copyProperties(postDTO, post, "id", "userId", "createTime", "createBy",
                "likeCount", "commentCount", "collectCount", "shareCount", "viewCount",
                "isTop", "isEssence", "status");
        
        return updateById(post);
    }
    
    @Override
    @Transactional
    public boolean deletePost(Long id, Long userId) {
        Post post = getById(id);
        if (post == null) {
            throw new RuntimeException("帖子不存在");
        }
        
        // 验证权限：只有帖子作者才能删除
        if (!post.getUserId().equals(userId)) {
            throw new RuntimeException("无权限删除此帖子");
        }
        
        // 逻辑删除：设置状态为已删除
        post.setStatus(2);
        return updateById(post);
    }
    
    @Override
    @Transactional
    public boolean likePost(Long id, Long userId) {
        Post post = getById(id);
        if (post == null) {
            throw new RuntimeException("帖子不存在");
        }
        
        // TODO: 这里应该检查用户是否已经点赞过，实现点赞/取消点赞逻辑
        // 目前简单实现：直接增加点赞数
        post.setLikeCount(post.getLikeCount() + 1);
        return updateById(post);
    }
    
    @Override
    @Transactional
    public boolean collectPost(Long id, Long userId) {
        Post post = getById(id);
        if (post == null) {
            throw new RuntimeException("帖子不存在");
        }
        
        // TODO: 这里应该检查用户是否已经收藏过，实现收藏/取消收藏逻辑
        // 目前简单实现：直接增加收藏数
        post.setCollectCount(post.getCollectCount() + 1);
        return updateById(post);
    }
    
    @Override
    @Transactional
    public void increaseViewCount(Long id) {
        baseMapper.increaseViewCount(id);
    }
    
    /**
     * 将Post实体转换为PostVO
     */
    private PostVO convertToVO(Post post) {
        PostVO postVO = new PostVO();
        BeanUtil.copyProperties(post, postVO);
        
        // 获取发布用户信息
        UserVO userVO = userService.getUserById(post.getUserId());
        
        // 确保即使用户不存在也能返回带有默认值的userInfo
        if (userVO == null) {
            userVO = new UserVO();
            userVO.setId(post.getUserId());
            userVO.setNickname("未知用户");
            userVO.setAvatar("");
        }
        
        postVO.setUserInfo(userVO);
        
        return postVO;
    }
} 