package model;

import java.util.List;

public class NewCustomerFilmRentRequest {

    private String customerCode;
    private List<Integer> filmIds;
    private Integer numberRentDays;


    public String getCustomerCode() {
        return customerCode;
    }


    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }


    public List<Integer> getFilmIds() {
        return filmIds;
    }


    public void setFilmIds(List<Integer> filmIds) {
        this.filmIds = filmIds;
    }


    public Integer getNumberRentDays() {
        return numberRentDays;
    }


    public void setNumberRentDays(Integer numberRentDays) {
        this.numberRentDays = numberRentDays;
    }
}
