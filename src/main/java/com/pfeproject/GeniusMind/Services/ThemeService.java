package com.pfeproject.GeniusMind.Services;

import com.pfeproject.GeniusMind.Entity.Level;
import com.pfeproject.GeniusMind.Entity.Theme;
import com.pfeproject.GeniusMind.Repository.ThemeRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ThemeService {

    @Autowired
    private ThemeRepository repository ;

    public Theme addTheme(Theme theme) {
        return repository.save(theme);
    }
    public List<Theme> getThemes() {
        List<Theme> themes = repository.findAll();
        return themes;
    }

    public Theme getTheme(Long idTheme) {
        return repository.findById(idTheme).get();
    }

    public void deleteTheme(Long idTheme) {
         repository.deleteById(idTheme);
    }

    public Theme updateTheme(Theme theme) {
        return repository.save(theme);
    }
}
