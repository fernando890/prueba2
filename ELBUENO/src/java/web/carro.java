/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import entidad.Product;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author fer
 */
@ManagedBean
@SessionScoped
public class carro implements Serializable {

    private List<Product> products = new ArrayList();//CREA UNA INSTANCIA DE TIPO LISTA Y LA GUARDA EN EL ARRAY

    public carro() {
    }

    public void add(Product p) {
        products.add(p);
    }

    public void remove(Product p) {
        products.remove(p);
    }

    public int getcartcount() {
        return products.size();

    }

    public Map<Product, Integer> getCartContents() {

        Map<Product, Integer> cartContents = new HashMap<>();

        for (Product obj : products) {
            if (cartContents.containsKey(obj)) {//para saber si hay algo en el carro para ver si es nulo o no
                cartContents.put(obj, cartContents.get(obj) + 1);
            } else {
                cartContents.put(obj, 1);

            }

        }
        return cartContents;
    }

}
