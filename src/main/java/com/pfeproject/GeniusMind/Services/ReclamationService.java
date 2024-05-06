package com.pfeproject.GeniusMind.Services;

import com.pfeproject.GeniusMind.Entity.reclamation;

import java.util.List;

public interface ReclamationService {

    reclamation AddReclamation(reclamation reclam);

    List<reclamation> GetAll();
}
