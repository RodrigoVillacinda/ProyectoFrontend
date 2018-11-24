package com.example.rodrigo.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

import cifrado.ZigZag;
import cifrado.DescifradoZigZag;

public class register extends AppCompatActivity {

    EditText txtName;
    EditText txtUser;
    EditText txtEmail;
    EditText txtPassword;
    Button btnRegisterC;
    //String password;
    //String username;
    //String email;
    //String name;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);

        //mapeo de variables
        txtName = (EditText)findViewById(R.id.txtName);
        txtUser = (EditText)findViewById(R.id.txtUser);
        txtPassword = (EditText)findViewById(R.id.txtPassword);
        txtEmail = (EditText)findViewById(R.id.txtEmail);
        btnRegisterC = (Button)findViewById(R.id.btnRegisterC);



        final String url = "http://192.168.1.14:5000/api/register";
        queue = Volley.newRequestQueue(this);

        obtenerDatosVolley(url);

        btnRegisterC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Regist(url);
                    Intent intent = new Intent(register.this,chat.class);
                    startActivity(intent);

            }
        });


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

                        Toast.makeText(register.this, "Nickname: " + name, Toast.LENGTH_SHORT).show();
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

    //------------------------------------Regist----------------------------------------------------
    private void Regist(String url){
        btnRegisterC.setVisibility(View.GONE);

        ZigZag hola= new ZigZag(3, this.txtPassword.getText().toString());
        String key = hola.Cifrado();

        final String name = this.txtName.getText().toString().trim();
        final String email = this.txtEmail.getText().toString().trim();
        final String username = this.txtUser.getText().toString().trim();
        final String password = key.trim();



        StringRequest stringRequest =  new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    String sucess = jsonObject.getString("token");
                    if (sucess.equals("token")){
                        Toast.makeText(register.this, "Register success!", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(register.this, "Register Error!" + e.toString(), Toast.LENGTH_SHORT).show();
                    btnRegisterC.setVisibility(View.VISIBLE);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(register.this, "Register Error!" + error.toString(), Toast.LENGTH_SHORT).show();
                        btnRegisterC.setVisibility(View.VISIBLE);
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("email", email);
                params.put("username", username);
                params.put("password", password);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


}
