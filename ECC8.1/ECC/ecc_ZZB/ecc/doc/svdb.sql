-- ----------------------------
-- Table structure for report
-- ----------------------------
CREATE TABLE `report` (
`id` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
`createTime` varchar(19) COLLATE utf8_unicode_ci NOT NULL,
`name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
PRIMARY KEY (`id`,`createTime`),
UNIQUE KEY `REPORT_INDEX` (`id`,`createTime`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for reportdata
-- ----------------------------
CREATE TABLE `reportdata` (
`reportId` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
`reportCreateTime` varchar(19) COLLATE utf8_unicode_ci NOT NULL,
`skey` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
`value` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
PRIMARY KEY (`reportId`,`reportCreateTime`,`skey`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


