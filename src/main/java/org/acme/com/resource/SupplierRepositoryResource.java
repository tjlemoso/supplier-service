package org.acme.com.resource;

import java.time.LocalDate;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.acme.com.model.Supplier;
import org.acme.com.repository.SupplierRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;


import io.quarkus.panache.common.Sort;

@Path("supplier")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class SupplierRepositoryResource {

    @Inject
    SupplierRepository supplierRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(SupplierRepositoryResource.class.getName());

    @GET
    public List<Supplier> get() {
        return supplierRepository.listAll(Sort.by("name"));
    }

    @GET
    @Path("{id}")
    public Supplier getSingle(Long id) {
      Supplier entity = supplierRepository.findById(id);
        if (entity == null) {
            throw new WebApplicationException("Supplier with id of " + id + " does not exist.", 404);
        }
        return entity;
    }

    @POST
    @Transactional
    public Response create(Supplier supplier) {
        if (supplier.getName() == "") {
            throw new WebApplicationException("Supplier was invalidly set on request.", 422);
        }
        supplier.setCreateDate(LocalDate.now());
        supplierRepository.persist(supplier);
        return Response.ok(supplier).status(201).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Supplier update(Long id, Supplier supplier) {
        if (supplier.getName() == null) {
            throw new WebApplicationException("Supplier was not set on request.", 422);
        }

        Supplier entity = supplierRepository.findById(id);
        if (entity == null) {
            throw new WebApplicationException("Supplier with id of " + id + " does not exist.", 404);
        }

        entity.setName(supplier.getName());  
        entity.setPhone(supplier.getPhone());
        entity.setEmail(supplier.getEmail());
        entity.setAddress(supplier.getAddress());
        entity.setAddress2(supplier.getAddress2());
        entity.setCity(supplier.getCity());
        entity.setState(supplier.getState());
        entity.setZip(supplier.getZip());
        entity.setCountry(supplier.getCountry());
        
        return entity;
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(Long id) {
      Supplier entity = supplierRepository.findById(id);
      if (entity == null) {
          throw new WebApplicationException("Supplier with id of " + id + " does not exist.", 404);
      }

      supplierRepository.delete(entity);
      return Response.status(204).build();
    }

    @Provider
    public static class ErrorMapper implements ExceptionMapper<Exception> {

        @Inject
        ObjectMapper objectMapper;

        @Override
        public Response toResponse(Exception exception) {
            LOGGER.error("Failed to handle request", exception);

            int code = 500;
            if (exception instanceof WebApplicationException) {
                code = ((WebApplicationException) exception).getResponse().getStatus();
            }

            ObjectNode exceptionJson = objectMapper.createObjectNode();
            exceptionJson.put("exceptionType", exception.getClass().getName());
            exceptionJson.put("code", code);

            if (exception.getMessage() != null) {
                exceptionJson.put("error", exception.getMessage());
            }

            return Response.status(code)
                    .entity(exceptionJson)
                    .build();
        }

    }
}