package Com.DCAD.Views;

import Com.DCAD.Entities.PropietarioHasVehiculo;
import Com.DCAD.Views.util.JsfUtil;
import Com.DCAD.Views.util.JsfUtil.PersistAction;
import Com.DCAD.Models.PropietarioHasVehiculoFacade;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@ManagedBean(name = "propietarioHasVehiculoController")
@SessionScoped
public class PropietarioHasVehiculoController implements Serializable {

    @EJB
    private Com.DCAD.Models.PropietarioHasVehiculoFacade ejbFacade;
    private List<PropietarioHasVehiculo> items = null;
    private PropietarioHasVehiculo selected;

    public PropietarioHasVehiculoController() {
    }

    public PropietarioHasVehiculo getSelected() {
        return selected;
    }

    public void setSelected(PropietarioHasVehiculo selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private PropietarioHasVehiculoFacade getFacade() {
        return ejbFacade;
    }

    public PropietarioHasVehiculo prepareCreate() {
        selected = new PropietarioHasVehiculo();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("PropietarioHasVehiculoCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("PropietarioHasVehiculoUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("PropietarioHasVehiculoDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<PropietarioHasVehiculo> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public List<PropietarioHasVehiculo> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<PropietarioHasVehiculo> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = PropietarioHasVehiculo.class)
    public static class PropietarioHasVehiculoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PropietarioHasVehiculoController controller = (PropietarioHasVehiculoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "propietarioHasVehiculoController");
            return controller.getFacade().find(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof PropietarioHasVehiculo) {
                PropietarioHasVehiculo o = (PropietarioHasVehiculo) object;
                return getStringKey(o.getIdPropietarioVehiculo());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), PropietarioHasVehiculo.class.getName()});
                return null;
            }
        }

    }

}
