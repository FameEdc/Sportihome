package fr.wildcodeschool.apprenti.sportihome.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import fr.wildcodeschool.apprenti.sportihome.R;

/**
 * Created by chantome on 16/01/2017.
 */

public class CheckMailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkmail);

        ImageView imgSendMail = (ImageView)findViewById(R.id.send_mail);
        Button btnValidate = (Button)findViewById(R.id.validate);

        Picasso.with(CheckMailActivity.this).load(R.drawable.new_message).fit().centerCrop().into(imgSendMail);

        btnValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckMailActivity.this, LogInActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}