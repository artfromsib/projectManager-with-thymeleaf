package com.ym.projectManager.conf;

import com.ym.projectManager.model.ItemSection;
import com.ym.projectManager.repository.SectionRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class Config {

    @Bean
    List<ItemSection> itemSections(SectionRepository sectionRepository) {
        return sectionRepository.findAll();
    }
}
