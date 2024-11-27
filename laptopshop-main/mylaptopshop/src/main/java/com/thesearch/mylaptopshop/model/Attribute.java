package com.thesearch.mylaptopshop.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Table(name = "attribute")
@Entity
@Getter
@Setter
public class Attribute {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "attribute",cascade = CascadeType.ALL)
    private Set<ProductAttribute> productAttributes = new HashSet<>();
    public Attribute(String name){
        this.name = name;
    }

}
