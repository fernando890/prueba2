/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import entidad.Product;
import java.io.IOException;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import proyecto.operaciones;

/**
 *
 * @author fer
 */
@ManagedBean
@RequestScoped
public class prod implements Serializable {

    @EJB
    private operaciones operaciones;

    @ManagedProperty(value = "#{carro}")
    carro mycarro;

    public carro getMycarro() {
        return mycarro;
    }

    public void setMycarro(carro mycarro) {
        this.mycarro = mycarro;
    }

    /**
     * Creates a new instance of prod
     */
    public prod() {
    }

    public String getQuery() {

        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("query");
    }

    public void checkIfQueryExists() throws IOException {

        if (operaciones.checkifQueryExistens(getQuery()) == 0) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("error.xhtml");

        }

    }

    public Product getProduct() {
        return operaciones.returnProduct(getQuery());

    }

    public void addtocart() {
        String query = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("query");
        mycarro.add(operaciones.returnProduct(query));
    }

}