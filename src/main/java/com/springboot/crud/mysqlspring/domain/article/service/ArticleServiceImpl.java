package com.springboot.crud.mysqlspring.domain.article.service;

import com.springboot.crud.mysqlspring.domain.article.dto.ArticleDto;
import com.springboot.crud.mysqlspring.domain.article.entity.ArticleEntity;
import com.springboot.crud.mysqlspring.domain.article.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService{

    @Autowired
    private final ArticleRepository articleRepository;

    @Transactional
    @Override
    public ArticleDto createArticle(ArticleDto article) {

        ArticleEntity articleEntity = ArticleEntity.builder()
                .title(article.getTitle())
                .description(article.getDescription())
                .author(article.getAuthor())
                .build();

        articleEntity = articleRepository.save(articleEntity);
        return convertEntityToDto(articleEntity, false, 0L);

    }

    @Override
    public ArticleDto getArticle(String title) {
        ArticleEntity found = articleRepository.findByTitle(title);
        return convertEntityToDto(found, false, 0L);
    }

    @Transactional
    @Override
    public void deleteArticle(String title) {
        ArticleEntity found = articleRepository.findByTitle(title);
        articleRepository.delete(found);
    }

    @Transactional
    @Override
    public ArticleDto updateArticle(String title, ArticleDto.Update article) {
        ArticleEntity found = articleRepository.findByTitle(title);

        if (article.getTitle() != null) {
            String newSlug = String.join("-", article.getTitle().split(" "));
            found.setTitle(article.getTitle());
            found.setTitle(newSlug);
        }

        if (article.getDescription() != null) {
            found.setDescription(article.getDescription());
        }

        if (article.getAuthor() != null) {
            found.setAuthor(article.getAuthor());
        }

        articleRepository.save(found);

        return getArticle(title);
    }

    private ArticleDto convertEntityToDto(ArticleEntity entity, Boolean favorited, Long favoritesCount) {
        return ArticleDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .author(entity.getAuthor())
                .build();
    }



}
