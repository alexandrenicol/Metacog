package com.example.metacog;

import java.io.IOException;
import java.io.InputStream;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class SauterConcluActivity extends Activity {
	
	private TextView moduleName;
	private TextView questionNumber;
	
	private Button next;
	
	private ImageView image;
	
	private RadioGroup radioGroup;
	
	private String module;
	private Integer id_module;
	private Integer id_serie;
	private Integer id_question = 1;
	private Integer id_question_state = 1;
	private Integer nbQuestions = 0;
	private Integer nbQuestionStates = 0;
	private Dictionary<Integer, String> imagesList = new Hashtable<Integer, String>();
	
	private String [] answersList;
	private String goodAnswer;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sauter_conclu);
		
		Bundle extra = getIntent().getExtras();
        module = extra.getString("module");
        id_module = extra.getInt("id_module");
        id_serie = extra.getInt("id_serie");
        
        moduleName = (TextView) findViewById(R.id.moduleName);
        moduleName.setText(module);
        
        next=(Button) findViewById(R.id.suivant);
        next.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(id_question_state < nbQuestionStates){
					id_question_state++;
					loadElements();
				}else{
					if(id_question < nbQuestions){
						id_question++;
						id_question_state = 1;
						setImageList();
						createAnswers();
						loadElements();
					}else{
						Intent t=new Intent (SauterConcluActivity.this,EndExerciceActivity.class);
						t.putExtra("moduleId",id_module);
				    	t.putExtra("module",module);
						startActivity(t);
						finish();
					}
				}
			}
		});
        
        
    	nbQuestions = countQuestions();
    	setImageList();
    	createAnswers();
    	loadElements();
	}
	
    protected int countQuestions(){
    	try {
	    	InputStream is = getResources().openRawResource(R.raw.modules);
			Document xml = Utils.readXml(is);
			Element nodeSerie = xml.getElementById("m"+id_module.toString()+"s"+id_serie.toString());
			NodeList nodeQuestionsList = nodeSerie.getChildNodes();
			for (int i = 0; i < nodeQuestionsList.getLength(); ++i)
			{
				if (nodeQuestionsList.item(i).getNodeType()== Node.ELEMENT_NODE){
					nbQuestions += 1;
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
		return nbQuestions;
    }
    
    protected void setImageList(){
    	imagesList = new Hashtable<Integer, String>();
    	try {
        	InputStream is = getResources().openRawResource(R.raw.modules);
			Document xml = Utils.readXml(is);
									
			Element nodeQuestion = xml.getElementById("m"+id_module.toString()+"s"+id_serie.toString()+"q"+id_question.toString());
			NodeList nodePictsQuestList = nodeQuestion.getElementsByTagName("img");
			nbQuestionStates = nodePictsQuestList.getLength();
			
			questionNumber = (TextView) findViewById(R.id.questionNumber);
			questionNumber.setText("Question "+ id_question);
			
		//	NodeList nodePictsQuestList = nodeQuestion.getChildNodes();

			for (int i = 0; i < nodePictsQuestList.getLength(); ++i)
			{
				//if (nodePictsQuestList.item(i).getNodeType()== Node.ELEMENT_NODE){
					Element nodeImage = (Element) nodePictsQuestList.item(i);
					
					//String nodeValue = nodeModule.getTextContent();
					//if(nodeImage.getAttribute("id") != ""){
						int imageId = Integer.parseInt(nodeImage.getAttribute("id"));
						String imageSrc = nodeImage.getAttribute("src");
						imagesList.put(imageId,imageSrc);
						
						//nbQuestionStates++;
					//}
				//}
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
    }
    
    public void createAnswers(){
    	try {
        	InputStream is = getResources().openRawResource(R.raw.modules);
			Document xml = Utils.readXml(is);
									
			Element nodeQuestion = xml.getElementById("m"+id_module.toString()+"s"+id_serie.toString()+"q"+id_question.toString());
			NodeList nodePictsQuestList = nodeQuestion.getElementsByTagName("rep");
			Element rep = (Element) nodePictsQuestList.item(0);
			String tmpAnswer = rep.getAttribute("list");
			answersList=tmpAnswer.split(",");
			goodAnswer = rep.getAttribute("goodanswer");
    	}catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	radioGroup= (RadioGroup) findViewById(R.id.radioGroupSauterConclu);
    	radioGroup.removeAllViews();
    	for(int i = 0; i<answersList.length;i++){
    		RadioButton rb = new RadioButton(this);
            rb.setText(answersList[i]);
            moduleName.setText(answersList[i]);
            radioGroup.addView(rb);
    	}
    }
    
    public void loadElements(){
    	Uri uri1 = Uri.parse("android.resource://com.example.metacog/"+imagesList.get(id_question_state));
        image = (ImageView) findViewById(R.id.imageSauterConclu);
        image.setImageURI(uri1);
        
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sauter_conclu, menu);
		return true;
	}

}
