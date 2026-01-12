
package videogamestore;

public class DigitalGame extends Game {
    private double downloadSizeMb;

    public DigitalGame(int id, String name, double basePrice, String platform, double downloadSizeMb) {
        super(id, name, basePrice, platform, false);
        this.downloadSizeMb = downloadSizeMb;
    }

    public double getDownloadSizeMb() { return downloadSizeMb; }

    @Override
    public String getDisplayString() {
        return super.getDisplayString() + String.format(" [Digital: %.0f MB]", downloadSizeMb);
    }
}
