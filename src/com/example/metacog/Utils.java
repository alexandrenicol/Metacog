package com.example.metacog;

/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;


public class Utils {
  /**
   * Read XML as DOM.
   */
  public static Document readXml(InputStream is) throws SAXException, IOException,
      ParserConfigurationException {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

      dbf.setValidating(false);
      dbf.setIgnoringComments(false);
      dbf.setIgnoringElementContentWhitespace(true);
      dbf.setNamespaceAware(true);
      // dbf.setCoalescing(true);
      // dbf.setExpandEntityReferences(true);

      DocumentBuilder db = null;
      db = dbf.newDocumentBuilder();
      db.setEntityResolver(new NullResolver());

      // db.setErrorHandler( new MyErrorHandler());

      return db.parse(is);
  }
  
  public static String SaveRenamePic(String source, String picName){
	  /** 
	   * Copy the file from source in the Metacog folder and rename it with the name picName
	   */
	  String[] sourceSplit = source.split("\\.");

	  String extension = "."+sourceSplit[sourceSplit.length-1];
	  
	  File theDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Metacog/Img");  // Defining Directory/Folder Name  
		try{   
		    if (!theDir.exists()){  // Checks that Directory/Folder Doesn't Exists!  
		    	boolean result = theDir.mkdir();    
		    	if(result){  
		    	//JOptionPane.showMessageDialog(null, "New Folder created!");
		    	}
		    }
		}catch(Exception e){  
		    //JOptionPane.showMessageDialog(null, e);  
		}
	  
	  String picPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Metacog/Img/"+picName+extension;
	  
	  
	  File imgSrcFile = new File(source);
	  File imgNewFile = new File(picPath);
	  
	//  imgSrcFile.renameTo(imgNewFile);
	  
	  if (imgSrcFile.exists()) {
			Bitmap myBitmap = BitmapFactory.decodeFile(imgSrcFile.getAbsolutePath());
			
			int maxSize = 300;
			
			int outWidth;
			int outHeight;
			int inWidth = myBitmap.getWidth();
			int inHeight = myBitmap.getHeight();
			 
			if (inWidth > inHeight){
			
			outWidth = maxSize;
			outHeight = (inHeight * maxSize ) / inWidth;
			
			}else{
			
			outHeight = maxSize;
			outWidth = (inWidth * maxSize ) / inHeight;
			
			}
			
			Bitmap resizedBitmap = Bitmap.createScaledBitmap(myBitmap, outWidth, outHeight, false );
			
			try {
				imgNewFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			FileOutputStream ostream;
			try {
				ostream = new FileOutputStream(imgNewFile);
				resizedBitmap.compress(CompressFormat.PNG, 1, ostream);
				ostream.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	  }
	  return picPath;
  }
  
  public static Uri formatImageSource (String originalSource){
	  /**
	   * This function determinates if the originalSource is from the resources of the app
	   * or from the sd card
	   * 
	   * Parameters :
	   * 	originalSource : a String which is the original source path for an image
	   * 
	   * Return : 
	   * 	outputUri : a Uri which is the uri to the image
	   * 
	   * Example of use :
	   * 	Uri myUri = Utils.formatImageSource(source);
	   */
	  Uri outputUri ;
	  if (originalSource.startsWith("drawable")){
		  outputUri = Uri.parse("android.resource://com.example.metacog/"+originalSource);
      }else{
      	File file1 = new File (originalSource);
      	outputUri = Uri.fromFile(file1);
      }
	  
	  return outputUri;
  }
}

class NullResolver implements EntityResolver {
  public InputSource resolveEntity(String publicId, String systemId) throws SAXException,
      IOException {
    return new InputSource(new StringReader(""));
  }
}