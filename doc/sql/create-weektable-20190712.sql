SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_week
-- ----------------------------
DROP TABLE IF EXISTS `t_week`;
CREATE TABLE `t_week` (
  `week_name` varchar(255) COLLATE utf8_bin DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_week
-- ----------------------------
BEGIN;
INSERT INTO `t_week` VALUES ('Monday');
INSERT INTO `t_week` VALUES ('Tuesday');
INSERT INTO `t_week` VALUES ('Wednesday');
INSERT INTO `t_week` VALUES ('Thursday');
INSERT INTO `t_week` VALUES ('Friday');
INSERT INTO `t_week` VALUES ('Saturday');
INSERT INTO `t_week` VALUES ('Sunday');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
