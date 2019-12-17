SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_hours
-- ----------------------------
DROP TABLE IF EXISTS `t_hours`;
CREATE TABLE `t_hours`
(
  `hour` int(255) DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin;

-- ----------------------------
-- Records of t_hours
-- ----------------------------
BEGIN;
INSERT INTO `t_hours`
VALUES (0);
INSERT INTO `t_hours`
VALUES (1);
INSERT INTO `t_hours`
VALUES (3);
INSERT INTO `t_hours`
VALUES (2);
INSERT INTO `t_hours`
VALUES (4);
INSERT INTO `t_hours`
VALUES (5);
INSERT INTO `t_hours`
VALUES (6);
INSERT INTO `t_hours`
VALUES (7);
INSERT INTO `t_hours`
VALUES (8);
INSERT INTO `t_hours`
VALUES (9);
INSERT INTO `t_hours`
VALUES (10);
INSERT INTO `t_hours`
VALUES (11);
INSERT INTO `t_hours`
VALUES (12);
INSERT INTO `t_hours`
VALUES (13);
INSERT INTO `t_hours`
VALUES (14);
INSERT INTO `t_hours`
VALUES (15);
INSERT INTO `t_hours`
VALUES (16);
INSERT INTO `t_hours`
VALUES (17);
INSERT INTO `t_hours`
VALUES (18);
INSERT INTO `t_hours`
VALUES (19);
INSERT INTO `t_hours`
VALUES (20);
INSERT INTO `t_hours`
VALUES (21);
INSERT INTO `t_hours`
VALUES (22);
INSERT INTO `t_hours`
VALUES (23);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;