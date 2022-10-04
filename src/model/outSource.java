package model;


public class outSource extends Part{
    private String companyName;

    public outSource(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    public String getCompanyName() {
        return companyName;
    }
    public void setCompanyName() {
        this.companyName = companyName;
    }
}