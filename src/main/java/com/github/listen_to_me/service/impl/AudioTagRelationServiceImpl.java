package com.github.listen_to_me.service.impl;

import com.github.listen_to_me.domain.entity.AudioTagRelation;
import com.github.listen_to_me.mapper.AudioTagRelationMapper;
import com.github.listen_to_me.service.IAudioTagRelationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 音频标签关联表 服务实现类
 * </p>
 *
 * @author ListenToMe Team
 * @since 2026-03-25
 */
@Service
public class AudioTagRelationServiceImpl extends ServiceImpl<AudioTagRelationMapper, AudioTagRelation> implements IAudioTagRelationService {

}
