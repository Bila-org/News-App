package com.example.newsapp.data.mapper

import com.example.newsapp.data.dto.Article
import com.example.newsapp.data.local.EntityArticle

object ArticleMapper {
    fun toEntity(article: Article): EntityArticle {
        return EntityArticle(
            id = 0,
            author = article.author,
            content = article.content,
            description = article.description,
            publishedAt = article.publishedAt,
            source = article.source,
            title = article.title,
            url = article.url,
            urlToImage = article.urlToImage,
            isBookmarked = false
        )
    }

    fun toArticle(entityArticle: EntityArticle): Article {
        return Article(
            author = entityArticle.author,
            content = entityArticle.content,
            description = entityArticle.description,
            publishedAt = entityArticle.publishedAt,
            source = entityArticle.source,
            title = entityArticle.title,
            url = entityArticle.url,
            urlToImage = entityArticle.urlToImage
        )
    }

    fun toEntityList(articles: List<Article>): List<EntityArticle> {
        return  articles.map { toEntity(it) }
    }

    fun toArticleList(entityArticles: List<EntityArticle>): List<Article> {
        return entityArticles.map { toArticle(it) }
    }
}