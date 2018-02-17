package model;

public class NewFilmRequest {

    private String name;
    private Integer filmTypeId;


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public Integer getFilmTypeId() {
        return filmTypeId;
    }


    public void setFilmTypeId(Integer filmTypeId) {
        this.filmTypeId = filmTypeId;
    }
}
