package mejia.oscar.tiendisuperv4.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import mejia.oscar.tiendisuperv4.Adapter.Interface.IItemClickListener;
import mejia.oscar.tiendisuperv4.Model.ItemData;
import mejia.oscar.tiendisuperv4.ProductDetailsActivity;
import mejia.oscar.tiendisuperv4.R;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyItemAdapter extends RecyclerView.Adapter<MyItemAdapter.MyViewHolder> {
    private Context context;
    private List<ItemData> itemDataList;
    private DatabaseReference mDatabaseReference;
    public MyItemAdapter(Context context, List<ItemData> itemDataList) {
        this.context = context;
        this.itemDataList = itemDataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from( context ).inflate( R.layout.layout_item, viewGroup, false );
        return new MyViewHolder( itemView );
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        myViewHolder.txt_item_title.setText( itemDataList.get( i ).getName() );
        myViewHolder.precio.setText( "$"+itemDataList.get(i).getPrice()+".00" );
        Picasso.get().load( itemDataList.get( i ).getImage()).into( myViewHolder.img_item );


        ////

        myViewHolder.setiItemClickListener( new IItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
             
                Intent intent = new Intent(context, ProductDetailsActivity.class);
                intent.putExtra("pid", itemDataList.get(i).getPid());
                intent.putExtra("category", itemDataList.get(i).getCategory());
                intent.putExtra( "posicion", itemDataList.get(i).getPosicion() );

                context.startActivity( intent );
            }
        } );

    }


    @Override
    public int getItemCount() {
        return (itemDataList != null ? itemDataList.size()  : 0) ;
    }

    public class MyViewHolder  extends  RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txt_item_title;
        TextView precio;
        ImageView img_item;
        IItemClickListener iItemClickListener;



        public void setiItemClickListener(IItemClickListener iItemClickListener){
this.iItemClickListener = iItemClickListener;

        }

        public MyViewHolder(@NonNull View itemView) {
            super( itemView );
                  precio = itemView.findViewById( R.id.price );
            txt_item_title = itemView.findViewById( R.id.tv_tittle);
            img_item = itemView.findViewById( R.id.item_image );
            itemView.setOnClickListener( this );

        }

        @Override
        public void onClick(View view) {
            iItemClickListener.onItemClickListener( view, getAdapterPosition() );
        }
    }
}
