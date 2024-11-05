package com.udemy.projeto.model.dao;

import com.udemy.projeto.model.entities.Department;

import java.util.List;

public interface DepartmentDAO {

    void insert(Department department);

    void update(Department department);

    void deleteById(int id);

    Department findById(int id);

    List<Department> findAll();
    
}
