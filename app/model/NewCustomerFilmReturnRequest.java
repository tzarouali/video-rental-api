package model;

import java.util.List;

public class NewCustomerFilmReturnRequest {

    private String customerCode;
    private List<Integer> filmIds;


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
}
