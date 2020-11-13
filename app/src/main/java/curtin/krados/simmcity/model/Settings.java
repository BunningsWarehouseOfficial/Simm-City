package curtin.krados.simmcity.model;

import android.database.sqlite.SQLiteDatabase;

public class Settings {
    public static final int MAX_NAME_LENGTH = 15;

    private String cityName = "Perth";
    private int mapWidth = 50;
    private int mapHeight = 10;
    private int initialMoney = 1000;
    private int familySize = 4;
    private int shopSize = 6;
    private int salary = 10;
    private double taxRate = 0.3;
    private int serviceCost = 2;
    private int houseBuildingCost = 100;
    private int commBuildingCost = 500;
    private int roadBuildingCost = 20;

    //Constructor
    public Settings() { }

    //Accessors
    public String getCityName() {
        return cityName;
    }
    public int getMapWidth() {
        return mapWidth;
    }
    public int getMapHeight() {
        return mapHeight;
    }
    public int getInitialMoney() {
        return initialMoney;
    }
    public int getFamilySize() {
        return familySize;
    }
    public int getShopSize() {
        return shopSize;
    }
    public int getSalary() {
        return salary;
    }
    public double getTaxRate() {
        return taxRate;
    }
    public int getServiceCost() {
        return serviceCost;
    }
    public int getHouseBuildingCost() {
        return houseBuildingCost;
    }
    public int getCommBuildingCost() {
        return commBuildingCost;
    }
    public int getRoadBuildingCost() {
        return roadBuildingCost;
    }

    //Mutators
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
    public void setMapWidth(int mapWidth) {
        this.mapWidth = mapWidth;
    }
    public void setMapHeight(int mapHeight) {
        this.mapHeight = mapHeight;
    }
    public void setInitialMoney(int initialMoney) {
        this.initialMoney = initialMoney;
    }
    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
    }
}
