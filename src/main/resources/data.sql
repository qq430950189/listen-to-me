SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 1. RBAC权限域 - 幂等插入
-- ----------------------------
INSERT IGNORE INTO `sys_role` (`id`, `role_name`, `role_code`) VALUES
(1, '超级管理员', 'ROLE_ADMIN'),
(2, '音频创作者', 'ROLE_CREATOR'),
(3, '普通听众', 'ROLE_USER');

INSERT IGNORE INTO `sys_permission` (`id`, `perm_name`, `perm_code`) VALUES
(1, '音频上传', 'audio:upload'),
(2, '音频删除', 'audio:delete'),
(3, '音频审核', 'audio:audit'),
(4, '订单管理', 'order:manage'),
(5, '个人中心', 'user:info');

-- 用户密码: 123456 (BCrypt哈希)
INSERT IGNORE INTO `sys_user` (`id`, `username`, `password`, `nickname`, `avatar`, `phone`, `openid`, `is_creator`, `balance`, `frozen_balance`, `version`) VALUES
(1, 'admin', '$2a$10$3HfMvHbj1A9/gEajtqy2G.GTbKl3joKmJ/J7RDo9uDoOlqe/MIoS.', '系统管理员', 'https://avatar.com/admin.jpg', '13800138000', 'wx_admin_001', 0, 0.00, 0.00, 0),
(2, 'creator_01', '$2a$10$3HfMvHbj1A9/gEajtqy2G.GTbKl3joKmJ/J7RDo9uDoOlqe/MIoS.', '有声的小雅', 'https://avatar.com/creator01.jpg', '13800138001', 'wx_creator_001', 1, 1256.88, 320.50, 0),
(3, 'listener_01', '$2a$10$3HfMvHbj1A9/gEajtqy2G.GTbKl3joKmJ/J7RDo9uDoOlqe/MIoS.', '听书小迷弟', 'https://avatar.com/listener01.jpg', '13800138002', 'wx_listener_001', 0, 89.60, 0.00, 0),
(4, 'listener_02', '$2a$10$3HfMvHbj1A9/gEajtqy2G.GTbKl3joKmJ/J7RDo9uDoOlqe/MIoS.', '深夜听众', 'https://avatar.com/listener02.jpg', '13800138003', 'wx_listener_002', 0, 56.20, 0.00, 0),
(5, 'creator_02', '$2a$10$3HfMvHbj1A9/gEajtqy2G.GTbKl3joKmJ/J7RDo9uDoOlqe/MIoS.', '老杨说故事', 'https://avatar.com/creator02.jpg', '13800138004', 'wx_creator_002', 1, 890.30, 156.20, 0);

INSERT IGNORE INTO `sys_user_role` (`user_id`, `role_id`) VALUES
(1, 1), (2, 2), (5, 2), (3, 3), (4, 3);

INSERT IGNORE INTO `sys_role_permission` (`role_id`, `perm_id`) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5),
(2, 1), (2, 2), (2, 5),
(3, 5);

-- ----------------------------
-- 2. 音频资产域 - 幂等插入
-- ----------------------------
INSERT IGNORE INTO `audio_info` (`id`, `creator_id`, `title`, `cover_url`, `raw_path`, `hls_path`, `price`, `trial_duration`, `audit_status`, `status`, `view_count`) VALUES
(1, 2, '心理学入门30讲', 'https://cover.com/psy30.jpg', 'minio/audio/psy30_raw.mp3', 'minio/audio/psy30_hls.m3u8', 29.90, 60, 1, 2, 1568),
(2, 2, '职场沟通技巧', 'https://cover.com/office_talk.jpg', 'minio/audio/office_raw.mp3', 'minio/audio/office_hls.m3u8', 19.90, 45, 1, 2, 892),
(3, 5, '民间故事大全', 'https://cover.com/story.jpg', 'minio/audio/story_raw.mp3', 'minio/audio/story_hls.m3u8', 9.90, 30, 1, 2, 2356),
(4, 5, '悬疑短篇合集', 'https://cover.com/suspense.jpg', 'minio/audio/suspense_raw.mp3', NULL, 15.90, 40, 0, 1, 328),
(5, 2, '负能量清理指南', 'https://cover.com/negative.jpg', 'minio/audio/negative_raw.mp3', NULL, 12.90, 30, 2, 0, 105);

INSERT IGNORE INTO `audio_transcript` (`audio_id`, `full_text`, `segment_json`) VALUES
(1, '大家好，今天我们开始心理学入门的第一讲...', '[{"time":0,"title":"第1讲：心理学的定义"}]'),
(2, '职场沟通中，倾听是最重要的环节...', '[{"time":0,"title":"沟通的核心：有效倾听"}]'),
(3, '今天给大家讲一个民间的狐仙故事...', '[{"time":0,"title":"民国狐仙故事"}]');

-- ----------------------------
-- 3. 交易/社交/服务域 - 幂等插入
-- ----------------------------
INSERT IGNORE INTO `order_info` (`order_sn`, `user_id`, `audio_id`, `pay_amount`, `pay_status`, `pay_channel`, `pay_time`) VALUES
('20250520123456', 3, 1, 29.90, 1, 'wechat', '2025-05-20 14:30:25'),
('20250521654321', 3, 3, 9.90, 1, 'alipay', '2025-05-21 09:15:40');

INSERT IGNORE INTO `sys_tag` (`id`, `name`) VALUES
(1, '心理学'), (2, '职场'), (3, '民间故事'), (4, '悬疑'), (5, '自我提升'), (6, '情感');

INSERT IGNORE INTO `audio_tag_relation` (`audio_id`, `tag_id`) VALUES
(1, 1), (2, 2), (3, 3), (4, 4), (5, 5);

INSERT IGNORE INTO `play_history` (`user_id`, `audio_id`, `last_position`) VALUES
(3, 1, 1250), (4, 2, 560);

INSERT IGNORE INTO `consult_slot` (`creator_id`, `start_time`, `end_time`, `status`) VALUES
(2, '2025-05-25 10:00:00', '2025-05-25 11:00:00', 0),
(5, '2025-05-26 09:00:00', '2025-05-26 10:00:00', 1);

SET FOREIGN_KEY_CHECKS = 1;