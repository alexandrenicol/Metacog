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
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;

public class LogActivity extends Activity 
{ 
	private Button quit;
	private Button valid;
	private EditText name;
	private EditText firstname;
	private NodeList nodeUser = null;
	private String[] list;
	private ListView userView;
/** Called when the activity is first created. */ 
@Override 
public void onCreate(Bundle savedInstanceState) 
{ 
super.onCreate(savedInstanceState); 
setContentView(R.layout.activity_log);
	userView = (ListView) findViewById(R.id.listView1);
	name = (EditText) findViewById(R.id.editText1);
	quit = (Button) findViewById(R.id.Quit);
	quit.setOnClickListener(new View.OnClickListener() {
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		finish();
	}
	});
		
TabHost tabs=(TabHost)findViewById(R.id.tabhost); 
tabs.setup();

TabHost.TabSpec spec=tabs.newTabSpec("tag1"); 
spec.setContent(R.id.userName);
spec.setIndicator("New User");
 

tabs.addTab(spec); 
spec=tabs.newTabSpec("tag2"); 
spec.setContent(R.id.listView1);
spec.setIndicator("Choose User"); 

tabs.addTab(spec); 
tabs.setCurrentTab(0); 

name = (EditText) findViewById(R.id.editText1);
firstname = (EditText) findViewById(R.id.editText2);
//InputStream is = getResources().openRawResource(R.raw.users);
try {
	//Document xml = Utils.readXml(is);
	DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder docBuilder = docFactory.newDocumentBuilder();		
	Document xml = docBuilder.parse(new File("/sdcard/users.xml"));
	nodeUser = xml.getElementsByTagName("user");
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

List<String> tmp = new ArrayList<String>();
for (int i = 0; i< nodeUser.getLength();i++){
	Element Node = (Element) nodeUser.item(i);
	//String nodeValue = nodeModule.getTextContent();
	String nodeValue = Node.getAttribute("id");
	//tmp.add(nodeUser.item(i).getTextContent());
	tmp.add(nodeValue);
}

list = new String[tmp.size()];
tmp.toArray(list);
userView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list));
userView.setOnItemClickListener(new OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
    	String userchoisi = list[position];
    	Intent t = new Intent(LogActivity.this, MainActivity.class);
    	//t.putExtra("user",userchoisi);
		startActivity(t);
    }
	});

valid = (Button) findViewById(R.id.valid);
valid.setOnClickListener(new View.OnClickListener() {
	@Override
	public void onClick(View v) {
	// TODO Auto-generated method stub
		NodeList NodeUsers = null;
		Document xml = null;
		try {
			//String filepath = "C:/test/users.xml";
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();		
			xml = docBuilder.parse(new File("/sdcard/users.xml"));
			NodeUsers = xml.getElementsByTagName("users");
			Element Node = xml.createElement("user");
			Node.setAttribute("id", firstname.getText().toString()+" "+name.getText().toString());
			Element tmp = (Element) NodeUsers.item(0);
			tmp.appendChild(Node);
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
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = null;
		try {
			transformer = transformerFactory.newTransformer();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DOMSource source = new DOMSource(xml);
		StreamResult result = new StreamResult("/sdcard/users.xml");
		try {
			transformer.transform(source, result);
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	});
}

}


