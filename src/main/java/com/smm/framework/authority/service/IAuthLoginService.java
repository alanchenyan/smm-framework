package com.smm.framework.authority.service;

import com.smm.framework.authority.LoginAccount;

/**
 * @author Alan Chen
 * @description
 * @date 2019-09-21
 */
public interface IAuthLoginService {

    LoginAccount getLoginAccount(String accountName);
}
