package eu.themetacloudservice.network.service;

import eu.themetacloudservice.network.service.proxyconnect.Service;

import java.util.ArrayList;

public class PackageConnectedProxyCallBackData {

    private ArrayList<Service> services;


    public PackageConnectedProxyCallBackData() {
    }

    public PackageConnectedProxyCallBackData(ArrayList<Service> services) {
        this.services = services;
    }

    public ArrayList<Service> getServices() {
        return services;
    }
}
