package fr.wildcodeschool.apprenti.sportihome.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import javax.net.ssl.HttpsURLConnection;
import fr.wildcodeschool.apprenti.sportihome.Model.IdentityModel;
import fr.wildcodeschool.apprenti.sportihome.R;

/**
 * Created by chantome on 12/01/2017.
 */

public class SignUpActivity extends AppCompatActivity{

    private EditText editEmail, editFirstname, editName, editPassword, editConfirm;
    private String email, firstname, name, password, confirm;
    private Button signup, cgu;
    private HttpURLConnection client;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editEmail = (EditText)findViewById(R.id.email);
        editFirstname = (EditText)findViewById(R.id.firstname);
        editName = (EditText)findViewById(R.id.name);
        editPassword = (EditText)findViewById(R.id.pass);
        editConfirm = (EditText)findViewById(R.id.confirm_pass);

        signup = (Button)findViewById(R.id.connection);
        cgu = (Button)findViewById(R.id.btn_cgu);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editEmail.getText().toString().isEmpty()
                        && !editFirstname.getText().toString().isEmpty()
                        && !editName.getText().toString().isEmpty()
                        && !editPassword.getText().toString().isEmpty()
                        && !editConfirm.getText().toString().isEmpty()
                        ){
                    if (editPassword.getText().toString().equals(editConfirm.getText().toString())){
                        new SignUpActivity.SendPostRequest().execute();
                    }else{
                        editConfirm.setError("erreur de confirmation de mot de passe");
                    }
                }
                if(editEmail.getText().toString().isEmpty()){
                    editEmail.setError("email vide !");
                }
                if(editFirstname.getText().toString().isEmpty()){
                    editFirstname.setError("prénom vide !");
                }
                if(editName.getText().toString().isEmpty()){
                    editName.setError("nom vide !");
                }
                if(editPassword.getText().toString().isEmpty()){
                    editPassword.setError("mot de passe vide !");
                }
                if(editConfirm.getText().toString().isEmpty()){
                    editConfirm.setError("confirmation mot de passe vide !");
                }
            }
        });

        cgu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this,CguActivity.class);
                startActivity(intent);
            }
        });
    }

    public class SendPostRequest extends AsyncTask<String, Void, String[]> {

        protected void onPreExecute(){
            email = editEmail.getText().toString();
            firstname = editFirstname.getText().toString();
            name = editName.getText().toString();
            password = editPassword.getText().toString();
            confirm = editConfirm.getText().toString();
        }

        protected String[] doInBackground(String... arg0) {

            try {

                URL url = new URL("https://sportihome.com/api/users");

                IdentityModel mIdentity = new IdentityModel();
                mIdentity.setFirstName(firstname);
                mIdentity.setLastName(name);

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("identity",mIdentity);
                postDataParams.put("password", password);
                postDataParams.put("email", email);
                postDataParams.put("passwordConfirm", confirm);
                Log.e("params",postDataParams.toString());

                client = (HttpURLConnection) url.openConnection();
                client.setRequestMethod("POST");
                client.setDoOutput(true);
                client.setDoInput(true);

                OutputStream os = client.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode=client.getResponseCode();
                String res[] = new String[2];
                InputStream input;

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    //RESPONSE OK
                    input = client.getInputStream();

                }
                else {
                    //RESPONSE ERROR
                    input = client.getErrorStream();

                }

                BufferedReader in=new BufferedReader(new InputStreamReader(input));

                StringBuffer sb = new StringBuffer("");
                String line="";

                while((line = in.readLine()) != null) {

                    sb.append(line);
                    break;
                }

                in.close();

                res[0] = String.valueOf(responseCode);
                res[1] = sb.toString();

                return res;
            }
            catch(Exception e){
                Log.e("ERR : ",new String("Exception: " + e.getMessage()));
                return null;
            }

        }

        @Override
        protected void onPostExecute(String[] result) {
            //Log.i("RES : ", result);
            if(result != null){
                switch (result[0]){
                    case "200":
                        //Envoyer vers une page qui dit de consulter les mails
                        //Toast.makeText(getApplicationContext(), result[1], Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignUpActivity.this,CheckMailActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case "401":
                        Toast.makeText(getApplicationContext(), result[1], Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }

}
