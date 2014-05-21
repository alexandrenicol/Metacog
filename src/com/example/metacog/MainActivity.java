package com.example.metacog;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity 
{ 
	private Button utilisateur;
	private Button admin;
	private Button quit;
	
	private String externalStorage;
	private String structureFilename;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		createMetacogFolder();
		
		createStructureModuleXML();
		
		utilisateur=(Button) findViewById(R.id.activity_main_utilisateur);
        utilisateur.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {

				Intent t = new Intent(MainActivity.this, LogActivity.class);
				startActivity(t);
				finish();
				
			}
		});
        admin=(Button) findViewById(R.id.activity_main_administrateur);
        admin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				Intent t = new Intent(MainActivity.this, AdminActivity.class);
				startActivity(t);
				finish();
			}
		});
        
        quit=(Button) findViewById(R.id.activity_main_quitter);
    	quit.setOnClickListener(new View.OnClickListener() {
    		@Override
    		public void onClick(View v) {
    			// TODO Auto-generated method stub
    			finish();
    		}
    		});
	}
/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.module_choice, menu);
		return true;
	}
*/
	
	protected void createMetacogFolder(){
		File theDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Metacog");  // Defining Directory/Folder Name  
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
	}
	
	protected void createStructureModuleXML(){
		externalStorage = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Metacog";
        structureFilename = externalStorage+"/structure_modules.xml";
        
        InputStream is = getResources().openRawResource(R.raw.modules);
        
        File f =  new File(structureFilename);
    	if (!f.exists()){
    		try {
    			//lecture du fichier xml 
				Document xml = Utils.readXml(is);
				
				// write the content into xml file
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(xml);
				StreamResult result = new StreamResult(new File(structureFilename));
		 
				// Output to console for testing
				// StreamResult result = new StreamResult(System.out);
		 
				transformer.transform(source, result);
		 
				//System.out.println("File saved!");
				
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerFactoryConfigurationError e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

    	}
	}
}
