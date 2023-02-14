package org.acme.com.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import javax.enterprise.context.ApplicationScoped;

import org.acme.com.model.Supplier;


@ApplicationScoped
public class SupplierRepository implements PanacheRepository<Supplier> {

}
