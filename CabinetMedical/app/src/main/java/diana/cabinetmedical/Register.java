package diana.cabinetmedical;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Register extends Activity implements View.OnClickListener, Spinner.OnItemSelectedListener {

    private EditText mNumeUtilizator, mEmailUser, mParolaUser, mNumeDoctor, mAdresa, mDescriere, mProgram, mTarif, mTelefon;
    private Spinner mSpecializare;
    private Button btnRegister;
    boolean isCompleted;

    // private String UPLOAD_URL = "http://10.0.3.2/licenta/register.php";
    private String UPLOAD_URL="http://192.168.2.106/licenta/register.php";

    private final String TAG=this.getClass().getSimpleName();
    private ArrayList<String> specializari;
    private JSONArray result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        specializari = new ArrayList<String>();
        mSpecializare = (Spinner) findViewById(R.id.regTxtSpecializare);
        mSpecializare.setOnItemSelectedListener(this);

        getData();

        mNumeUtilizator = (EditText) findViewById(R.id.regTxtUser);
        mEmailUser = (EditText) findViewById(R.id.regTxtEmail);
        mParolaUser = (EditText) findViewById(R.id.regTxtParola);
        mNumeDoctor = (EditText) findViewById(R.id.regTxtNumeCabinet);
        mAdresa = (EditText) findViewById(R.id.regTxtAdresa);
        mDescriere = (EditText) findViewById(R.id.regTxtDescriere);
        mProgram = (EditText) findViewById(R.id.regTxtProgram);

        mTarif = (EditText) findViewById(R.id.regTxtTarif);
        mTelefon = (EditText) findViewById(R.id.regTxtTelefon);

        btnRegister = (Button) findViewById(R.id.regBtn);
        btnRegister.setOnClickListener(this);
    }

    private void getData(){
        StringRequest stringRequest = new StringRequest(SpecConfig.Data_URL,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        JSONObject j = null;
                        try{
                            j = new JSONObject(response);
                            result = j.getJSONArray(SpecConfig.JSON_ARRAY);
                            getSpecializari(result);
                        }catch(JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener(){
            @Override
        public void onErrorResponse(VolleyError error){

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getSpecializari(JSONArray j){
        for(int i=0; i<j.length();i++){
            try{
                JSONObject json = j.getJSONObject(i);
                specializari.add(json.getString(SpecConfig.TAG_Denumire));
            } catch (JSONException e){
                e.printStackTrace();
            }
        }
        mSpecializare.setAdapter(new ArrayAdapter<String>(Register.this, android.R.layout.simple_spinner_dropdown_item, specializari));
    }

    private String getDenumire(int position){
        String denumire="";
        try{
            JSONObject json = result.getJSONObject(position);
            denumire = json.getString(SpecConfig.TAG_Denumire);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return denumire;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void onClick(View v){

     //  sendRequest();
         //String url="http://10.0.3.2/licenta/register.php";
        //String url="http://192.168.2.106/licenta/register.php";
        if((!mNumeUtilizator.getText().toString().equals("")) && (!mEmailUser.getText().toString().equals(""))
                && (!mParolaUser.getText().toString().equals("")) && (!mNumeDoctor.getText().toString().equals(""))
                && (!mAdresa.getText().toString().equals("")) && (!mDescriere.getText().toString().equals(""))
                && (!mProgram.getText().toString().equals(""))
                && (!mSpecializare.getSelectedItem().equals(null))
                && (!mTarif.getText().toString().equals("")) && (!mTelefon.getText().toString().equals(""))){

            if(mNumeUtilizator.getText().length() < 5){
                isCompleted = false;
                Toast.makeText(getApplicationContext(),"Numele de utilizator este prea mic !",Toast.LENGTH_LONG).show();
            }
            else if (mNumeUtilizator.getText().length() > 10){
                isCompleted = false;
                Toast.makeText(getApplicationContext(),"Numele de utilizator este prea mare !",Toast.LENGTH_LONG).show();
            }
            else if(!(mParolaUser.getText().length() > 6)){
                isCompleted = false;
                Toast.makeText(getApplicationContext(),"Parola trebuie sa aibe minim 6 caractere !",Toast.LENGTH_LONG).show();
            }
            else if (mNumeDoctor.getText().length() < 5){
                isCompleted = false;
                Toast.makeText(getApplicationContext(),"Numele cabinetului este prea mic !",Toast.LENGTH_LONG).show();
            }
            else if (mAdresa.getText().length() < 5){
                isCompleted = false;
                Toast.makeText(getApplicationContext(),"Adresa este prea scurta !",Toast.LENGTH_LONG).show();
            }
            else if (mTelefon.getText().length() < 10){
                isCompleted = false;
                Toast.makeText(getApplicationContext(),"Numarul de telefon nu este corect !",Toast.LENGTH_LONG).show();
            }
            else if(!isValidEmail(mEmailUser.getText().toString())){
                isCompleted = false;
                Toast.makeText(getApplicationContext(),"Adresa de e-mail nu este corecta !",Toast.LENGTH_LONG).show();
            }

            else isCompleted = true;

        }
        else if(mSpecializare.getSelectedItem().equals(null)){
            isCompleted=false;
            Toast.makeText(getApplicationContext(),"Nu ai selectat o specializare !",Toast.LENGTH_LONG).show();
        }
        else{
            isCompleted = false;
            Toast.makeText(getApplicationContext(), "Campurile sunt goale", Toast.LENGTH_LONG).show();
        }

        if(isCompleted == true){
            uploadRegister();
            }
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private void uploadRegister(){
       // final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, UPLOAD_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                if(response.contains("success")){
                    //loading.dismiss();
                    Toast.makeText(getApplicationContext(), "Register complete", Toast.LENGTH_SHORT).show();
                    Intent in=new Intent(Register.this,MainActivity.class);
                    startActivity(in);
                    finish();
                }
               else{
                    Toast.makeText(getApplicationContext(), "Numele de utilizator exista deja!", Toast.LENGTH_SHORT).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // loading.dismiss();
                if(error instanceof TimeoutError){
                    Toast.makeText(getApplicationContext(),"Timeout Error",Toast.LENGTH_SHORT).show();
                }
                else if(error instanceof NoConnectionError){
                    Toast.makeText(getApplicationContext(),"No Connection Error",Toast.LENGTH_SHORT).show();
                }
                else if(error instanceof AuthFailureError){
                    Toast.makeText(getApplicationContext(),"Auth Failure Error",Toast.LENGTH_SHORT).show();
                }
                else if(error instanceof NetworkError){
                    Toast.makeText(getApplicationContext(),"NetworkError",Toast.LENGTH_SHORT).show();
                }
                else if(error instanceof ServerError){
                    Toast.makeText(getApplicationContext(),"Server Error",Toast.LENGTH_SHORT).show();
                }
                else if(error instanceof ParseError){
                    Toast.makeText(getApplicationContext(),"JSON Parse Error",Toast.LENGTH_SHORT).show();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params=new HashMap<>();
                params.put("txtNumeUser",mNumeUtilizator.getText().toString());
                params.put("txtEmailUser",mEmailUser.getText().toString());
                params.put("txtParolaUser",mParolaUser.getText().toString());
                params.put("txtNumeDoctor",mNumeDoctor.getText().toString());
                params.put("txtAdresaDoctor",mAdresa.getText().toString());
                params.put("txtDescriere",mDescriere.getText().toString());
                params.put("txtProgram", mProgram.getText().toString());
                params.put("txtSpecId", String.valueOf(mSpecializare.getSelectedItemPosition() + 1));
                params.put("txtTarifDoctor",mTarif.getText().toString());
                params.put("txtTelefonDoctor",mTelefon.getText().toString());

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers=new HashMap<>();
                headers.put("User-Agent","Android");
                return headers;
            }
        };

        MySingleton.getInstance().addToRequestQueue(stringRequest);
    }

    public void onBackPressed(){
        Intent intent = new Intent(Register.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
