package com.example.Repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.Domain.Article;

/**
 * 記事を操作するリポジトリ．
 * 
 * @author yuiko.mitsui
 *
 */
@Repository
public class ArticleRepository {
	@Autowired
	private NamedParameterJdbcTemplate template;

	private static final RowMapper<Article> ARTICLE_ROW_MAPPER = (rs, i) -> {
		Article article = new Article(rs.getInt("id"), rs.getString("name"), rs.getString("content"));

		return article;
	};

	/**
	 * 全記事情報を取得する．
	 * 
	 * @return 全記事情報
	 */
	public List<Article> findAll() {

		String sql = "SELECT id,name,content FROM articles ORDER BY id DESC;";

		List<Article> articleList = template.query(sql, ARTICLE_ROW_MAPPER);

		return articleList;
	}

	/**
	 * 記事情報を挿入する．
	 * 
	 * @param article 記事情報
	 */
	public void Insert(Article article) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(article);

		String sql = "INSERT INTO articles(name,content)" + " VALUES (:name,:content);";

		template.update(sql, param);
	}

}
