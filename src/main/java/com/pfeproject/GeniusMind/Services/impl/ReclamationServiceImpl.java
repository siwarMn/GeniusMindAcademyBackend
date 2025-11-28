package com.pfeproject.GeniusMind.Services.impl;
import com.pfeproject.GeniusMind.Entity.reclamation;
import com.pfeproject.GeniusMind.Exceptions.NotFoundException;
import com.pfeproject.GeniusMind.Exceptions.ReclamationNotFoundException;
import com.pfeproject.GeniusMind.Repository.ReclamationRepository;
import com.pfeproject.GeniusMind.Services.ReclamationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    @Override
    public reclamation getReclamById(Long id) {
        Optional<reclamation> optionalReclamation = reclamationRepository.findById(id);
        return optionalReclamation.orElseThrow(() -> new ReclamationNotFoundException("Réclamation non trouvée pour l'ID : " + id));
    }



    @Override
    public long GetCount() {
        return reclamationRepository.count() ;
    }
}
