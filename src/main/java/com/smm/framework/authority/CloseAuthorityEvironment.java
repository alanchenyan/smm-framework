package com.smm.framework.authority;


import lombok.Data;

/**
 * @author Alan Chen
 * @description
 * @date 2019-10-12
 */
@Data
public class CloseAuthorityEvironment {

    public CloseAuthorityEvironment(){
        super();
    }

    public CloseAuthorityEvironment(String currentRunEnvironment, String... closeAuthEnvironments) {
        this.currentRunEnvironment = currentRunEnvironment;
        this.closeAuthEnvironments = closeAuthEnvironments;
    }

    private String currentRunEnvironment;

    private String[] closeAuthEnvironments;

}
