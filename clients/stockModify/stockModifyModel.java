package clients.stockModify;

import catalogue.Basket;
import catalogue.BetterBasket;
import catalogue.Product;
import debug.DEBUG;
import middle.MiddleFactory;
import middle.StockException;
import middle.StockReadWriter;

import java.util.Observable;

/**
 * Implements the Model of the back door client
 */
public class stockModifyModel extends Observable
{
    private Basket      theBasket  = null;            // Bought items
    private String      pn = "";                      // Product being processed

    private StockReadWriter theStock     = null;

    /*
     * Construct the model of the back door client
     * @param mf The factory to create the connection objects
     */

    public stockModifyModel(MiddleFactory mf)
    {
        try                                           //
        {
            theStock = mf.makeStockReadWriter();        // Database access
        } catch ( Exception e )
        {
            DEBUG.error("CustomerModel.constructor\n%s", e.getMessage() );
        }

        theBasket = makeBasket();                     // Initial Basket
    }

    public void addToStock(Product product )
    {
        String theAction = "";
        theBasket = makeBasket();
        pn  = product.getProductNum();                    // Product no.
        String pn  = product.getProductNum().trim();             // Product no.
        int amount = 0;
        try
        {
            String aQuantity = String.valueOf(product.getQuantity());
            try
            {
                amount = Integer.parseInt(aQuantity);   // Convert
                if ( amount < 0 )
                    throw new NumberFormatException("-ve");
            }
            catch ( Exception err)
            {
                theAction = "Invalid quantity";
                setChanged(); notifyObservers(theAction);
                return;
            }

            if ( ! theStock.exists( pn ) )              // Stock Exists?
            {                                         // T
                Product pr = theStock.getDetails(pn);   //  Get details
                theStock.addNewStock(product);          //  Re stock
                theAction = "Product Added";                         // Display
            } else {                                  // F
                theAction =                             //  Inform Unknown
                        "product number already exists " + pn;       //  product number
            }
        } catch( StockException e )
        {
            theAction = e.getMessage();
        }
        setChanged(); notifyObservers(theAction);
    }

    public void removeFromStock(Product product )
    {
        String theAction = "";
        theBasket = makeBasket();
        pn  = product.getProductNum();                    // Product no.
        String pn  = product.getProductNum().trim();             // Product no.
        int amount = 0;
        try
        {
            String aQuantity = String.valueOf(product.getQuantity());
            try
            {
                amount = Integer.parseInt(aQuantity);   // Convert
                if ( amount < 0 )
                    throw new NumberFormatException("-ve");
            }
            catch ( Exception err)
            {
                theAction = "Invalid quantity";
                setChanged(); notifyObservers(theAction);
                return;
            }

            if ( theStock.exists( pn ) )              // Stock Exists?
            {                                         // T
                Product pr = theStock.getDetails(pn);   //  Get details
                theStock.removeFromStock(product);          //  Re stock
                theAction = "Product removed";                         // Display
            } else {                                  // F
                theAction =                             //  Inform Unknown
                        "product doesn't exist " + pn;       //  product number
            }
        } catch( StockException e )
        {
            theAction = e.getMessage();
        }
        setChanged(); notifyObservers(theAction);
    }

    /**
     * Get the Basket of products
     * @return basket
     */
    public Basket getBasket()
    {
        return theBasket;
    }

    /**
     * return an instance of a Basket
     * @return a new instance of a Basket
     */
    protected Basket makeBasket()
    {
        return new Basket();
    }
}


