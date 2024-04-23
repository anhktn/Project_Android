package lab.poly.lab1_md18306;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Lab1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab1);

        Button btnLoginEmail = findViewById(R.id.btnLoginEmail);
        Button btnLoginPhonenumber = findViewById(R.id.btnLoginPhonenumber);

        btnLoginEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Lab1.this, Login.class));
            }
        });

        btnLoginPhonenumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Lab1.this, LoginPhonenumber.class));
            }
        });
    }
}