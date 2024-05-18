import java.math.BigDecimal;

public class Food {

    private String title;
    private BigDecimal price;
    private int preparationTime;
    private String image;
    private boolean available;
    private String foodCode;

    public String getFileLine(){
        return foodCode + Setting.SEPARATOR() +
                title + Setting.SEPARATOR() +
                price + Setting.SEPARATOR() +
                preparationTime + Setting.SEPARATOR() +
                image + Setting.SEPARATOR() +
                available;// + Setting.NEWLINE();
    }

    public Food(String title, BigDecimal price, int preparationTime, String image, boolean available, String foodCode) throws RestaurantException {
        this.title = title;
        this.price = price;
        setPreparationTime(preparationTime);
        setImage(image);
        this.available=available;
        this.foodCode=foodCode;
    }

    public Food(String title, BigDecimal price, int preparationTime,String foodCode) throws RestaurantException {
        this(title,price,preparationTime,"",true,foodCode);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(int preparationTime) throws RestaurantException {
        if (preparationTime>0) {
            this.preparationTime = preparationTime;
        } else {
            throw new RestaurantException("Doba přípravy jídla musí být větší nula.");
        }
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        if (image.length()==0){
            this.image=Setting.DEFAULTIMAGE();
        } else {
            this.image = image;
        }
    }

    public boolean getAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getFoodCode() {
        return foodCode;
    }

    public void setFoodCode(String foodCode) {
        this.foodCode = foodCode;
    }
}
