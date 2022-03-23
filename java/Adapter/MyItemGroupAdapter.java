package mejia.oscar.tiendisuperv4.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import java.util.List;

import mejia.oscar.tiendisuperv4.Model.ItemData;
import mejia.oscar.tiendisuperv4.Model.ItemGroup;
import mejia.oscar.tiendisuperv4.R;

public class MyItemGroupAdapter extends RecyclerView.Adapter<MyItemGroupAdapter.MyViewHolder> {
    private Context context;
    private List<ItemGroup>dataList;

    public MyItemGroupAdapter(Context context, List<ItemGroup> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       View itemView = LayoutInflater.from( context ).inflate( R.layout.layout_group, viewGroup, false );
    return new MyViewHolder( itemView );

    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
myViewHolder.item_title.setText( dataList.get( i ).getHeaderTitle() );
List<ItemData> itemData = dataList.get(i).getListItem();
mejia.oscar.tiendisuperv4.Adapter.MyItemAdapter itemListAdapter = new mejia.oscar.tiendisuperv4.Adapter.MyItemAdapter(context, itemData );
myViewHolder.recyclerView_view_item_list.setHasFixedSize( true );
myViewHolder.recyclerView_view_item_list.setLayoutManager( new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false) );
myViewHolder.recyclerView_view_item_list.setAdapter( itemListAdapter );

myViewHolder.recyclerView_view_item_list.setNestedScrollingEnabled(false );

//boton


        myViewHolder.btn_more.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText( context, "Botonmore : " + myViewHolder.item_title.getText(), Toast.LENGTH_SHORT ).show();
            }
        } );


    }

    @Override
    public int getItemCount() {
        return (dataList != null ? dataList.size() : 0);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{


        TextView item_title;
        RecyclerView recyclerView_view_item_list;
        Button btn_more;
        public MyViewHolder(@NonNull View itemView) {
            super( itemView );
        item_title = itemView.findViewById( R.id.item_title );
        btn_more = itemView.findViewById( R.id.btn_more );
        recyclerView_view_item_list = itemView.findViewById( R.id.recycle_view_list );


        }
    }

}
