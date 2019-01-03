import java.util.Observable;

class OpponentController extends Observable{
  
  void setState(NetworkPlayer opponetState){
    setChanged();
    notifyObservers(opponetState);
  }
}