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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

public class LoginPhonenumber extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_phonenumber);

        TextInputEditText edtPhonenumber = findViewById(R.id.edtPhonenumber);
        TextInputLayout errPhone = findViewById(R.id.errPhone);
        Button btnGetOTP = findViewById(R.id.btnGetOTP);
        TextInputEditText edtOTP = findViewById(R.id.edtOTP);
        TextInputLayout errOTP = findViewById(R.id.errOTP);
        Button btnLogin = findViewById(R.id.btnLogin);

        mAuth = FirebaseAuth.getInstance();

        //hàm này tự ộng được gọi khi chúng ta gửi yêu cầu lấy OTP
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                //khi số điện thoại đăng nhập của bạn nằm trong máy bạn thực hiện lấy OTP thì chúng lập tức lấy mã OTP và setText lên ô nhập
                edtOTP.setText(phoneAuthCredential.getSmsCode());
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                //khi fail sẽ chạy vào hàm này
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//                super.onCodeSent(s, forceResendingToken);
                mVerificationId = s;
            }
        };

        btnGetOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtPhonenumber.getText().toString().isEmpty()) {
                    errPhone.setError("Phone number is required");
                    return;
                }else{
                    errPhone.setError(null);
                }
                getOTP(edtPhonenumber.getText().toString());
//                Toast.makeText(LoginPhonenumber.this, "hhh", Toast.LENGTH_SHORT).show();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtOTP.getText().toString().isEmpty()){
                    errOTP.setError("OTP is required");
                    return;
                }else{
                    errOTP.setError(null);
                }

                verifyOTP(edtOTP.getText().toString());
            }
        });
    }

    private void getOTP(String phoneNumber) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber("+84" + phoneNumber) // Phone number to verify
                .setTimeout(60L, java.util.concurrent.TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this) // Activity (for callback binding)
                .setCallbacks(mCallbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        Log.e(TAG, "getOTP: " + phoneNumber);
    }

    private void verifyOTP(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithPhoneAuthCredential(credential);
        Log.e(TAG, "verifyOTP: " + code);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success");

                    FirebaseUser user = task.getResult().getUser();
                    startActivity(new Intent(LoginPhonenumber.this, MainActivity.class));
                    // Update UI
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.getException());
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                }
            }
        });
    }
}