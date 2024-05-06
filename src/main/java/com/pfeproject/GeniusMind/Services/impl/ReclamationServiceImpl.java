package com.pfeproject.GeniusMind.Services.impl;

import com.pfeproject.GeniusMind.Entity.reclamation;
import com.pfeproject.GeniusMind.Repository.ReclamationRepository;
import com.pfeproject.GeniusMind.Services.ReclamationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReclamationServiceImpl implements ReclamationService {

    @Autowired
    private ReclamationRepository reclamationRepository;
    @Override
    public reclamation AddReclamation(reclamation reclam) {
        return reclamationRepository.save(reclam);
    }

    @Override
    public List<reclamation> GetAll() {
        return reclamationRepository.findAll();
    }
}
