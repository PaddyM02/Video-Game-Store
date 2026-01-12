package videogamestore;

public class PhysicalGame extends Game {

    private double weightGrams;
    private int stock;     

    public PhysicalGame(int id, String name, double basePrice, String platform, double weightGrams) {
        super(id, name, basePrice, platform, true);
        this.weightGrams = weightGrams;
        this.stock = 5;  
    }

    public double getWeightGrams() { 
        return weightGrams; 
    }

    public int getStock() { 
        return stock; 
    }

    public boolean isInStock() { 
        return stock > 0; 
    }

    public void reduceStock() {
        if (stock > 0) stock--;
    }

    @Override
    public String getDisplayString() {
        return super.getDisplayString() + 
              String.format(" [Physical: %.0fg | Stock: %d]", weightGrams, stock);
    }

    @Override
    public boolean isPhysical() {
        return true;
    }
}
