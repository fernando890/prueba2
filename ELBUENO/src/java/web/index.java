/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import entidad.Product;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import proyecto.operaciones;

/**
 *
 * @author fer
 */
@ManagedBean
@RequestScoped
public class index implements Serializable {
    @EJB
    private operaciones operaciones;

    /**
     * Creates a new instance of index
     */
    
    public index() {
    }
    public List<Product> getProducts(){

return operaciones.reciveProducts();
}
}
