package diana.cabinetmedical;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kosalgeek.android.json.JsonConverter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static diana.cabinetmedical.R.layout.activity_login;

public class LoginActivity extends Activity implements View.OnClickListener{
    final String LOG = "LoginActivity";
    Button btnLog, btnReg;
    EditText mNume, mPass;
    boolean Nume;
    public static boolean isLog = false;

    private final String TAG=this.getClass().getSimpleName();
    ArrayList<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_login);

        mNume = (EditText)findViewById(R.id.txtNume);
        mPass = (EditText)findViewById(R.id.txtParola);
        btnLog = (Button)findViewById(R.id.btnLog);
        btnReg = (Button) findViewById(R.id.btnSignUp);

        btnLog.setOnClickListener(this);
        btnReg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,Register.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void onClick(View v){
        // String url="http://10.0.3.2/licenta/login.php";
       String url="http://192.168.2.106/licenta/login.php";

       if((!mNume.getText().toString().equals("")) && (!mPass.getText().toString().equals(""))){
          if(mPass.getText().length() > 6){
              Nume = true;
          }
          else{
              Nume = false;
              Toast.makeText(getApplicationContext(),"Parola trebuie sa aibe minim 6 caractere!",Toast.LENGTH_SHORT).show();
          }
       }
        else{
             Toast.makeText(getApplicationContext(),"Campurile sunt goale!",Toast.LENGTH_LONG).show();
        }
        if(Nume==true){
            StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                        Log.d(TAG,response);
                        if(response.contains("failed_login"))
                            Toast.makeText(getApplicationContext(),"Username sau parola sunt incorecte!",Toast.LENGTH_SHORT).show();
                        else{
                            userList=new JsonConverter<User>().toArrayList(response, User.class);
                            User LoggedUser=userList.get(0);

                            String n = mNume.getText().toString();
                            String p = mPass.getText().toString();
                            SharedPreferences sharedpreferences = getSharedPreferences("login", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("username", n);
                            editor.putString("password", p);
                            editor.commit();
                            Toast.makeText(LoginActivity.this,"Thanks",Toast.LENGTH_LONG).show();

                            Intent in=new Intent(LoginActivity.this,MainActivity.class);
                            in.putExtra("user", (Serializable) LoggedUser);
                            isLog = true;
                            startActivity(in);
                            finish();
                        }
                    }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
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
                    params.put("txtNume",mNume.getText().toString());
                    params.put("txtParola",mPass.getText().toString());
                    return params;
                }
            };

            MySingleton.getInstance().addToRequestQueue(stringRequest);
        }
    }
    public void onBackPressed(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
