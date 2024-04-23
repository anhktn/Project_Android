package lab.poly.lab1_md18306.Model;

public class City {
    private String id;
    private String country;
    private String name;
    private int population;
//    private String regions;
    private boolean capital;

    public City() {
    }

    public City(String id, String country, String name, int population, boolean capital) {
        this.id = id;
        this.country = country;
        this.name = name;
        this.population = population;
        this.capital = capital;
    }

    public City(String country, String name, int population, boolean capital) {
        this.country = country;
        this.name = name;
        this.population = population;
        this.capital = capital;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public boolean getCapital() {
        return capital;
    }

    public void setCapital(boolean capital) {
        this.capital = capital;
    }
}
