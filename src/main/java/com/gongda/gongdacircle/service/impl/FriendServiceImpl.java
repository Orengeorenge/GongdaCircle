package com.gongda.gongdacircle.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gongda.gongdacircle.dto.FriendDTO;
import com.gongda.gongdacircle.entity.Friend;
import com.gongda.gongdacircle.entity.User;
import com.gongda.gongdacircle.enums.FriendStatus;
import com.gongda.gongdacircle.mapper.FriendMapper;
import com.gongda.gongdacircle.service.FriendService;
import com.gongda.gongdacircle.service.UserService;
import com.gongda.gongdacircle.vo.FriendVO;
import com.gongda.gongdacircle.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 好友关系服务实现类
 *
 * @author GongdaCircle
 * @since 2024-01-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FriendServiceImpl extends ServiceImpl<FriendMapper, Friend> implements FriendService {
    
    private final UserService userService;
    
    @Override
    @Transactional
    public boolean applyFriend(FriendDTO friendDTO, Long userId) {
        // 验证不能添加自己为好友
        if (userId.equals(friendDTO.getFriendId())) {
            throw new RuntimeException("不能添加自己为好友");
        }
        
        // 验证目标用户是否存在
        User targetUser = userService.getById(friendDTO.getFriendId());
        if (targetUser == null) {
            throw new RuntimeException("目标用户不存在");
        }
        
        // 检查是否已经是好友或已申请
        Friend existingFriendship = baseMapper.selectFriendship(userId, friendDTO.getFriendId());
        if (existingFriendship != null) {
            if (existingFriendship.getStatus().equals(FriendStatus.APPROVED.getCode())) {
                throw new RuntimeException("你们已经是好友了");
            } else if (existingFriendship.getStatus().equals(FriendStatus.PENDING.getCode())) {
                throw new RuntimeException("好友申请已发送，请等待对方处理");
            }
        }
        
        // 检查是否已发送过申请
        Friend pendingApplication = baseMapper.selectPendingOrApprovedApplication(userId, friendDTO.getFriendId());
        if (pendingApplication != null) {
            throw new RuntimeException("已经发送过好友申请，请勿重复申请");
        }
        
        // 创建好友申请
        Friend friend = new Friend();
        friend.setUserId(userId);
        friend.setFriendId(friendDTO.getFriendId());
        friend.setStatus(FriendStatus.PENDING.getCode());
        friend.setRemark(friendDTO.getRemark());
        friend.setReason(friendDTO.getReason());
        
        return save(friend);
    }
    
    @Override
    @Transactional
    public boolean approveFriendRequest(Long friendshipId, Long userId) {
        Friend friendship = getById(friendshipId);
        if (friendship == null) {
            throw new RuntimeException("好友申请不存在");
        }
        
        // 验证权限：只有被申请者可以同意申请
        if (!friendship.getFriendId().equals(userId)) {
            throw new RuntimeException("无权限处理此好友申请");
        }
        
        // 验证状态
        if (!friendship.getStatus().equals(FriendStatus.PENDING.getCode())) {
            throw new RuntimeException("好友申请状态无效");
        }
        
        // 更新申请状态为已通过
        friendship.setStatus(FriendStatus.APPROVED.getCode());
        boolean result = updateById(friendship);
        
        if (result) {
            // 创建反向好友关系（双向好友）
            Friend reverseFriendship = new Friend();
            reverseFriendship.setUserId(friendship.getFriendId());
            reverseFriendship.setFriendId(friendship.getUserId());
            reverseFriendship.setStatus(FriendStatus.APPROVED.getCode());
            reverseFriendship.setRemark(""); // 反向关系默认无备注
            save(reverseFriendship);
        }
        
        return result;
    }
    
    @Override
    @Transactional
    public boolean rejectFriendRequest(Long friendshipId, Long userId) {
        Friend friendship = getById(friendshipId);
        if (friendship == null) {
            throw new RuntimeException("好友申请不存在");
        }
        
        // 验证权限：只有被申请者可以拒绝申请
        if (!friendship.getFriendId().equals(userId)) {
            throw new RuntimeException("无权限处理此好友申请");
        }
        
        // 验证状态
        if (!friendship.getStatus().equals(FriendStatus.PENDING.getCode())) {
            throw new RuntimeException("好友申请状态无效");
        }
        
        // 更新申请状态为已拒绝
        friendship.setStatus(FriendStatus.REJECTED.getCode());
        return updateById(friendship);
    }
    
    @Override
    @Transactional
    public boolean deleteFriend(Long friendshipId, Long userId) {
        Friend friendship = getById(friendshipId);
        if (friendship == null) {
            throw new RuntimeException("好友关系不存在");
        }
        
        // 验证权限：只有好友关系的参与者可以删除
        if (!friendship.getUserId().equals(userId) && !friendship.getFriendId().equals(userId)) {
            throw new RuntimeException("无权限删除此好友关系");
        }
        
        // 验证状态
        if (!friendship.getStatus().equals(FriendStatus.APPROVED.getCode())) {
            throw new RuntimeException("好友关系状态无效");
        }
        
        // 逻辑删除双向好友关系
        friendship.setStatus(FriendStatus.DELETED.getCode());
        boolean result = updateById(friendship);
        
        if (result) {
            // 删除反向好友关系
            LambdaQueryWrapper<Friend> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Friend::getUserId, friendship.getFriendId())
                       .eq(Friend::getFriendId, friendship.getUserId())
                       .eq(Friend::getStatus, FriendStatus.APPROVED.getCode());
            
            Friend reverseFriendship = getOne(queryWrapper);
            if (reverseFriendship != null) {
                reverseFriendship.setStatus(FriendStatus.DELETED.getCode());
                updateById(reverseFriendship);
            }
        }
        
        return result;
    }
    
    @Override
    public Page<FriendVO> getFriendList(Long userId, Integer page, Integer size, String keyword) {
        Page<Friend> friendPage = new Page<>(page, size);
        
        LambdaQueryWrapper<Friend> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Friend::getUserId, userId)
                   .eq(Friend::getStatus, FriendStatus.APPROVED.getCode())
                   .orderByDesc(Friend::getCreateTime);
        
        Page<Friend> result = page(friendPage, queryWrapper);
        
        // 转换为VO并填充用户信息
        Page<FriendVO> voPage = new Page<>();
        BeanUtil.copyProperties(result, voPage, "records");
        
        List<FriendVO> friendVOList = result.getRecords().stream()
                .map(friend -> convertToVO(friend, userId))
                .filter(friendVO -> {
                    // 关键词搜索
                    if (StrUtil.isNotBlank(keyword)) {
                        UserVO friendInfo = friendVO.getFriendInfo();
                        if (friendInfo != null) {
                            return friendInfo.getUsername().contains(keyword) ||
                                   (friendInfo.getNickname() != null && friendInfo.getNickname().contains(keyword));
                        }
                        return false;
                    }
                    return true;
                })
                .collect(Collectors.toList());
        
        voPage.setRecords(friendVOList);
        return voPage;
    }
    
    @Override
    public Page<FriendVO> getFriendRequests(Long userId, Integer page, Integer size) {
        Page<Friend> friendPage = new Page<>(page, size);
        
        LambdaQueryWrapper<Friend> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Friend::getFriendId, userId)
                   .eq(Friend::getStatus, FriendStatus.PENDING.getCode())
                   .orderByDesc(Friend::getCreateTime);
        
        Page<Friend> result = page(friendPage, queryWrapper);
        
        // 转换为VO
        Page<FriendVO> voPage = new Page<>();
        BeanUtil.copyProperties(result, voPage, "records");
        
        List<FriendVO> friendVOList = result.getRecords().stream()
                .map(friend -> convertToVO(friend, userId))
                .collect(Collectors.toList());
        
        voPage.setRecords(friendVOList);
        return voPage;
    }
    
    @Override
    public Page<FriendVO> getMySentRequests(Long userId, Integer page, Integer size) {
        Page<Friend> friendPage = new Page<>(page, size);
        
        LambdaQueryWrapper<Friend> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Friend::getUserId, userId)
                   .in(Friend::getStatus, FriendStatus.PENDING.getCode(), FriendStatus.REJECTED.getCode())
                   .orderByDesc(Friend::getCreateTime);
        
        Page<Friend> result = page(friendPage, queryWrapper);
        
        // 转换为VO
        Page<FriendVO> voPage = new Page<>();
        BeanUtil.copyProperties(result, voPage, "records");
        
        List<FriendVO> friendVOList = result.getRecords().stream()
                .map(friend -> convertToVO(friend, userId))
                .collect(Collectors.toList());
        
        voPage.setRecords(friendVOList);
        return voPage;
    }
    
    @Override
    public boolean isFriend(Long userId1, Long userId2) {
        Friend friendship = baseMapper.selectFriendship(userId1, userId2);
        return friendship != null && friendship.getStatus().equals(FriendStatus.APPROVED.getCode());
    }
    
    @Override
    public boolean hasApplied(Long userId, Long friendId) {
        Friend application = baseMapper.selectPendingOrApprovedApplication(userId, friendId);
        return application != null;
    }
    
    @Override
    public FriendVO getFriendshipById(Long friendshipId, Long userId) {
        Friend friendship = getById(friendshipId);
        if (friendship == null) {
            return null;
        }
        
        // 验证权限
        if (!friendship.getUserId().equals(userId) && !friendship.getFriendId().equals(userId)) {
            throw new RuntimeException("无权限查看此好友关系");
        }
        
        return convertToVO(friendship, userId);
    }
    
    /**
     * 将Friend实体转换为FriendVO
     */
    private FriendVO convertToVO(Friend friend, Long currentUserId) {
        FriendVO friendVO = new FriendVO();
        BeanUtil.copyProperties(friend, friendVO);
        
        // 设置状态描述
        friendVO.setStatusDesc(FriendStatus.getDescByCode(friend.getStatus()));
        
        // 确定好友用户ID和当前用户关系
        Long friendUserId = friend.getUserId().equals(currentUserId) ? friend.getFriendId() : friend.getUserId();
        friendVO.setIsInitiator(friend.getUserId().equals(currentUserId));
        
        // 获取好友用户信息
        UserVO friendInfo = userService.getUserById(friendUserId);
        friendVO.setFriendInfo(friendInfo);
        
        return friendVO;
    }
} 