package diana.cabinetmedical;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    static Button btnNav;
    static TextView txtUs;
    User usernameLog;
    String valoare;
    Button btnAlergologie, btnBInfectioase, btnCardiologie, btnChirurgie, btnDermato, btnEndocrinologie, btnEstetica,
            btnGastro, btnHematologie, btnHomeopatie, btnLaborator, btnMedicFamilie, btnNeurologie, btnGinecologie,
            btnOftalmologie, btnOncologie, btnOrl, btnPediatrie, btnRecuperare, btnStomatologie;
    NavigationView navigationView;

    private List<User> array = new ArrayList<User>();
    private ArrayList<User> userList;
    private UserAdapter adapter;
    int userid;

    private final String TAG = this.getClass().getSimpleName();
    private static final String url_delete = "http://192.168.2.106/licenta/deleteAccount.php";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnAlergologie = (Button) findViewById(R.id.btnAlergologie);
        btnAlergologie.setOnClickListener(this);

        btnBInfectioase = (Button) findViewById(R.id.btnBInfectioase);
        btnBInfectioase.setOnClickListener(this);

        btnCardiologie = (Button) findViewById(R.id.btnCardiologie);
        btnCardiologie.setOnClickListener(this);

        btnChirurgie = (Button) findViewById(R.id.btnChirurgie);
        btnChirurgie.setOnClickListener(this);

        btnDermato = (Button) findViewById(R.id.btnDermato);
        btnDermato.setOnClickListener(this);

        btnEndocrinologie = (Button) findViewById(R.id.btnEndocrinologie);
        btnEndocrinologie.setOnClickListener(this);

        btnEstetica = (Button) findViewById(R.id.btnEstetica);
        btnEstetica.setOnClickListener(this);

        btnGastro = (Button) findViewById(R.id.btnGastro);
        btnGastro.setOnClickListener(this);

        btnHematologie = (Button) findViewById(R.id.btnHematologie);
        btnHematologie.setOnClickListener(this);

        btnHomeopatie = (Button) findViewById(R.id.btnHomeopatie);
        btnHomeopatie.setOnClickListener(this);

        btnLaborator = (Button) findViewById(R.id.btnLaborator);
        btnLaborator.setOnClickListener(this);

        btnMedicFamilie = (Button) findViewById(R.id.btnMedicFamilie);
        btnMedicFamilie.setOnClickListener(this);

        btnNeurologie = (Button) findViewById(R.id.btnNeurologie);
        btnNeurologie.setOnClickListener(this);

        btnGinecologie = (Button) findViewById(R.id.btnGinecologie);
        btnGinecologie.setOnClickListener(this);

        btnOftalmologie = (Button) findViewById(R.id.btnOftalmologie);
        btnOftalmologie.setOnClickListener(this);

        btnOncologie = (Button) findViewById(R.id.btnOncologie);
        btnOncologie.setOnClickListener(this);

        btnOrl = (Button) findViewById(R.id.btnOrl);
        btnOrl.setOnClickListener(this);

        btnPediatrie = (Button) findViewById(R.id.btnPediatrie);
        btnPediatrie.setOnClickListener(this);

        btnRecuperare = (Button) findViewById(R.id.btnRecuperare);
        btnRecuperare.setOnClickListener(this);

        btnStomatologie = (Button) findViewById(R.id.btnStomatologie);
        btnStomatologie.setOnClickListener(this);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
        View headerview = navigationView.getHeaderView(0);

        btnNav = (Button) headerview.findViewById(R.id.navLog);
        txtUs = (TextView) headerview.findViewById(R.id.txtUser);

        if (LoginActivity.isLog) {
            usernameLog = (User) getIntent().getSerializableExtra("user");
            adapter = new UserAdapter(this, array);
            userid = usernameLog.getUserID();

            SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
            String nume = sharedPreferences.getString("username", "");
            txtUs.setText(nume);

            if (txtUs.getText() != null) {
                btnNav.setVisibility(View.GONE);
                txtUs.setVisibility(View.VISIBLE);
                navigationView.getMenu().findItem(R.id.menuProfil).setVisible(true);
                navigationView.getMenu().findItem(R.id.menuCont).setVisible(true);
            }
        } else {
            LoginActivity.isLog = false;
            txtUs.setVisibility(View.GONE);
            btnNav.setVisibility(View.VISIBLE);
        }

        btnNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                drawer.closeDrawer(GravityCompat.START);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.


        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_about) {

        } else if (id == R.id.nav_modificaNumeCabinet) {


        } else if (id == R.id.nav_modificaDescriere) {

        } else if (id == R.id.nav_modificaProgram) {

        } else if (id == R.id.nav_modificaTarif) {

        } else if (id == R.id.nav_modificaAdresa) {

        } else if (id == R.id.nav_modificaTelefon) {

        } else if (id == R.id.nav_autentificare) {

        } else if (id == R.id.nav_deleteAccount) {

            DeleteAccount();

        } else if (id == R.id.nav_deconectare) {
            SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username", "");
            editor.commit();
            String nume = sharedPreferences.getString("username", "");
            txtUs.setText(nume);

            if (txtUs.getText().equals("")) {
                try {
                    LoginActivity.isLog = false;
                    txtUs.setVisibility(View.GONE);
                    btnNav.setVisibility(View.VISIBLE);

                    navigationView.getMenu().findItem(R.id.menuProfil).setVisible(false);
                    navigationView.getMenu().findItem(R.id.menuCont).setVisible(false);

                    Toast.makeText(MainActivity.this, "Logout", Toast.LENGTH_LONG).show();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        LoginActivity.isLog = false;
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnAlergologie:
                valoare = "Alergologie";
                break;
            case R.id.btnBInfectioase:
                valoare = "Boli Infectioase";
                break;
            case R.id.btnCardiologie:
                valoare = "Cardiologie";
                break;
            case R.id.btnChirurgie:
                valoare = "Chirurgie Generala";
                break;
            case R.id.btnDermato:
                valoare = "Dermato Venerologie";
                break;
            case R.id.btnEndocrinologie:
                valoare = "Endocrinologie";
                break;
            case R.id.btnEstetica:
                valoare = "Estetica-Infrumusetare";
                break;
            case R.id.btnGastro:
                valoare = "Gastro Enterologie";
                break;
            case R.id.btnHematologie:
                valoare = "Hematologie";
                break;
            case R.id.btnHomeopatie:
                valoare = "Homeopatie";
                break;
            case R.id.btnLaborator:
                valoare = "Laborator Analize";
                break;
            case R.id.btnMedicFamilie:
                valoare = "Medicina de familie";
                break;
            case R.id.btnNeurologie:
                valoare = "Neurologie";
                break;
            case R.id.btnGinecologie:
                valoare = "Obstretica-Ginecologie";
                break;
            case R.id.btnOftalmologie:
                valoare = "Oftalmologie";
                break;
            case R.id.btnOncologie:
                valoare = "Oncologie";
                break;
            case R.id.btnOrl:
                valoare = "ORL";
                break;
            case R.id.btnPediatrie:
                valoare = "Pediatrie";
                break;
            case R.id.btnRecuperare:
                valoare = "Recuperare-Kinetoterapie";
                break;
            case R.id.btnStomatologie:
                valoare = "Stomatologie";
                break;
        }
        getSpecializare(valoare);
        Intent intent = new Intent(MainActivity.this, DoctoriActivity.class);
        startActivity(intent);
    }

    private void getSpecializare(final String valoare) {
        this.valoare = valoare;
        RequestQueue queue = Volley.newRequestQueue(this);
        // String url = "http://10.0.3.2/licenta/getDoctori.php";
        String url = "http://192.168.2.106/licenta/getDoctori.php";
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response.toString());
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("valoareSpecializare", valoare);
                return params;
            }
        };
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        sr.setRetryPolicy(policy);
        queue.add(sr);
    }

    private void DeleteAccount() {
        StringRequest deleteRequest = new StringRequest(Request.Method.DELETE, url_delete, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG,response);
                if(response.contains("success")){
                    userList.remove(userid);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(),"Contul a fost sters",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Eroare in baza de date!",Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e(TAG, "Error: " + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<>();
                params.put("userId", String.valueOf(userid));
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers=new HashMap<>();
                headers.put("User-Agent","Android");
                return headers;
            }
        };
        MySingleton.getInstance().addToRequestQueue(deleteRequest);
    }
}