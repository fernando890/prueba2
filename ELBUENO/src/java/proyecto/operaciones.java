/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto;

import entidad.Product;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
/**
 *
 * @author fer
 */
@Stateless
public class operaciones {
    @PersistenceContext(unitName = "WebApplication10PU")
    private EntityManager em;
      public List<Product> reciveProducts(){

return em.createQuery("SELECT p FROM Product p").getResultList();
}

public int checkifQueryExistens(String query){
List <Product> productos=em.createQuery("SELECT p FROM Product p WHERE p.descripcion= :descripcion").setParameter("descripcion",query).getResultList();
return productos.size();
}
public Product returnProduct(String query){
Product product = (Product)em.createQuery("SELECT p FROM Product p WHERE p.descripcion= :descripcion").setParameter("descripcion",query).getSingleResult();
return product;

}
}
