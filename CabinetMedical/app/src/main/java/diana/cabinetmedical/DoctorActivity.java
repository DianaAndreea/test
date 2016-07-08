package diana.cabinetmedical;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoctorActivity extends FragmentActivity implements View.OnClickListener {
    private static final String TAG = DoctorActivity.class.getSimpleName();
   /* private static final String url_review = "http://10.0.3.2/licenta/doctorComments.json";
    private static final String url_info = "http://10.0.3.2/licenta/doctorInfo.json";
    private static final String url_upload = "http://10.0.3.2/licenta/uploadReview.php";*/

    private static final String url_review="http://192.168.2.106/licenta/doctorComments.json";
    private static final String url_info="http://192.168.2.106/licenta/doctorInfo.json";
    private static final String url_upload="http://192.168.2.106/licenta/uploadReview.php";
    private ProgressDialog dialog;

    TextView numeDr, descriereDr, programDr, tarifDr, adresaDr, telefonDr, reviewTitlu, reviewComment;
    RatingBar rating;
    Button btnMap, btnApel, btnAdauga;

    private List<User> users = new ArrayList<User>();
    private List<Review> array = new ArrayList<Review>();
    private ListView reviewlist;
    private ReviewAdapter reviewAdapter;

    String id, map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_doctor_layout);

        numeDr = (TextView) findViewById(R.id.txtNumeDoctor);
        descriereDr = (TextView) findViewById(R.id.txtDescriere);
        programDr = (TextView) findViewById(R.id.txtProgram);
        tarifDr = (TextView) findViewById(R.id.txtTarif);
        adresaDr = (TextView) findViewById(R.id.txtAdresa);
        telefonDr = (TextView) findViewById(R.id.txtTelefon);

        btnMap = (Button) findViewById(R.id.btnHarta);
        btnMap.setOnClickListener(this);
        btnApel = (Button) findViewById(R.id.btnSuna);
        btnApel.setOnClickListener(this);

        reviewTitlu = (TextView) findViewById(R.id.txtTitlu);
        reviewComment = (TextView) findViewById(R.id.txtComentariu);
        rating = (RatingBar) findViewById(R.id.ratingBar);
        btnAdauga = (Button) findViewById(R.id.btnComentariu);
        btnAdauga.setOnClickListener(this);

        reviewlist = (ListView) findViewById(R.id.list_review);
        reviewAdapter = new ReviewAdapter(this, array);
        reviewlist.setAdapter(reviewAdapter);

        dialog=new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();

        setInfo();
        setComments();

    }

    private void setInfo(){
        JsonArrayRequest infoRequest = new JsonArrayRequest(url_info,new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());
                try{
                    for(int i=0; i<response.length(); i=i+7){
                        id = response.getJSONObject(i).getString("UserID");
                        numeDr.setText(response.getJSONObject(i+1).getString("numeDoctor"));
                        adresaDr.setText(response.getJSONObject(i+2).getString("adresa"));
                        descriereDr.setText(response.getJSONObject(i+3).getString("descriere"));
                        programDr.setText(response.getJSONObject(i+4).getString("program"));
                        tarifDr.setText(response.getJSONObject(i+5).getString("tarif"));
                        telefonDr.setText(response.getJSONObject(i+6).getString("telefon"));
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }
                hideDialog();
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hideDialog();
            }
        });
        MySingleton.getInstance().addToRequestQueue(infoRequest);
    }

    private void setComments(){
        JsonArrayRequest reviewReq = new JsonArrayRequest(url_review,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e(TAG, response.toString());
                hideDialog();
                for(int i = 0; i<response.length(); i=i+3){
                    try{
                        Review item = new Review();
                        item.setCommentUserName(response.getJSONObject(i).getString("UserName"));
                        item.setRate(Float.parseFloat(response.getJSONObject(i+1).getString("RatingValue")));
                        item.setComment(response.getJSONObject(i+2).getString("Comentariu"));

                        array.add(item);

                    }catch(JSONException ex){
                        ex.printStackTrace();
                    }
                }
                reviewAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                hideDialog();
            }
        });

        MySingleton.getInstance().addToRequestQueue(reviewReq);
    }

    public void hideDialog(){
        if(dialog != null){
            dialog.dismiss();
            dialog = null;
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        hideDialog();
    }

    @Override
    public void onClick(View v) {
        if( v == btnMap){
          String  adr = adresaDr.getText().toString();
          getMap(adr);

        }else if( v == btnApel){
            String p = "tel:" + telefonDr.getText();
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse(p));
            startActivity(intent);
        }

        else if ( v == btnAdauga){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url_upload, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                if(response.contains("success")){
                    Toast.makeText(getApplicationContext(), "Comentariul a fost adaugat!", Toast.LENGTH_SHORT).show();
                    Intent in=new Intent(DoctorActivity.this,DoctoriActivity.class);
                    startActivity(in);
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), "eroare", Toast.LENGTH_SHORT).show();
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
                params.put("txtDoctorid",id);
                params.put("txtUserName",reviewTitlu.getText().toString());
                params.put("txtRating", String.valueOf(rating.getRating()));
                params.put("txtComentariu",reviewComment.getText().toString());

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
    }

    private void getMap(String adresa){

        String uri = "google.navigation:q=" +adresa +" Galati";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        startActivity(intent);
    }
}
