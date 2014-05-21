package com.example.metacog;

import java.io.File;
import java.io.IOException;

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

import ar.com.daidalos.afiledialog.*;
import ar.com.daidalos.afiledialog.FileChooserDialog.OnFileSelectedListener;
import ar.com.daidalos.afiledialog.view.*;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.Dialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

public class AdminSerieAddM1Activity extends Activity {

	private String externalStorage;
	private String structureFilename;
	
	private Integer selectedModuleId;
	private String selectedModuleName;
	private Integer selectedSerieId;
	private String selectedSerieName;
	
	private String image1 = "";
	private String image2 = "";
	private String image3 = "";
	private String proposition1 = "";
	private String proposition2 = "";
	private String proposition3 = "";
	
	private String cursor = "image1";
	private Integer cursorPos = 0;
	private String[] listOfImages = {"image1","image2","image3","proposition1","proposition2","proposition3"};
	
	public Button add;
	public Button valid;
	public Button retour;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_serie_add_m1);
		// Show the Up button in the action bar.
		setupActionBar();
		
		externalStorage = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Metacog";
        structureFilename = externalStorage+"/structure_modules.xml";
		
		Bundle extras = getIntent().getExtras();
	    selectedModuleId = extras.getInt("selectedModuleId");
	    selectedModuleName = extras.getString("selectedModuleName");
	    selectedSerieId = extras.getInt("selectedSerieId");
	    selectedSerieName = extras.getString("selectedSerieName");
		
	    retour=(Button) findViewById(R.id.activity_admin_serie_add_m1_retour);
	    add=(Button) findViewById(R.id.add_image1);
	    
	    retour.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent t=new Intent (AdminSerieAddM1Activity.this,AdminSerieActivity.class);
				t.putExtra("selectedModuleId", selectedModuleId);
        		t.putExtra("selectedModuleName", selectedModuleName);
        		t.putExtra("selectedSerieId", selectedSerieId);
        		t.putExtra("selectedSerieName", selectedSerieName);
				startActivity(t);
				finish();
				
			}
	});
	    add.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				FileChooserDialog dialog = new FileChooserDialog(AdminSerieAddM1Activity.this);
			    dialog.loadFolder(Environment.getExternalStorageDirectory().getAbsolutePath());
			     
			    // Assign listener for the select event.
			    dialog.addListener(AdminSerieAddM1Activity.this.onFileSelectedListener);
		    		
		    	// Define the filter for select images.
		    	dialog.setFilter(".*jpg|.*png|.*gif|.*JPG|.*PNG|.*GIF");
		    	dialog.setShowOnlySelectable(false);
			     
			    dialog.show();
			}
		});
	    
	    valid=(Button) findViewById(R.id.valid_button);
	    valid.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if ((image1!="")&&(image2!="")&&(image3!="")&&(proposition1!="")&&(proposition2!="")&&(proposition3!="")){
					
					DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		    		DocumentBuilder docBuilder;
					try {
						docBuilder = docFactory.newDocumentBuilder();
						Document xml = docBuilder.parse(new File(structureFilename));
						
						Element nodeSerie = xml.getElementById("m"+selectedModuleId+"s"+selectedSerieId);
						Element nodeQuestion = xml.createElement("question");
						
						NodeList nodeSerieChild = nodeSerie.getChildNodes();
						Integer num_question = 0;
						
						for (int i = 0; i < nodeSerieChild.getLength(); ++i)
						{
							if (nodeSerieChild.item(i).getNodeType()== Node.ELEMENT_NODE){
								num_question++;
							}
						}
						num_question++;
						
						String question_id = "m"+selectedModuleId+"s"+selectedSerieId+"q"+num_question;
						nodeQuestion.setAttribute("id", question_id);
						
						EditText answer = (EditText) findViewById(R.id.new_proposition);
						answer.getText();
						
						nodeQuestion.setAttribute("reponse", answer.getText().toString());
						
						Element nodeImg1 = xml.createElement("img");
						nodeImg1.setAttribute("id", "Q1");
						nodeImg1.setAttribute("src", image1);
						
						Element nodeImg2 = xml.createElement("img");
						nodeImg2.setAttribute("id", "Q2");
						nodeImg2.setAttribute("src", image2);
						
						Element nodeImg3 = xml.createElement("img");
						nodeImg3.setAttribute("id", "Q3");
						nodeImg3.setAttribute("src", image3);
						
						Element nodeImg4 = xml.createElement("img");
						nodeImg4.setAttribute("id", "R1");
						nodeImg4.setAttribute("src", proposition1);
						
						Element nodeImg5 = xml.createElement("img");
						nodeImg5.setAttribute("id", "R2");
						nodeImg5.setAttribute("src", proposition2);
						
						Element nodeImg6 = xml.createElement("img");
						nodeImg6.setAttribute("id", "R3");
						nodeImg6.setAttribute("src", proposition3);
						
						nodeQuestion.appendChild(nodeImg1);
						nodeQuestion.appendChild(nodeImg2);
						nodeQuestion.appendChild(nodeImg3);
						nodeQuestion.appendChild(nodeImg4);
						nodeQuestion.appendChild(nodeImg5);
						nodeQuestion.appendChild(nodeImg6);
						
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
						
						Toast toast = Toast.makeText(AdminSerieAddM1Activity.this,R.string.add_question, Toast.LENGTH_LONG);
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
		    	    
				}else{
					Toast toast = Toast.makeText(AdminSerieAddM1Activity.this,R.string.must_choose_eI, Toast.LENGTH_LONG);
					toast.show();
				}
				
			}
		});
	    
	}
	
	// ---- Methods for display the results ----- //
	
	private FileChooserDialog.OnFileSelectedListener onFileSelectedListener = new FileChooserDialog.OnFileSelectedListener() {
		public void onFileSelected(Dialog source, File file) {
			source.hide();
			Toast toast = Toast.makeText(AdminSerieAddM1Activity.this, "File selected: " + file.getAbsolutePath(), Toast.LENGTH_LONG);
			toast.show();
			
			TextView t ;
			t = (TextView) findViewById(R.id.image1);
			switch (cursorPos) {
				case 0 :
					t = (TextView) findViewById(R.id.image1);
					image1 = file.getAbsolutePath();
					break;
				case 1 :
					t = (TextView) findViewById(R.id.image2);
					image2 = file.getAbsolutePath();
					break;
				case 2 :
					t = (TextView) findViewById(R.id.image3);
					image3 = file.getAbsolutePath();
					break;
				case 3 :
					t = (TextView) findViewById(R.id.proposition1);
					proposition1 = file.getAbsolutePath();
					break;
				case 4 :
					t = (TextView) findViewById(R.id.proposition2);
					proposition2 = file.getAbsolutePath();
					break;
				case 5 :
					t = (TextView) findViewById(R.id.proposition3);
					proposition3 = file.getAbsolutePath();
					break;
			
			}
			
			t.setText(t.getText() +" "+file.getAbsolutePath());
			if (cursorPos < 5){
				cursorPos = cursorPos + 1;
				cursor = listOfImages[cursorPos];
			}
			
		}
		public void onFileSelected(Dialog source, File folder, String name) {
			source.hide();
			Toast toast = Toast.makeText(AdminSerieAddM1Activity.this, "File created: " + folder.getName() + "/" + name, Toast.LENGTH_LONG);
			toast.show();
		}
	};

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
		getMenuInflater().inflate(R.menu.admin_serie_add, menu);
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
	

	void changeValeur(Object o, String nomChamp, Object val) throws Exception
	{
	   java.lang.reflect.Field f = o.getClass().getField(nomChamp);
	   f.set(o,val);
	}


}
