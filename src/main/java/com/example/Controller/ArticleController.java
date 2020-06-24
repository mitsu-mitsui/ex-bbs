package com.example.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.Domain.Article;
import com.example.Repository.ArticleRepository;

@Controller
public class ArticleController {
	@Autowired
	private ArticleRepository articleRepository;
	
	@RequestMapping("")
	public String index(Model model) {
		
		List<Article> articleList = articleRepository.findAll();
		model.addAttribute("articleList", articleList );
		
		return "article";
	}
	
	
	
}
