package com.housaire.dal.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.housaire.security.crypto.Crypto;
import com.housaire.security.crypto.Decryption;
import com.housaire.security.crypto.Desensitization;
import com.housaire.security.crypto.Encryption;
import lombok.Data;

import java.util.Date;

/**
 * Desc:
 *
 * @author: Zhang Kai
 * @email: <a href="mailto:zhangkai@kltb.com.cn">zhangkai@kltb.com.cn</a>
 * @date: 2020/8/18
 */
@Crypto
@Data
@TableName("send_log")
public class SendLog {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String appid;

    private String templateCode;

    @Encryption
    @Decryption
    @Desensitization(Desensitization.Type.MOBILE_PHONE)
    private String mobile;

    private String clientIp;

    private String supplier;

    private Integer status;

    private String response;

    private Date createtime;

}
