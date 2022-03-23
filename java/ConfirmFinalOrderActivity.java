package mejia.oscar.tiendisuperv4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import mejia.oscar.tiendisuperv4.Prevalent.Prevalent;

public class ConfirmFinalOrderActivity extends AppCompatActivity {
    private EditText nameEditText, phoneEditText, addressEditText, cityEditText;
    private TextView total_final;
    private Button confirmOrderBtn;
    int maxm = 8;
  private static Uri alarmSound;
    private PendingIntent pendingIntent;  //-
    private final static String CHANNEL_ID= "NOTIFICACION";
    private final static int NOTIFICATION_ID=0;
    private String totalAmount = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_confirm_final_order );

        totalAmount = getIntent().getStringExtra("Total Price");
        Toast.makeText(this, "Precio Total =  $ " + totalAmount, Toast.LENGTH_LONG).show();


        total_final = findViewById( R.id.total_final );
        confirmOrderBtn = findViewById( R.id.confirm_final_order_btn );
        nameEditText = findViewById( R.id.shippment_name );
        phoneEditText = findViewById( R.id.shippment_phone_number );
        addressEditText = findViewById( R.id.shippment_address );
        cityEditText = findViewById( R.id.shippment_city );


        confirmOrderBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check();
            }
        } );

        total_final.setText("Total a pagar  : $"+totalAmount+ "  pesos.");


    }

    private void check() {
        if (TextUtils.isEmpty(nameEditText.getText().toString()))
        {
            Toast.makeText(this, "Por favor,introduce un nombre.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phoneEditText.getText().toString()))
        {
            Toast.makeText(this, "Por favor, introduce tu número telefónico.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(addressEditText.getText().toString()))
        {
            Toast.makeText(this, "Por favor, introduce tu dirección.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(cityEditText.getText().toString()))
        {
            Toast.makeText(this, "Introduce tu colonia o Fracc..", Toast.LENGTH_SHORT).show();
        }
        else
        {
            ConfirmOrder();
        }
    }





    private void ConfirmOrder()
    {
        final String saveCurrentDate, saveCurrentTime;

        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child( Prevalent.currentOnlineUser.getId());

        final HashMap<String, Object> ordersMap = new HashMap<>();
        ordersMap.put("totalAmount", totalAmount);
        ordersMap.put("name", nameEditText.getText().toString());
        ordersMap.put("phone", phoneEditText.getText().toString());
        ordersMap.put("address", addressEditText.getText().toString());
        ordersMap.put("city", cityEditText.getText().toString());
        ordersMap.put("date", saveCurrentDate);
        ordersMap.put("time", saveCurrentTime);
        ordersMap.put("state", "not shipped");
        ordersMap.put("id", Prevalent.currentOnlineUser.getId()) ;

        ordersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isSuccessful())
                {
                    FirebaseDatabase.getInstance().getReference()
                            .child("Cart List")
                            .child("User View")
                            .child( Prevalent.currentOnlineUser.getId())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful())
                                    {


                                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)

                                        {
                                            CharSequence name = "Notificación V2";
                                            NotificationChannel notificationChannel = new NotificationChannel( CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT );
                                            NotificationManager notificationManager = (NotificationManager) getSystemService( NOTIFICATION_SERVICE );
                                            notificationManager.createNotificationChannel( notificationChannel );



                                        }


                                        String maximo = Prevalent.currentOnlineUser.getId();
                                        String cortado = maximo.substring( 0,6 );




                                           alarmSound = RingtoneManager.getDefaultUri( RingtoneManager.TYPE_NOTIFICATION );


                                        NotificationCompat.Builder builder = new NotificationCompat.Builder( getApplicationContext(), CHANNEL_ID );
                                        builder.setSmallIcon( R.drawable.iconotransparente2 );
                                        builder.setContentTitle( "Tu compra en Abarrotes Querétaro" );
                                        builder.setContentText( "Monto a pagar:$"+totalAmount+" .Tu ID de pedido :" + cortado);
                                        builder.setColor( Color.GREEN );
                                       builder.setPriority( NotificationCompat.DEFAULT_SOUND);
                                        builder.setLights( Color.GREEN, 1000, 1000 );
                                        builder.setSound( alarmSound);
                                   //    builder.setVibrate( new long[]{500, 500, 500, 500, 500} );

                                        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from( getApplicationContext() );
                                        notificationManagerCompat.notify( NOTIFICATION_ID, builder.build() );






                                        Toast.makeText(ConfirmFinalOrderActivity.this, "Su pedido  se ha realizado correctamente.", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(ConfirmFinalOrderActivity.this, HomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }
            }
        });
    }


}
