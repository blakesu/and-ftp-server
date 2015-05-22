package com.github.andftpserver;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.apache.ftpserver.ConnectionConfigFactory;
import org.apache.ftpserver.DataConnectionConfigurationFactory;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.listener.ListenerFactory;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class AndftpserverActivity extends Activity {
	
	protected TextView ftpUrlLabel;
	private int port = 8899;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ftpUrlLabel = (TextView) findViewById(R.id.ftpurl);
        
        FtpServerFactory serverFactory = new FtpServerFactory();
        
        ConnectionConfigFactory connectionConfigFactory = new ConnectionConfigFactory();
        connectionConfigFactory.setMaxLogins(10);
        connectionConfigFactory.setMaxThreads(10);
        serverFactory.setConnectionConfig(connectionConfigFactory.createConnectionConfig());
        
        serverFactory.setUserManager(new MemoryUserManagerFactory().createUserManager());
        
        ListenerFactory factory = new ListenerFactory();
        factory.setPort( port );
        DataConnectionConfigurationFactory dataConnectionConfigFactory = new DataConnectionConfigurationFactory();
        dataConnectionConfigFactory.setActiveEnabled(false);
        dataConnectionConfigFactory.setPassiveExternalAddress(getLocalIpAddress());
        factory.setDataConnectionConfiguration( dataConnectionConfigFactory.createDataConnectionConfiguration() );
        serverFactory.addListener("default", factory.createListener());
        
    	FtpServer server = serverFactory.createServer();
    	
        try {
			server.start();
			displayUrl();
		} catch (Throwable e) {
			Log.d("", e.toString());
		}
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	displayUrl();
    }
    
    private void displayUrl(){
    	String ip = getLocalIpAddress();
    	if( ip == null ){
    		ftpUrlLabel.setText("no available network");
    	}else{
    		ftpUrlLabel.setText(  "ftp://" + getLocalIpAddress() + ":" + port );
    	}
    }
    
    private String getLocalIpAddress() {  
        try {  
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {  
                NetworkInterface intf = en.nextElement();  
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {  
                    InetAddress inetAddress = enumIpAddr.nextElement();  
                    if (!inetAddress.isLoopbackAddress()) {  
                        return inetAddress.getHostAddress().toString();  
                    }  
                }  
            }  
        } catch (SocketException ex) {  
        	Log.e("", ex.toString());
        }  
        return null;  
    }  

}



