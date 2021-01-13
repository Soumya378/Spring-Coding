package com.ranjan.ecommerce.config;

import com.ranjan.ecommerce.entity.Product;
import com.ranjan.ecommerce.entity.ProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Configuration
public class MyDataRestConifg implements RepositoryRestConfigurer {

    private EntityManager entityManager;

    @Autowired
    public MyDataRestConifg(EntityManager entityManager)
    {
        this.entityManager = entityManager;
    }

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {

        HttpMethod[] theUnsupportedActions = {HttpMethod.DELETE, HttpMethod.POST, HttpMethod.PUT};

        // disable for Product
        config.getExposureConfiguration()
                .forDomainType(Product.class)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions));

        // disable for ProductCategory
        config.getExposureConfiguration()
                .forDomainType(ProductCategory.class)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions));

        //call on internal helper method
        exposeIds(config);
    }

    private void exposeIds(RepositoryRestConfiguration config){

        //Get all the list of entity classes from the entity manager
        Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();

        //create an array of entity types
        List<Class> entityClasses = new ArrayList<>();



        //get the entity types for the entities
        for(EntityType tempEntityType: entities)
        {
            entityClasses.add(tempEntityType.getJavaType());
        }

        // expose the entity Ids for the array of entity/domain types
        Class[] domainTypes = entityClasses.toArray(new Class[0]);
        config.exposeIdsFor(domainTypes);
    }
}
