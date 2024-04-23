package lab.poly.lab1_md18306;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class FogotPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fogot_password);

        TextInputEditText edtEmail = findViewById(R.id.edtEmailAddress);
        TextInputLayout errEmail = findViewById(R.id.errEmail);
        Button btnSend = findViewById(R.id.btnSend);

        FirebaseAuth auth = FirebaseAuth.getInstance();


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailAddress = edtEmail.getText().toString();

                if(emailAddress.isEmpty()){
                    errEmail.setError("Email is required");
                    return;
                }else{
                    errEmail.setError(null);
                }
                auth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "Email sent.");
                                    Toast.makeText(FogotPassword.this, "Mở email để cập nhật lại mật khẩu", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(FogotPassword.this, Login.class));
                                } else {
                                    Log.d(TAG, "Email not sent.");
                                }

                            }
                        });
            }
        });

    }
}