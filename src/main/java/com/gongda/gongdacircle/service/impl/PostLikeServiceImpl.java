package com.gongda.gongdacircle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gongda.gongdacircle.entity.PostLike;
import com.gongda.gongdacircle.mapper.PostLikeMapper;
import com.gongda.gongdacircle.mapper.PostMapper;
import com.gongda.gongdacircle.service.PostLikeService;
import com.gongda.gongdacircle.utils.IdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 帖子点赞服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PostLikeServiceImpl extends ServiceImpl<PostLikeMapper, PostLike> implements PostLikeService {
    
    private final PostLikeMapper postLikeMapper;
    private final PostMapper postMapper;
    
    @Override
    @Transactional
    public Boolean likePost(Long postId, Long userId) {
        // 检查是否已经点赞
        LambdaQueryWrapper<PostLike> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PostLike::getPostId, postId)
                   .eq(PostLike::getUserId, userId)
                   .eq(PostLike::getDeleted, 0);
        
        PostLike existingLike = postLikeMapper.selectOne(queryWrapper);
        if (existingLike != null) {
            log.warn("用户{}已经点赞过帖子{}", userId, postId);
            return false;
        }
        
        // 创建点赞记录
        PostLike postLike = new PostLike();
        postLike.setId(IdGenerator.generateId());
        postLike.setPostId(postId);
        postLike.setUserId(userId);
        postLike.setCreateBy(userId);
        postLike.setUpdateBy(userId);
        
        // 保存点赞记录
        postLikeMapper.insert(postLike);
        
        // 更新帖子点赞数
        postMapper.increaseLikeCount(postId);
        
        log.info("用户{}点赞帖子{}成功", userId, postId);
        return true;
    }
    
    @Override
    @Transactional
    public Boolean unlikePost(Long postId, Long userId) {
        // 查找点赞记录
        LambdaQueryWrapper<PostLike> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PostLike::getPostId, postId)
                   .eq(PostLike::getUserId, userId)
                   .eq(PostLike::getDeleted, 0);
        
        PostLike postLike = postLikeMapper.selectOne(queryWrapper);
        if (postLike == null) {
            log.warn("用户{}未点赞过帖子{}", userId, postId);
            return false;
        }
        
        // 逻辑删除点赞记录
        postLike.setDeleted(1);
        postLike.setUpdateBy(userId);
        postLikeMapper.updateById(postLike);
        
        // 更新帖子点赞数
        postMapper.decreaseLikeCount(postId);
        
        log.info("用户{}取消点赞帖子{}成功", userId, postId);
        return true;
    }
    
    @Override
    public Boolean isUserLiked(Long postId, Long userId) {
        LambdaQueryWrapper<PostLike> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PostLike::getPostId, postId)
                   .eq(PostLike::getUserId, userId)
                   .eq(PostLike::getDeleted, 0);
        
        return postLikeMapper.selectCount(queryWrapper) > 0;
    }
    
    @Override
    public Integer getLikeCount(Long postId) {
        return postLikeMapper.countByPostId(postId);
    }
} 