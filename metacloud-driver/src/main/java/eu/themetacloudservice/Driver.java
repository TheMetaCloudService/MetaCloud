package eu.themetacloudservice;

public class Driver {

    private static  Driver instance;


    public Driver(){
        instance = this;
    }



    public static Driver getInstance() {
        return instance;
    }
}
