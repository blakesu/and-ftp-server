package com.github.andftpserver;

import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.usermanager.ClearTextPasswordEncryptor;
import org.apache.ftpserver.usermanager.UserManagerFactory;

class MemoryUserManagerFactory implements UserManagerFactory {

    @Override
    public UserManager createUserManager() {
        return new MemeoryUserManager("admin", new ClearTextPasswordEncryptor());
    }
}
