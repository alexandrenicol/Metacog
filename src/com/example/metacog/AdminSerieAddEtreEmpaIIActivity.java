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

//import android.support.v7.app.ActionBarActivity;
//import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;
import ar.com.daidalos.afiledialog.FileChooserDialog;

public class AdminSerieAddEtreEmpaIIActivity extends Activity {

	private String externalStorage;
	private String structureFilename;
	
	private Integer selectedModuleId;
	private String selectedModuleName;
	private Integer selectedSerieId;
	private String selectedSerieName;
	
	public Button add;
	public Button add_little_image;
		
	public Button valid;
	
	private String proposition = "";
	private String answer = "";
	
	private String image_path;
	private String little_image_path;
	
	public String cursor = "image_path";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_serie_add_etre_empa_ii);
		
		externalStorage = Environment.getExternalStorageDirectory().getAbsolutePath();
        structureFilename = externalStorage+"/structure_modules.xml";
		
		Bundle extras = getIntent().getExtras();
	    selectedModuleId = extras.getInt("selectedModuleId");
	    selectedModuleName = extras.getString("selectedModuleName");
	    selectedSerieId = extras.getInt("selectedSerieId");
	    selectedSerieName = extras.getString("selectedSerieName");
	    
	    add=(Button) findViewById(R.id.add_image1);
	    add.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				cursor = "image_path";
				
				FileChooserDialog dialog = new FileChooserDialog(AdminSerieAddEtreEmpaIIActivity.this);
			    dialog.loadFolder(Environment.getExternalStorageDirectory().getAbsolutePath());
			     
			    // Assign listener for the select event.
			    dialog.addListener(AdminSerieAddEtreEmpaIIActivity.this.onFileSelectedListener);
		    		
		    	// Define the filter for select images.
		    	dialog.setFilter(".*jpg|.*png|.*gif|.*JPG|.*PNG|.*GIF");
		    	dialog.setShowOnlySelectable(false);
			     
			    dialog.show();
			}
		});
	    
	    add_little_image=(Button) findViewById(R.id.add_petite_image);
	    add_little_image.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				cursor = "little_image_path";
				
				FileChooserDialog dialog = new FileChooserDialog(AdminSerieAddEtreEmpaIIActivity.this);
			    dialog.loadFolder(Environment.getExternalStorageDirectory().getAbsolutePath());
			     
			    // Assign listener for the select event.
			    dialog.addListener(AdminSerieAddEtreEmpaIIActivity.this.onFileSelectedListener);
		    		
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
				if ((image_path!="")&&(answer!="")&&(proposition!="")){
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
						
						
						Element nodeImg1 = xml.createElement("img");
						nodeImg1.setAttribute("id", "1");
						nodeImg1.setAttribute("src", image_path);
						
						Element nodeImg2 = xml.createElement("img");
						nodeImg2.setAttribute("id", "2");
						nodeImg2.setAttribute("src", little_image_path);
						
						Element nodeRep = xml.createElement("rep");
						nodeRep.setAttribute("list", proposition);
						nodeRep.setAttribute("goodanswer", answer);
						
						nodeQuestion.appendChild(nodeImg1);
						nodeQuestion.appendChild(nodeImg2);
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
						
						Toast toast = Toast.makeText(AdminSerieAddEtreEmpaIIActivity.this,"Votre question a été ajoutée", Toast.LENGTH_LONG);
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
					Toast toast = Toast.makeText(AdminSerieAddEtreEmpaIIActivity.this,"Merci de choisir une images et de remplir les champs de proposition et de réponse", Toast.LENGTH_LONG);
					toast.show();
				}
			}
	    });
		
	    
	    Button add_prop = (Button)findViewById(R.id.add_proposition);
	    add_prop.setOnClickListener(new View.OnClickListener() {
	    	@Override
			public void onClick(View arg0) {
				EditText edit_prop = (EditText)findViewById(R.id.edit_proposition);
				String new_proposition = edit_prop.getText().toString();
				
				if (!new_proposition.equals("")){
				
					TextView all_propositions = (TextView) findViewById(R.id.text_proposition);
					if (proposition.equals("")) {
						proposition = new_proposition;
						all_propositions.setText(new_proposition);
					}else{
						proposition = proposition.concat(","+new_proposition);
						all_propositions.setText(proposition);
					}
					edit_prop.setText("");
				}
			}
	    });
		
	    Button add_answer = (Button)findViewById(R.id.add_answer);
	    add_answer.setOnClickListener(new View.OnClickListener() {
	    	@Override
			public void onClick(View arg0) {
	    		EditText edit_answer = (EditText)findViewById(R.id.edit_answer);
				String new_answer = edit_answer.getText().toString();
				TextView text_answer = (TextView) findViewById(R.id.text_answer);
				answer=new_answer;
				text_answer.setText(answer);				
				edit_answer.setText("");
			}
	    });
		

		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.admin_serie_add_etre_empa_ii, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
	
	private FileChooserDialog.OnFileSelectedListener onFileSelectedListener = new FileChooserDialog.OnFileSelectedListener() {
		public void onFileSelected(Dialog source, File file) {
			source.hide();
			Toast toast = Toast.makeText(AdminSerieAddEtreEmpaIIActivity.this, "File selected: " + file.getAbsolutePath(), Toast.LENGTH_LONG);
			toast.show();
			
			TextView t ;
			t = (TextView) findViewById(R.id.text_image);
			
			if (cursor.equals("image_path")){
				t = (TextView) findViewById(R.id.text_image);
				image_path = file.getAbsolutePath();
			}else if (cursor.equals("little_image_path")){
				t = (TextView) findViewById(R.id.text_petite_image);
				little_image_path = file.getAbsolutePath();
			}
						
			t.setText(file.getAbsolutePath());
			
			
		}
		public void onFileSelected(Dialog source, File folder, String name) {
			source.hide();
			Toast toast = Toast.makeText(AdminSerieAddEtreEmpaIIActivity.this, "File created: " + folder.getName() + "/" + name, Toast.LENGTH_LONG);
			toast.show();
		}
	};

}