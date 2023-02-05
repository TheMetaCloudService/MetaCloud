package eu.themetacloudservice.manager.cloudservices.interfaces;

import eu.themetacloudservice.manager.cloudservices.enums.TaskedServiceStatus;

public interface ITaskedService {

  void handelExecute(String line);
  void handelSync();
  void handelLaunch();
  void handelScreen();
  void handelQuit();
  void handelStatusChange(TaskedServiceStatus status);
  void handelCloudPlayerConnection(boolean connect);

}
