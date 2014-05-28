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

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class EtreEmpathiqueIII_Activity extends Activity {

	private TextView moduleName;
	private TextView questionNumber;
	private TextView encouragement;
	
	private Button next;
	
	private ImageView image1;
	private ImageView image2;
	private ImageView image3;
	private ImageView image4;
	
	private RadioGroup radioGroup;
	
	private String module;
	private Integer id_module;
	private Integer id_serie;
	private Integer id_question = 1;
	private Integer id_question_state = 1;
	private Integer nbQuestions = 0;
	private Integer nbQuestionStates = 5;
	private Dictionary<Integer, String> imagesList = new Hashtable<Integer, String>();
	
	private String [] answersList;
	private String goodAnswer;
	private int id_radio_goodanswer;
	
	private Handler customHandler = new Handler();
	private long timeInMilliseconds = 0L;
	private long timeSwapBuff = 0L;
	private long updatedTime = 0L;
	private long startTime = 0L;
	private Runnable updateTimerThread;
	private String filePath;
	private int min;
	private int sec;
	private String playerName;
	private Date Date;
	private String structureFilename;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_etre_empathique_3);
		
		String externalStorage = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Metacog";
        structureFilename = externalStorage+"/structure_modules.xml";
		
		Bundle extra = getIntent().getExtras();
        module = extra.getString("module");
        id_module = extra.getInt("id_module");
        id_serie = extra.getInt("id_serie");
        playerName = extra.getString("name");
        
        moduleName = (TextView) findViewById(R.id.moduleName);
        moduleName.setText(module);
        
        encouragement = (TextView) findViewById(R.id.encouragement);
        encouragement.setText("");
        
        next=(Button) findViewById(R.id.suivant);
        next.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(id_question_state < nbQuestionStates-1){
					id_question_state++;
					majResultat();
					radioGroup.clearCheck();
					loadElements();
					
				}else if(id_question_state == nbQuestionStates-1){
					id_question_state++;
					loadElements();
					majResultat();
					ShowGoodAnswer();
				}else if(id_question < nbQuestions){
					encouragement.setText("");
					for (int i = 0; i < radioGroup.getChildCount(); i++) {
						 radioGroup.getChildAt(i).setEnabled(true);
					 }
					id_question++;
					id_question_state = 1;
					setImageList();
					createAnswers();
					createquestion();
					loadElements();
				}else{
					Intent t=new Intent (EtreEmpathiqueIII_Activity.this,EndExerciceActivity.class);
					t.putExtra("moduleId",id_module);
			    	t.putExtra("module",module);
			    	t.putExtra("name", playerName);
					startActivity(t);
					finish();
					
				}
			}
		});
        
        
    	nbQuestions = countQuestions();
    	
    	Date = new Date(); 
        filePath=Environment.getExternalStorageDirectory().getAbsolutePath()+"/Metacog/Resultats"+"/"+playerName.replace(" ", "_")+"_results.xml";
        initFileTimer();
        createquestion();
        
    	setImageList(); 	
    	createAnswers();
    	loadElements();
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
    		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();		
    	    Document xml = docBuilder.parse(new File(structureFilename));
									
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
    		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();		
    	    Document xml = docBuilder.parse(new File(structureFilename));
									
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
    	
    	radioGroup= (RadioGroup) findViewById(R.id.radioGroupEtreEmpathique3);
    	radioGroup.removeAllViews();
    	for(int i = 0; i<answersList.length;i++){
    		RadioButton rb = new RadioButton(this);
            rb.setText(answersList[i]);
            radioGroup.addView(rb);
            if(rb.getText().equals(goodAnswer)){
            	id_radio_goodanswer = rb.getId();
            };
    	}
    	radioGroup.clearCheck();
    }
    
    
    public void loadElements(){
    		 Uri uri1;
    		 if (id_question_state==1){
	 	    	//uri1 = Uri.parse("android.resource://com.example.metacog/"+imagesList.get(id_question_state));
	 	    	uri1 = Utils.formatImageSource(imagesList.get(id_question_state));
	 	    	
	 	        image1 = (ImageView) findViewById(R.id.imageEtreEmpathique3_1);
	 	        image1.setImageURI(uri1);
	 	        
	 	        
	 	        image2 = (ImageView) findViewById(R.id.imageEtreEmpathique3_2);
	 	        image2.setImageDrawable(null);
	 	        
	 	        image3 = (ImageView) findViewById(R.id.imageEtreEmpathique3_3);
	 	        image3.setImageDrawable(null);
	 	        
	 	        image4 = (ImageView) findViewById(R.id.imageEtreEmpathique3_4);
	 	        image4.setImageDrawable(null);
 	        
    		 }else if(id_question_state==2){
    			    uri1 = Utils.formatImageSource(imagesList.get(id_question_state));

			        image2 = (ImageView) findViewById(R.id.imageEtreEmpathique3_2);
			        image2.setImageURI(uri1);

    			 }
    			 
    		 else if(id_question_state==3){
    			    uri1 = Utils.formatImageSource(imagesList.get(id_question_state));

			        image3 = (ImageView) findViewById(R.id.imageEtreEmpathique3_3);
			        image3.setImageURI(uri1);
			        
			       
    			 }
    		 else if(id_question_state==4){
    			    uri1 = Utils.formatImageSource(imagesList.get(id_question_state));		      
 			    			      
 			        image4 = (ImageView) findViewById(R.id.imageEtreEmpathique3_4);
 			        image4.setImageURI(uri1);
     			 }
    			 
    		 }
    	 
			        
			        
	        
	    
    			 	
    	
    

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.etre_empathique_3, menu);
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
	    			rootElement.setAttribute("name",playerName);
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
	    	}
	    	else{
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
	 public void createquestion(){
		 	NodeList NodeResult = null;
	    	Document xml = null;

		    	try {		
					DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
					DocumentBuilder docBuilder = docFactory.newDocumentBuilder();		
					xml = docBuilder.parse(new File(filePath));
					NodeResult = xml.getElementsByTagName("result");
					Element element = (Element) NodeResult.item(NodeResult.getLength()-1);
					Element quest = xml.createElement("question");
					quest.setAttribute("question", ""+id_question+"");
					element.appendChild(quest);
					
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
	 public void majResultat(){
	    	NodeList NodeResult = null;
	    	NodeList NodeQuestion = null;
	    	Document xml = null;
	    	int checkedRadioButton = -1;
	    	checkedRadioButton = radioGroup.getCheckedRadioButtonId();
	    	RadioButton radio = (RadioButton) findViewById(checkedRadioButton);
	    	String playerAnswer = "";
	    	if (checkedRadioButton != -1){
	    		playerAnswer = radio.getText().toString();
	    	}

		    	try {		
					DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
					DocumentBuilder docBuilder = docFactory.newDocumentBuilder();		
					xml = docBuilder.parse(new File(filePath));
					NodeResult = xml.getElementsByTagName("results");
					Node node = NodeResult.item(0);
					NodeQuestion = node.getChildNodes();
					node=NodeQuestion.item(NodeQuestion.getLength()-1);
					
					Element Node = xml.createElement("reponse");
					String time = min + ":"
		                    + String.format("%02d", sec);
					Node.setAttribute("time", time);
					Node.setAttribute("choice", playerAnswer);
					String Rep;
					if(!playerAnswer.equals("")){
						boolean reponse = playerAnswer.equals(goodAnswer);
						Rep ="Wrong";
						if(reponse){
							Rep ="Good";
						}
					}else {
						Rep ="Unknow";
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
		 int checkedRadioButton = -1;
		 checkedRadioButton = radioGroup.getCheckedRadioButtonId();
		 RadioButton radioAnswered = (RadioButton) findViewById(checkedRadioButton);
		 RadioButton radioGood = (RadioButton) findViewById(id_radio_goodanswer);
		 String playerAnswer = "";
		 for (int i = 0; i < radioGroup.getChildCount(); i++) {
			 radioGroup.getChildAt(i).setEnabled(false);
		 }
		 if (checkedRadioButton != -1){
			 playerAnswer = radioAnswered.getText().toString();
			 if(playerAnswer != null){
				 boolean reponse = playerAnswer.equals(goodAnswer);
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

