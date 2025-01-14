package clients.stockModify;

import catalogue.Product;
import middle.MiddleFactory;
import middle.StockReadWriter;

import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import javax.swing.filechooser.*;
import javax.swing.filechooser.FileFilter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Observable;
import java.util.Observer;

/**
 * Implements the Customer view.
 */

public class stockModifyView implements Observer
{
    private static final String ADD  = "Add New Product";
    private static final String REMOVE    = "Remove Product";
    private static final String CLEAR   = "Clear";
    private static final String UPDATE = "Update Product Image";

    private static final int H = 350;       // Height of window pixels
    private static final int W = 475;       // Width  of window pixels

    private final JLabel      pageTitle  = new JLabel();
    private final JLabel      theAction  = new JLabel();
    private final JTextField  theInputName   = new JTextField();
    private final JTextField  theInputPNum = new JTextField();
    private final JTextField  theInputPrice   = new JTextField();
    private final JTextField  theInputQnt = new JTextField();
    private final JTextArea   theOutput  = new JTextArea();
    private final JScrollPane theSP      = new JScrollPane();
    private final JButton     theBtAdd = new JButton( ADD );
    private final JButton     theBtRemove = new JButton( REMOVE );
    private final JButton     theBtClear = new JButton( CLEAR );
    private final JButton     theBtImgUpdt = new JButton( UPDATE );

    private JFileChooser fileChooser = new JFileChooser();
    private StockReadWriter theStock     = null;
    private stockModifyController cont= null;

    /**
     * Construct the view
     * @param rpc   Window in which to construct
     * @param mf    Factor to deliver order and stock objects
     * @param x     x-cordinate of position of window on screen
     * @param y     y-cordinate of position of window on screen
     */
    public stockModifyView(  RootPaneContainer rpc, MiddleFactory mf, int x, int y )
    {
        try                                             //
        {
            theStock = mf.makeStockReadWriter();          // Database access
        } catch ( Exception e )
        {
            System.out.println("Exception: " + e.getMessage() );
        }
        Container cp         = rpc.getContentPane();    // Content Pane
        Container rootWindow = (Container) rpc;         // Root Window
        cp.setLayout(null);                             // No layout manager
        rootWindow.setSize( W, H );                     // Size of Window
        rootWindow.setLocation( x, y );

        Font f = new Font("Monospaced",Font.PLAIN,12);  // Font f is

        pageTitle.setBounds( 210, 10 , 270, 20 );
        pageTitle.setText( "Add and Remove Products from Stock" );
        cp.add( pageTitle );

        theBtAdd.setBounds( 5, 50+60*0, 180, 40 );    // Buy button
        theBtAdd.addActionListener(                  // Call back code
                e -> cont.addToStock(new Product(
                                    theInputPNum.getText(),
                                    theInputName.getText(),
                                    Double.parseDouble(theInputPrice.getText()),
                                    Integer.parseInt(theInputQnt.getText())
                                    ) ) );
        cp.add( theBtAdd );                           //  Add to canvas

        theBtRemove.setBounds( 5, 50+60*1, 180, 40 );   // Check Button
        theBtRemove.addActionListener(                  // Call back code
                e -> cont.removeFromStock(new Product(
                                    theInputPNum.getText(),
                                    "blank",
                                     0.00,
                                    0
                                    ) ) );
        cp.add( theBtRemove );                          //  Add to canvas

        theBtClear.setBounds( 5, 50+60*2, 180, 40);
        theBtClear.addActionListener(
                e -> {
                theInputQnt.setText("");
                theInputName.setText("");
                theInputPrice.setText("");
                theInputPNum.setText("");
        });
        cp.add(theBtClear);

        theBtImgUpdt.setBounds(5, 50+60*3, 180, 40);
        theBtImgUpdt.addActionListener(
                e -> {
                    try {
                        cont.updateImage(theInputPNum.getText(), selectImage(theInputPNum.getText()));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
        cp.add(theBtImgUpdt);

        theAction.setBounds( 200, 25 , 870, 20 );       // Message area
        theAction.setText( "" );                        // Blank
        cp.add( theAction );                            //  Add to canvas

        theInputName.setBounds( 200, 50, 120, 40 );         // Input Area
        theInputName.setText("");                           // Blank
        cp.add( theInputName );                             //  Add to canvas

        theInputPNum.setBounds( 330, 50, 120, 40 );       // Input Area
        theInputPNum.setText("");                        // 0
        cp.add( theInputPNum );                           //  Add to canvas

        theInputPrice.setBounds( 200, 100, 120, 40 );         // Input Area
        theInputPrice.setText("");                           // Blank
        cp.add( theInputPrice );                             //  Add to canvas

        theInputQnt.setBounds( 330, 100, 120, 40 );       // Input Area
        theInputQnt.setText("");                        // 0
        cp.add( theInputQnt );

        theSP.setBounds( 200, 150, 250, 150 );          // Scrolling pane
        theOutput.setText( "" );                        //  Blank
        theOutput.setFont( f );                         //  Uses font
        cp.add( theSP );                                //  Add to canvas
        theSP.getViewport().add( theOutput );           //  In TextArea
        rootWindow.setVisible( true );                  // Make visible
        theInputName.requestFocus();                        // Focus is here
    }

    public void setController( stockModifyController c )
    {
        cont = c;
    }

    /**
     * Update the view, called by notifyObservers(theAction) in model,
     * @param modelC   The observed model
     * @param arg      Specific args
     */
    @Override
    public void update( Observable modelC, Object arg )
    {
        stockModifyModel model  = (stockModifyModel) modelC;
        String        message = (String) arg;
        theAction.setText( message );

        theOutput.setText( model.getBasket().getDetails() );
        theInputName.requestFocus();
    }

    public String selectImage(String Pnum) throws IOException {
        FileFilter imageFilter = new FileNameExtensionFilter(
                "Image files", "jpg");

        //Attaching Filter to JFileChooser object
        fileChooser.setFileFilter(imageFilter);
        // calling the showOpenDialog method to display the save dialog on the frame
        int r = fileChooser.showOpenDialog(null);

        // if the user selects a file
        File file = null;
        String filepath = null;
        if (r == JFileChooser.APPROVE_OPTION) {
            // setting the label as the path of the selected file
            theAction.setText("Image Added");
            filepath = fileChooser.getSelectedFile().getAbsolutePath();
        }
        // if the user canceled the operation
        else
            theAction.setText("The user cancelled the operation");
        return filepath;
    }
}
