package com.thesearch.mylaptopshop.service.attribute;

import java.util.List;

import com.thesearch.mylaptopshop.model.Attribute;

public interface IAttributeService {
    Attribute addAttribute(Attribute attribute);
    Attribute getAttributeById(Long id);
    List<Attribute> getAllAttributes();
    void deleteAttribute(Long id);
    Attribute updateAttribute(Attribute attribute, Long id);
}
