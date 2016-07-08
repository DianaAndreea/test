package diana.cabinetmedical;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DoctoriActivity extends AppCompatActivity {
    private static final String TAG = DoctoriActivity.class.getSimpleName();
    //  private static final String url = "http://10.0.3.2/licenta/result.json";
   private static final String url = "http://192.168.2.106/licenta/result.json";
    private ProgressDialog dialog;

    private List<User> array = new ArrayList<User>();
    private ListView listView;
    private UserAdapter adapter;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctori);

        listView= (ListView) findViewById(R.id.list_item);
        adapter = new UserAdapter(this, array);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User value = array.get(position);
                userId= value.getUserID();
                getUserListInfo(userId);
                getUserComments(userId);
                Intent i = new Intent(DoctoriActivity.this, DoctorActivity.class);
                startActivity(i);
            }
        });


        dialog=new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();

        setList();

    }

    private void setList(){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());
                hideDialog();
                for(int i = 0; i < response.length(); i=i+2){
                    try{
                        JSONObject obj = response.getJSONObject(i);
                        User item = new User();
                        item.setUserID(Integer.parseInt(response.getJSONObject(i).getString("UserID")));
                        item.setNumeDoctor(response.getJSONObject(i+1).getString("numeDoctor"));
                        array.add(item);
                    }catch(JSONException ex){
                        ex.printStackTrace();
                        Log.e(TAG, String.valueOf(ex));
                    }
                }
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hideDialog();
            }
        });

        MySingleton.getInstance().addToRequestQueue(jsonArrayRequest);
    }

    private void getUserListInfo(final int userId){
        this.userId = userId;
        RequestQueue queue = Volley.newRequestQueue(this);
        // String url = "http://10.0.3.2/licenta/getInfoDoctor.php";
         String url="http://192.168.2.106/licenta/getInfoDoctor.php";
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response.toString());
            }
        },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Log.e(TAG, error.toString());
                    }
                })
        {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("userId", String.valueOf(userId));
                return params;
            }
        };
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        sr.setRetryPolicy(policy);
        queue.add(sr);
    }

    private void getUserComments(final int userId){
        this.userId = userId;
        RequestQueue queue = Volley.newRequestQueue(this);
        //String url = "http://10.0.3.2/licenta/getReviews.php";
         String url="http://192.168.2.106/licenta/getReviews.php";
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response.toString());
            }
        },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Log.e(TAG, error.toString());
                    }
                })
        {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("userId", String.valueOf(userId));
                return params;
            }
        };
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        sr.setRetryPolicy(policy);
        queue.add(sr);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        hideDialog();
    }

    public void hideDialog(){
        if(dialog != null){
            dialog.dismiss();
            dialog = null;
        }
    }
    public void onBackPressed(){
        Intent intent = new Intent(DoctoriActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
