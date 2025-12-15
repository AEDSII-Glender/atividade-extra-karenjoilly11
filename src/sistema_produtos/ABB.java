package sistema_produtos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.TreeMap;
import java.util.function.Function;

public class ABB<K, V> implements IMapeamento<K, V> {

	private No<K, V> raiz; 
	private Comparator<K> comparador; 
	private int tamanho;
	private long comparacoes;
	private long inicio;
	private long termino;

	/**
	 * Método auxiliar para inicialização da árvore binária de busca.
	 * 
	 * Este método define a raiz da árvore como {@code null} e seu tamanho como 0.
	 * Utiliza o comparador fornecido para definir a organização dos elementos na
	 * árvore.
	 * 
	 * @param comparador o comparador para organizar os elementos da árvore.
	 */
	private void init(Comparator<K> comparador) {
		raiz = null;
		tamanho = 0;
		this.comparador = comparador;
	}

	
public Map<K, List<V>> agruparPorValor() {
    Map<K, List<V>> grupos = new TreeMap<>();
    agruparPorValorRecursivo(raiz, grupos);
    return grupos;
}

private void agruparPorValorRecursivo(No<K, V> no, Map<K, List<V>> grupos) {
    if (no != null) {
        grupos.computeIfAbsent(no.getChave(), k -> new ArrayList<>()).add(no.getItem());
        agruparPorValorRecursivo(no.getEsquerda(), grupos);
        agruparPorValorRecursivo(no.getDireita(), grupos);
    }
}

	/**
	 * Construtor da classe.
	 * O comparador padrão de ordem natural será utilizado.
	 */
	@SuppressWarnings("unchecked")
	public ABB() {
		init((Comparator<K>) Comparator.naturalOrder());
	}

	/**
	 * Construtor da classe.
	 * Esse construtor cria uma nova árvore binária de busca vazia.
	 * 
	 * @param comparador o comparador a ser utilizado para organizar os elementos da
	 *                   árvore.
	 */
	public ABB(Comparator<K> comparador) {
		init(comparador);
	}

	/**
	 * Construtor da classe.
	 * Esse construtor cria uma nova árvore binária a partir de uma outra árvore
	 * binária de busca,
	 * com os mesmos itens, mas usando uma nova chave.
	 * 
	 * @param original    a árvore binária de busca original.
	 * @param funcaoChave a função que irá extrair a nova chave de cada item para a
	 *                    nova árvore.
	 */
	public ABB(ABB<?, V> original, Function<V, K> funcaoChave) {
		ABB<K, V> nova = new ABB<>();
		nova = copiarArvore(original.raiz, funcaoChave, nova);
		this.raiz = nova.raiz;
	}

	/**
	 * Recursivamente, copia os elementos da árvore original para esta, num processo
	 * análogo ao caminhamento em ordem.
	 * 
	 * @param <T>         Tipo da nova chave.
	 * @param raizArvore  raiz da árvore original que será copiada.
	 * @param funcaoChave função extratora da nova chave para cada item da árvore.
	 * @param novaArvore  Nova árvore. Parâmetro usado para permitir o retorno da
	 *                    recursividade.
	 * @return A nova árvore com os itens copiados e usando a chave indicada pela
	 *         função extratora.
	 */
	private <T> ABB<T, V> copiarArvore(No<?, V> raizArvore, Function<V, T> funcaoChave, ABB<T, V> novaArvore) {

		if (raizArvore != null) {
			novaArvore = copiarArvore(raizArvore.getEsquerda(), funcaoChave, novaArvore);
			V item = raizArvore.getItem();
			T chave = funcaoChave.apply(item);
			novaArvore.inserir(chave, item);
			novaArvore = copiarArvore(raizArvore.getDireita(), funcaoChave, novaArvore);
		}
		return novaArvore;
	}

	/**
	 * Método booleano que indica se a árvore está vazia ou não.
	 * 
	 * @return
	 *         verdadeiro: se a raiz da árvore for null, o que significa que a
	 *         árvore está vazia.
	 *         falso: se a raiz da árvore não for null, o que significa que a árvore
	 *         não está vazia.
	 */
	public Boolean vazia() {
		return (this.raiz == null);
	}

