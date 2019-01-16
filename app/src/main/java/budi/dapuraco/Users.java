package budi.dapuraco;


public class Users {
    String name;
    public String User_id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Users(){

    }

    public Users WithId(String id){
        this.User_id = id;
        return this;
    }

}
