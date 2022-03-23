package mejia.oscar.tiendisuperv4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import mejia.oscar.tiendisuperv4.Model.Cart;
import mejia.oscar.tiendisuperv4.Prevalent.Prevalent;
import mejia.oscar.tiendisuperv4.ViewHolder.CartViewHolder;

public class UserUserProductsActivity extends AppCompatActivity {
    private RecyclerView productsList;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference cartListRef;
    private TextView id_number;

    private String userID = "", info = "";
    private DatabaseReference mDatabaseReference, myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_user_user_products );
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        userID = getIntent().getStringExtra("uid");
        info = getIntent().getStringExtra( "uid" );
        id_number = findViewById( R.id.id_pedido );
        productsList = findViewById(R.id.products_list);
        productsList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        productsList.setLayoutManager(layoutManager);


        cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List").child("Admin View").child(Prevalent.currentOnlineUser.getId()).child("Products");
        mDatabaseReference.child( "Users" ).addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String id = dataSnapshot.child( Prevalent.currentOnlineUser.getId()).getValue().toString();

                id_number.setText("Su ID de pedido es: " + Prevalent.currentOnlineUser.getId());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }

    @Override
    protected void onStart()
    {
        super.onStart();



        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(cartListRef, Cart.class)
                        .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model)
            {
                holder.txtProductQuantity.setText("Cantidad = " + model.getQuantity());
                holder.txtProductPrice.setText("Precio " + model.getPrice() + "$");
                holder.txtProductName.setText(model.getName());
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent, false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };

        productsList.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(UserUserProductsActivity.this, HomeActivity.class);
        startActivity(intent);

    }
}
