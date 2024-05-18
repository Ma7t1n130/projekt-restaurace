import java.io.*;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OrderBook {
    private List<Order> orders;
    private final String fileName = "OrderBook.txt";

    public OrderBook(){
        this.orders = new ArrayList<>();
    }
    public List<Order> getOrders() {
        return orders;
    }

    public void addOrder(int tableNumber,String foodCode, int quantity) throws RestaurantException {
        this.orders.add(new Order(tableNumber, foodCode, quantity));
    }

    public void addOrder(Order order){
        this.orders.add(order);
    }

    public void removeUnpaidOrder(int tableNumber,String foodCode) throws RestaurantException {
        Order order = getUnpaidOrder(tableNumber,foodCode);
        this.orders.remove(this.orders.indexOf(order));
    }

    public void paidOrder(int tableNumber,String foodCode) throws RestaurantException {
        Order order = getUnpaidOrder(tableNumber,foodCode);
        this.orders.get(this.orders.indexOf(order)).setPaid(true);
    }

    public void deliveryOrder(int tableNumber,String foodCode) throws RestaurantException {
        Order order = getUnpaidOrder(tableNumber,foodCode);
        this.orders.get(this.orders.indexOf(order)).deliveryOrder();
    }

    public Order getUnpaidOrder(int tableNumber,String foodCode) throws RestaurantException {
        for(Order order : orders){
            if ((order.getTableNumber()==tableNumber) && (order.getDish().getFoodCode().equals(foodCode)) && (order.getPaid()==false)){
                return order;
            }
        }
        throw new RestaurantException("Nezaplacená objednávka nenalezena.");
    }

    public void saveToFile() throws RestaurantException {
        try(PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName)))){
            for(Order order : orders){
                writer.println(order.getFileLine());
            }
        } catch (IOException e) {
            throw new RestaurantException("Chyba při zápisu do souboru " + fileName + " " + e.getLocalizedMessage());
        }
    }

    public void loadFromFile() throws RestaurantException {
        orders.clear();
        int lineCounter=0;
        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(fileName)))) {
            while(scanner.hasNextLine()){
                lineCounter++;
                String fileLine = scanner.nextLine();
                String[] parts = fileLine.split(Setting.SEPARATOR());
                if (parts.length!=6){
                    throw new RestaurantException("Nesprávný formát řádku.");
                }
                int tableName=Integer.parseInt(parts[0]);
                String foodCode = parts[1];
                int quantity = Integer.parseInt(parts[2]);
                LocalDateTime orderedTime=LocalDateTime.parse(parts[3]);
                LocalDateTime fulfilmentTime;
                if (parts[4].equals("null")){
                    fulfilmentTime=null;
                } else{
                    fulfilmentTime=LocalDateTime.parse(parts[4]);
                }
                boolean isPaid = Boolean.parseBoolean(parts[5]);
                addOrder(new Order(tableName,foodCode,quantity,orderedTime,fulfilmentTime,isPaid));
            }
        } catch (RestaurantException e) {
            throw new RestaurantException("Nesprávný formát řádku " + lineCounter);
        } catch (FileNotFoundException e) {
            //System.out.println("Soubor " +fileName+ " zatím neexistuje.");
        } catch (NumberFormatException e) {
            throw new RestaurantException("Chyba při čtení číselné hodnoty na řádku " + lineCounter);
        }
    }

}
