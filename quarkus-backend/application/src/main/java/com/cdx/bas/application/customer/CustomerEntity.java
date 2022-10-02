package com.cdx.bas.application.customer;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.cdx.bas.application.bank.account.BankAccountEntity;
import com.cdx.bas.domain.customer.Gender;
import com.cdx.bas.domain.customer.MaritalStatus;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import io.quarkiverse.hibernate.types.json.JsonBinaryType;
import io.quarkiverse.hibernate.types.json.JsonTypes;

@Entity
@Table(schema = "basapp", name = "customers", uniqueConstraints = @UniqueConstraint(columnNames = "customer_id"))
@TypeDef(name = JsonTypes.JSON_BIN, typeClass = JsonBinaryType.class)
public class CustomerEntity {
    
    @Id
    @Column(name = "customer_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customers_customer_id_seq_gen")
    @SequenceGenerator(name = "customers_customer_id_seq_gen", sequenceName = "customers_customer_id_seq", allocationSize = 1, initialValue = 1)
    private long id;
    
    @Column(name = "first_name", nullable = false)
    private String firstName;
    
    @Column(name = "last_name", nullable = false)
    private String lastName;
    
    @Column(name = "gender", nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;
    
    @Column(name = "marital_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private MaritalStatus maritalStatus;
    
    @Column(name = "birthday", nullable = false)
    private LocalDateTime birthdate;
    
    @Column(name = "country", nullable = false)
    private String country;
    
    @Column(name = "address", nullable = false)
    private String address;
    
    @Column(name = "city", nullable = false)
    private String city;
    
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;
    
    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "bank_accounts_customers", joinColumns = @JoinColumn(name = "customer_id"), inverseJoinColumns = @JoinColumn(name = "account_id"))
    private Set<BankAccountEntity> accounts = new HashSet<>();
    
    @Type(type = JsonTypes.JSON_BIN)
    @Column(name = "metadatas", columnDefinition = JsonTypes.JSON_BIN, nullable = true)
    private String metadatas;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public LocalDateTime getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDateTime birthdate) {
        this.birthdate = birthdate;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Set<BankAccountEntity> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<BankAccountEntity> accounts) {
        this.accounts = accounts;
    }

    public String getMetadatas() {
        return metadatas;
    }

    public void setMetadatas(String metadatas) {
        this.metadatas = metadatas;
    }

    @Override
    public int hashCode() {
        return Objects.hash(accounts, address, birthdate, city, email, firstName, gender, id, lastName, maritalStatus,
                metadatas, country, phoneNumber);
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CustomerEntity other = (CustomerEntity) obj;
        return Objects.equals(accounts, other.accounts) && Objects.equals(address, other.address)
                && Objects.equals(birthdate, other.birthdate) && Objects.equals(city, other.city)
                && Objects.equals(email, other.email) && Objects.equals(firstName, other.firstName)
                && gender == other.gender && Objects.equals(id, other.id) && Objects.equals(lastName, other.lastName)
                && maritalStatus == other.maritalStatus && Objects.equals(metadatas, other.metadatas)
                && Objects.equals(country, other.country) && Objects.equals(phoneNumber, other.phoneNumber);
    }
}
