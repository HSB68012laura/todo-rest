package com.dwes.todo_rest.service;

import com.dwes.todo_rest.model.Tag;
import com.dwes.todo_rest.repos.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    public List<Tag> findAll() {
        return tagRepository.findAll();
    }

    public Tag findById(Long id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag no encontrado con id: " + id));
    }

    public Tag save(Tag tag) {
        return tagRepository.save(tag);
    }

    public Tag update(Long id, Tag tag) {
        Tag existing = findById(id);
        existing.setName(tag.getName());
        return tagRepository.save(existing);
    }

    public void deleteById(Long id) {
        tagRepository.deleteById(id);
    }
}
