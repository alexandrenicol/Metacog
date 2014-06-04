package com.example.metacog;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
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
import android.widget.Toast;

public class BroadcastService {
	
	private static final String TAG = "BcastService";
	public static String PATH = "test";
    private static final boolean D = true;
    
    public static final int MESSAGE_READ = 1;
    public static final int MESSAGE_WRITE = 2;
    public static final int MESSAGE_READ_TCPIP = 3;
    
    
	private final Handler mHandler;
    private ComThread mConnectedThread;
    public ComTCPThread mConnectedTCPThread;
    public String ipServer = "";
    Context mContext ;
    
    public BroadcastService(Context context, Handler handler) {
        //mAdapter = BluetoothAdapter.getDefaultAdapter();
    	mContext = context;
        mHandler = handler;
        mConnectedThread = new ComThread();
        mConnectedThread.start();
        mConnectedTCPThread = new ComTCPThread();
        //mConnectedTCPThread.start();
    }    

    public synchronized void start() {
        if (D) Log.d(TAG, "start");
        
        mConnectedThread = new ComThread();
        mConnectedThread.start();
        mConnectedTCPThread = new ComTCPThread();
       // mConnectedTCPThread.start();
    }
    /**
     * Stop thread
     */
    public synchronized void stop() {
        if (D) Log.d(TAG, "stop");
        if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}
        if (mConnectedTCPThread != null) {mConnectedTCPThread.cancel(); mConnectedTCPThread = null;}
    }

    
    public void write(byte[] out) {
    	if (D) Log.d(TAG, "Write");
        mConnectedThread.write(out);
    }
    
    public void writeIP() {
    	if (D) Log.d(TAG, "Write IP");
        mConnectedThread.write(getIPAddress().getBytes());
        Toast toast = Toast.makeText(mContext,R.string.envoi_ip, Toast.LENGTH_SHORT);
		toast.show();
    }
    
    public void writeTCPIP(){
    	if (D) Log.d(TAG, "Write TCP/IP");
    	/*mConnectedTCPThread.writeTcpIp("/Img/module01_serie03_question01_image01.jpg");*/
    	mConnectedTCPThread.writeTcpIp("/structure_modules.xml");
    	File dirImg = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Metacog/Img/");
    	String[] imgs=dirImg.list();
    	for (String img : imgs) {
    		mConnectedTCPThread.writeTcpIp("/Img/"+img);
		}
    }
    
    private class ComThread extends Thread {
        
    	private static final int BCAST_PORT = 8020;
    	DatagramSocket mSocket ;
    	InetAddress myBcastIP;
    	String myLocalIP;
    	
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
        
        public void cancel() {
            try {
                mSocket.close();
            } catch (Exception e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
        
        public void run() {
        	
    		try {
    			
    			byte[] buf = new byte[1024]; 
    			
    			//Listen on socket to receive messages 
    			while (true) { 
    				DatagramPacket packet = new DatagramPacket(buf, buf.length); 
	    			mSocket.receive(packet); 
	
	    			InetAddress remoteIP = packet.getAddress();
	    			if(remoteIP.equals(myLocalIP))
	    				continue;
	    			
	    			String s = new String(packet.getData(), 0, packet.getLength()); 
	    			if(D)Log.d(TAG, "Received response " + s); 
	    			// Send the obtained bytes to the UI Activity
	    			mHandler.obtainMessage(BroadcastService.MESSAGE_READ,-1,-1, s)
	    			.sendToTarget(); 
	    			
	    		} 
			} catch (IOException e) {
    			e.printStackTrace();
			}
        }
        
        /**
          * Write broadcast packet.
          */
        public void write(byte[] buffer) {
        	 try {
             	String data = new String (buffer);

                 DatagramPacket packet = new DatagramPacket(data.getBytes(), data.length(), 
                 		myBcastIP, BCAST_PORT);
                 
                 mSocket.send(packet); 
                 // Share the sent message back to the UI Activity
                 mHandler.obtainMessage(BroadcastService.MESSAGE_WRITE, -1, -1, buffer).sendToTarget();
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
    
 class ComTCPThread extends Thread {
        
    	private static final int TCP_PORT = 9666;
    	String myLocalIP ;
    	Socket sock;
    	ServerSocket servsock;
    	boolean xml;
    	boolean image;
    	String nameFile;
    	
        public ComTCPThread() {
        		   myLocalIP = getIPTCPAddress();
        		   if(D)Log.d(TAG,"my local ip : "+myLocalIP);   
        }
        
        public void cancel() {
            try {
                sock.close();
            } catch (Exception e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
        
        public void run() {
        	
        	try {
        			int bytesRead;
        			int current = 0;
        			byte [] mybytearray  = new byte [900000];
        			while(true){
        			sock = new Socket(ipServer,TCP_PORT);
        			BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        			PATH = in.readLine();
        			
        			InputStream is = sock.getInputStream();
        			bytesRead= is.read(mybytearray);
        			current = bytesRead;
        			do {
						bytesRead = is.read(mybytearray, current, (mybytearray.length-current));
						if(bytesRead >= 0) current += bytesRead;
					} while(bytesRead > -1);
        			FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Metacog"+PATH);
					BufferedOutputStream bos = new BufferedOutputStream(fos);
					bos.write(mybytearray,0,current);
					
        			sock.close();
        			 
        			 mHandler.obtainMessage(BroadcastService.MESSAGE_READ_TCPIP,-1,-1).sendToTarget();
        			}
				} catch ( UnknownHostException e ) {
				Log.i("******* :( ", "UnknownHostException");
			} catch (IOException e){
				Log.i("Read has IOException", "e: " + e);
			}
        }
        
        public void writeTcpIp(String path){
        	try {
        		servsock = new ServerSocket(TCP_PORT);
        		Log.i("************", "Waiting...");
				Socket sock = servsock.accept(); // blocks until connection opened
				try{
					OutputStream out = sock.getOutputStream();
					String outgoingMsg = path+ System.getProperty("line.separator");
					File myFile = new File (Environment.getExternalStorageDirectory().getAbsolutePath()+"/Metacog"+path);
					byte [] mybytearray  = new byte [(int)myFile.length()];
					FileInputStream fis = new FileInputStream(myFile);
					BufferedInputStream bis = new BufferedInputStream(fis);
					out.write(outgoingMsg.getBytes());
					bis.read(mybytearray,0,mybytearray.length);
					out.write(mybytearray,0,mybytearray.length);
					out.flush();
					sock.close();
				}catch (IOException e) {
					Log.i("Io execption ", "e: " + e);
				}
				servsock.close();
				/*Thread t = new WriteTCPThread(sock, path);
				t.start();*/
			} catch (IOException e) {
				Log.i("Io execption ", "e: " + e);
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
    
    public String getIPTCPAddress() {
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

}

class WriteTCPThread extends Thread {
	public Socket sock;
	public String path;

	public WriteTCPThread(Socket s,String p) {
		sock=s;
		path=p;
	}
	
	public void run(){
		try{
			OutputStream out = sock.getOutputStream();
			String outgoingMsg = path+ System.getProperty("line.separator");
			File myFile = new File (Environment.getExternalStorageDirectory().getAbsolutePath()+"/Metacog"+path);
			byte [] mybytearray  = new byte [(int)myFile.length()];
			FileInputStream fis = new FileInputStream(myFile);
			BufferedInputStream bis = new BufferedInputStream(fis);
			out.write(outgoingMsg.getBytes());
			bis.read(mybytearray,0,mybytearray.length);
			out.write(mybytearray,0,mybytearray.length);
			out.flush();
			sock.close();
		}catch (IOException e) {
			Log.i("Io execption ", "e: " + e);
		}
	}
}

/*class ListenTCPThread extends Thread {
	public Socket sock;
	public String path;
	private static final int TCP_PORT = 9666;
	ServerSocket servsock;
	List<InetAddress> connect = new ArrayList<InetAddress>();
	public ListenTCPThread() {

	}
	
	public void run(){
		while(true){
			try{
				servsock = new ServerSocket(TCP_PORT);
        		Log.i("************", "Waiting...");
				sock = servsock.accept(); // blocks until connection opened
				servsock.close();
				if(!connect.contains(sock.getInetAddress())){
					connect.add(sock.getInetAddress());
					write("/structure_modules.xml");
			    	File dirImg = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Metacog/Img/");
			    	String[] imgs=dirImg.list();
			    	for (String img : imgs) {
			    		write("/Img/"+img);
					}
				}
			}catch (IOException e) {
				Log.i("Io execption ", "e: " + e);
			}
		}
	}
	
	public void write(String p){
		Thread t = new WriteTCPThread(sock, p);
		t.start();
	}
}*/

