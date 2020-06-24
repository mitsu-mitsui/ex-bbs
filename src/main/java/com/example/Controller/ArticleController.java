package com.example.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.Domain.Article;
import com.example.Repository.ArticleRepository;
import com.example.Repository.CommentRepository;

/**
 * 記事情報を操作するコントローラ．
 * 
 * @author yuiko.mitsui
 *
 */
@Controller
public class ArticleController {
	@Autowired
	private ArticleRepository articleRepository;
	@Autowired
	private CommentRepository commentRepository;

	/**
	 * 掲示板画面に遷移．
	 * 
	 * @param model モデル
	 * @return 掲示板画面
	 */
	@RequestMapping("")
	public String index(Model model) {

		List<Article> articleList = articleRepository.findAll();
		
		for(Article article:articleList) {
			int nowArticleId = article.getId();
			article.setCommentList(commentRepository.findByArticleId(nowArticleId));
		}
		
		model.addAttribute("articleList", articleList);

		return "article";
	}

	/**
	 * 記事を投稿する．
	 * 
	 * @param model   モデル
	 * @param name    投稿者名
	 * @param content 投稿内容
	 * @return 掲示板画面
	 */
	@RequestMapping("/insert")
	public String insert(Model model, String name, String content) {

		articleRepository.Insert(new Article(name, content));

		return index(model);
	}

}
