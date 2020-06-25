package com.example.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.Domain.Article;
import com.example.Domain.Comment;

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
	 * 記事とコメント情報を取得する．
	 * 
	 * @return コメント情報を含む記事情報
	 */
	public List<Article> findAllArticleAndComment() {
		String sql = "SELECT articles.id as id , articles.name as name , articles.content as content , comments.id as com_id , comments.content as com_content , comments.name as com_name , article_id\r\n"
				+ "FROM articles \r\n" + "LEFT OUTER JOIN comments \r\n" + "ON articles.id = comments.article_id\r\n"
				+ "ORDER BY  id DESC , com_id DESC;";

		List<Article> articleList = template.query(sql, new ResultSetExtractor<List<Article>>() {

			public List<Article> extractData(ResultSet rs) throws SQLException, DataAccessException {

				List<Article> list = new ArrayList<>();

				while (rs.next()) {
					Article article = new Article();
					// article詰め替え
					article.setId(rs.getInt("id"));
					article.setName(rs.getString("name"));
					article.setContent(rs.getString("content"));

					// コメント詰め替え
					Comment comment = new Comment();
					comment.setId(rs.getInt("com_id"));
					comment.setName(rs.getString("com_name"));
					comment.setContent(rs.getString("com_content"));
					comment.setArticleId(rs.getInt("article_id"));

					boolean isExist = false;
					int existNum = -1;

					for (int i = 0; i < list.size(); i++) {
						if (list.get(i).getId() == article.getId()) {
							isExist = true;
							existNum = i;
						}
					}

					if (isExist) {// 既出記事

						if (comment.getId() != null) {// コメント有
							System.out.println("既出・有ーーーーーー");
							System.out.println("existId:"+existNum);
							list.get(existNum).getCommentList().add(comment);
							System.out.println("既出・有(終)ーーーー");
						}

					} else {// 新規記事
						System.out.println("新規ーーーー");
						article.setCommentList(new ArrayList<Comment>());

						if (comment.getId() != null) {// コメント有
							System.out.println("新規・有ーーーー");
							article.getCommentList().add(comment);
							System.out.println("新規・有（終）ーーーー");
						}
						System.out.println("新規addーーーー");
						list.add(article);
					}
				}
				
				return list;
			}
		});

		return articleList;
	}

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
	 * 記事情報をDBに挿入する．
	 * 
	 * @param article 記事情報
	 */
	public void Insert(Article article) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(article);

		String sql = "INSERT INTO articles(name,content)" + " VALUES (:name,:content);";

		template.update(sql, param);
	}

	/**
	 * 記事をDBから削除する．
	 * 
	 * @param id 記事ID
	 */
	public void deleteById(Integer id) {
		String sql = "DELETE FROM articles WHERE id =:id";

		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		template.update(sql, param);

	}
}
