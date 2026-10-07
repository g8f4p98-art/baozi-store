package br.com.baozistore.controller;

import br.com.baozistore.model.Produto;
import br.com.baozistore.repository.ProdutoRepository;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoRepository produtoRepository;

    public ProdutoController(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @PostMapping
    public Produto criar(@RequestBody Produto produto) {
        return produtoRepository.save(produto);
    }

    @GetMapping
    public List<Produto> listar() {
        return produtoRepository.findAll();
    }

    @GetMapping("/{id}")
    public Produto buscarPorId(@PathVariable Long id) {
        return produtoRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Produto atualizar(
            @PathVariable Long id,
            @RequestBody Produto produto) {

        Produto produtoExistente = produtoRepository
                .findById(id)
                .orElse(null);

        if (produtoExistente == null) {
            return null;
        }

        produtoExistente.setNome(produto.getNome());
        produtoExistente.setPreco(produto.getPreco());
        produtoExistente.setQuantidadeEstoque(
                produto.getQuantidadeEstoque()
        );

        return produtoRepository.save(produtoExistente);
    }

    @DeleteMapping("/{id}")
    public void apagar(@PathVariable Long id) {
        produtoRepository.deleteById(id);
    }
}