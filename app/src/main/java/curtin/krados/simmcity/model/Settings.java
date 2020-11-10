package curtin.krados.simmcity.model;

public class Settings {
    private String cityName = "Perth";
    private int mapWidth = 30; //TODO Changed from 50, which is required
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

    //Accessors //TODO remove unused methods
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
    public void setFamilySize(int familySize) {
        this.familySize = familySize;
    }
    public void setShopSize(int shopSize) {
        this.shopSize = shopSize;
    }
    public void setSalary(int salary) {
        this.salary = salary;
    }
    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
    }
    public void setServiceCost(int serviceCost) {
        this.serviceCost = serviceCost;
    }
    public void setHouseBuildingCost(int houseBuildingCost) {
        this.houseBuildingCost = houseBuildingCost;
    }
    public void setCommBuildingCost(int commBuildingCost) {
        this.commBuildingCost = commBuildingCost;
    }
    public void setRoadBuildingCost(int roadBuildingCost) {
        this.roadBuildingCost = roadBuildingCost;
    }
}
