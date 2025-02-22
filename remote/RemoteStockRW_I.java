/**
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */

package remote;

import catalogue.Product;
import middle.StockException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Defines the RMI interface for read/write access to the stock object.
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */

public interface RemoteStockRW_I
       extends   RemoteStockR_I, Remote
{
  boolean buyStock(String number, int amount)
          throws RemoteException, StockException;
  void    addStock(String number, int amount)
          throws RemoteException, StockException;
  void    modifyStock(Product detail)
          throws RemoteException, StockException;
  void    addNewStock(Product product)
          throws RemoteException, StockException;

  void    removeFromStock(Product product)
          throws RemoteException, StockException;

  void updateImage(String pn, String filepath)
          throws RemoteException, StockException;

  List<List<String>> listStock()
          throws RemoteException, StockException;
}

