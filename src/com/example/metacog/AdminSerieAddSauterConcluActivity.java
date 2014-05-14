package com.example.metacog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Dialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.res.Resources;
import android.os.Build;
import ar.com.daidalos.afiledialog.FileChooserDialog;

public class AdminSerieAddSauterConcluActivity extends Activity {

	private Button add_image1;
	private Button add_image_field;
	private Button valid;
	private Button add_proposition;
	private TextView text_proposition;
	
	private String externalStorage;
	private String structureFilename;
	
	private Integer selectedModuleId;
	private String selectedModuleName;
	private Integer selectedSerieId;
	private String selectedSerieName;
	
	private Integer cursor = 1;
	private Integer buttonClicked = 1;
	private Integer id_ = 1;
	
	private String[] listOfImage;
	
	private Map<Integer, String> dictionaryImage = new HashMap<Integer, String>();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_serie_add_sauter_conclu);
		// Show the Up button in the action bar.
		setupActionBar();
		
		TextView t = (TextView) findViewById(R.id.image1);
		t.setId(9990+buttonClicked);
		dictionaryImage.put(buttonClicked, "");
		
		text_proposition = (TextView) findViewById(R.id.text_proposition);
		
		externalStorage = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Metacog";
        structureFilename = externalStorage+"/structure_modules.xml";
		
		Bundle extras = getIntent().getExtras();
	    selectedModuleId = extras.getInt("selectedModuleId");
	    selectedModuleName = extras.getString("selectedModuleName");
	    selectedSerieId = extras.getInt("selectedSerieId");
	    selectedSerieName = extras.getString("selectedSerieName");
	    
		
		add_image1=(Button) findViewById(R.id.add_image1);
		add_image1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				FileChooserDialog dialog = new FileChooserDialog(AdminSerieAddSauterConcluActivity.this);
			    dialog.loadFolder(Environment.getExternalStorageDirectory().getAbsolutePath());
			     
			    // Assign listener for the select event.
			    dialog.addListener(AdminSerieAddSauterConcluActivity.this.onFileSelectedListener);
		    		
		    	// Define the filter for select images.
		    	dialog.setFilter(".*jpg|.*png|.*gif|.*JPG|.*PNG|.*GIF");
		    	dialog.setShowOnlySelectable(false);
			     
			    dialog.show();
			}
		});
		
		add_image_field=(Button) findViewById(R.id.add_image_field);
		add_image_field.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				cursor++;
				id_ = cursor;
				
				//adding a new button to lineaLayoutButton
				LinearLayout linearLayoutButton = (LinearLayout)findViewById(R.id.linearLayoutButton);
				LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
								
				Button btn1 = new Button(AdminSerieAddSauterConcluActivity.this);
				btn1.setText("Choisissez votre image "+cursor);
				btn1.setId(id_);
				linearLayoutButton.addView(btn1, lp);
				
				btn1 = ((Button) findViewById(id_));
				
				dictionaryImage.put(id_, "");
				
			    btn1.setOnClickListener(new View.OnClickListener() {
			        public void onClick(View view) {
			        	FileChooserDialog dialog = new FileChooserDialog(AdminSerieAddSauterConcluActivity.this);
					    dialog.loadFolder(Environment.getExternalStorageDirectory().getAbsolutePath());
					     
					    // Assign listener for the select event.
					    dialog.addListener(AdminSerieAddSauterConcluActivity.this.onFileSelectedListener);
				    		
				    	// Define the filter for select images.
				    	dialog.setFilter(".*jpg|.*png|.*gif|.*JPG|.*PNG|.*GIF");
				    	dialog.setShowOnlySelectable(false);
					     
					    dialog.show();
					    buttonClicked = id_;
					    
			        }
			    });
			    
			    //adding a text field to linearLayoutTextView
			    LinearLayout linearLayoutTextView = (LinearLayout)findViewById(R.id.linearLayoutTextView);
				
								
				TextView tv1 = new TextView(AdminSerieAddSauterConcluActivity.this);
				tv1.setText("image "+cursor+" :");
				tv1.setId(9990+id_);
				linearLayoutTextView.addView(tv1, lp);
			    
			    
			    
			}
		});
		
		add_proposition = (Button) findViewById(R.id.add_proposition);
		add_proposition.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				EditText new_prop = (EditText) findViewById(R.id.new_proposition);
				if (text_proposition.getText().equals("")){
					text_proposition.setText(new_prop.getText());
				}else{
					text_proposition.setText(text_proposition.getText()+","+new_prop.getText());
				}
				new_prop.setText("");
			}
			
		});
		
		
		
		valid=(Button) findViewById(R.id.valid_button);
	    valid.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				boolean toBeValid = true;
				
				for (Entry<Integer, String> e : dictionaryImage.entrySet()) {
						if (e.getValue().equals("")){
							toBeValid = false;
							Toast toast = Toast.makeText(AdminSerieAddSauterConcluActivity.this,"Merci de charger toutes les images", Toast.LENGTH_LONG);
							toast.show();
						}
					}
				
				
				if (toBeValid){
					
					DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		    		DocumentBuilder docBuilder;
					try {
						docBuilder = docFactory.newDocumentBuilder();
						Document xml = docBuilder.parse(new File(structureFilename));
						
						Element nodeSerie = xml.getElementById("m"+selectedModuleId+"s"+selectedSerieId);
						Element nodeQuestion = xml.createElement("question");
						
						NodeList nodeSerieChild = nodeSerie.getChildNodes();
						Integer num_question = 0;
						
						// get the number of question in the serie
						for (int i = 0; i < nodeSerieChild.getLength(); ++i)
						{
							if (nodeSerieChild.item(i).getNodeType()== Node.ELEMENT_NODE){
								num_question++;
							}
						}
						//number of the new question
						num_question++;
						
						String question_id = "m"+selectedModuleId+"s"+selectedSerieId+"q"+num_question;
						nodeQuestion.setAttribute("id", question_id);
						
						
						//---------------------------------------------------------------------------
						/*EditText answer = (EditText) findViewById(R.id.editText1);
						answer.getText();
						
						nodeQuestion.setAttribute("reponse", answer.getText().toString());*/
						Element nodeRep = xml.createElement("rep");
						nodeRep.setAttribute("list", text_proposition.getText().toString());
						EditText answer = (EditText) findViewById(R.id.answer);
						nodeRep.setAttribute("goodanswer", answer.getText().toString());
						
						List<Node> listOfImage = new ArrayList<Node>() ;
						
						for (Entry<Integer, String> e : dictionaryImage.entrySet()) {
							Element nodeImg = xml.createElement("img");
							nodeImg.setAttribute("id", e.getKey().toString());
							nodeImg.setAttribute("src", e.getValue());
							listOfImage.add(nodeImg);
						}
						
						for (Node anImg : listOfImage ){
							nodeQuestion.appendChild(anImg);
							
						}
						nodeQuestion.appendChild(nodeRep);
						
						/*nodeQuestion.appendChild(nodeImg1);
						nodeQuestion.appendChild(nodeImg2);
						nodeQuestion.appendChild(nodeImg3);
						nodeQuestion.appendChild(nodeImg4);
						nodeQuestion.appendChild(nodeImg5);
						nodeQuestion.appendChild(nodeImg6);*/
						
						nodeSerie.appendChild(nodeQuestion);
						
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
						
						Toast toast = Toast.makeText(AdminSerieAddSauterConcluActivity.this,"Votre question a été ajoutée", Toast.LENGTH_LONG);
						toast.show();
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
				
			}
		});
		
		
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.admin_serie_add_sauter_conclu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	// ---- Methods for display the results ----- //
	
		private FileChooserDialog.OnFileSelectedListener onFileSelectedListener = new FileChooserDialog.OnFileSelectedListener() {
			public void onFileSelected(Dialog source, File file) {
				source.hide();
				Toast toast = Toast.makeText(AdminSerieAddSauterConcluActivity.this, "File selected: " + file.getAbsolutePath() + ", button clicked " + buttonClicked.toString(), Toast.LENGTH_LONG);
				toast.show();
				
				TextView t = (TextView) findViewById(9990+buttonClicked);
				t.setText(t.getText()+" "+file.getAbsolutePath());
				
				dictionaryImage.put(buttonClicked, file.getAbsolutePath());
				
				
				
			}
			public void onFileSelected(Dialog source, File folder, String name) {
				source.hide();
				Toast toast = Toast.makeText(AdminSerieAddSauterConcluActivity.this, "File created: " + folder.getName() + "/" + name, Toast.LENGTH_LONG);
				toast.show();
			}
		};

}
