package com.example.ACsecurity.Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.example.ACsecurity.model.Category;
import com.example.ACsecurity.model.CategoryRepository;
import com.example.ACsecurity.model.Product;
import com.example.ACsecurity.model.ProductRepository;
@Controller
@RequestMapping("/admin/products")
public class AdminProductController {
     
	@Autowired
	private ProductRepository productRepo;
	@Autowired
	private CategoryRepository categoryRepo;
	
	 @GetMapping
	    public String index(Model model, @RequestParam(value="page", required = false) Integer p) {

	        int perPage = 6;
	        int page = (p != null) ? p : 0;
	        Pageable pageable = PageRequest.of(page, perPage);
	        
	        Page<Product> products = productRepo.findAll(pageable);
	        List<Category> categories = categoryRepo.findAll();

	        HashMap<Integer, String> memberships = new HashMap<>();
	        for (Category membership : categories) {
	            memberships.put(membership.getId(), membership.getName());
	        }

	        model.addAttribute("products", products);
	        model.addAttribute("cats", memberships);

	        long count = productRepo.count();
	        double pageCount = Math.ceil((double)count / (double)perPage);

	        model.addAttribute("pageCount", (int)pageCount);
	        model.addAttribute("perPage", perPage);
	        model.addAttribute("count", count);
	        model.addAttribute("page", page);

	        return "admin/products/index";
	    }
	
	@GetMapping("/add") 
	public String add(Product product,Model model) {
		
		List<Category> categories = categoryRepo.findAll();
		model.addAttribute("categories", categories);
		
		
		
		return "admin/products/add";
	}
	
	@PostMapping("/add")
	public String add (@Valid Product product, BindingResult bindingResult,
			MultipartFile file, RedirectAttributes redirectAttributes,
			Model model) throws IOException {
		
		List<Category> categories = categoryRepo.findAll();
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("categories",categories);
			return "admin/products/add";
		}
		// uploading image and checking if its jpg or png
		boolean fileOk = false;
		byte[] bytes = file.getBytes();
		String filename = file.getOriginalFilename();
		Path path = Paths.get("src/main/resources/static/" + filename);
		if(filename.endsWith("jpg")|| filename.endsWith("png")) {
			
			fileOk = true;
		}
		
		redirectAttributes.addFlashAttribute("message","Product added");
		redirectAttributes.addFlashAttribute("alertClass","alert-success");
		
		String slug = product.getName().toLowerCase().replace(" ","-");
		
		Product productExists = productRepo.findBySlug(slug);
		
		if(!fileOk) {
			
			redirectAttributes.addFlashAttribute("message","Image must be a jpg or a png");
			redirectAttributes.addFlashAttribute("alertClass","alert-danger");
			redirectAttributes.addFlashAttribute("product",product);
			
		}
		
		else if (productExists != null) {
			redirectAttributes.addFlashAttribute("message","Product exists, choose another");
			redirectAttributes.addFlashAttribute("alertClass","alert-danger");
			redirectAttributes.addFlashAttribute("product",product);
			
			
		} else {
			product.setSlug(slug);
			product.setImage(filename);
			productRepo.save(product);
			Files.write(path, bytes);
		}
		
		return "redirect:/admin/products/add";
	}
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable int id , Model model) {
		
		Product product = productRepo.getOne(id);
		
		List<Category> categories = categoryRepo.findAll();
		
		
		model.addAttribute("product",product);
		model.addAttribute("categories",categories);
		
		return "admin/products/edit";
	}
	
	@PostMapping("/edit")
	public String edit (@Valid Product product, BindingResult bindingResult,
			MultipartFile file, RedirectAttributes redirectAttributes,
			Model model) throws IOException {
		
		Product currentProduct = productRepo.getOne(product.getId());
		
		
		List<Category> categories = categoryRepo.findAll();
		
		if(bindingResult.hasErrors()) {
			
			model.addAttribute("productName",currentProduct.getName());
			model.addAttribute("categories",categories);
			
			return "admin/products/edit";
		}
		// uploading image and checking if its jpg or png
		boolean fileOk = false;
		byte[] bytes = file.getBytes();
		String filename = file.getOriginalFilename();
		Path path = Paths.get("src/main/resources/static/" + filename);
		
		if(!file.isEmpty()) {
		if(filename.endsWith("jpg")|| filename.endsWith("png")) {
			
			fileOk = true;
		}
		}else {
			
			fileOk = true;
		}
		
		redirectAttributes.addFlashAttribute("message","Product edited");
		redirectAttributes.addFlashAttribute("alertClass","alert-success");
		
		String slug = product.getName().toLowerCase().replace(" ","-");
		
		Product productExists = productRepo.findBySlugAndIdNot(slug, product.getId());
		
		if(!fileOk) {
			
			redirectAttributes.addFlashAttribute("message","Image must be a jpg or a png");
			redirectAttributes.addFlashAttribute("alertClass","alert-danger");
			redirectAttributes.addFlashAttribute("product",product);
			
		}
		
		else if (productExists != null) {
			redirectAttributes.addFlashAttribute("message","Product exists, choose another");
			redirectAttributes.addFlashAttribute("alertClass","alert-danger");
			redirectAttributes.addFlashAttribute("product",product);
			
			
		} else {
			
			
			product.setSlug(slug);
			
			if(!file.isEmpty()) {
				Path path2 = Paths.get("src/main/resources/static/" + currentProduct.getImage());
			    Files.delete(path2);
			    product.setImage(filename);
			    Files.write(path, bytes);
			}else {
				product.setImage(currentProduct.getImage());
			}
			
			
			
			productRepo.save(product);
			
		}
		
		return "redirect:/admin/products/edit/" + product.getId();
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable int id , RedirectAttributes redirectAttributes)throws IOException {
		
		Product product = productRepo.getOne(id);
		
		Product currentProduct = productRepo.getOne(product.getId());
		
		Path path2 = Paths.get("src/main/resources/static/" + currentProduct.getImage());
	   
		Files.delete(path2);
		productRepo.deleteById(id);
		redirectAttributes.addFlashAttribute("message","product deleted");
		redirectAttributes.addFlashAttribute("alertClass","alert-succes");	
		
		return "redirect:/admin/products";
	}
}