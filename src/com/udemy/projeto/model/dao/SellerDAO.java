package com.udemy.projeto.model.dao;

import com.udemy.projeto.model.entities.Department;
import com.udemy.projeto.model.entities.Seller;

import java.util.List;

public interface SellerDAO {

    void insert(Seller seller);

    void update(Seller seller);

    void deleteById(int id);

    Seller findById(int id);

    List<Seller> findAll();

    List<Seller> findByDepartment(Department department);
    
}
