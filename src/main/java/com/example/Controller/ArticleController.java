package com.example.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.Domain.Article;
import com.example.Repository.ArticleRepository;

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

	/**
	 * 掲示板画面に遷移．
	 * 
	 * @param model モデル
	 * @return 掲示板画面
	 */
	@RequestMapping("")
	public String index(Model model) {

		List<Article> articleList = articleRepository.findAll();
		model.addAttribute("articleList", articleList);

		return "article";
	}
	
	@RequestMapping("/insert")
	public String insert(Model model,String name,String content) {

		articleRepository.Insert(new Article(name, content));
		
		return index(model);
	}

}
