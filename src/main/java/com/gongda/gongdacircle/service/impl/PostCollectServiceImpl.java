package com.gongda.gongdacircle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gongda.gongdacircle.entity.PostCollect;
import com.gongda.gongdacircle.mapper.PostCollectMapper;
import com.gongda.gongdacircle.mapper.PostMapper;
import com.gongda.gongdacircle.service.PostCollectService;
import com.gongda.gongdacircle.utils.IdGenerator;
import com.gongda.gongdacircle.vo.PostVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 帖子收藏服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PostCollectServiceImpl extends ServiceImpl<PostCollectMapper, PostCollect> implements PostCollectService {
    
    private final PostCollectMapper postCollectMapper;
    private final PostMapper postMapper;
    
    @Override
    @Transactional
    public Boolean collectPost(Long postId, Long userId) {
        // 检查是否已经收藏
        LambdaQueryWrapper<PostCollect> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PostCollect::getPostId, postId)
                   .eq(PostCollect::getUserId, userId)
                   .eq(PostCollect::getDeleted, 0);
        
        PostCollect existingCollect = postCollectMapper.selectOne(queryWrapper);
        if (existingCollect != null) {
            log.warn("用户{}已经收藏过帖子{}", userId, postId);
            return false;
        }
        
        // 创建收藏记录
        PostCollect postCollect = new PostCollect();
        postCollect.setId(IdGenerator.generateId());
        postCollect.setPostId(postId);
        postCollect.setUserId(userId);
        postCollect.setCreateBy(userId);
        postCollect.setUpdateBy(userId);
        
        // 保存收藏记录
        postCollectMapper.insert(postCollect);
        
        // 更新帖子收藏数
        postMapper.increaseCollectCount(postId);
        
        log.info("用户{}收藏帖子{}成功", userId, postId);
        return true;
    }
    
    @Override
    @Transactional
    public Boolean uncollectPost(Long postId, Long userId) {
        // 查找收藏记录
        LambdaQueryWrapper<PostCollect> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PostCollect::getPostId, postId)
                   .eq(PostCollect::getUserId, userId)
                   .eq(PostCollect::getDeleted, 0);
        
        PostCollect postCollect = postCollectMapper.selectOne(queryWrapper);
        if (postCollect == null) {
            log.warn("用户{}未收藏过帖子{}", userId, postId);
            return false;
        }
        
        // 逻辑删除收藏记录
        postCollect.setDeleted(1);
        postCollect.setUpdateBy(userId);
        postCollectMapper.updateById(postCollect);
        
        // 更新帖子收藏数
        postMapper.decreaseCollectCount(postId);
        
        log.info("用户{}取消收藏帖子{}成功", userId, postId);
        return true;
    }
    
    @Override
    public Boolean isUserCollected(Long postId, Long userId) {
        LambdaQueryWrapper<PostCollect> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PostCollect::getPostId, postId)
                   .eq(PostCollect::getUserId, userId)
                   .eq(PostCollect::getDeleted, 0);
        
        return postCollectMapper.selectCount(queryWrapper) > 0;
    }
    
    @Override
    public Integer getCollectCount(Long postId) {
        return postCollectMapper.countByPostId(postId);
    }
    
    @Override
    public Page<PostVO> getUserCollects(Long userId, Integer page, Integer size) {
        // TODO: 实现用户收藏列表查询
        // 这里需要关联查询帖子信息
        Page<PostVO> postPage = new Page<>(page, size);
        
        log.info("查询用户{}的收藏列表", userId);
        return postPage;
    }
} 