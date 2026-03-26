package com.github.listen_to_me.mapper;

import com.github.listen_to_me.domain.entity.AudioTagRelation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 音频标签关联表 Mapper 接口
 * </p>
 *
 * @author ListenToMe Team
 * @since 2026-03-25
 */
@Mapper
public interface AudioTagRelationMapper extends BaseMapper<AudioTagRelation> {

}
