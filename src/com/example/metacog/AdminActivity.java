package com.example.metacog;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class AdminActivity extends Activity {

	private NodeList nodeModuleList = null;
	private NodeList nodeModuleList2 = null;
	public ListView list = null;
	private String[] list2;
	private String externalStorage;
	private String structureFilename;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        
        externalStorage = Environment.getExternalStorageDirectory().getAbsolutePath();
        structureFilename = externalStorage+"/structure_modules.xml";
        
        list = (ListView)findViewById(R.id.listView1);
        
        InputStream is = getResources().openRawResource(R.raw.modules);
        
        List<String> moduleList = new ArrayList<String>();
        
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
        
    	
        try {
        	DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();		
    	    Document xml = docBuilder.parse(new File(structureFilename));
			
			TextView t = (TextView)findViewById(R.id.textView4);
			t.setText(externalStorage);
			
//			OutputStreamWriter out = new OutputStreamWriter(openFileOutput("myFileName2.txt",0));
//			out.write("toto");
//			out.close();
			
						
			nodeModuleList = xml.getElementsByTagName("module");
			

			for (int i = 0; i < nodeModuleList.getLength(); ++i)
			{
				Element nodeModule = (Element) nodeModuleList.item(i);
				//String nodeValue = nodeModule.getTextContent();
				String nodeValue = nodeModule.getAttribute("name");
				moduleList.add(nodeValue);
			}
			
			list2 = new String[moduleList.size()];
			moduleList.toArray(list2);
			
			// Create The Adapter with passing ArrayList as 3rd parameter
			ArrayAdapter<String> arrayAdapter = 
					new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list2);
			
            // Set The Adapter
            list.setAdapter(arrayAdapter); 
			
			
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        list.setClickable(true);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3){
        		//Cursor c = (Cursor) arg0.getAdapter().getItem(position); //WRONG
				String modulechoisi = list2[position];
        		Intent second = new Intent(AdminActivity.this,AdminModuleActivity.class);
        		second.putExtra("id_module", position+1);
        		second.putExtra("name_module", modulechoisi);
        		startActivity(second);
        		
        	}    
        });
        
        
    }

    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    
    
    
}
