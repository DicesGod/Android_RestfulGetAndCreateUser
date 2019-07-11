package com.example.lasalleuser;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //ArrayList<String> usersList;
    ArrayAdapter<LasalleUserForm> usersListArrayAdapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //usersList = new ArrayList<>();
        usersListArrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
        listView = findViewById(R.id.listview);
    }

    public void registerUser(View view) {
        new RegisterTaskJson().execute();
    }

    public void getAllUsers(View view) {
        new RESTTaskString().execute();

    }

    //Get All Users
    class RESTTaskString extends AsyncTask<Void, Void, ResponseEntity<LasalleUserForm[]>> {

        @Override
        protected ResponseEntity<LasalleUserForm[]> doInBackground(Void... voids) {

            final String url = "http://10.0.2.2:8080/users";

            HttpHeaders httpHeaders = new HttpHeaders();
//            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            httpHeaders.set("Accept", "application/json");

            HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            ResponseEntity<LasalleUserForm[]> responseEntity = new ResponseEntity<>(HttpStatus.SEE_OTHER);

            try {
                responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, LasalleUserForm[].class);
            } catch (RestClientException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "User is already registered!", Toast.LENGTH_LONG).show();
                    }
                });
                e.printStackTrace();
            }

            return responseEntity;
        }

        @Override
        protected void onPostExecute(ResponseEntity<LasalleUserForm[]> result) {
            if (result.getStatusCode() == HttpStatus.OK) {
                usersListArrayAdapter.addAll(result.getBody());
                listView.setAdapter(usersListArrayAdapter);
                Toast.makeText(getApplicationContext(), "All users retrieved!", Toast.LENGTH_LONG).show();
            } else {
                Log.d("REGISTERING STATUS CODE", "onPostExecute: " + result.getStatusCode().toString());
            }
        }
    }

    //Register with mutil form
    private class RegisterTaskForm extends AsyncTask<Void, Void, Void> {

        private MultiValueMap<String, Object> formData;

        @Override
        protected void onPreExecute() {
            formData = new LinkedMultiValueMap<>();
            String email = ((EditText)findViewById(R.id.emailET)).getText().toString();
            String password = ((EditText)findViewById(R.id.passwordET)).getText().toString();
            String name = ((EditText)findViewById(R.id.nameET)).getText().toString();
            formData.add("email", email);
            formData.add("password", password);
            formData.add("name", name);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            final String url = "http://10.0.2.2:8080/user/create";

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
            HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(formData, httpHeaders);

            RestTemplate restTemplate = new RestTemplate(true);
            restTemplate.exchange(url, HttpMethod.POST, httpEntity, null);

            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {
            Toast.makeText(getApplicationContext(), "User registered!", Toast.LENGTH_LONG).show();
        }
    }

    //Register with Json
    private class RegisterTaskJson extends AsyncTask<Void, Void, ResponseEntity<LasalleUserForm>> {

        private JSONObject jsonData;

        @Override
        protected void onPreExecute() {
            jsonData = new JSONObject();
            String email = ((EditText)findViewById(R.id.emailET)).getText().toString();
            String password = ((EditText)findViewById(R.id.passwordET)).getText().toString();
            String name = ((EditText)findViewById(R.id.nameET)).getText().toString();
            try {
                jsonData.put("email", email);
                jsonData.put("password", password);
                jsonData.put("name", name);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected ResponseEntity<LasalleUserForm> doInBackground(Void... voids) {
            final String url = "http://10.0.2.2:8080/user/create";

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> httpEntity = new HttpEntity<>(jsonData.toString(), httpHeaders);

            RestTemplate restTemplate = new RestTemplate(true);

            ResponseEntity<LasalleUserForm> responseEntity = new ResponseEntity<>(HttpStatus.SEE_OTHER);

            try {
                responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, LasalleUserForm.class);
            } catch (RestClientException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "User is already registered!", Toast.LENGTH_LONG).show();
                    }
                });
                e.printStackTrace();
            }

            return responseEntity;
        }

        @Override
        protected void onPostExecute(ResponseEntity<LasalleUserForm> result) {
            if (result.getStatusCode() == HttpStatus.OK) {
                Toast.makeText(getApplicationContext(), "User registered!", Toast.LENGTH_LONG).show();
            } else {
                Log.d("REGISTERING STATUS CODE", "onPostExecute: " + result.getStatusCode().toString());
            }
        }
    }
}