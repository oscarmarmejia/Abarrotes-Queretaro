package mejia.oscar.tiendisuperv4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

import mejia.oscar.tiendisuperv4.Model.Users;
import mejia.oscar.tiendisuperv4.Prevalent.Prevalent;

public class AdminLoginActivity extends AppCompatActivity {
    private EditText telefonolog_log, passwordlog_log;
    private DatabaseReference mDatabaseReference;
    private Button entrar2;
    private TextView admin2;
    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_admin_login );
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        entrar2 = findViewById( R.id.btn_entrada_tienda );
        telefonolog_log = findViewById( R.id.telefono_log_id );
        passwordlog_log = findViewById( R.id.password_entrada_log_id );
        admin2 = findViewById( R.id.admin_panel );
        loadingBar = new ProgressDialog( this );
        entrar2.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Users usuariorandow = new Users();
                usuariorandow.setTelefono( UUID.randomUUID().toString() );

                mDatabaseReference.child( "Users" ).child( usuariorandow.getTelefono() ).setValue( usuariorandow );

                usuariorandow.getTelefono();

                Prevalent.currentOnlineUser = usuariorandow;
                Intent intent = new Intent( AdminLoginActivity.this, HomeActivity.class );
                startActivity( intent );




            }
        } );


        admin2.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        } );




    }

    private void login() {
        String telefono = telefonolog_log.getText().toString();
        String password = passwordlog_log.getText().toString();

        if (TextUtils.isEmpty( telefono )) {
            Toast.makeText( this, "Ingresa tu teléfono", Toast.LENGTH_SHORT ).show();
        } else if (TextUtils.isEmpty( password )) {
            Toast.makeText( this, "Ingresa una contraseña", Toast.LENGTH_SHORT ).show();
        } else {
            loadingBar.setTitle( "entrando.." );
            loadingBar.setMessage( "Por favor, espere..." );
            loadingBar.setCanceledOnTouchOutside( false );
            loadingBar.show();

            AllowAccessToAccount( telefono, password );
        }

    }

    private void AllowAccessToAccount(final String telefono, final String password) {


        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();


        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.child("admin").child(telefono).exists())
                {
                    Users usersData = dataSnapshot.child("admin").child(telefono).getValue(Users.class);

                    if (usersData.getTelefono().equals(telefono))
                    {
                        if (usersData.getPassword().equals(password))
                        {

                            Toast.makeText(AdminLoginActivity.this, "has accesado como administrador...", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            Intent intent = new Intent(AdminLoginActivity.this, AdminCategoryActivity.class);
                            startActivity(intent);






                        }
                        else
                        {
                            loadingBar.dismiss();
                            Toast.makeText(AdminLoginActivity.this, "Password incorrecto.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    Toast.makeText(AdminLoginActivity.this, "Cuenta con el número " + telefono + " no existe.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
