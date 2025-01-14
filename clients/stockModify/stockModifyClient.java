package clients.stockModify;

import middle.MiddleFactory;
import middle.Names;
import middle.RemoteMiddleFactory;

import javax.swing.*;

/**
 * The standalone BackDoor Client
 */

public class stockModifyClient
{
    public static void main (String args[])
    {
        String stockURL = args.length < 1     // URL of stock RW
                        ? Names.STOCK_RW      //  default  location
                        : args[0];            //  supplied location

        RemoteMiddleFactory mrf = new RemoteMiddleFactory();
        mrf.setStockRWInfo(stockURL);
        displayGUI(mrf);                       // Create GUI
    }

    private static void displayGUI(MiddleFactory mf)
    {
        JFrame  window = new JFrame();

        window.setTitle( "Stock Modification Client (MVC RMI)");
        window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        stockModifyModel      model = new stockModifyModel(mf);
        stockModifyView       view  = new stockModifyView( window, mf, 0, 0 );
        stockModifyController cont  = new stockModifyController( model, view );
        view.setController( cont );

        model.addObserver( view );       // Add observer to the model - view is observer, model is Observable
        window.setVisible(true);         // Display Screen
    }
}
