package com.example.metacog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AdminDelNodeActivity extends Activity {

	private String externalStorage;
	private String structureFilename;
	private ListView myListView;
	
	private String[] list2;
	
	private Integer selectedModuleId;
	private String selectedModuleName;
	private Integer selectedSerieId;
	private String selectedSerieName;
	
	private String typeToDel;
	
	private Document xml;
	private Element nodeModule;
	private Element nodeSerie;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_del_node);
		
		externalStorage = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Metacog";
        structureFilename = externalStorage+"/structure_modules.xml";
        
        myListView = (ListView)findViewById(R.id.listView1);
		
        Bundle extras = getIntent().getExtras();
	    selectedModuleId = extras.getInt("selectedModuleId");
	    selectedModuleName = extras.getString("selectedModuleName");
	    selectedSerieId = extras.getInt("selectedSerieId");
	    selectedSerieName = extras.getString("selectedSerieName");
	    typeToDel = extras.getString("type");
	    
	    if (typeToDel.equals("Serie")){
	    	List<String> serieList = new ArrayList<String>();

		    
			try {
				//Document xml = Utils.readXml(is);
				
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	    		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();		
	    	    xml = docBuilder.parse(new File(structureFilename));
				
											
				nodeModule = xml.getElementById("m"+selectedModuleId);
				NodeList nodeSerieList = nodeModule.getChildNodes();

				for (int i = 0; i < nodeSerieList.getLength(); ++i)
				{
					if (nodeSerieList.item(i).getNodeType()== Node.ELEMENT_NODE){
						Element nodeSerie = (Element) nodeSerieList.item(i);
						
						String nodeValue = nodeSerie.getAttribute("name");
						serieList.add(nodeValue);
					}
					
				}
				
				list2 = new String[serieList.size()];
				serieList.toArray(list2);
				
				// Create The Adapter with passing ArrayList as 3rd parameter
				ArrayAdapter<String> arrayAdapter = 
						new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list2);
				
	            // Set The Adapter
				myListView.setAdapter(arrayAdapter); 
				
				myListView.setClickable(true);
				myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		        	@Override
		        	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3){
		        		Integer serieChoisie = position+1;
		        		String serieId = "m"+selectedModuleId+"s"+serieChoisie;
		        		
		        		Element nodeSerie = xml.getElementById(serieId);
		        		nodeModule.removeChild(nodeSerie);
		        		
		        		TransformerFactory transformerFactory = TransformerFactory.newInstance();
						Transformer transformer = null;
						try {
							transformer = transformerFactory.newTransformer();
						} catch (TransformerConfigurationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						DOMSource source = new DOMSource(xml);
						StreamResult result = new StreamResult(structureFilename);
						try {
							transformer.transform(source, result);
						} catch (TransformerException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						Toast toast = Toast.makeText(AdminDelNodeActivity.this,R.string.del_serie, Toast.LENGTH_LONG);
						toast.show();
						finish();
		        	}    
		        });
				
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
	    }
	    else if (typeToDel.equals("Question")){
	    	List<String> questionList = new ArrayList<String>();

		    
			try {
				//Document xml = Utils.readXml(is);
				
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	    		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();		
	    	    xml = docBuilder.parse(new File(structureFilename));
				
											
				nodeSerie = xml.getElementById("m"+selectedModuleId+"s"+selectedSerieId);
				NodeList nodeQuestionList = nodeSerie.getChildNodes();

				for (int i = 0; i < nodeQuestionList.getLength(); ++i)
				{
					if (nodeQuestionList.item(i).getNodeType()== Node.ELEMENT_NODE){
						Element nodeSerie = (Element) nodeQuestionList.item(i);
						
						String nodeValue = nodeSerie.getAttribute("id");
						
						questionList.add("Question "+nodeValue.split("q")[1]);
					}
					
				}
				
				list2 = new String[questionList.size()];
				questionList.toArray(list2);
				
				// Create The Adapter with passing ArrayList as 3rd parameter
				ArrayAdapter<String> arrayAdapter = 
						new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list2);
				
	            // Set The Adapter
				myListView.setAdapter(arrayAdapter); 
				
				myListView.setClickable(true);
				myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		        	@Override
		        	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3){
		        		Integer questionChoisie = position+1;
		        		String questionId = "m"+selectedModuleId+"s"+selectedSerieId+"q"+questionChoisie;
		        		
		        		Element nodeQuestion = xml.getElementById(questionId);
		        		nodeSerie.removeChild(nodeQuestion);
		        		
		        		TransformerFactory transformerFactory = TransformerFactory.newInstance();
						Transformer transformer = null;
						try {
							transformer = transformerFactory.newTransformer();
						} catch (TransformerConfigurationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						DOMSource source = new DOMSource(xml);
						StreamResult result = new StreamResult(structureFilename);
						try {
							transformer.transform(source, result);
						} catch (TransformerException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						Toast toast = Toast.makeText(AdminDelNodeActivity.this,R.string.del_question, Toast.LENGTH_LONG);
						toast.show();
						finish();
		        	}    
		        });
				
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
	    }
	    
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.admin_del_node, menu);
		return true;
	}

}
