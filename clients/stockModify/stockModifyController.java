package clients.stockModify;

import catalogue.Product;

public class stockModifyController {
    private stockModifyModel model = null;
    private stockModifyView view = null;

    /**
     * Constructor
     *
     * @param model The model
     * @param view  The view from which the interaction came
     */
    public stockModifyController(stockModifyModel model, stockModifyView view) {
        this.view = view;
        this.model = model;
    }

    public void addToStock(Product product)
    {
        model.addToStock(product);
    }

    public void removeFromStock(Product product)
    {
        model.removeFromStock(product);
    }
}