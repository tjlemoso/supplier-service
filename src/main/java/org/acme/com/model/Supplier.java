package org.acme.com.model;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "supplier")
@Getter
@Setter
public class Supplier {

  @Id
  @Column(name = "supplierId")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long supplierId;

  @Column(name = "createData")
  private LocalDate createData;

  @Column(name = "name")
  private String name;

  @Column(name = "phone")
  private String phone;

  @Column(name = "email")
  private String email;

  @Column(name = "identity")
  private String identity;

  @Column(name = "description")
  private String description;
  
  @Column(name = "addressId")
  private Long addressId;
}
