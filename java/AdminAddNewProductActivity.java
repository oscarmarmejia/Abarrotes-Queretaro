package mejia.oscar.tiendisuperv4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddNewProductActivity extends AppCompatActivity {
    private String CategoryName, Description, Price, Pname, saveCurrentDate, saveCurrentTime;
    private Button AddNewProductButton;
    private ImageView InputProductImage;
    private EditText InputProductName, InputProductDescription, InputProductPrice;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String productRandomKey, downloadImageUrl;
    private StorageReference ProductImagesRef;
    private DatabaseReference ProductsRef,  ProductsCat ;
    private ProgressDialog loadingBar;
    private DatabaseReference mDatabaseReference;
    public long numHijos = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_admin_add_new_product );

        CategoryName = getIntent().getExtras().get("category").toString();

        String categoria = String.valueOf( CategoryName );
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

        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("Product Images");
      //  ProductsRef = FirebaseDatabase.getInstance().getReference().child("MYData").child( categoria );
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("MyData").child( clasificacion).child( "listItem" );
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        AddNewProductButton =  findViewById(R.id.add_new_product);
        InputProductImage =  findViewById(R.id.select_product_image);
        InputProductName =  findViewById(R.id.product_name);
        InputProductDescription =  findViewById(R.id.product_description);
        InputProductPrice =  findViewById(R.id.product_price);
        loadingBar = new ProgressDialog(this);
        Toast.makeText( AdminAddNewProductActivity.this, "Añadir   "+ categoria, Toast.LENGTH_LONG ).show();
        mDatabaseReference.child( "MyData" ).child( clasificacion ).child( "listItem" ).addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 numHijos = dataSnapshot.getChildrenCount();
             //   Toast.makeText( AdminAddNewProductActivity.this, "el número de productos es :  "+numHijos, Toast.LENGTH_LONG ).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );


        InputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                OpenGallery();

            }
        });


        AddNewProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ValidateProductData();
            }
        });
    }



    private void OpenGallery()
    {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GalleryPick  &&  resultCode==RESULT_OK  &&  data!=null)
        {
            ImageUri = data.getData();
            InputProductImage.setImageURI(ImageUri);
        }
    }


    private void ValidateProductData()
    {
        Description = InputProductDescription.getText().toString();
        Price = InputProductPrice.getText().toString();
        Pname = InputProductName.getText().toString();


        if (ImageUri == null)
        {
            Toast.makeText(this, "Se necesita una imagen del producto...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Description))
        {
            Toast.makeText(this, "Se necesita una descripción del producto..", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Price))
        {
            Toast.makeText(this, "Se requiere un precio para el producto..", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Pname))
        {
            Toast.makeText(this, "Se requiere un nombre para el producto..", Toast.LENGTH_SHORT).show();
        }
        else
        {
            StoreProductInformation();
        }
    }



    private void StoreProductInformation()
    {
        loadingBar.setTitle("añadir nuevo producto");
        loadingBar.setMessage("Administrador, espera a  que se carge el producto, no seas desesperado :P.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;


        final StorageReference filePath = ProductImagesRef.child(ImageUri.getLastPathSegment() + productRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri);


        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                String message = e.toString();
                Toast.makeText(AdminAddNewProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Toast.makeText(AdminAddNewProductActivity.this, "La imagen se cargó exitosamente...", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask( new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();
                        }

                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task)
                    {
                        if (task.isSuccessful())
                        {
                            downloadImageUrl = task.getResult().toString();

                            Toast.makeText(AdminAddNewProductActivity.this, "Se a guardado la URL exitosamente del producto...", Toast.LENGTH_SHORT).show();

                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });
    }



    private void SaveProductInfoToDatabase()
    {
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", productRandomKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("description", Description);
        productMap.put("image", downloadImageUrl);
        productMap.put("category", CategoryName);
        productMap.put("price", Price);
        productMap.put("name", Pname);

        long suma = numHijos;
        String sumaa = String.valueOf( suma );
        productMap.put( "posicion", sumaa);


        ProductsRef.child(sumaa).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            Intent intent = new Intent(AdminAddNewProductActivity.this, AdminCategoryActivity.class);
                            startActivity(intent);

                            loadingBar.dismiss();
                            Toast.makeText(AdminAddNewProductActivity.this, "El producto se añadió..", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(AdminAddNewProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}