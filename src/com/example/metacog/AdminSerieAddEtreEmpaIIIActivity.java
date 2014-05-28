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
import ar.com.daidalos.afiledialog.FileChooserDialog;

public class AdminSerieAddEtreEmpaIIIActivity extends Activity {

	private String externalStorage;
	private String structureFilename;
	
	private Integer selectedModuleId;
	private String selectedModuleName;
	private Integer selectedSerieId;
	private String selectedSerieName;
	
	private Integer cursor;
	private Button eeiii_add_image1;
	private Button eeiii_add_image2;
	private Button eeiii_add_image3;
	private Button eeiii_add_image4;
	
	private String eeiii_image1;
	private String eeiii_image2;
	private String eeiii_image3;
	private String eeiii_image4;
	
	private String eeiii_answer_prop = "";
	private String eeiii_good_answer = "";
	
	private Button eeiii_valid;
	private Button eeiii_return;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_serie_add_etre_empa_iii);
		// Show the Up button in the action bar.
		
		externalStorage = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Metacog";
        structureFilename = externalStorage+"/structure_modules.xml";
		
		Bundle extras = getIntent().getExtras();
	    selectedModuleId = extras.getInt("selectedModuleId");
	    selectedModuleName = extras.getString("selectedModuleName");
	    selectedSerieId = extras.getInt("selectedSerieId");
	    selectedSerieName = extras.getString("selectedSerieName");
	    
	    eeiii_add_image1=(Button) findViewById(R.id.eeiii_add_image1);
	    eeiii_add_image1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				cursor = 1;
				
				FileChooserDialog dialog = new FileChooserDialog(AdminSerieAddEtreEmpaIIIActivity.this);
			    dialog.loadFolder(Environment.getExternalStorageDirectory().getAbsolutePath());
			     
			    // Assign listener for the select event.
			    dialog.addListener(AdminSerieAddEtreEmpaIIIActivity.this.onFileSelectedListener);
		    		
		    	// Define the filter for select images.
		    	dialog.setFilter(".*jpg|.*png|.*gif|.*JPG|.*PNG|.*GIF");
		    	dialog.setShowOnlySelectable(false);
			     
			    dialog.show();
			}
		});
	    
	    eeiii_add_image2=(Button) findViewById(R.id.eeiii_add_image2);
	    eeiii_add_image2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				cursor = 2;
				
				FileChooserDialog dialog = new FileChooserDialog(AdminSerieAddEtreEmpaIIIActivity.this);
			    dialog.loadFolder(Environment.getExternalStorageDirectory().getAbsolutePath());
			     
			    // Assign listener for the select event.
			    dialog.addListener(AdminSerieAddEtreEmpaIIIActivity.this.onFileSelectedListener);
		    		
		    	// Define the filter for select images.
		    	dialog.setFilter(".*jpg|.*png|.*gif|.*JPG|.*PNG|.*GIF");
		    	dialog.setShowOnlySelectable(false);
			     
			    dialog.show();
			}
		});
	    
	    eeiii_add_image3=(Button) findViewById(R.id.eeiii_add_image3);
	    eeiii_add_image3.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				cursor = 3;
				
				FileChooserDialog dialog = new FileChooserDialog(AdminSerieAddEtreEmpaIIIActivity.this);
			    dialog.loadFolder(Environment.getExternalStorageDirectory().getAbsolutePath());
			     
			    // Assign listener for the select event.
			    dialog.addListener(AdminSerieAddEtreEmpaIIIActivity.this.onFileSelectedListener);
		    		
		    	// Define the filter for select images.
		    	dialog.setFilter(".*jpg|.*png|.*gif|.*JPG|.*PNG|.*GIF");
		    	dialog.setShowOnlySelectable(false);
			     
			    dialog.show();
			}
		});
	    
	    eeiii_add_image4=(Button) findViewById(R.id.eeiii_add_image4);
	    eeiii_add_image4.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				cursor = 4;
				
				FileChooserDialog dialog = new FileChooserDialog(AdminSerieAddEtreEmpaIIIActivity.this);
			    dialog.loadFolder(Environment.getExternalStorageDirectory().getAbsolutePath());
			     
			    // Assign listener for the select event.
			    dialog.addListener(AdminSerieAddEtreEmpaIIIActivity.this.onFileSelectedListener);
		    		
		    	// Define the filter for select images.
		    	dialog.setFilter(".*jpg|.*png|.*gif|.*JPG|.*PNG|.*GIF");
		    	dialog.setShowOnlySelectable(false);
			     
			    dialog.show();
			}
		});
	    
	    Button add_prop = (Button)findViewById(R.id.eeiii_add_prop);
	    add_prop.setOnClickListener(new View.OnClickListener() {
	    	@Override
			public void onClick(View arg0) {
				EditText edit_prop = (EditText)findViewById(R.id.eeiii_edit_prop);
				String new_proposition = edit_prop.getText().toString();
				
				if (!new_proposition.equals("")){
				
					TextView all_propositions = (TextView) findViewById(R.id.eeiii_text_proposition);
					if (eeiii_answer_prop.equals("")) {
						eeiii_answer_prop = new_proposition;
						all_propositions.setText(new_proposition);
					}else{
						eeiii_answer_prop = eeiii_answer_prop.concat(","+new_proposition);
						all_propositions.setText(eeiii_answer_prop);
					}
					edit_prop.setText("");
				}
			}
	    });
		
	    Button add_answer = (Button)findViewById(R.id.eeiii_add_good_answer);
	    add_answer.setOnClickListener(new View.OnClickListener() {
	    	@Override
			public void onClick(View arg0) {
	    		EditText edit_answer = (EditText)findViewById(R.id.eeiii_edit_answer);
				String new_answer = edit_answer.getText().toString();
				TextView text_answer = (TextView) findViewById(R.id.eeiii_text_answer);
				eeiii_good_answer=new_answer;
				text_answer.setText(eeiii_good_answer);				
				edit_answer.setText("");
			}
	    });
	    
	    eeiii_return= (Button) findViewById(R.id.eeiii_return);
	    eeiii_return.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent t=new Intent (AdminSerieAddEtreEmpaIIIActivity.this,AdminSerieActivity.class);
				t.putExtra("selectedModuleId", selectedModuleId);
        		t.putExtra("selectedModuleName", selectedModuleName);
        		t.putExtra("selectedSerieId", selectedSerieId);
        		t.putExtra("selectedSerieName", selectedSerieName);
				startActivity(t);
				finish();
			}
		});
		
	    eeiii_valid= (Button) findViewById(R.id.eeiii_valid);
	    eeiii_valid.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if ((eeiii_image1!="")&&(eeiii_image2!="")&&(eeiii_image3!="")&&(eeiii_image4!="")&&(eeiii_good_answer!="")&&(eeiii_answer_prop!="")){
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
						
						String selectedModuleStr = String.format("%02d", selectedModuleId);
						String selectedSerieStr = String.format("%02d", selectedSerieId);
						String num_question_Str = String.format("%02d", num_question);
						
						eeiii_image1 = Utils.SaveRenamePic(eeiii_image1, "module"+selectedModuleStr+"_serie"+selectedSerieStr+"_question"+num_question_Str+"_image01");
						eeiii_image2 = Utils.SaveRenamePic(eeiii_image2, "module"+selectedModuleStr+"_serie"+selectedSerieStr+"_question"+num_question_Str+"_image02");
						eeiii_image3 = Utils.SaveRenamePic(eeiii_image3, "module"+selectedModuleStr+"_serie"+selectedSerieStr+"_question"+num_question_Str+"_image03");
						eeiii_image4 = Utils.SaveRenamePic(eeiii_image4, "module"+selectedModuleStr+"_serie"+selectedSerieStr+"_question"+num_question_Str+"_image04");
						
						
						Element nodeImg1 = xml.createElement("img");
						nodeImg1.setAttribute("id", "1");
						nodeImg1.setAttribute("src", eeiii_image1);
						
						Element nodeImg2 = xml.createElement("img");
						nodeImg2.setAttribute("id", "2");
						nodeImg2.setAttribute("src", eeiii_image2);
						
						Element nodeImg3 = xml.createElement("img");
						nodeImg3.setAttribute("id", "3");
						nodeImg3.setAttribute("src", eeiii_image3);
						
						Element nodeImg4 = xml.createElement("img");
						nodeImg4.setAttribute("id", "4");
						nodeImg4.setAttribute("src", eeiii_image4);
						
						Element nodeRep = xml.createElement("rep");
						nodeRep.setAttribute("list", eeiii_answer_prop);
						nodeRep.setAttribute("goodanswer", eeiii_good_answer);
						
						nodeQuestion.appendChild(nodeImg1);
						nodeQuestion.appendChild(nodeImg2);
						nodeQuestion.appendChild(nodeImg3);
						nodeQuestion.appendChild(nodeImg4);
						nodeQuestion.appendChild(nodeRep);
						
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
						
						Toast toast = Toast.makeText(AdminSerieAddEtreEmpaIIIActivity.this,"Votre question a été ajoutée", Toast.LENGTH_LONG);
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
					Toast toast = Toast.makeText(AdminSerieAddEtreEmpaIIIActivity.this,"Merci de choisir 4 images et de remplir les champs de proposition et de réponse", Toast.LENGTH_LONG);
					toast.show();
				}
			}
	    });
		
		setupActionBar();
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
		getMenuInflater().inflate(R.menu.admin_serie_add_etre_empa_iii, menu);
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
	
	private FileChooserDialog.OnFileSelectedListener onFileSelectedListener = new FileChooserDialog.OnFileSelectedListener() {
		public void onFileSelected(Dialog source, File file) {
			source.hide();
			Toast toast = Toast.makeText(AdminSerieAddEtreEmpaIIIActivity.this, "File selected: " + file.getAbsolutePath(), Toast.LENGTH_LONG);
			toast.show();
			
			TextView t ;
			t = (TextView) findViewById(R.id.eeiii_image1);
			
			if (cursor == 1){
				t = (TextView) findViewById(R.id.eeiii_image1);
				eeiii_image1 = file.getAbsolutePath();
			}else if (cursor == 2){
				t = (TextView) findViewById(R.id.eeiii_image2);
				eeiii_image2 = file.getAbsolutePath();
			}else if (cursor == 3){
				t = (TextView) findViewById(R.id.eeiii_image3);
				eeiii_image3 = file.getAbsolutePath();
			}else if (cursor == 4){
				t = (TextView) findViewById(R.id.eeiii_image4);
				eeiii_image4 = file.getAbsolutePath();
			}
						
			t.setText(file.getAbsolutePath());
			
			
		}
		public void onFileSelected(Dialog source, File folder, String name) {
			source.hide();
			Toast toast = Toast.makeText(AdminSerieAddEtreEmpaIIIActivity.this, "File created: " + folder.getName() + "/" + name, Toast.LENGTH_LONG);
			toast.show();
		}
	};

}