	@Override
	/**
	 * Método que encapsula a pesquisa recursiva de itens na árvore.
	 * 
	 * @param chave a chave do item que será pesquisado na árvore.
	 * @return o valor associado à chave.
	 */
	public V pesquisar(K chave) {
		comparacoes = 0;
		inicio = System.nanoTime();
		V procurado = pesquisar(raiz, chave);
		termino = System.nanoTime();
		return procurado;
	}

	private V pesquisar(No<K, V> raizArvore, K procurado) {

		int comparacao;

		comparacoes++;
		if (raizArvore == null)
			/// Se a raiz da árvore ou sub-árvore for null, a árvore/sub-árvore está vazia e
			/// então o item não foi encontrado.
			throw new NoSuchElementException("O item não foi localizado na árvore!");

		comparacao = comparador.compare(procurado, raizArvore.getChave());

		if (comparacao == 0)
			/// O item procurado foi encontrado.
			return raizArvore.getItem();
		else if (comparacao < 0)
			/// Se o item procurado for menor do que o item armazenado na raiz da árvore:
			/// pesquise esse item na sub-árvore esquerda.
			return pesquisar(raizArvore.getEsquerda(), procurado);
		else
			/// Se o item procurado for maior do que o item armazenado na raiz da árvore:
			/// pesquise esse item na sub-árvore direita.
			return pesquisar(raizArvore.getDireita(), procurado);
	}

	@Override
	/**
	 * Método que encapsula a adição recursiva de itens à árvore, associando-o à
	 * chave fornecida.
	 * 
	 * @param chave a chave associada ao item que será inserido na árvore.
	 * @param item  o item que será inserido na árvore.
	 * 
	 * @return o tamanho atualizado da árvore após a execução da operação de
	 *         inserção.
	 */

	public int inserir(K chave, V item) {
		raiz = inserirRecursivo(raiz, chave, item);
		tamanho++;
		return tamanho;
	}

	@Override
	public String toString() {
		return percorrer();
	}

	@Override
	public String percorrer() {
		return caminhamentoEmOrdem();
	}

	public String caminhamentoEmOrdem() {
		if (raiz == null) {
			return "Árvore vazia";
		}

		StringBuilder sb = new StringBuilder();
		caminhamentoEmOrdemRecursivo(raiz, sb);
		return sb.toString();
	}

	private void caminhamentoEmOrdemRecursivo(No<K, V> no, StringBuilder sb) {
		if (no != null) {
			caminhamentoEmOrdemRecursivo(no.getEsquerda(), sb);
			sb.append(no.getChave()).append(" - ").append(no.getItem()).append("\n");
			caminhamentoEmOrdemRecursivo(no.getDireita(), sb);
		}
	}

	@Override
	/**
	 * Método que encapsula a remoção recursiva de um item da árvore.
	 * 
	 * @param chave a chave do item que deverá ser localizado e removido da árvore.
	 * @return o valor associado ao item removido.
	 */
	public V remover(K chave) {
		try {
			V itemRemovido = pesquisar(chave); // Verifica se existe
			raiz = removerRecursivo(raiz, chave);
			tamanho--;
			return itemRemovido;
		} catch (NoSuchElementException e) {
			throw new NoSuchElementException("Item não encontrado para remoção");
		}
	}

	private No<K, V> removerRecursivo(No<K, V> no, K chave) {
		if (no == null) {
			return null;
		}

		int comparacao = comparador.compare(chave, no.getChave());

		if (comparacao < 0) {
			no.setEsquerda(removerRecursivo(no.getEsquerda(), chave));
		} else if (comparacao > 0) {
			no.setDireita(removerRecursivo(no.getDireita(), chave));
		} else {
			// Nó encontrado
			if (no.getEsquerda() == null) {
				return no.getDireita();
			} else if (no.getDireita() == null) {
				return no.getEsquerda();
			} else {
				// Nó com dois filhos
				No<K, V> sucessor = encontrarMinimo(no.getDireita());
				no.setChave(sucessor.getChave());
				no.setItem(sucessor.getItem());
				no.setDireita(removerRecursivo(no.getDireita(), sucessor.getChave()));
			}
		}

		no.setAltura();
		return no;
	}

