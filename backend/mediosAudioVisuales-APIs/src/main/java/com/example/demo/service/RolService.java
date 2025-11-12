package com.example.demo.service;

import com.example.demo.dto.Rol;
import com.example.demo.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolService {

    @Autowired
    private RolRepository rolRepository;

    
 
    public List<Rol> listarTodosLosRoles() {
        return rolRepository.listarTodosLosRoles();
    }
    
 
    public Rol buscarRolPorId(Long id) {
        return rolRepository.buscarRolPorId(id);
    }

  

}