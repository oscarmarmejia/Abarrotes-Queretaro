package mejia.oscar.tiendisuperv4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;
import mejia.oscar.tiendisuperv4.Adapter.Interface.IFirebaseLoadListener;
import mejia.oscar.tiendisuperv4.Adapter.MyItemGroupAdapter;
import mejia.oscar.tiendisuperv4.Model.ItemData;
import mejia.oscar.tiendisuperv4.Model.ItemGroup;

public class HomeActivity extends AppCompatActivity  implements IFirebaseLoadListener {



    IFirebaseLoadListener  iFirebaseLoadListener;

    RecyclerView my_recycler_view;
 private BottomNavigationView bottomNavigationView;
    DatabaseReference myData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_home );
        Paper.init( this );


        bottomNavigationView = findViewById( R.id.barra_abajo );
    bottomNavigationView.setOnNavigationItemSelectedListener( new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.unos:
                    Toast.makeText( HomeActivity.this, "Esta aplicación fue programada por Óscar Mejía. Deje sus quejas o comentarios al correo oscarmarmejia@gmail.com o comuníquese al teléfono (+52)4427205697",Toast.LENGTH_SHORT ).show();
                    break;
                case R.id.tress:
                    Intent intent3 = new Intent(HomeActivity.this, CartActivity.class);
                    startActivity( intent3 );
                    break;
                default:
                    break;


            }   return true;

        }


    } );
     // bottomNavigationView.setOnNavigationItemSelectedListener( (BottomNavigationView.OnNavigationItemSelectedListener) navListener );


        myData = FirebaseDatabase.getInstance().getReference("MyData");

        iFirebaseLoadListener = this;

//ver

        my_recycler_view = findViewById( R.id.recycle_menu );
        my_recycler_view.setHasFixedSize( true );
        my_recycler_view.setLayoutManager( new LinearLayoutManager( this ) );
//leer datos

        getFirebaseData();

    }

    private void getFirebaseData() {

        myData.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<ItemGroup> itemGroups = new ArrayList<>();
                for(DataSnapshot groupSnapshot:dataSnapshot.getChildren()){


                    ItemGroup itemGroup = new ItemGroup(  );
                    itemGroup.setHeaderTitle( groupSnapshot.child( "headerTitle" ).getValue(true).toString()  );
                    GenericTypeIndicator<ArrayList<ItemData>> genericTypeIndicator = new GenericTypeIndicator<ArrayList<ItemData>>(){};
                    itemGroup.setListItem( groupSnapshot.child( "listItem" ).getValue(genericTypeIndicator) );
                    itemGroups.add( itemGroup );

                }

                iFirebaseLoadListener.onFirebaseLoadSuccess( itemGroups );


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                iFirebaseLoadListener.onFirebaseLoadFailed( databaseError.getMessage() );
            }
        } );

    }






    @Override
    public void onFirebaseLoadSuccess(List<ItemGroup> itemGroupList) {
        MyItemGroupAdapter adapter = new MyItemGroupAdapter( this, itemGroupList );
        my_recycler_view.setAdapter( adapter );

    }

    @Override
    public void onFirebaseLoadFailed(String message) {
        Toast.makeText( this, "holamundo",Toast.LENGTH_SHORT ).show();

    }

     private BottomNavigationView.OnNavigationItemReselectedListener navListenera =  new BottomNavigationView.OnNavigationItemReselectedListener() {
         @Override
         public void onNavigationItemReselected(@NonNull MenuItem item) {
       switch (item.getItemId()){
           case R.id.unos:
               Intent intent = new Intent(HomeActivity.this, UserActivity.class);
               startActivity( intent );
               break;
           case R.id.tress:
               Intent intent3 = new Intent(HomeActivity.this, CartActivity.class);
               startActivity( intent3 );
               break;
           default:
               break;


       }
         }
     };
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Paper.book().destroy();

        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();


    }


}


/*
        Paper.init( this );
        ProductsRef = FirebaseDatabase.getInstance().getReference().child( "Products" );

        FloatingActionButton fab = findViewById( R.id.fab );
        fab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(intent);
            }
        } );
        listView = findViewById( R.id.listvieww );
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        recyclerView = findViewById( R.id.recycle_menu );
        recyclerView2 = findViewById( R.id.recycle_menu2 );
        recyclerView3 = findViewById( R.id.recycle_menu3 );
        recyclerView4 = findViewById( R.id.recycle_menu4 );
        recyclerView.setHasFixedSize( true );
        layoutManager = new LinearLayoutManager( this, LinearLayoutManager.HORIZONTAL, true);
        recyclerView.setLayoutManager( layoutManager);




    }


    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery( ProductsRef, Products.class )
                        .build();


        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>( options ) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model) {
                        holder.txtProductName.setText( model.getPname() );
                        holder.txtProductDescription.setText( model.getDescription() );
                        holder.txtProductPrice.setText( "Precio = " + model.getPrice() + "$" );
                        Picasso.get().load( model.getImage() ).into( holder.imageView );


                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {


                                Intent intent = new Intent(HomeActivity.this, ProductDetailsActivity.class);
                                intent.putExtra("pid", model.getPid());
                                startActivity(intent);

                            }
                        });

                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.product_items_layout, parent, false );
                        ProductViewHolder holder = new ProductViewHolder( view );
                        return holder;
                    }
                };
        recyclerView.setAdapter( adapter );

        adapter.startListening();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Paper.book().destroy();

        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();


    }

}

 */