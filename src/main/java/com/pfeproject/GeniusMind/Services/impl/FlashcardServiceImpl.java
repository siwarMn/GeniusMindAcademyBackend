package com.pfeproject.GeniusMind.Services.impl;
import com.pfeproject.GeniusMind.Entity.Flashcard;
import com.pfeproject.GeniusMind.Repository.FlashcardRepository;
import com.pfeproject.GeniusMind.Services.FlashcardService;
import com.pfeproject.GeniusMind.dto.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FlashcardServiceImpl implements FlashcardService {

    private final FlashcardRepository repo;

    public FlashcardServiceImpl(FlashcardRepository repo) {
        this.repo = repo;
    }

    @Override
    public FlashcardResponse create(String ownerEmail, FlashcardCreateRequest req) {
        Flashcard fc = new Flashcard();
        fc.setOwnerEmail(ownerEmail);
        fc.setFront(req.getFront());
        fc.setBack(req.getBack());
        fc.setCategory(req.getCategory());
        fc.setMemorized(req.getMemorized() != null ? req.getMemorized() : false);

        Flashcard saved = repo.save(fc);
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FlashcardResponse> list(String ownerEmail, Pageable pageable) {
        return repo.findByOwnerEmail(ownerEmail, pageable).map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public FlashcardResponse get(String ownerEmail, Long id) {
        Flashcard fc = repo.findByIdAndOwnerEmail(id, ownerEmail)
                .orElseThrow(() -> new EntityNotFoundException("Flashcard not found"));
        return toResponse(fc);
    }

    @Override
    public FlashcardResponse update(String ownerEmail, Long id, FlashcardUpdateRequest req) {
        Flashcard fc = repo.findByIdAndOwnerEmail(id, ownerEmail)
                .orElseThrow(() -> new EntityNotFoundException("Flashcard not found"));

        fc.setFront(req.getFront());
        fc.setBack(req.getBack());
        fc.setCategory(req.getCategory());
        if (req.getMemorized() != null) fc.setMemorized(req.getMemorized());

        Flashcard saved = repo.save(fc);
        return toResponse(saved);
    }

    @Override
    public void delete(String ownerEmail, Long id) {
        // ensures user can only delete their own
        Flashcard fc = repo.findByIdAndOwnerEmail(id, ownerEmail)
                .orElseThrow(() -> new EntityNotFoundException("Flashcard not found"));
        repo.delete(fc);
    }

    private FlashcardResponse toResponse(Flashcard fc) {
        return new FlashcardResponse(
                fc.getId(),
                fc.getFront(),
                fc.getBack(),
                fc.getCategory(),
                fc.getMemorized(),
                fc.getCreatedAt(),
                fc.getUpdatedAt()
        );
    }
}
