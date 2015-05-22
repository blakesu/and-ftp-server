package com.github.andftpserver;

import java.util.Arrays;

import org.apache.ftpserver.ftplet.Authentication;
import org.apache.ftpserver.ftplet.AuthenticationFailedException;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.User;
import org.apache.ftpserver.usermanager.PasswordEncryptor;
import org.apache.ftpserver.usermanager.UsernamePasswordAuthentication;
import org.apache.ftpserver.usermanager.impl.AbstractUserManager;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.ConcurrentLoginPermission;
import org.apache.ftpserver.usermanager.impl.TransferRatePermission;
import org.apache.ftpserver.usermanager.impl.WritePermission;

class MemeoryUserManager extends AbstractUserManager {
    private BaseUser testUser;
    
    private String TEST_USER_FTP_ROOT = "/sdcard"; 
    private String TEST_USERNAME = "admin"; 
    private String TEST_PASSWORD = "admin"; 

    public MemeoryUserManager(String adminName, PasswordEncryptor passwordEncryptor) {
        super(adminName, passwordEncryptor);

        testUser = new BaseUser();
        testUser.setAuthorities(Arrays.asList(new Authority[] {new ConcurrentLoginPermission(0, 0), new WritePermission(), new TransferRatePermission(0,0)}));
        testUser.setEnabled(true);
        testUser.setHomeDirectory(TEST_USER_FTP_ROOT);
        testUser.setMaxIdleTime(0);
        testUser.setName(TEST_USERNAME);
        testUser.setPassword(TEST_PASSWORD);
    }

    @Override
    public User getUserByName(String username) throws FtpException {
        if(TEST_USERNAME.equals(username)) {
            return testUser;
        } 
        return null;
    }

    @Override
    public String[] getAllUserNames() throws FtpException {
        return new String[] {TEST_USERNAME};
    }

    @Override
    public void delete(String username) throws FtpException {
        //no opt
    }

    @Override
    public void save(User user) throws FtpException {
        //no opt
        System.out.println("save");
    }

    @Override
    public boolean doesExist(String username) throws FtpException {
        return (TEST_USERNAME.equals(username)) ? true : false;
    }

    @Override
    public User authenticate(Authentication authentication) throws AuthenticationFailedException {
        if(UsernamePasswordAuthentication.class.isAssignableFrom(authentication.getClass())) {
            UsernamePasswordAuthentication upAuth = (UsernamePasswordAuthentication) authentication;

            if(TEST_USERNAME.equals(upAuth.getUsername()) && TEST_PASSWORD.equals(upAuth.getPassword())) {
                return testUser;
            }

        } 
        return null;
    }
}

