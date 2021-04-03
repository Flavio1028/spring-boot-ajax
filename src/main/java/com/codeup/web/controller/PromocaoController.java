package com.codeup.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.codeup.domain.Categoria;
import com.codeup.repository.CategoriaRepository;

@Controller
@RequestMapping("/promocao")
public class PromocaoController {

	@Autowired
	private CategoriaRepository categoriaRepository;

	@ModelAttribute("categorias")
	public List<Categoria> getCategoria() {
		return this.categoriaRepository.findAll();
	}

	@GetMapping("/add")
	public String abrirCadastro() {
		return "promo-add";
	}

}