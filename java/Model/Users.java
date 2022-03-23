package mejia.oscar.tiendisuperv4.Model;



public class Users {

    private  String nombre, telefono, password, image, address, id;

    public Users(){


    }

    public Users(String nombre, String telefono, String password, String image, String address) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.password = password;
        this.image = image;
        this.address = address;
    this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
