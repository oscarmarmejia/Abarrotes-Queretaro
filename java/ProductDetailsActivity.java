package mejia.oscar.tiendisuperv4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import io.paperdb.Paper;
import mejia.oscar.tiendisuperv4.Model.ItemData;
import mejia.oscar.tiendisuperv4.Model.Products;
import mejia.oscar.tiendisuperv4.Prevalent.Prevalent;

public class ProductDetailsActivity extends AppCompatActivity {
    private Button addToCartButton;
    private ImageView productImage;
    private TextView showValue;
    private DatabaseReference mDatabaseReference;
    private TextView productPrice, productDescription, productName;
    private String productID = "", state = "Normal", categoryID ="", posicionID="";
    int counter = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_product_details );
        Paper.init( this );

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        addToCartButton =  findViewById(R.id.pd_add_to_cart_button);
        showValue = findViewById( R.id.resultado );

categoryID = getIntent().getStringExtra( "category" );


        productID = getIntent().getStringExtra("pid");
posicionID = getIntent().getStringExtra( "posicion" );
        productImage =  findViewById(R.id.product_image_details);
        productName =  findViewById(R.id.product_name_details);
        productDescription =  findViewById(R.id.product_description_details);
        productPrice =  findViewById(R.id.product_price_details2);
String test = String.valueOf( productID );

        getProductDetails(productID, categoryID, posicionID);


        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (state.equals("Orden echa") || state.equals("Orden es espera"))
                {
                    Toast.makeText(ProductDetailsActivity.this, "Puedes añadir más productos una vez que tu compra anterior sea autorizada", Toast.LENGTH_LONG).show();
                }
                else
                {
                   addingToCartList();
                }
            }
        });


    }


    @Override
    protected void onStart()
    {
        super.onStart();

       CheckOrderState();
    }

    private void addingToCartList()
    {
        if(counter == 0 ){


            Intent intent = new Intent(ProductDetailsActivity.this, HomeActivity.class);
            startActivity(intent);
        }

        else {
            String saveCurrentTime, saveCurrentDate;

            Calendar calForDate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat( "MMM dd, yyyy" );
            saveCurrentDate = currentDate.format( calForDate.getTime() );

            SimpleDateFormat currentTime = new SimpleDateFormat( "HH:mm:ss a" );
            saveCurrentTime = currentTime.format( calForDate.getTime() );

            final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child( "Cart List" );


            final HashMap<String, Object> cartMap = new HashMap<>();
            cartMap.put( "pid", productID );
            cartMap.put( "name", productName.getText().toString() );
            cartMap.put( "price", productPrice.getText().toString() );
            cartMap.put( "date", saveCurrentDate );
            cartMap.put( "time", saveCurrentTime );
            cartMap.put( "quantity", showValue.getText().toString() );
            cartMap.put( "discount", "" );

            cartListRef.child( "User View" ).child( Prevalent.currentOnlineUser.getId() ).child( "Products" ).child( productID ).updateChildren( cartMap )


                    .addOnCompleteListener( new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                cartListRef.child( "Admin View" )
                                        .child( Prevalent.currentOnlineUser.getId() )
                                        .child( "Products" ).child( productID )
                                        .updateChildren( cartMap )
                                        .addOnCompleteListener( new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful()) {
                                                    Toast.makeText( ProductDetailsActivity.this, "Añadido al carrito.", Toast.LENGTH_SHORT ).show();

                                                    Intent intent = new Intent( ProductDetailsActivity.this, HomeActivity.class );
                                                    startActivity( intent );
                                                }


                                            }
                                        } );
                            }


                        }


                    } );


        }
    }



    private void getProductDetails(String productID, String categoryID, String posicionID)
    {
        String categoria = String.valueOf( categoryID);
        String clasificacion = "";

        if(categoria.equals(  "abarrotes")){
            clasificacion = "0";
        }
        else if (categoria.equals( "bebidas" )){
            clasificacion = "1";
        }
        else if (categoria.equals( "lacteos" )){
            clasificacion = "2";
        }
        else if (categoria.equals( "harinas" )){
            clasificacion = "3";
        }
        else if (categoria.equals( "botanas" )){
            clasificacion = "4";
        }
        else if (categoria.equals( "dulces" )){
            clasificacion = "5";
        }
        else if (categoria.equals( "frutas" )){
            clasificacion = "6";
        }
        else if (categoria.equals( "congelados" )){
            clasificacion = "7";
        }
        else if (categoria.equals( "enlatados" )){
            clasificacion = "8";
        }
        else if (categoria.equals( "hogar" )){
            clasificacion = "9";
        }
        else if (categoria.equals( "higiene" )){
            clasificacion = "10";
        }
        else if (categoria.equals( "otros" )){
            clasificacion = "11";
        }
        else{

            clasificacion = "error";
        }

        String posicionn = String.valueOf( posicionID);



String posicion = String.valueOf( posicionID );
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("MyData").child( clasificacion ).child( "listItem" ).child( posicion );

        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    ItemData products = dataSnapshot.getValue( ItemData.class);

                    productName.setText(products.getName());
                    productPrice.setText( products.getPrice());
                    productDescription.setText(products.getDescription());
                    Picasso.get().load(products.getImage()).into(productImage);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void CheckOrderState()
    {
        DatabaseReference ordersRef;
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child( Prevalent.currentOnlineUser.getId());



        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {/*
                if (dataSnapshot.exists())
                {
                    String shippingState = dataSnapshot.child("state").getValue().toString();

                    if (shippingState.equals("shipped"))
                    {
                        state = "Order Shipped";
                    }
                    else if(shippingState.equals("not shipped"))
                    {
                        state = "Order Placed";
                    }
                }

                */
                if (dataSnapshot.exists())
                {
                    String shippingState = dataSnapshot.child("state").getValue().toString();
                    String userName = dataSnapshot.child("name").getValue().toString();

                    if (shippingState.equals("shipped"))
                    {
                    }
                    else if(shippingState.equals("not shipped"))
                    {
                        Intent intent = new Intent(ProductDetailsActivity.this, UserUserProductsActivity.class);
                        startActivity(intent);





                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }





    public void sumar(View view) {
        if(counter < 10) {
            counter++;
            showValue.setText( Integer.toString( counter ) );
        }
        else{


        }
    }
    public void restar(View view) {
        if(counter > 0 ){
            counter --;
            showValue.setText(Integer.toString( counter ));
        }
        else {


        }

    }






}
