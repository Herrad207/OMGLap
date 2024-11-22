package com.thesearch.mylaptopshop.service.attribute;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.thesearch.mylaptopshop.exception.AlreadyExistException;
import com.thesearch.mylaptopshop.exception.ResourceNotFoundException;
import com.thesearch.mylaptopshop.model.Attribute;
import com.thesearch.mylaptopshop.repository.AttributeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttributeService implements IAttributeService {
    
    private final AttributeRepository attributeRepository;

    @Override
    public Attribute addAttribute(Attribute attribute){
        return Optional.of(attribute).filter(att -> !attributeRepository.existsByName(att.getName()))
                .map(attributeRepository :: save).orElseThrow(()->new AlreadyExistException(attribute.getName()+"already exist!"));
    }

    @Override
    public Attribute getAttributeById(Long id){
        return attributeRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Cannot find attribute!"));
    }
    @Override
    public List<Attribute> getAllAttributes(){
        return attributeRepository.findAll();
    }
    @Override
    public void deleteAttribute(Long id){
        attributeRepository.findById(id)
            .ifPresentOrElse(attributeRepository::delete, ()->{
                throw new ResourceNotFoundException("Attribute not found!");
            });
    }
    @Override
    public Attribute updateAttribute(Attribute attribute, Long id){
        return Optional.ofNullable(getAttributeById(id)).map(oldAttribute->{
            oldAttribute.setName((attribute.getName()));
            return attributeRepository.save(oldAttribute);
        }).orElseThrow(()-> new ResourceNotFoundException("No Attribute Found!"));
    }
}
