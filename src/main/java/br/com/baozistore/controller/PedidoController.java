package br.com.baozistore.controller;

import br.com.baozistore.model.ErroResponse;
import br.com.baozistore.model.Pedido;
import br.com.baozistore.model.Produto;
import br.com.baozistore.repository.ClienteRepository;
import br.com.baozistore.repository.PedidoRepository;
import br.com.baozistore.repository.ProdutoRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;
    private final ClienteRepository clienteRepository;

    public PedidoController(
            PedidoRepository pedidoRepository,
            ProdutoRepository produtoRepository,
            ClienteRepository clienteRepository) {

        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
        this.clienteRepository = clienteRepository;
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Pedido pedido) {

        // Verifica se a quantidade do pedido é válida
        if (pedido.getQuantidade() == null || pedido.getQuantidade() <= 0) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErroResponse(
                            "Quantidade deve ser maior que zero"
                    ));
        }

        // Verifica se o cliente existe
        var cliente = clienteRepository
                .findById(pedido.getClienteId())
                .orElse(null);

        if (cliente == null) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErroResponse(
                            "Cliente não encontrado"
                    ));
        }

        // Verifica se o produto existe
        Produto produto = produtoRepository
                .findById(pedido.getProdutoId())
                .orElse(null);

        if (produto == null) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErroResponse(
                            "Produto não encontrado"
                    ));
        }

        // Verifica se existe estoque suficiente
        if (produto.getQuantidadeEstoque() < pedido.getQuantidade()) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErroResponse(
                            "Estoque insuficiente"
                    ));
        }

        // Diminui o estoque pela quantidade comprada
        produto.setQuantidadeEstoque(
                produto.getQuantidadeEstoque() - pedido.getQuantidade()
        );

        // Salva o estoque atualizado
        produtoRepository.save(produto);

        // Salva o pedido
        return ResponseEntity.ok(
                pedidoRepository.save(pedido)
        );
    }

    @GetMapping
    public List<Pedido> listar() {
        return pedidoRepository.findAll();
    }

    @GetMapping("/{id}")
    public Pedido buscarPorId(@PathVariable Long id) {
        return pedidoRepository.findById(id).orElse(null);
    }

    @DeleteMapping("/{id}")
    public void apagar(@PathVariable Long id) {
        pedidoRepository.deleteById(id);
    }
}