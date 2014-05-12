package com.example.metacog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import org.apache.http.conn.util.InetAddressUtils;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

public class BroadcastService {
	
	private static final String TAG = "BcastService";
    private static final boolean D = true;
    
    public static final int MESSAGE_READ 	= 1;
    public static final int MESSAGE_WRITE 	= 2;
    public static final int MESSAGE_TOAST 	= 3;
    
	private final Handler mHandler;
    private ComThread mConnectedThread;

    Context mContext ;
    
    public BroadcastService(Context context, Handler handler) {
        //mAdapter = BluetoothAdapter.getDefaultAdapter();
    	mContext = context;
        mHandler = handler;
        mConnectedThread = new ComThread();
        mConnectedThread.start();
    }    

    public synchronized void start() {
        if (D) Log.d(TAG, "start");
        
        mConnectedThread = new ComThread();
        mConnectedThread.start();
    }
    /**
     * Stop thread
     */
    public synchronized void stop() {
        if (D) Log.d(TAG, "stop");
       // if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}
    }


    public void write(byte[] out) {
    	if (D) Log.d(TAG, "Write");
        mConnectedThread.write(out);
    }
    
    public void writeIP() {
    	if (D) Log.d(TAG, "Write IP");
        mConnectedThread.write(getIPAddress().getBytes());
    }
    private class ComThread extends Thread {
        
    	private static final int BCAST_PORT = 8000;
    	DatagramSocket mSocket ;
    	InetAddress myBcastIP;
    	String myLocalIP ;
    	
        public ComThread() {
        	
        	try { 
        		   myBcastIP = getBroadcastAddress();
        		   if(D)Log.d(TAG,"my bcast ip : "+myBcastIP);
        		   
        		   
        		   myLocalIP = getIPAddress();
        		   if(D)Log.d(TAG,"my local ip : "+myLocalIP);
        		   
        		   mSocket = new DatagramSocket(BCAST_PORT); 
        		   mSocket.setBroadcast(true); 
        	   
        	     } catch (IOException e) { 
        	    	 Log.e(TAG, "Could not make socket", e); 
        	     } 
        }
        

        public void run() {
        	
    		try {
    			
    			byte[] buf = new byte[400*1024]; 
    			
    			//Listen on socket to receive messages 
    			while (true) { 
	    			DatagramPacket packet = new DatagramPacket(buf, buf.length); 
	    			mSocket.receive(packet); 
	
	    			String remoteIP = packet.getAddress().toString();
	    			if(!remoteIP.equals(myLocalIP)){	    				
	
		    			String s = new String(packet.getData(), 0, packet.getLength()); 
		    			if(D)Log.d(TAG, "Received response " + s); 
		    			// Send the obtained bytes to the UI Activity
		    			mHandler.obtainMessage(MESSAGE_READ,-1,-1, s)
		    			.sendToTarget();
	    			
	    			}
	    		} 
			} catch (IOException e) {
    			e.printStackTrace();
			}
        }

        
        /**
          * Write broadcast packet.
          */
        public void write(byte[] buffer) {
        	//byte[] buf = new byte[64*1024]; 
            try {

            	String data = readFileAsString(Environment.getExternalStorageDirectory().getAbsolutePath()+"/structure_modules.xml");
                DatagramPacket packet = new DatagramPacket(data.getBytes(), data.length(),
                		myBcastIP, BCAST_PORT);
                
                mSocket.send(packet);
                // Share the sent message back to the UI Activity
                mHandler.obtainMessage(MESSAGE_WRITE, -1, -1, buffer)
                        .sendToTarget();
            } catch (Exception e) {
                Log.e(TAG, "Exception during write", e);
            }
        }


        /** 
         * Calculate the broadcast IP we need to send the packet along. 
         */ 
        private InetAddress getBroadcastAddress() throws IOException {
          WifiManager mWifi = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
          
          WifiInfo info = mWifi.getConnectionInfo();
  		  if(D)Log.d(TAG,"\n\nWiFi Status: " + info.toString());
  		
    	  // DhcpInfo  is a simple object for retrieving the results of a DHCP request
          DhcpInfo dhcp = mWifi.getDhcpInfo(); 
          if (dhcp == null) { 
            Log.d(TAG, "Could not get dhcp info"); 
            return null; 
          } 
       
          
          int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask; 
          byte[] quads = new byte[4]; 
          for (int k = 0; k < 4; k++) 
            quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
          
          // Returns the InetAddress corresponding to the array of bytes. 
          return InetAddress.getByAddress(quads);  // The high order byte is quads[0].
        }
    }
    
    public String getIPAddress() {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress().toUpperCase();
                        boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr); 
                            if (isIPv4) 
                                return sAddr;
                    }
                }
            }
        } catch (Exception ex) { } // for now eat exceptions
        return "";
    }

    public String readFileAsString(String filePath)
        throws java.io.IOException {
    StringBuilder fileData = new StringBuilder();
    BufferedReader reader = new BufferedReader(new FileReader(filePath));
    char[] buf = new char[1024];
    int numRead = 0;
    while ((numRead = reader.read(buf)) != -1) {
        fileData.append(buf, 0, numRead);
    }
    reader.close();
    return fileData.toString();
}
}

