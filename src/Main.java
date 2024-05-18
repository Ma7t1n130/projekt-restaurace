//discord username: ma7t1n_17695

public class Main {

    public static void main(String[] args) throws RestaurantException {
        //jídelní lístek a kniha objednávek
        CookBook cookBook = new CookBook(false);
        OrderBook orderBook = new OrderBook();
        /*
        1. Načti stav evidence z disku. Pokud se aplikace spouští poprvé a soubory neexistují, budou veškeré seznamy
        a repertoár zatím prázdné. (Aplikace nepotřebuje předvytvořené žádné soubory.)
        * */
        try {
            cookBook.loadFromFile();
        } catch (RestaurantException e){
            System.out.println("Chyba při čtení souboru jídelního lístku: "+e.getMessage());
        }
        try {
            orderBook.loadFromFile();
        } catch (RestaurantException e){
            System.out.println("Chyba při čtení souboru knihy objednávek: "+e.getMessage());
        }
        /*
        2.Připrav testovací data
        2.1 sestavit jídelní lístek, pokud není načten ze souboru
        * */
        if(cookBook.getFoods().size()==0) {
            cookBook.setDefaultCookBook();
            cookBook.saveToFile();
        }
        //pro správu jídelního lístku jsou k dispozici metody addFood, delFood, editFood, enableFood a disableFood
        //cookBook.getFoods().forEach(System.out::println);
        /*
        2.2.1 Zákazníci u stolu 15 si objednali dvakrát kuřecí řízek, dvakrát hranolky a dvakrát Kofolu. Kofolu už dostali, na řízek ještě čekají.
        * */
        if(orderBook.getOrders().size()==0) { //jen poprvé
            orderBook.addOrder(15, "ŘÍZEK", 2);
            orderBook.addOrder(15, "HRANOLKY", 2);
            orderBook.addOrder(15, "KOFOLA", 2);
            orderBook.deliveryOrder(15, "KOFOLA"); //doručeno na stůl
            orderBook.paidOrder(15, "KOFOLA"); //zaplacení kofoly
        /*
        2.2.2 Vytvoř také objednávku pro stůj číslo 2.
        * */
            orderBook.addOrder(2, "KOFOLA", 4);
            orderBook.addOrder(2, "HRANOLKY", 4);
            orderBook.saveToFile();
        }
        //orderBook.getOrders().forEach(System.out::println);
        /*
        3. Vypiš celkovou cenu konzumace pro stůl číslo 15.
        * */
        RestaurantManager restaurantManager=new RestaurantManager();
        System.out.println("Celková částka pro stůl 15: "+restaurantManager.getSumPriceByTableNumber(15)+"Kč");
        /*
        4. Použij všechny připravené metody pro získání informací pro management — údaje vypisuj na obrazovku.
        * */
        //Kolik objednávek je aktuálně rozpracovaných a nedokončených.
        System.out.println("Počet nedokončených, tedy nezaplacených, objednávek je: "+restaurantManager.getCountOfUncompleteOrders());
        //Možnost seřadit objednávky podle času zadání.
        System.out.println("Objednávky seřazené podle času zadání:");
        for(Order order: restaurantManager.getOrdersSortByTime(false)){
            System.out.println(order);
        }
        //Průměrná dobu zpracování objednávek
        System.out.println("Průměrná doba zpracování objednávek: "+restaurantManager.getAveragePrepareTime()+" minut.");
        //Seznam jídel, která byla dnes objednána. Bez ohledu na to, kolikrát bylo dané jídlo objednáno.
        System.out.println("Seznam dnes objednaných jídel:");
        for(String foodCode: restaurantManager.getTodayDishList()){
            System.out.println(foodCode);
        }
        //Export seznamu objednávek pro stůl 15
        restaurantManager.getTableOrdersTxtReport(15);
        //Export seznamu objednávek pro stůl 2
        restaurantManager.getTableOrdersTxtReport(2);
        /*
        5. Změněná data ulož na disk.
        * */
        orderBook.saveToFile();
        cookBook.saveToFile();
        /*
        6.Po opětovném spuštění aplikace musí být data opět v pořádku načtena. (Vyzkoušej!)

            viz řádky 14-23

        * */
        /*
        Pokud budou data ve vstupních souborech poškozená či v nesprávném formátu, aplikace se při spuštění s těmito soubory
        musí zachovat korektně — zahlásí chybu a bude pokračovat bez načtených dat. Aplikace nesmí havarovat.

            testováno s nesmyslným obsahem souboru s knihou objednávek

        * */
    }
}