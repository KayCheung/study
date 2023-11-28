package com.housaire.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.housaire.dal.domain.SendLog;
import com.housaire.security.crypto.Encryption;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Desc:
 *
 * @author: Zhang Kai
 * @email: <a href="mailto:zhangkai@kltb.com.cn">zhangkai@kltb.com.cn</a>
 * @date: 2020/8/18
 */
public interface SendLogMapper extends BaseMapper<SendLog> {

    List<SendLog> selectSendLogs(@Encryption @Param("mobile") String mobile, @Param("status") int status);

}
