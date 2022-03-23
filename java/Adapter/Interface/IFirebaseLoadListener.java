package mejia.oscar.tiendisuperv4.Adapter.Interface;

import java.util.List;

import mejia.oscar.tiendisuperv4.Model.ItemGroup;

public interface IFirebaseLoadListener {
    void onFirebaseLoadSuccess(List<ItemGroup> itemGroupList);
    void onFirebaseLoadFailed(String message);

}
