package com.example.metacog;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class QuestionActivity extends Activity {
	
	private TextView moduleName;
	private TextView questionNumber;
	private Button button1;
	private Button button2;
	private ImageView image1;
	private ImageView image2;
	private ImageView image3;
	private ImageView image4;
	private ImageView image5;
	private ImageView image6;
	private String module;
	private String serie;
	private Integer id_module;
	private Integer id_serie;
	private Integer id_question = 1;
	private Dictionary<String, String> imagesList = new Hashtable<String, String>();
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        
        Bundle extra = getIntent().getExtras();
        module = extra.getString("module");
        serie = extra.getString("serie");
        id_module = extra.getInt("id_module");
        id_serie = extra.getInt("id_serie");
        
        moduleName = (TextView) findViewById(R.id.moduleName);
        moduleName.setText(module);
             
        loadPictures();
    }
    
    public void loadPictures(){
    	// TODO : y'a que 5 images 
    	// TODO : a pusher : QuestionActivity, MainActivity et le layout activity_question
        try {
        	InputStream is = getResources().openRawResource(R.raw.modules);
			Document xml = Utils.readXml(is);
									
			Element nodeQuestion = xml.getElementById("m"+id_module.toString()+"s"+id_serie.toString()+"q"+id_question.toString());
			
			
			questionNumber = (TextView) findViewById(R.id.questionNumber);
			questionNumber.setText("Question "+ id_question);
			
			NodeList nodeQuestionsList = nodeQuestion.getChildNodes();

			for (int i = 0; i < nodeQuestionsList.getLength(); ++i)
			{
				if (nodeQuestionsList.item(i).getNodeType()== Node.ELEMENT_NODE){
					Element nodeImage = (Element) nodeQuestionsList.item(i);
					//String nodeValue = nodeModule.getTextContent();
					
					String imageId = nodeImage.getAttribute("id");
					String imageSrc = nodeImage.getAttribute("src");
					imagesList.put(imageId,imageSrc);
				}
				
			}
			
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
        
        
        Uri uri1 = Uri.parse("android.resource://com.example.metacog/"+imagesList.get("Q1"));
        Uri uri2 = Uri.parse("android.resource://com.example.metacog/"+imagesList.get("Q2"));
        Uri uri3 = Uri.parse("android.resource://com.example.metacog/"+imagesList.get("Q3"));
        Uri uri4 = Uri.parse("android.resource://com.example.metacog/"+imagesList.get("R1"));
        Uri uri5 = Uri.parse("android.resource://com.example.metacog/"+imagesList.get("R2"));
        Uri uri6 = Uri.parse("android.resource://com.example.metacog/"+imagesList.get("R3"));
        
        image1 = (ImageView) findViewById(R.id.imageQuestion1);
        image2 = (ImageView) findViewById(R.id.imageQuestion2);
        image3 = (ImageView) findViewById(R.id.imageQuestion3);
        image4 = (ImageView) findViewById(R.id.imageAnswer1);
        image5 = (ImageView) findViewById(R.id.imageAnswer2);
        image6 = (ImageView) findViewById(R.id.imageAnswer3);
        
        image1.setImageURI(uri1);
        image2.setImageURI(uri2);
        image3.setImageURI(uri3);
        image4.setImageURI(uri4);
        image5.setImageURI(uri5);
        image6.setImageURI(uri6);

        button2=(Button) findViewById(R.id.suivant);
        button2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				id_question = id_question + 1;
				loadPictures();
			}
		});
        
        button1=(Button) findViewById(R.id.menu);
        button1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Aller au menu
				
			}
		});
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.question, menu);
        return true;
    }
    
}
