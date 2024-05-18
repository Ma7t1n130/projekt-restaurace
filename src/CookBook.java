import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CookBook {
    private List<Dish> foods;
    private final String fileName = "CookBook.txt";

    public CookBook(boolean loadFromFileImmediate) throws RestaurantException {
        this.foods = new ArrayList<>();
        if(loadFromFileImmediate){
            this.loadFromFile();
        }
    }

    /* zkontroluje zda je kód jídla v jídelním lístku ještě volný*/
    private boolean isFoodCodeUnique(String foodCode){
        try{
            Dish dish = getFoodByCode(foodCode);
            return false;
        } catch (RestaurantException e){
            return true;
        }
    }

    /* najde v jídelním lístku jídlo se zadaným kódem*/
    public Dish getFoodByCode(String foodCode) throws RestaurantException {
        for(Dish dish : foods){
            if(dish.getFoodCode().equals(foodCode)) {
                return dish;
            }
        }
        throw new RestaurantException("Jídlo nebylo v jídelním lístku nalezeno.");
    }

    /*přidá jídlo do jídelního lístku*/
    public void addFood(String foodCode,String title, BigDecimal price, int preparationTime, String image, boolean available) throws RestaurantException {
        if (isFoodCodeUnique(foodCode)) {
            this.foods.add(new Dish(title, price, preparationTime, image, available,foodCode));
        }
    }

    /*přidá jídlo do jídelního lístku*/
    public void addFood(String foodCode,String title, BigDecimal price, int preparationTime) throws RestaurantException {
        if (isFoodCodeUnique(foodCode)) {
            this.foods.add(new Dish(title, price, preparationTime,foodCode));
        }
    }

    /*vymaze jídlo z jídelního lístku*/
    public void delFood(String foodCode) throws RestaurantException {
        try {
            foods.remove(foods.indexOf(getFoodByCode(foodCode)));
        } catch (Exception e) {
            throw new RestaurantException("Chyba při mazání jídla z jídelního lístku: " + e.getLocalizedMessage());
        }
    }

    /*editace názvu, ceny a času přípravy jídla*/
    public void editFood(String foodCode,String title,BigDecimal price,int preparationTime,String image) throws RestaurantException {
        try {
            int idx = foods.indexOf(getFoodByCode(foodCode));
            foods.get(idx).setTitle(title);
            foods.get(idx).setPrice(price);
            foods.get(idx).setPreparationTime(preparationTime);
            foods.get(idx).setImage(image);
        } catch (Exception e){
            throw new RestaurantException("Chyba při editaci jídla v jídelním lístku: " + e.getLocalizedMessage());
        }
    }

    /*nastaví dostupnost jídla*/
    public void enableFood(String foodCode) throws RestaurantException {
        foods.get(foods.indexOf(getFoodByCode(foodCode))).setAvailable(true);
    }

    /*nastaví nedostupnost jídla*/
    public void disableFood(String foodCode) throws RestaurantException {
        foods.get(foods.indexOf(getFoodByCode(foodCode))).setAvailable(false);
    }

    /*uloží aktuální jídelní lístek do souboru*/
    public void saveToFile() throws RestaurantException {
        try(PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName)))){
            for(Dish dish : foods){
                writer.println(dish.getFileLine());
            }
        } catch (IOException e) {
            throw new RestaurantException("Chyba při zápisu do souboru " + fileName + " " + e.getLocalizedMessage());
        }
    }

    /*načte jídelní lístek ze souboru*/
    public void loadFromFile() throws RestaurantException {
        foods.clear();
        int lineCounter=0;
        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(fileName)))) {
            while(scanner.hasNextLine()){
                lineCounter++;
                String fileLine = scanner.nextLine();
                String[] parts = fileLine.split(Setting.SEPARATOR());
                if (parts.length!=6){
                    throw new RestaurantException("Nesprávný formát řádku.");
                }
                String foodCode = parts[0];
                String title = parts[1];
                BigDecimal price = new BigDecimal(parts[2]);
                int preparationTime = Integer.parseInt(parts[3]);
                String image = parts[4];
                String available = parts[5];
                addFood(foodCode,title,price,preparationTime,image,available.equals("true"));
            }
        } catch (RestaurantException e) {
            throw new RestaurantException("Nesprávný formát řádku " + lineCounter);
        } catch (FileNotFoundException e) {
            //System.out.println("Soubor " +fileName+ " zatím neexistuje.");
        } catch (NumberFormatException e) {
            throw new RestaurantException("Chyba při čtení číselné hodnoty na řádku " + lineCounter);
        }
    }

    public List<Dish> getFoods() {
        return foods;
    }

    public void setDefaultCookBook() throws RestaurantException {
        addFood("ŘÍZEK", "Kuřecí řízek obalovaný 150 g", BigDecimal.valueOf(165), 15, "kureci-rizek", true);
        addFood("HRANOLKY", "Hranolky 150 g", BigDecimal.valueOf(105), 12, "hranolky", true);
        addFood("PSTRUH", "Pstruh na víně 200 g", BigDecimal.valueOf(190), 25, "pstruh", true);
        addFood("KOFOLA", "Kofola 0,5 l", BigDecimal.valueOf(39), 3, "kofola", true);
    }


}