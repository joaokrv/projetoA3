import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Produtos {

    // Atributos privados
    private int quantity;
    private int id;
    private String name, desc;
    private double costPrice, sellPrice;
    private Set<Integer> produtosSet = new HashSet<>(); // Conjunto de IDs únicos


    // Construtor com validações e exceções personalizadas
    public Produtos(String name, int id, int quantity,  String desc, double costPrice, double sellPrice, double valorEstoque, double valorLucro) throws InvalidProductException {
        validateName(name);
        validateQuantity(quantity);
        validateId(id);
        validateCostPrice(costPrice);
        validateSellPrice(sellPrice);

        this.name = name;
        this.quantity = quantity;
        this.id = id;
        this.costPrice = costPrice;
        this.sellPrice = sellPrice;
        this.desc = desc;
    }

    //Retorna os valores de todos os atributos do produto como uma lista.
    public List<Object> getValores() {
        List<Object> valores = new ArrayList<>();
        valores.add(id);
        valores.add(name);
        valores.add(quantity);
        valores.add(desc);
        valores.add(costPrice);
        valores.add(sellPrice);
        return valores;
    }

    public void setValores(String nome, int id, int quantidade, String descricao, double precoCusto, double precoVenda) {
        this.id = id;
        this.name = nome;
        this.quantity = quantidade;
        this.desc = descricao;
        this.costPrice = precoCusto;
        this.sellPrice = precoVenda;
    }

    // Métodos privados de validação com exceções
    private void validateName(String name) throws InvalidProductException {
        if (name == null || name.isBlank()) {
            throw new InvalidProductException("Nome não pode ser nulo ou vazio.");
        }
    }

    private void validateQuantity(int quantity) throws InvalidProductException {
        if (quantity < 0) {
            throw new InvalidProductException("Quantidade não pode ser negativa.");
        }
    }

    private void validateId(int id) throws InvalidProductException {
        if (id < 0) {
            throw new InvalidProductException("ID não pode ser negativo.");
        }
    
        if (produtosSet.contains(id)) {
            throw new InvalidProductException("ID já existe em uso. Escolha outro ID.");
        }
    }

    private void validateCostPrice(double costPrice) throws InvalidProductException {
        if (costPrice < 0) {
            throw new InvalidProductException("Preço de custo não pode ser negativo.");
        }
    }

    private void validateSellPrice(double sellPrice) throws InvalidProductException {
        if (sellPrice < 0) {
            throw new InvalidProductException("Preço de venda não pode ser negativo.");
        }
        if (sellPrice < costPrice) {
            throw new InvalidProductException("Preço de venda não pode ser menor que o preço de custo.");
        }
    }

    // Gets e Sets com validação e tratamento de erros
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) {
            System.err.println("Error: Quantidade não pode ser negativa. Colocando quantidade em 0.");
            this.quantity = 0;
        } else {
            this.quantity = quantity;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id < 0) {
            System.err.println("Error: ID não pode ser negativo. Colocando o ID em 0.");
            this.id = 0;
        } else {
            this.id = id;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            System.err.println("Error: Nome não pode ser nulo ou vazio.");
        } else {
            this.name = name;
        }
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(double costPrice) {
        if (costPrice < 0) {
            System.err.println("Error: Preço de custo não pode ser negativo. Colocando o valor de custo = 0.");
            this.costPrice = 0;
        } else {
            this.costPrice = costPrice;
        }
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(double sellPrice) {
        if (sellPrice < 0) {
            System.err.println("Error: Valor de venda não pode ser negativo. Colocando valor de venda = 0");
            this.sellPrice = 0;
        } else if (sellPrice < costPrice) {
            System.err.println("Error: Valor de venda não pode ser menor que o de custo. Colocando o valor de venda igual ao de custo.");
            sellPrice = costPrice;
        }
        this.sellPrice = sellPrice;
    }
}
