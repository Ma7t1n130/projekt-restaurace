import java.time.LocalDateTime;

public class Order {
    private int tableNumber;
    private Dish dish;
    private int quantity;
    private LocalDateTime orderedTime;
    private LocalDateTime fulfilmentTime;
    boolean isPaid;

    public Order(int tableNumber,String foodCode,int quantity) throws RestaurantException {
        this.tableNumber=tableNumber;
        this.dish=this.getDishByFoodCode(foodCode);
        this.quantity=quantity;
        this.orderedTime=LocalDateTime.now();
        this.fulfilmentTime=null;
        this.isPaid=false;
    }

    public Order(int tableNumber,String foodCode,int quantity, LocalDateTime orderedTime,LocalDateTime fulfilmentTime, boolean isPaid) throws RestaurantException {
        this.tableNumber=tableNumber;
        this.dish=this.getDishByFoodCode(foodCode);
        this.quantity=quantity;
        this.orderedTime=orderedTime;
        this.fulfilmentTime=fulfilmentTime;
        this.isPaid=isPaid;
    }

    private Dish getDishByFoodCode(String foodCode) throws RestaurantException {
        CookBook cookBook=new CookBook(true);
        return cookBook.getFoodByCode(foodCode);
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public LocalDateTime getFulfilmentTime() {
        return fulfilmentTime;
    }

    public void orderComplete(){
        this.deliveryOrder();
        this.setPaid(true);
    }

    public void deliveryOrder(){
        //this.fulfilmentTime=LocalDateTime.now();
        /*
        pro potřeby testování zde dám náhodnou dobu přípravy, aby byly smysluplný hodnoty při výpočtu průměrné doby přípravy
        * */
        int randomMinute;
        randomMinute=(int)(Math.random()*(15-1))+1;
        this.fulfilmentTime=LocalDateTime.now().plusMinutes(randomMinute);
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public boolean getPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public String getFileLine(){
        return  this.getTableNumber() + Setting.SEPARATOR() +
                this.getDish().getFoodCode() + Setting.SEPARATOR() +
                this.getQuantity() + Setting.SEPARATOR() +
                this.getOrderedTime() + Setting.SEPARATOR() +
                this.getFulfilmentTime() + Setting.SEPARATOR() +
                this.getPaid();
    }

    @Override
    public String toString() {
        return "Order{" +
                "tableNumber=" + tableNumber +
                ", dish=" + dish +
                ", quantity=" + quantity +
                ", orderedTime=" + orderedTime +
                ", fulfilmentTime=" + fulfilmentTime +
                ", isPaid=" + isPaid +
                '}';
    }
}
