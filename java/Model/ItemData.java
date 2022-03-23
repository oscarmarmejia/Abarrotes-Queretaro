package mejia.oscar.tiendisuperv4.Model;

public class ItemData {

    private String name, image, pid, category, posicion, price, description;

    public ItemData(){

    }

    public ItemData(String name, String image, String pid, String category, String posicion, String price) {

        this.name = name;
        this.image = image;
        this.pid = pid;
        this.category = category;
        this.posicion = posicion;
        this.price = price;
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPosicion() {
        return posicion;
    }

    public void setPosicion(String posicion) {
        this.posicion = posicion;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
