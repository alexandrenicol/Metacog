package com.example.metacog;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class AdminModuleActivity extends Activity {

	private TextView text1;
	private String[] list2;
	private Integer selectedModuleId;
	private String selectedModuleName;
	private String externalStorage;
	private String structureFilename;
	private Button add;
	private Button del;
	private Button retour;

	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_admin_module);
	    
	    
	    externalStorage = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Metacog";
        structureFilename = externalStorage+"/structure_modules.xml";
	    
	    text1 = (TextView) findViewById(R.id.textView1);
	    
	    Bundle extras = getIntent().getExtras();
	    //text1.setText(extras.getString("textView1"));
	    
	    selectedModuleId = extras.getInt("id_module");
	    selectedModuleName = extras.getString("name_module");
	    
	    String selectedModule = "m"+selectedModuleId;
	    text1.setText(selectedModuleName);

	    ListView list = (ListView)findViewById(R.id.listView1);
        
        //InputStream is = getResources().openRawResource(R.raw.modules);
        
        List<String> serieList = new ArrayList<String>();
        
        retour = (Button) findViewById(R.id.activity_admin_module_retour);
        retour.setOnClickListener(new View.OnClickListener() {
    		@Override
    		public void onClick(View v) {
    			// TODO Auto-generated method stub
    			Intent t=new Intent (AdminModuleActivity.this,AdminActivity.class);
				startActivity(t);
				finish();
    		}
    		});

	    
		try {
			//Document xml = Utils.readXml(is);
			
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();		
    	    Document xml = docBuilder.parse(new File(structureFilename));
			
			TextView t = (TextView)findViewById(R.id.textView4);
						
			Element nodeModule = xml.getElementById(selectedModule);
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

	    // TODO Auto-generated method stub

	    list.setClickable(true);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3){
        		//Cursor c = (Cursor) arg0.getAdapter().getItem(position); //WRONG
				String serieChoisi = list2[position];
        		Intent third = new Intent(AdminModuleActivity.this,AdminSerieActivity.class);
        		third.putExtra("selectedModuleId", selectedModuleId);
        		third.putExtra("selectedModuleName", selectedModuleName);
        		third.putExtra("selectedSerieId", position+1);
        		third.putExtra("selectedSerieName", serieChoisi);
        		startActivity(third);
        	}    
        });
        
        
        add=(Button) findViewById(R.id.add_serie);
	    add.setOnClickListener(new View.OnClickListener() {
	    	
	    	@Override
			public void onClick(View arg0) {
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	    		DocumentBuilder docBuilder;
				try {
					docBuilder = docFactory.newDocumentBuilder();
					Document xml = docBuilder.parse(new File(structureFilename));
					
					Element nodeModule = xml.getElementById("m"+selectedModuleId);
					Element nodeSerie = xml.createElement("serie");
					
					NodeList nodeModuleChild = nodeModule.getChildNodes();
					Integer num_serie = 0;
					
					for (int i = 0; i < nodeModuleChild.getLength(); ++i)
					{
						if (nodeModuleChild.item(i).getNodeType()== Node.ELEMENT_NODE){
							num_serie++;
						}
					}
					num_serie++;
					
					String serie_id = "m"+selectedModuleId+"s"+num_serie.toString();
					nodeSerie.setAttribute("id", serie_id);
					nodeSerie.setAttribute("name", "Serie "+num_serie.toString());
					nodeModule.appendChild(nodeSerie);
	
	
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
					
					Toast toast = Toast.makeText(AdminModuleActivity.this,R.string.add_serie, Toast.LENGTH_LONG);
					toast.show();
					startActivity(getIntent());
					finish();

	
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}
		});
	    
	    del=(Button) findViewById(R.id.del_serie);
	    del.setOnClickListener(new View.OnClickListener() {
	    	
	    	@Override
			public void onClick(View arg0) {
	    		Intent third = new Intent(AdminModuleActivity.this,AdminDelNodeActivity.class);
        		third.putExtra("selectedModuleId", selectedModuleId);
        		third.putExtra("selectedModuleName", selectedModuleName);
        		third.putExtra("selectedSerieId", -1);
        		third.putExtra("selectedSerieName", "None");
        		third.putExtra("type", "Serie");
        		startActivity(third);
        		finish();
	    	}
	    });
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.admin_module, menu);
		return true;
	}

	

	

}
