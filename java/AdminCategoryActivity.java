package mejia.oscar.tiendisuperv4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminCategoryActivity extends AppCompatActivity {

    private Switch abrirswitch;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private EditText mensaje1, mensaje2;

    private Button abarotes, bebidas, lacteos, frutas, botanas, dulces, congelados, harina, enlatado, hogar, higiene, otros, LogoutBtn, CheckOrdersBtn, maintainProductsBtn, actualizar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_admin_category );  FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("Puerta");
        final DatabaseReference mensaje_arriba = database.getReference("Persona");
        final DatabaseReference mensaje_abajo = database.getReference( "Persona");

        abarotes = findViewById( R.id.Abarrotes );
        bebidas = findViewById( R.id.Bebidas );
        lacteos = findViewById( R.id.Lacteos );
        frutas = findViewById( R.id.Frutas );
        botanas = findViewById( R.id.Botanas );
        dulces = findViewById( R.id.Dulces );
        congelados = findViewById( R.id.Congelados );
        harina = findViewById( R.id.Harinas );
        enlatado = findViewById( R.id.Enlatados );
        hogar = findViewById( R.id.Hogar );
        higiene = findViewById( R.id.Higiene );
        otros = findViewById( R.id.Otros );

        LogoutBtn =  findViewById(R.id.admin_logout_btn);
        CheckOrdersBtn =  findViewById(R.id.check_orders_btn);
        maintainProductsBtn =  findViewById(R.id.maintain_btn);
        abrirswitch = findViewById( R.id.switch_abrir );
        mensaje1 = findViewById( R.id.mensaje1 );
        mensaje2 = findViewById( R.id.mensaje2 );
        actualizar = findViewById( R.id.actualizar_btn );


        actualizar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String valor1 = mensaje1.getText().toString();
                mensaje_arriba.child( "anuncioarriba" ).setValue( valor1 );

                String valor2 = mensaje2.getText().toString();
                mensaje_abajo.child( "anuncioabajo" ).setValue( valor2 );

                Toast.makeText( AdminCategoryActivity.this, "tu mensaje se ha actualizado", Toast.LENGTH_SHORT ).show();
            }
        } );



        abrirswitch.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(abrirswitch.isChecked()){

                    myRef.setValue(1);
                    Toast.makeText( AdminCategoryActivity.this, "acabas de activar la tienda", Toast.LENGTH_LONG ).show();

                }
                else{

                    myRef.setValue(0);
                    Toast.makeText( AdminCategoryActivity.this, "acabas de desactivar la tienda", Toast.LENGTH_LONG ).show();
                }



            }
        } );



        maintainProductsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, HomeActivity.class);
                intent.putExtra("Admin", "Admin");
                startActivity(intent);
            }
        });


        LogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        CheckOrdersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminNewOrdersActivity.class);
                startActivity(intent);
            }
        });







        abarotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "abarrotes");
                startActivity(intent);
            }
        });


        bebidas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "bebidas");
                startActivity(intent);
            }
        });

        lacteos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "lacteos");
                startActivity(intent);
            }
        });
        harina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "harinas");
                startActivity(intent);
            }
        });

        botanas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "botanas");
                startActivity(intent);
            }
        });

        dulces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "dulces");
                startActivity(intent);
            }
        });

        frutas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "frutas");
                startActivity(intent);
            }
        });

        congelados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "congelados");
                startActivity(intent);
            }
        });

        enlatado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "enlatados");
                startActivity(intent);
            }
        });

        hogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "hogar");
                startActivity(intent);
            }
        });


        higiene.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "higiene");
                startActivity(intent);
            }
        });

        otros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "otros");
                startActivity(intent);
            }
        });
    }
}