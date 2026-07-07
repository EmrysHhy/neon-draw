-- 设置客户端与服务器之间的字符集为utf8mb4，这个字符集可以存储任何Unicode字符。
SET NAMES utf8mb4;
-- 关闭外键约束检查，这通常在创建或修改表结构时使用，以避免由于外键约束而导致的创建失败。
SET FOREIGN_KEY_CHECKS = 0;

drop database IF EXISTS `neon_draw`;
create DATABASE `neon_draw` CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE `neon_draw`;
-- ----------------------------
-- Table structure for admin（管理员，可登录，邮箱/手机号全局唯一）
-- ----------------------------
drop table IF EXISTS `normal_user`;
drop table IF EXISTS `admin`;
drop table IF EXISTS `user`;
create TABLE `admin`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT comment '管理员id',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP comment '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON update CURRENT_TIMESTAMP comment '更新时间',
  `user_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL comment '姓名',
  `email` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL comment '邮箱',
  `phone_number` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL comment '手机号',
  `password` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL comment '登录密码',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_admin_email`(`email`(30) ASC) USING BTREE,
  UNIQUE INDEX `uk_admin_phone`(`phone_number`(11) ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for normal_user（普通用户/参与人员，不登录，无邮箱/手机号唯一约束）
-- ----------------------------
create TABLE `normal_user`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT comment '用户id',
  `admin_id` bigint UNSIGNED NOT NULL comment '所属管理员id',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP comment '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON update CURRENT_TIMESTAMP comment '更新时间',
  `user_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL comment '用户姓名',
  `email` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL comment '邮箱',
  `phone_number` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL comment '手机号',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_admin_id`(`admin_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for prize
-- ----------------------------
drop table IF EXISTS `prize`;
create TABLE `prize`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT comment '主键',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP comment '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON update CURRENT_TIMESTAMP comment '更新时间',
  `admin_id` bigint UNSIGNED NOT NULL comment '所属管理员id',
  `name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL comment '奖品名称',
  `description` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL comment '奖品描述',
  `price` decimal(10, 2) NOT NULL comment '奖品价值',
  `image_url` varchar(2048) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL comment '奖品展示图',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for group（小组信息，id 自增；group 为保留字须反引号）
-- ----------------------------
drop table IF EXISTS `group_member`;
drop table IF EXISTS `group`;
create TABLE `group`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT comment '小组id',
  `admin_id` bigint UNSIGNED NOT NULL comment '所属管理员id',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP comment '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON update CURRENT_TIMESTAMP comment '更新时间',
  `name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL comment '小组名称',
  `description` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL comment '小组描述',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_admin_group_name`(`admin_id` ASC, `name`(50) ASC) USING BTREE,
  INDEX `idx_admin_id`(`admin_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for group_member（小组成员）
-- ----------------------------
create TABLE `group_member`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT comment '主键',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP comment '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON update CURRENT_TIMESTAMP comment '更新时间',
  `group_id` bigint UNSIGNED NOT NULL comment '小组id，对应 group.id',
  `user_id` bigint UNSIGNED NOT NULL comment '成员用户id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_group_user`(`group_id` ASC, `user_id` ASC) USING BTREE,
  INDEX `idx_group_id`(`group_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for activity
-- ----------------------------
drop table IF EXISTS `activity`;
create TABLE `activity`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT comment '主键',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP comment '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON update CURRENT_TIMESTAMP comment '更新时间',
  `activity_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL comment '活动名称',
  `description` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL comment '活动描述',
  `status` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL comment '活动状态',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;
-- ENGINE = InnoDB：指定表的存储引擎为InnoDB，这是MySQL的默认存储引擎，支持事务、外键等特性。
-- AUTO_INCREMENT = 24：为自动增长的ID字段设置起始值。
-- ROW_FORMAT = DYNAMIC：设置行的存储格式为动态，允许行随着数据的变化而变化。

-- ----------------------------
-- Table structure for activity_prize
-- ----------------------------
drop table IF EXISTS `activity_prize`;
create TABLE `activity_prize`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT comment '主键',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP comment '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON update CURRENT_TIMESTAMP comment '更新时间',
  `activity_id` bigint NOT NULL comment '活动id',
  `prize_id` bigint NOT NULL comment '活动关联的奖品id',
  `prize_amount` bigint NOT NULL DEFAULT 1 comment '关联奖品的数量',
  `prize_tiers` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL comment '奖品等级',
  `status` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL comment '活动奖品状态',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_id`(`id` ASC) USING BTREE,
  UNIQUE INDEX `uk_a_p_id`(`activity_id` ASC, `prize_id` ASC) USING BTREE,
  INDEX `idx_activity_id`(`activity_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for activity_user
-- ----------------------------
drop table IF EXISTS `activity_user`;
create TABLE `activity_user`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT comment '主键',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP comment '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON update CURRENT_TIMESTAMP comment '更新时间',
  `activity_id` bigint NOT NULL comment '活动时间',
  `user_id` bigint NOT NULL comment '圈选的用户id',
  `user_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL comment '用户名',
  `status` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL comment '用户状态',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_id`(`id` ASC) USING BTREE,
  UNIQUE INDEX `uk_a_u_id`(`activity_id` ASC, `user_id` ASC) USING BTREE,
  INDEX `idx_activity_id`(`activity_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for winning_record
-- ----------------------------
drop table IF EXISTS `winning_record`;
create TABLE `winning_record`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT comment '主键',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP comment '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON update CURRENT_TIMESTAMP comment '更新时间',
  `activity_id` bigint NOT NULL comment '活动id',
  `activity_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL comment '活动名称',
  `prize_id` bigint NOT NULL comment '奖品id',
  `prize_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL comment '奖品名称',
  `prize_tier` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL comment '奖品等级',
  `winner_id` bigint NOT NULL comment '中奖人id',
  `winner_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL comment '中奖人姓名',
  `winner_email` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL comment '中奖人邮箱',
  `winner_phone_number` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL comment '中奖人电话',
  `winning_time` datetime NOT NULL comment '中奖时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_id`(`id` ASC) USING BTREE,
  UNIQUE INDEX `uk_w_a_p_id`(`winner_id` ASC, `activity_id` ASC, `prize_id` ASC) USING BTREE,
  INDEX `idx_activity_id`(`activity_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 69 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- SET FOREIGN_KEY_CHECKS = 1;：在脚本的最后，重新开启外键约束检查。
SET FOREIGN_KEY_CHECKS = 1;
