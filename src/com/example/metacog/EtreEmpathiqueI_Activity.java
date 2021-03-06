package com.example.metacog;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Random;

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
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class EtreEmpathiqueI_Activity extends Activity {
	
	private TextView moduleName;
	private TextView questionNumber;
	private Button menu;
	private Button next;
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
	private Integer nbQuestion = 0;
	private Dictionary<String, String> imagesList = new Hashtable<String, String>();
	
	private boolean showAnswer = true;
	
	private RadioGroup radioGroup;
	private Integer id_question_state = 1;
	private Integer id_radio_goodanswer = -1;
	private TextView encouragement;
	
	private Handler customHandler = new Handler();
	private long timeInMilliseconds = 0L;
	private long timeSwapBuff = 0L;
	private long updatedTime = 0L;
	private long startTime = 0L;
	private Runnable updateTimerThread;
	private String filePath;
	private int min;
	private int sec;
	private RadioGroup radio;
	private String rep;
	private String name;
	private Date Date;
	private String externalStorage;
	private String structureFilename;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etre_empathique_1);
        
        externalStorage = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Metacog";
        structureFilename = externalStorage+"/structure_modules.xml";
        
        Bundle extra = getIntent().getExtras();
        module = extra.getString("module");
        serie = extra.getString("serie");
        id_module = extra.getInt("id_module");
        id_serie = extra.getInt("id_serie");
        name = extra.getString("name");
        
        moduleName = (TextView) findViewById(R.id.moduleName);
        moduleName.setText(module);
        
        encouragement = (TextView) findViewById(R.id.encouragement);
        encouragement.setText("");
        
        radio = (RadioGroup)findViewById(R.id.radioGroup1);
        Date = new Date(); 
        filePath=Environment.getExternalStorageDirectory().getAbsolutePath()+"/Metacog/Resultats"+"/"+name.replace(" ", "_")+"_results.xml";
        //+"m"+id_module.toString()+"_"+"s"+id_serie.toString()+"_"+maDate.getHours()+"_"+maDate.getMinutes()+"_"+maDate.getDay()+"_"+maDate.getMonth()+"_"+maDate.getYear()+".xml";
        initFileTimer();
        
    	nbQuestion = countQuestions();
    	loadPictures();
     
    	next=(Button) findViewById(R.id.suivant);
        next.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(showAnswer){ 
					if(majResultat()){
						showAnswer = false;
						//loadPictures();
						ShowGoodAnswer();
						id_question++;
						//id_question++;
						//radio.clearCheck();
						//encouragement.setText("");
					}else{
						Toast toast = Toast.makeText(EtreEmpathiqueI_Activity.this,R.string.select_answer, Toast.LENGTH_LONG);
						toast.show();
					}
				}else if(id_question < nbQuestion){
					encouragement.setText("");
					for (int i = 0; i < radio.getChildCount(); i++) {
						 radio.getChildAt(i).setEnabled(true);
						 RadioButton radioBtn = (RadioButton) radio.getChildAt(i);
						 radioBtn.setTextColor(Color.BLACK);
					 }
					loadPictures();
					radio.clearCheck();
					
					showAnswer = true;
				}else{
					majResultat();
					Intent t=new Intent (EtreEmpathiqueI_Activity.this,EndExerciceActivity.class);
					id_question = 1;
					t.putExtra("moduleId",id_module);
			    	t.putExtra("module",module);
			    	t.putExtra("name",name);
					startActivity(t);
					finish();
				}
			}
		});
    }
    
    protected int countQuestions(){
    	try {
    		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();		
    	    Document xml = docBuilder.parse(new File(structureFilename));
    	    
			Element nodeSerie = xml.getElementById("m"+id_module.toString()+"s"+id_serie.toString());
			NodeList nodeQuestionsList = nodeSerie.getChildNodes();
			for (int i = 0; i < nodeQuestionsList.getLength(); ++i)
			{
				if (nodeQuestionsList.item(i).getNodeType()== Node.ELEMENT_NODE){
					nbQuestion += 1;
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
		return nbQuestion;
    }
    
    public void loadPictures(){
        try {
        	DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();		
    	    Document xml = docBuilder.parse(new File(structureFilename));
									
			Element nodeQuestion = xml.getElementById("m"+id_module.toString()+"s"+id_serie.toString()+"q"+id_question.toString());
			rep = nodeQuestion.getAttribute("reponse");
			questionNumber = (TextView) findViewById(R.id.questionNumber);
			questionNumber.setText("Question "+ id_question);
			
			NodeList nodePictsQuestList = nodeQuestion.getChildNodes();

			for (int i = 0; i < nodePictsQuestList.getLength(); ++i)
			{
				if (nodePictsQuestList.item(i).getNodeType()== Node.ELEMENT_NODE){
					Element nodeImage = (Element) nodePictsQuestList.item(i);
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
        
        
        Uri uri1;
        Uri uri2;
        Uri uri3;
        Uri uri4;
        Uri uri5;
        Uri uri6;
        
        uri1 = Utils.formatImageSource(imagesList.get("Q1"));
        uri2 = Utils.formatImageSource(imagesList.get("Q2"));
        uri3 = Utils.formatImageSource(imagesList.get("Q3"));
        uri4 = Utils.formatImageSource(imagesList.get("R1"));
        uri5 = Utils.formatImageSource(imagesList.get("R2"));
        uri6 = Utils.formatImageSource(imagesList.get("R3"));
        
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

        image1.setAdjustViewBounds(true);
        image2.setAdjustViewBounds(true);
        image3.setAdjustViewBounds(true);
        image4.setAdjustViewBounds(true);
        image5.setAdjustViewBounds(true);
        image6.setAdjustViewBounds(true);
        
        image1.setMinimumHeight(240);
        image2.setMinimumHeight(240);
        image3.setMinimumHeight(240);
        image4.setMinimumHeight(240);
        image5.setMinimumHeight(240);
        image6.setMinimumHeight(240);
        
        image1.setMaxHeight(248);
        image2.setMaxHeight(248);
        image3.setMaxHeight(248);
        image4.setMaxHeight(248);
        image5.setMaxHeight(248);
        image6.setMaxHeight(248);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.question, menu);
        return true;
    }
    
    public void initFileTimer(){
    	File f =  new File(filePath);
    	Document xml = null;
    	if (!f.exists()){
    		try {
    			 
    			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
    	 
    			// root elements
    			xml = docBuilder.newDocument();
    			Element rootElement = xml.createElement("results");
    			rootElement.setAttribute("name",name);
    			Element childElement = xml.createElement("result");
    			childElement.setAttribute("module", id_module.toString());
    			childElement.setAttribute("serie", id_serie.toString());
    			rootElement.appendChild(childElement);
    			xml.appendChild(rootElement);
    	
    			// write the content into xml file
    			TransformerFactory transformerFactory = TransformerFactory.newInstance();
    			Transformer transformer = transformerFactory.newTransformer();
    			DOMSource source = new DOMSource(xml);
    			StreamResult result = new StreamResult(new File(filePath));
    	 
    			// Output to console for testing
    			// StreamResult result = new StreamResult(System.out);
    	 
    			transformer.transform(source, result);
    	 
    			//System.out.println("File saved!");
    	 
    		  } catch (ParserConfigurationException pce) {
    			pce.printStackTrace();
    		  } catch (TransformerException tfe) {
    			tfe.printStackTrace();
    		  }
    	}else{
    		try {		
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();		
				xml = docBuilder.parse(new File(filePath));				
				Element childElement = xml.createElement("result");
    			childElement.setAttribute("module", id_module.toString());
    			childElement.setAttribute("serie", id_serie.toString());
    			NodeList rootList = xml.getElementsByTagName("results");
    			Element rootElement = (Element) rootList.item(0);
    			rootElement.appendChild(childElement);
				
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
			StreamResult result = new StreamResult(filePath);
			try {
				transformer.transform(source, result);
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();		
			}
    	}
    	startTime = SystemClock.uptimeMillis();
    	updateTimerThread = new Runnable() {
    		        public void run() {
    		            timeInMilliseconds = SystemClock.uptimeMillis()-startTime;
    		            updatedTime = timeSwapBuff + timeInMilliseconds;
    		            sec = (int) (updatedTime / 1000);
    		            min = sec / 60;
    		            sec = sec % 60;
    		            //int milliseconds = (int) (updatedTime % 1000);
    		           /* timerValue.setText("" + mins + ":"
    		                    + String.format("%02d", secs) + ":"
    		                    + String.format("%03d", milliseconds));*/
    		            customHandler.postDelayed(this, 0);
    		        }
    		    };
    	customHandler.postDelayed(updateTimerThread, 0);
    }
    
    public boolean majResultat(){
    	NodeList NodeResult = null;
    	Document xml = null;
    	int checkedRadioButton = radio.getCheckedRadioButtonId();
    	 
    	String radioButtonSelected = "";
    	boolean isChecked = true;
    	
    	switch (checkedRadioButton) {
    	  case R.id.radio0 :
    		  radioButtonSelected = "A";
    	      break;
    	  case R.id.radio1: 
    		  radioButtonSelected = "B";
    		  break;
    	  case R.id.radio2 :
    		  radioButtonSelected = "C";
    		  break;
    	  default:
    		  isChecked=false;
    	}
    	
    	if(isChecked){
	    	try {		
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();		
				xml = docBuilder.parse(new File(filePath));
				NodeResult = xml.getElementsByTagName("results");
				Node node = NodeResult.item(0);
				Element Node = xml.createElement("reponse");
				String time = min + ":"
	                    + String.format("%02d", sec);
				Node.setAttribute("time", time);
				Node.setAttribute("choice", radioButtonSelected);
				boolean reponse = radioButtonSelected.equals(rep);
				String Rep ="false";
				if(reponse){
					Rep ="true";
				}
				Node.setAttribute("correct", Rep);
				Element tmp = (Element) node.getLastChild();
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
			StreamResult result = new StreamResult(filePath);
			try {
				transformer.transform(source, result);
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();		
			}
			startTime = SystemClock.uptimeMillis();
			//moduleName.setText(radio.getCheckedRadioButtonId());
    	}
    	
    	return isChecked;
    }
    public void ShowGoodAnswer(){
		 String encouragementTxt = "";
		 try {
			 InputStream is = getResources().openRawResource(R.raw.encouragement);
			 Document xml = Utils.readXml(is);
			 Element nodeText= xml.getElementById("encouragements");
			 NodeList nodeEncouragementList = nodeText.getElementsByTagName("encouragement");
			 int nbElements = nodeEncouragementList.getLength();
			 Random randomno = new Random();
			 int idEncouragement = randomno.nextInt(nbElements);
			 encouragementTxt = nodeEncouragementList.item(idEncouragement).getTextContent();
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
		 if (rep.equals("A")) {
	   		  id_radio_goodanswer = R.id.radio0;
		 }else if(rep.equals("B")){ 
	   		id_radio_goodanswer = R.id.radio1;
		 }else if(rep.equals("C")){
	   		id_radio_goodanswer = R.id.radio2;	   	 
	   	}
		 int checkedRadioButton = radio.getCheckedRadioButtonId();
		 //checkedRadioButton = radioGroup.getCheckedRadioButtonId();
		 RadioButton radioAnswered = (RadioButton) findViewById(checkedRadioButton);
		 RadioButton radioGood = (RadioButton) findViewById(id_radio_goodanswer);
		 String playerAnswer = "";
		 for (int i = 0; i < radio.getChildCount(); i++) {
			 radio.getChildAt(i).setEnabled(false);
		 }
		 if (checkedRadioButton != -1){
			 playerAnswer = radioAnswered.getText().toString();
			 if(playerAnswer != null){
				 boolean reponse = playerAnswer.equals(rep);
				 if(reponse){
					 radioAnswered.setTextColor(Color.GREEN);
					 encouragement.setText(encouragementTxt);
				 }else{
					 radioAnswered.setTextColor(Color.RED);
					 radioGood.setTextColor(Color.GREEN);
				 }
			 }
		 }else{
			 radioGood.setTextColor(Color.GREEN);
		 }
		 id_question_state++;
	 }
    
}
