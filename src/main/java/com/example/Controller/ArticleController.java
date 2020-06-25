package com.example.Controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.Domain.Article;
import com.example.Domain.Comment;
import com.example.Form.ArticleForm;
import com.example.Form.CommentForm;
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

	@Autowired
	public CommentForm setupCommentForm() {
		return new CommentForm();
	}

	@Autowired
	public ArticleForm setupArticleForm() {
		return new ArticleForm();
	}

	/**
	 * 掲示板画面に遷移．
	 * 
	 * @param model モデル
	 * @return 掲示板画面
	 */
	@RequestMapping("")
	public String index(Model model) {

		List<Article> articleList = articleRepository.findAll();

		for (Article article : articleList) {
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
	@RequestMapping("/insertArticle")
	public String insertArticle(ArticleForm form) {
		Article article = new Article();
		BeanUtils.copyProperties(form, article);
		
		articleRepository.Insert(article);

		
		return "redirect:/";
	}

	/**
	 * コメントを投稿する．
	 * 
	 * @param form コメント投稿フォーム
	 * @return 掲示板画面
	 */
	@RequestMapping("/insertComment")
	public String insertComment(CommentForm form) {
		Comment comment = new Comment();
		BeanUtils.copyProperties(form, comment);
		
		comment.setArticleId(Integer.parseInt(form.getArticleId()));

		commentRepository.insert(comment);

		return "redirect:/";
	}
	
	@RequestMapping("/deleteArticle")
	public String deleteArticle(Integer id) {
		//記事ID
		Integer articleId = id;
		
		//コメントを削除します
		commentRepository.deleteById(articleId);
		
		return "redirect:/";
	}

}
