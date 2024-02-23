-- --------------------------------------------------------
-- 호스트:                          127.0.0.1
-- 서버 버전:                        10.5.8-MariaDB - mariadb.org binary distribution
-- 서버 OS:                        Win64
-- HeidiSQL 버전:                  12.5.0.6677
-- --------------------------------------------------------

-- network 데이터베이스 구조 내보내기
CREATE DATABASE IF NOT EXISTS `network`
USE `network`;

-- 테이블 network.event 구조 내보내기
CREATE TABLE IF NOT EXISTS `event` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '식별자',
  `event_type` varchar(1000) NOT NULL DEFAULT '' COMMENT '사건_유형',
  `client_addr` varchar(1000) NOT NULL DEFAULT '' COMMENT '사건_발생_주체_주소',
  `user_id` varchar(1000) NOT NULL DEFAULT '' COMMENT '사건_발생_주체_아이디',
  `event_result` varchar(1000) NOT NULL DEFAULT '' COMMENT '사건_결과',
  `event_date_time` datetime NOT NULL DEFAULT current_timestamp() COMMENT '사건_발생_일시',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='사건 기록 테이블';

-- 테이블 network.host 구조 내보내기
CREATE TABLE IF NOT EXISTS `host` (
  `ip` varchar(15) NOT NULL DEFAULT '' COMMENT 'IP주소',
  `name` varchar(1000) NOT NULL DEFAULT '' COMMENT '호스트명',
  `create_date_time` datetime NOT NULL DEFAULT current_timestamp() COMMENT '생성일시',
  `update_date_time` datetime DEFAULT NULL COMMENT '수정일시',
  PRIMARY KEY (`ip`,`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='호스트 테이블';

-- 테이블 network.user 구조 내보내기
CREATE TABLE IF NOT EXISTS `user` (
  `id` varchar(20) NOT NULL COMMENT '아이디',
  `password` varchar(1000) NOT NULL COMMENT '비밀번호',
  `role` varchar(5) DEFAULT 'USER' COMMENT '사용자등급',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='사용자 테이블- 사용자 등급은 USER(일반사용자) / ADMIN(관리자)로 구분';

-- 테이블 데이터 network.user:~2 rows 내보내기
INSERT INTO `user` (`id`, `password`, `role`) VALUES
	('admin', '$2a$10$tiozuEO/6A4qCG8cwSSJgubR6O4E.6qyNEX96F5tbbLogaVXrj9JC', 'ADMIN'),
	('user', '$2a$10$tiozuEO/6A4qCG8cwSSJgubR6O4E.6qyNEX96F5tbbLogaVXrj9JC', 'USER');