package com.smm.framework.authority;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * @author Alan Chen
 * @description
 * @date 2019-09-21
 */
@Data
public class LoginAccount {

    @NotBlank(message = "登陆账号不能为空")
    @ApiModelProperty(value = "登陆账号")
    private String accountName;

    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(value = "登陆密码")
    private String password;

}
