import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RestaurantManager {

    private OrderBook orderBook;

    public RestaurantManager() throws RestaurantException {
        orderBook=new OrderBook();
        orderBook.loadFromFile();
    }

    List<Order> getOrdersByPaid(boolean paid){
        return this.orderBook.getOrders()
                .stream()
                .filter((Order currentElement) -> {return (paid == currentElement.getPaid());})
                .toList();
    }

    public int getCountOfUncompleteOrders(){
        return getOrdersByPaid(false).size();
    }

    public List<Order> getOrdersSortByTime(boolean onlyPaid){
        List<Order> filterOrders=new ArrayList<>();
        if(onlyPaid){
            filterOrders=getOrdersByPaid(true);
        }else{
            filterOrders=this.orderBook.getOrders();
        }
        Comparator<Order> compareByTime = Comparator
                .comparing(Order::getOrderedTime);
        return filterOrders
                .stream()
                .sorted(compareByTime)
                .toList();
    }

    public long getAveragePrepareTime(){
        List<Order> completeOrders=new ArrayList<>();
        completeOrders=getOrdersByPaid(true);
        long averageMinutes=0;
        if (completeOrders.size()>0) {
            for (Order order : completeOrders) {
                averageMinutes += ChronoUnit.MINUTES.between(order.getOrderedTime(), order.getFulfilmentTime());
            }
            averageMinutes=averageMinutes/completeOrders.size();
        }
        return averageMinutes;
    }

    public List<String> getTodayDishList(){
        DateTimeFormatter dateFormater = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        List<String> dishList=new ArrayList<>();
        for(Order order : this.orderBook.getOrders()){
            if(order.getOrderedTime().format(dateFormater).equals(LocalDate.now().format(dateFormater))){
                dishList.add(order.getDish().getFoodCode());
            }
        }
        return dishList
                .stream()
                .distinct()
                .toList();
    }

    public List<Order> getOrdersByTableNumber(int tableNumber){
        return this.orderBook.getOrders()
                .stream()
                .filter((Order currentElement) -> {return (tableNumber == currentElement.getTableNumber());})
                .toList();
    }

    public BigDecimal getSumPriceByTableNumber(int tableNumber){
        BigDecimal sumPrice=BigDecimal.valueOf(0);
        for(Order order : getOrdersByTableNumber(tableNumber)){
            sumPrice=sumPrice.add(order.getDish().getPrice().multiply(BigDecimal.valueOf(order.getQuantity())));
        }
        return sumPrice;
    }

    private String getIntegerFmt2(int paramNumber){
        String result = String.valueOf(paramNumber);
        if(paramNumber<10){
            result=" "+result;
        }
        return result;
    }

    private String getTimeFmt(LocalDateTime datevalue){
        String result=" ";
        if(datevalue!=null){
            result=datevalue.format(DateTimeFormatter.ofPattern("hh:mm"));
        }
        return result;
    }

    private String getPaidStr(boolean isPaid){
        String result="";
        if(isPaid){
            result="zaplaceno";
        }
        return result;
    }

    public void getTableOrdersTxtReport(int tableNumber){
        String reportLine;
        int rowNumber;
        System.out.println("** Objednávky pro stůl č. "+getIntegerFmt2(tableNumber)+" **");
        System.out.println("****");
        rowNumber=0;
        //DateTimeFormatter timeFormater = DateTimeFormatter.ofPattern("hh:mm");
        for(Order order : this.getOrdersByTableNumber(tableNumber)){
            rowNumber++;
            reportLine=String.format("%s. %s %dx (%s Kč):\t %s-%s\t %s",
                    getIntegerFmt2(rowNumber),
                    order.getDish().getTitle(),
                    order.getQuantity(),
                    order.getDish().getPrice().multiply(BigDecimal.valueOf(order.getQuantity())),
                    getTimeFmt(order.getOrderedTime()),
                    getTimeFmt(order.getFulfilmentTime()),
                    getPaidStr(order.getPaid())
                    );
            System.out.println(reportLine);
        }
        System.out.println("******");
    }

}
