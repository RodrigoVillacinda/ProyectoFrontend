package com.example.rodrigo.chat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Respuesta.TokenHandler;
import cifrado.ZigZag;
import cifrado.DescifradoZigZag;
import compresion.LZW;


import Respuesta.Constantes;

public class chat extends AppCompatActivity {

    private RequestQueue queue;
    Button btnNewmsg;
    Button btnSend;
    EditText txtMessage;
    ScrollView scrChat;
    TextView txtContent;
    String nick;
    String msg;
    String recept;
    String url = "http://localhost:5000/api/chat";

    private String MENSAJE_ENVIAR = "";
    private String EMISOR = "";
    private String RECEPTOR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);

        String prueba = "wabbawabba";
        LZW.Compresion(prueba);
        List<Integer> p= new ArrayList<Integer>();
        p=LZW.Compresion(prueba);
        String h= p.toString();
        String f=LZW.Descompresion(p);
        int x=0;

        //--------------------------mapeo--------------------------------
        btnNewmsg =(Button)findViewById(R.id.btnNewmsg);
        btnSend = (Button)findViewById(R.id.btnSend);
        txtMessage = (EditText) findViewById(R.id.txtMessage);
        txtContent = findViewById(R.id.txtContent);


        queue = Volley.newRequestQueue(this);



        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mensaje = txtMessage.getText().toString().trim();
                if(!mensaje.isEmpty() && !RECEPTOR.isEmpty()){
                    MENSAJE_ENVIAR = mensaje;
                    SendChat(url);
                   txtMessage.setText("");
                }

            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    //------------------------------------metodo get------------------------------------------------
    private void NewChats(String url){

        //String url = "http://localhost:3000/api/chat";

        JsonObjectRequest request = new JsonObjectRequest( url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray mJsonArray = response.getJSONArray("chats");
                    for(int i =0; i<mJsonArray.length(); i++){
                        JSONObject mJsonObject = mJsonArray.getJSONObject(i);
                        String msg = mJsonObject.getString("msg");
                        String name = mJsonObject.getString("nick");


                      txtContent.setText(msg+": "+name);

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
                }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                String credentials = "ezhu:Ccare@123";
                String auth = "Bearer " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                params.put("authorization", auth);

                return params;
            }
        };

        queue.add(request);
    }

    private void SendChat(String url){
        HashMap<String,String> hashMapToken = new HashMap<>();
        hashMapToken.put("nick",EMISOR);
        hashMapToken.put("recept",RECEPTOR);
        hashMapToken.put("msg",MENSAJE_ENVIAR);

        final JsonObjectRequest solicitud = new JsonObjectRequest(Request.Method.POST,url,new JSONObject(hashMapToken), new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject datos) {
                try {
                    Toast.makeText(chat.this,datos.getString("resultado"),Toast.LENGTH_SHORT).show();
                   // TokenHandler.setToken(solicitud.getHeaders(""));
                } catch (JSONException e){}
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(chat.this,"Ocurrio un error",Toast.LENGTH_SHORT).show();
            }

        }){
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                String credentials = "ezhu:Ccare@123";
                String auth = "Bearer " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                params.put("authorization", auth);

                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(solicitud);
    }


}
