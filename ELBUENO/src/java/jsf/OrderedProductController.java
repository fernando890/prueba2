package jsf;

import entidad.OrderedProduct;
import jsf.util.JsfUtil;
import jsf.util.PaginationHelper;
import session.OrderedProductFacade;

import java.io.Serializable;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

@ManagedBean(name = "orderedProductController")
@SessionScoped
public class OrderedProductController implements Serializable {

    private OrderedProduct current;
    private DataModel items = null;
    @EJB
    private session.OrderedProductFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public OrderedProductController() {
    }

    public OrderedProduct getSelected() {
        if (current == null) {
            current = new OrderedProduct();
            current.setOrderedProductPK(new entidad.OrderedProductPK());
            selectedItemIndex = -1;
        }
        return current;
    }

    private OrderedProductFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (OrderedProduct) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new OrderedProduct();
        current.setOrderedProductPK(new entidad.OrderedProductPK());
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            current.getOrderedProductPK().setCustomerOrderId(current.getPedido().getId());
            current.getOrderedProductPK().setProductId(current.getProduct().getId());
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("OrderedProductCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (OrderedProduct) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            current.getOrderedProductPK().setCustomerOrderId(current.getPedido().getId());
            current.getOrderedProductPK().setProductId(current.getProduct().getId());
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("OrderedProductUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (OrderedProduct) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("OrderedProductDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    @FacesConverter(forClass = OrderedProduct.class)
    public static class OrderedProductControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            OrderedProductController controller = (OrderedProductController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "orderedProductController");
            return controller.ejbFacade.find(getKey(value));
        }

        entidad.OrderedProductPK getKey(String value) {
            entidad.OrderedProductPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new entidad.OrderedProductPK();
            key.setCustomerOrderId(Integer.parseInt(values[0]));
            key.setProductId(Integer.parseInt(values[1]));
            return key;
        }

        String getStringKey(entidad.OrderedProductPK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getCustomerOrderId());
            sb.append(SEPARATOR);
            sb.append(value.getProductId());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof OrderedProduct) {
                OrderedProduct o = (OrderedProduct) object;
                return getStringKey(o.getOrderedProductPK());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + OrderedProduct.class.getName());
            }
        }

    }

}
