package com.example.rodrigo.chat;

import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import cifrado.ZigZag;
import cifrado.DescifradoZigZag;

public class Inicio extends AppCompatActivity {

    EditText txtUser;
    EditText txtPassword;
    Button btnLogin;
    Button btnRegister;
    String token;
    private RequestQueue queue;
    private RequestQueue Pqueue;
    TextView sal;
    int status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EnviaTexto mievento = new EnviaTexto();

        final String url = "http://192.168.1.14:5000/api/chat";
        btnLogin = (Button) findViewById(R.id.btnLogin);
        txtUser = (EditText)findViewById(R.id.txtUser);
        txtPassword = (EditText)findViewById(R.id.txtPassword);
        btnRegister = (Button)findViewById(R.id.btnRegister);
        sal  = (TextView) findViewById(R.id.salida);
        getData();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = txtUser.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();
              if(!username.isEmpty() || !password.isEmpty()){
                  Login(username, password, url);
                  openActivityChat();
              }else{
                  txtUser.setError("Porfavor ingrese el username");
                  txtPassword.setError("Porfavor ingrese la contraseña");

              }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               openActivityRegister();
            }
        });


        //peticiones http request
        queue = Volley.newRequestQueue(this);
        jsonParse();
        obtenerDatosVolley(url);

    }


    //------------------------------------login-----------------------------------------------------
    private void Login(final String email, final String password, String url){
        btnLogin.setVisibility(View.GONE);
        //status=result.getHeaders().code();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("token");
                    JSONArray jsonArray = jsonObject.getJSONArray("signIn");

                    if (success.equals("token")){
                        for (int i =0; i<jsonArray.length(); i++){
                            JSONObject object = jsonArray.getJSONObject(i);

                            String username = object.getString("username").trim();

                            Toast.makeText(Inicio.this, "Succes login. Tu nombre de usuario: " + username, Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Inicio.this, "Error: " + e.toString(), Toast.LENGTH_SHORT).show();

                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Inicio.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();

                    }
                }){
          @Override
          protected Map<String, String> getParams() throws AuthFailureError{
              Map<String, String> params = new HashMap<>();

              params.put("username", email);
              params.put("password", password);
              return params;

          }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //------------------------------------metodo get------------------------------------------------
    private void obtenerDatosVolley(String url){

        //String url = "http://localhost:3000/api/chat";

        JsonObjectRequest request = new JsonObjectRequest( url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray mJsonArray = response.getJSONArray("chats");
                    for(int i =0; i<mJsonArray.length(); i++){
                        JSONObject mJsonObject = mJsonArray.getJSONObject(i);
                        String name = mJsonObject.getString("token");

                        Toast.makeText(Inicio.this, "Nickname: " + name, Toast.LENGTH_SHORT).show();
                    }
                }
                catch (JSONException e){
                    e.printStackTrace();
                }}



        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        queue.add(request);
}

    //------------------------------------metodo post------------------------------------------------
    private void enviarDatosVolley(String url){

        JSONObject paramJson = new JSONObject();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, paramJson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        Pqueue.add(jsonObjectRequest);
    }

    //---------------------------------

    private void jsonParse() {

        String url = "https://api.myjson.com/bins/kp9wz";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("employees");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject employee = jsonArray.getJSONObject(i);

                                String firstName = employee.getString("firstname");
                                int age = employee.getInt("age");
                                String mail = employee.getString("mail");


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        queue.add(request);
    }

    private class EnviaTexto implements WifiP2pManager.ActionListener {

        @Override
        public void onSuccess() {
            try {
                Socket misocket = new Socket("192.168.1.14", 3000);

                DataOutputStream flujosalida = new DataOutputStream(misocket.getOutputStream());
                
                flujosalida.writeUTF(txtUser.getText().toString());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int reason) {

        }
    }

    public void openActivityRegister()
    {
        Intent intent = new Intent(this,register.class);
        startActivity(intent);
    }

    public void openActivityChat()
    {
        Intent intent = new Intent(this,chat.class);
        startActivity(intent);
    }

    public void getData(){
        String sql = "http://localhost:3000/api/chat";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        URL url = null;
        HttpURLConnection conn;

        try {
            url = new URL(sql);
            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");

            conn.connect();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String inputLine;

            StringBuffer response = new StringBuffer();

            String json = "";

            while((inputLine = in.readLine()) != null){
                response.append(inputLine);
            }

            json = response.toString();

            JSONArray jsonArr = null;

            jsonArr = new JSONArray(json);
            String mensaje = "";
            for(int i = 0;i<jsonArr.length();i++){
                JSONObject jsonObject = jsonArr.getJSONObject(i);

                //Log.d("SLIDA",jsonObject.optString("nick"));
                mensaje += "nick "+i+" "+jsonObject.optString("nick")+"\n";
            }
            sal.setText(mensaje);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