	private No<K, V> encontrarMinimo(No<K, V> no) {
		while (no.getEsquerda() != null) {
			no = no.getEsquerda();
		}
		return no;
	}

	@Override
	public int tamanho() {
		return tamanho;
	}

	@Override
	public long getComparacoes() {
		return comparacoes;
	}

	@Override
	public double getTempo() {
		return (termino - inicio) / 1_000_000;
	}

	public List<V> buscarEmFaixa(K min, K max) {
		List<V> resultado = new ArrayList<>();
		buscarEmFaixaRecursivo(raiz, min, max, resultado);
		return resultado;
	}

	private void buscarEmFaixaRecursivo(No<K, V> no, K min, K max, List<V> resultado) {
		if (no == null) {
			return;
		}

		int comparacaoMin = comparador.compare(no.getChave(), min);
		int comparacaoMax = comparador.compare(no.getChave(), max);

		// Se chave >= min, busca na subárvore esquerda
		if (comparacaoMin >= 0) {
			buscarEmFaixaRecursivo(no.getEsquerda(), min, max, resultado);
		}

		// Se min <= chave <= max, adiciona ao resultado
		if (comparacaoMin >= 0 && comparacaoMax <= 0) {
			resultado.add(no.getItem());
		}

		// Se chave <= max, busca na subárvore direita
		if (comparacaoMax <= 0) {
			buscarEmFaixaRecursivo(no.getDireita(), min, max, resultado);
		}
	}

	/**
	 * Exibe árvore de forma hierárquica 
	 */
	public String exibirHierarquia() {
		StringBuilder sb = new StringBuilder();
		exibirHierarquiaRecursivo(raiz, 0, sb);
		return sb.toString();
	}

	private void exibirHierarquiaRecursivo(No<K, V> no, int nivel, StringBuilder sb) {
		if (no != null) {
			exibirHierarquiaRecursivo(no.getDireita(), nivel + 1, sb);

			for (int i = 0; i < nivel; i++) {
				sb.append("    ");
			}
			sb.append(no.getChave()).append("\n");

			exibirHierarquiaRecursivo(no.getEsquerda(), nivel + 1, sb);
		}
	}

	// Métodos auxiliares 
	public void inserirRecursivo(K chave, V item) {
		raiz = inserirRecursivo(raiz, chave, item);
		tamanho++;
	}

	private No<K, V> inserirRecursivo(No<K, V> no, K chave, V item) {
		if (no == null) {
			return new No<>(chave, item);
		}

		int comparacao = comparador.compare(chave, no.getChave());

		if (comparacao < 0) {
			no.setEsquerda(inserirRecursivo(no.getEsquerda(), chave, item));
		} else if (comparacao > 0) {
			no.setDireita(inserirRecursivo(no.getDireita(), chave, item));
		} else {
			// Chave já existe - substitui o valor
			no.setItem(item);
			tamanho--; // ajusta pois não adicionou novo
		}

		no.setAltura();
		return no;
	}

	public String percursoPorNivel() {
    if (raiz == null) {
        return "";
    }
    StringBuilder sb = new StringBuilder();
    Queue<No<K, V>> fila = new LinkedList<>();
    fila.add(raiz);
    while (!fila.isEmpty()) {
        int nivelSize = fila.size();
        for (int i = 0; i < nivelSize; i++) {
            No<K, V> atual = fila.poll();
            sb.append(atual.getChave()).append(" ");
            if (atual.getEsquerda() != null) {
                fila.add(atual.getEsquerda());
            }
            if (atual.getDireita() != null) {
                fila.add(atual.getDireita());
            }
        }
        sb.append("\n");
    }
    return sb.toString();
}

}
