package com.joseleonardo.lojavirtual.controller;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.validation.Valid;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.joseleonardo.lojavirtual.exception.LojaVirtualException;
import com.joseleonardo.lojavirtual.model.Produto;
import com.joseleonardo.lojavirtual.repository.ProdutoRepository;
import com.joseleonardo.lojavirtual.service.EnvioEmailService;

@RestController
public class ProdutoController {

	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private EnvioEmailService envioEmailService;
	
	@ResponseBody
	@PostMapping(value = "**/salvarProduto")
	public ResponseEntity<Produto> salvarProduto(@RequestBody @Valid Produto produto) 
			throws LojaVirtualException, MessagingException, IOException {
		
		if (produto.getTipoUnidade() == null || produto.getTipoUnidade().trim().isEmpty()) {
			throw new LojaVirtualException("O tipo da unidade do produto deve ser informado");
		}
		
		if (produto.getNome().length() < 10) {
			throw new LojaVirtualException("O nome do produto deve ter mais de 9 letras");
		}
		
		if (produto.getEmpresa() == null || produto.getEmpresa().getId() <= 0) {
			throw new LojaVirtualException("A empresa do produto deve ser informada");
		}
		
		if (produto.getId() == null) {
			boolean existeProdutoComMesmoNomeNaEmpresa = produtoRepository
					.existeProdutoComMesmoNomeNaEmpresaId(produto.getNome().trim().toUpperCase(), 
							produto.getEmpresa().getId());
		
			if (existeProdutoComMesmoNomeNaEmpresa) {
				throw new LojaVirtualException("Não foi possível cadastrar, pois "
						+ "já existe um produto com o nome: " + produto.getNome());
			}
		}
		
		if (produto.getCategoriaProduto() == null || produto.getEmpresa().getId() <= 0) {
			throw new LojaVirtualException("A categoria do produto deve ser informada");
		}
		
		if (produto.getMarcaProduto() == null || produto.getMarcaProduto().getId() <= 0) {
			throw new LojaVirtualException("A marca do produto deve ser informada");
		}
		
		if (produto.getQuantidadeEstoque() < 1) {
			throw new LojaVirtualException("A quantidade de estoque do produto deve ser "
					+ "maior que 0");
		}
		
		if (produto.getImagensProduto() == null || produto.getImagensProduto().isEmpty() 
				|| produto.getImagensProduto().size() == 0) {
			throw new LojaVirtualException("As imagens do produto devem ser informadas");
		}
		
		if (produto.getImagensProduto().size() < 3) {
			throw new LojaVirtualException("As imagens do produto devem ser no mínimo 3");
		}
		
		if (produto.getImagensProduto().size() > 6) {
			throw new LojaVirtualException("As imagens do produto devem ser no máximo 6");
		}
		
		/* Processa as imagens */
		if (produto.getId() == null) {
			for (int i = 0; i < produto.getImagensProduto().size(); i++) {
				produto.getImagensProduto().get(i).setProduto(produto);
				produto.getImagensProduto().get(i).setEmpresa(produto.getEmpresa());
				
				String base64Image = "";
				
				if (produto.getImagensProduto().get(i).getImagemOriginal().contains("data:image")) {
					base64Image = produto.getImagensProduto()
							.get(i).getImagemOriginal().split(",")[1];
				} else {
					base64Image = produto.getImagensProduto().get(i).getImagemOriginal();
				}
				
				byte[] imagemBytes = DatatypeConverter.parseBase64Binary(base64Image);
				
				BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imagemBytes));
				
				if (bufferedImage != null) {
					/* Tipo da imagem PNG, JPG, etc...*/
					int type = bufferedImage.getType() == 0 
							? BufferedImage.TYPE_INT_ARGB : bufferedImage.getType(); 
					
					int largura = Integer.parseInt("800");
					int altura = Integer.parseInt("600");
					
					BufferedImage imagemRedimensionada = new BufferedImage(largura, altura, type);
					
					Graphics2D graphics2d = imagemRedimensionada.createGraphics();
					graphics2d.drawImage(bufferedImage, 0, 0, largura, altura, null);
					graphics2d.dispose();
					
					ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
					ImageIO.write(imagemRedimensionada, "png", byteArrayOutputStream);
					
					String imagemMiniaturaBase64 = "data:image/png;base64," 
					        + DatatypeConverter.printBase64Binary(
					        		byteArrayOutputStream.toByteArray());
					
					produto.getImagensProduto().get(i).setImagemMiniatura(imagemMiniaturaBase64);
					
					bufferedImage.flush();
					imagemRedimensionada.flush();
					byteArrayOutputStream.flush();
					byteArrayOutputStream.close();
				}
			}
		}
		
		Produto produtoSalvo = produtoRepository.save(produto);
		
		if (produto.getAlertaQuantidadeEstoque() && produto.getQuantidadeEstoque() <= 1) {
			StringBuilder html = new StringBuilder();
			html.append("<h2>")
			    .append("Produto: " + produto.getNome())
			    .append(" com estoque baixo: " + produto.getQuantidadeEstoque())
			    .append("</h2>")
			    .append("<p> Id do produto: " + produto.getId())
			    .append("</p>");
			
			if (produto.getEmpresa().getEmail() != null) {
				envioEmailService.enviarEmailHtml("Produto sem estoque", html.toString(), 
						produto.getEmpresa().getEmail());
			}
		}
		
		return new ResponseEntity<Produto>(produtoSalvo, HttpStatus.CREATED);
	}
	
	@ResponseBody
	@DeleteMapping(value = "**/deletarProdutoPorId/{id}")
	public ResponseEntity<?> deletarProdutoPorId(@PathVariable("id") Long id) {
		if (!produtoRepository.findById(id).isPresent()) {
			return new ResponseEntity<>("O produto de código "
					+ id + " já foi removido ou não existe", HttpStatus.OK);
		}
		
		produtoRepository.deleteById(id);
		
		return new ResponseEntity<>("Produto removido", HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/buscarProdutoPorId/{id}")
	public ResponseEntity<Produto> obterProdutoPorId(@PathVariable("id") Long id) 
			throws LojaVirtualException {
		Produto produto = produtoRepository.findById(id).orElse(null);
		
		if (produto == null) {
			throw new LojaVirtualException("Não econtrou nenhum produto com o código: " + id);
		}
		
		return new ResponseEntity<Produto>(produto, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/buscarProdutoPorNome/{nome}")
	public ResponseEntity<List<Produto>> buscarProdutoPorNome(@PathVariable("nome") String nome) {
		List<Produto> produtos = produtoRepository.buscarProdutoPorNome(nome.trim().toUpperCase());
		
		return new ResponseEntity<List<Produto>>(produtos, HttpStatus.OK);
	}
	
}
