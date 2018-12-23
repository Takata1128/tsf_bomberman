import java.util.Observable;

class OpponentController extends Observable{
  OpponentController(){
    
  }

  void setState(PlayerXY opponetState){
    setChanged();
    notifyObservers(opponetState);
  }
}