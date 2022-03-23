package mejia.oscar.tiendisuperv4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class MainActivity extends AppCompatActivity {
    private TextView entrada, anunciodos;

    private DatabaseReference mDatabaseReference, myRef;
    private Button entrar, admin;

    private String  TAG = "hola", idd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        myRef = FirebaseDatabase.getInstance().getReference();
        entrada= findViewById( R.id.addid );
        anunciodos = findViewById( R.id.addid2 );
        entrar = findViewById( R.id.entrar_button_main );
        admin = findViewById( R.id.administrador );
        mDatabaseReference.child( "Persona" ).addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    String nombre = dataSnapshot.child( "anuncioarriba" ).getValue().toString();
                    entrada.setText( nombre );
                    String anuncio2 = dataSnapshot.child( "anuncioabajo" ).getValue().toString();
                    anunciodos.setText(anuncio2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );





        entrar.setOnLongClickListener( new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                admin.setVisibility( View.VISIBLE );

                return true;
            }
        } );

        entrar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef.child( "Puerta" ).addValueEventListener( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            int valor = Integer.parseInt( dataSnapshot.getValue().toString() );
                            if(valor ==1) {
                                Users usuariorandow = new Users();
                                usuariorandow.setId( UUID.randomUUID().toString() );
                                mDatabaseReference.child( "Users" ).child( usuariorandow.getId() ).setValue( usuariorandow );

                                usuariorandow.getId();



                                Prevalent.currentOnlineUser = usuariorandow;
                                Intent intent = new Intent( MainActivity.this, HomeActivity.class );
                                startActivity( intent );
                            }
                            else{
                                Toast.makeText( MainActivity.this, "la tienda está cerrada", Toast.LENGTH_LONG ).show();

                            }


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                } );

            }
        });

        admin.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( MainActivity.this, AdminLoginActivity.class );
                startActivity( intent );}
        } );

    }

}
