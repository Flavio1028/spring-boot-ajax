package com.codeup.web.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.codeup.domain.Categoria;
import com.codeup.domain.Promocao;
import com.codeup.repository.CategoriaRepository;
import com.codeup.repository.PromocaoRepository;
import com.codeup.service.PromocaoDataTablesService;

@Controller
@RequestMapping("/promocao")
public class PromocaoController {

	private static Logger log = LoggerFactory.getLogger(PromocaoController.class);

	@Autowired
	private PromocaoRepository promocaoRepository;

	@Autowired
	private CategoriaRepository categoriaRepository;

	@GetMapping("/tabela")
	public String showTabela() {
		return "promo-datatables";
	}

	@GetMapping("/datatables/server")
	public ResponseEntity<?> datatables(HttpServletRequest request) {
		Map<String, Object> data = new PromocaoDataTablesService().execute(promocaoRepository, request);
		return ResponseEntity.ok(data);
	}

	@GetMapping("/site/list")
	public String listarPorSite(@RequestParam("site") String site, ModelMap model) {
		Sort sort = Sort.by(Sort.Direction.DESC, "dtCadastro");
		PageRequest pageRequest = PageRequest.of(0, 8, sort);
		model.addAttribute("promocoes", this.promocaoRepository.findBySite(site, pageRequest));
		return "promo-card";
	}

	@GetMapping("/site")
	public ResponseEntity<?> autocompleteByTermo(@RequestParam("termo") String termo) {
		List<String> sites = this.promocaoRepository.findSitesByTermo(termo);
		return ResponseEntity.ok(sites);
	}

	@PostMapping("/like/{id}")
	public ResponseEntity<?> adicionarLikes(@PathVariable("id") Long id) {
		this.promocaoRepository.updateSomarLikes(id);
		int likes = this.promocaoRepository.findLikesById(id);
		return ResponseEntity.ok(likes);
	}

	@GetMapping("/list")
	public String listarOfertas(ModelMap model) {
		Sort sort = Sort.by(Sort.Direction.DESC, "dtCadastro");
		PageRequest pageRequest = PageRequest.of(0, 8, sort);
		model.addAttribute("promocoes", this.promocaoRepository.findAll(pageRequest));
		return "promo-list";
	}

	@GetMapping("/list/ajax")
	public String listarCards(ModelMap model, @RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "site", defaultValue = "") String site) {
		Sort sort = Sort.by(Sort.Direction.DESC, "dtCadastro");
		PageRequest pageRequest = PageRequest.of(page, 8, sort);

		if (site.isEmpty()) {
			model.addAttribute("promocoes", this.promocaoRepository.findAll(pageRequest));
		} else {
			model.addAttribute("promocoes", this.promocaoRepository.findBySite(site, pageRequest));
		}

		return "promo-card";
	}

	@PostMapping("/save")
	public ResponseEntity<?> salvarPromocao(@Valid Promocao promocao, BindingResult result) {

		if (result.hasErrors()) {
			Map<String, String> errors = new HashMap<>();
			for (FieldError error : result.getFieldErrors()) {
				errors.put(error.getField(), error.getDefaultMessage());
			}

			return ResponseEntity.unprocessableEntity().body(errors);
		}

		log.info("Promocao {}", promocao.toString());
		promocao.setDtCadastro(LocalDateTime.now());
		promocaoRepository.save(promocao);
		return ResponseEntity.ok().build();

	}

	@ModelAttribute("categorias")
	public List<Categoria> getCategoria() {
		return this.categoriaRepository.findAll();
	}

	@GetMapping("/add")
	public String abrirCadastro() {
		return "promo-add";
	}

}