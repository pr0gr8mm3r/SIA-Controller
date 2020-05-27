class Boat {
  
  String name;
  String btAddress;
  public boolean connected;
  
  Boat(String name, String btAddress, boolean connected) { 
    this.name = name;
    this.btAddress = btAddress;
    this.connected = connected;
  }

  void setConnected(boolean connected) {
    this.connected = connected;
  }
  
}
