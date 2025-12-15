package sistema_playlist;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Playlist {
    private NoPlaylist inicio;
    private NoPlaylist fim;
    private NoPlaylist atual; // ponteiro para reprodução atual
    private int tamanho;

    public Playlist() {
        this.inicio = null;
        this.fim = null;
        this.atual = null;
        this.tamanho = 0;
    }

    // Operacoes básicas 

    public void adicionarNoFim(Musica musica) {
        NoPlaylist novoNo = new NoPlaylist(musica);

        if (vazia()) {
            inicio = fim = novoNo;
        } else {
            novoNo.setAnterior(fim);
            fim.setProximo(novoNo);
            fim = novoNo;
        }
        tamanho++;
    }

    public void adicionarNoInicio(Musica musica) {
        NoPlaylist novoNo = new NoPlaylist(musica);

        if (vazia()) {
            inicio = fim = novoNo;
        } else {
            novoNo.setProximo(inicio);
            inicio.setAnterior(novoNo);
            inicio = novoNo;
        }
        tamanho++;
    }

    public boolean removerPorId(int id) {
        NoPlaylist atual = inicio;

        while (atual != null) {
            if (atual.getMusica().getId() == id) {
                // Remover nó
                NoPlaylist anterior = atual.getAnterior();
                NoPlaylist proximo = atual.getProximo();

                if (anterior != null) {
                    anterior.setProximo(proximo);
                } else {
                    inicio = proximo; // era o primeiro
                }

                if (proximo != null) {
                    proximo.setAnterior(anterior);
                } else {
                    fim = anterior; // era o último
                }

                // Ajustar ponteiro de reprodução se necessário
                if (this.atual == atual) {
                    this.atual = proximo != null ? proximo : anterior;
                }

                tamanho--;
                return true;
            }
            atual = atual.getProximo();
        }
        return false;
    }

    public Musica buscarPorId(int id) {
        NoPlaylist atual = inicio;
        while (atual != null) {
            if (atual.getMusica().getId() == id) {
                return atual.getMusica();
            }
            atual = atual.getProximo();
        }
        return null;
    }

    /**
     * Remove uma música da playlist por título (case-insensitive)
     * 
     * @param titulo Título da música a ser removida
     * @return true se a música foi removida, false se não encontrada
     */
    public boolean removerPorTitulo(String titulo) {
        NoPlaylist atual = inicio;
        String tituloLower = titulo.toLowerCase();

        while (atual != null) {
            if (atual.getMusica().getTitulo().toLowerCase().equals(tituloLower)) {
                // Remover nó (mesma lógica de removerPorId)
                NoPlaylist anterior = atual.getAnterior();
                NoPlaylist proximo = atual.getProximo();

                if (anterior != null) {
                    anterior.setProximo(proximo);
                } else {
                    inicio = proximo;
                }

                if (proximo != null) {
                    proximo.setAnterior(anterior);
                } else {
                    fim = anterior;
                }

                if (this.atual == atual) {
                    this.atual = proximo != null ? proximo : anterior;
                }

                tamanho--;
                return true;
            }
            atual = atual.getProximo();
        }
        return false;
    }

    /**
     * Busca música por título na playlist (case-insensitive)
     * 
     * @param titulo Título da música a buscar
     * @return Música encontrada ou null se não existir
     */
    public Musica buscarPorTitulo(String titulo) {
        NoPlaylist atual = inicio;
        String tituloLower = titulo.toLowerCase();

        while (atual != null) {
            if (atual.getMusica().getTitulo().toLowerCase().equals(tituloLower)) {
                return atual.getMusica();
            }
            atual = atual.getProximo();
        }
        return null;
    }

    /**
     * Ordena playlist por artista usando QuickSort (particionamento)
     */
    public void ordenarPorArtistaQuickSort() {
        if (tamanho < 2)
            return;

        List<Musica> musicas = coletarMusicas();
        musicas = quickSort(musicas, Comparator.comparing(Musica::getArtista, String.CASE_INSENSITIVE_ORDER));

        atualizarLista(musicas);
        System.out.println("✓ Ordenada por Artista (QuickSort)");
    }

    /**
     * Obtém uma lista com todas as músicas (para reprodução aleatória)
     * 
     * @return Lista de músicas na playlist
     */
    public List<Musica> getTodasMusicas() {
        return coletarMusicas();
    }

    /**
     * Obtém uma música aleatória da playlist
     * 
     * @return Música aleatória ou null se playlist vazia
     */
    public Musica getMusicaAleatoria() {
        if (vazia())
            return null;

        List<Musica> musicas = coletarMusicas();
        int indice = (int) (Math.random() * musicas.size());
        return musicas.get(indice);
    }

    // Reproducao

    public Musica iniciarReproducaoInicio() {
        atual = inicio;
        return atual != null ? atual.getMusica() : null;
    }

    public Musica iniciarReproducaoFim() {
        atual = fim;
        return atual != null ? atual.getMusica() : null;
    }

    public Musica proxima() {
        if (atual == null || atual.getProximo() == null) {
            return null; // fim da playlist
        }
        atual = atual.getProximo();
        return atual.getMusica();
    }



    public Musica anterior() {
        if (atual == null || atual.getAnterior() == null) {
            return null; // início da playlist
        }
        atual = atual.getAnterior();
        return atual.getMusica();
    }

    public Musica getAtual() {
        return atual != null ? atual.getMusica() : null;
    }

    

    // Ordenação

    public void ordenarPorTitulo() {
        ordenarBubbleSort(Comparator.comparing(Musica::getTitulo, String.CASE_INSENSITIVE_ORDER));
    }

    public void ordenarPorArtista() {
        ordenarBubbleSort(Comparator.comparing(Musica::getArtista, String.CASE_INSENSITIVE_ORDER));
    }

    public void ordenarPorDuracao() {
        ordenarBubbleSort(Comparator.comparingDouble(Musica::getDuracao));
    }

    public void ordenarPorId() {
        ordenarBubbleSort(Comparator.comparingInt(Musica::getId));
    }

    private void ordenarBubbleSort(Comparator<Musica> comparador) {
        if (tamanho < 2)
            return;

        boolean trocou;
        do {
            trocou = false;
            NoPlaylist atual = inicio;

            while (atual != null && atual.getProximo() != null) {
                Musica m1 = atual.getMusica();
                Musica m2 = atual.getProximo().getMusica();

                if (comparador.compare(m1, m2) > 0) {
                    // Troca as músicas nos nós (mais simples que trocar nós)
                    Musica temp = atual.getMusica();
                    atual.setMusica(m2);
                    atual.getProximo().setMusica(temp);
                    trocou = true;
                }
                atual = atual.getProximo();
            }
        } while (trocou);
    }

    public boolean vazia() {
        return inicio == null;
    }

    public int getTamanho() {
        return tamanho;
    }

    public String exibir() {
        if (vazia()) {
            return "Playlist vazia";
        }

        StringBuilder sb = new StringBuilder();
        NoPlaylist atual = inicio;
        int pos = 1;

        while (atual != null) {
            String indicadorAtual = (this.atual == atual) ? "▶ " : "  ";
            sb.append(String.format("%s%d. %s\n", indicadorAtual, pos, atual.getMusica()));
            atual = atual.getProximo();
            pos++;
        }

        return sb.toString();
    }

    // Com ordenação usando QuickSort
    public void ordenarPorTituloQuickSort() {
        if (tamanho < 2)
            return;

        List<Musica> musicas = coletarMusicas();
        musicas = quickSort(musicas, Comparator.comparing(Musica::getTitulo, String.CASE_INSENSITIVE_ORDER));

        atualizarLista(musicas);
        System.out.println("✓ Ordenada por Título (QuickSort)");
    }

    public void ordenarPorDuracaoQuickSort() {
        if (tamanho < 2)
            return;

        List<Musica> musicas = coletarMusicas();
        musicas = quickSort(musicas, Comparator.comparingDouble(Musica::getDuracao));

        atualizarLista(musicas);
        System.out.println("✓ Ordenada por Duração (QuickSort)");
    }

    public void ordenarPorIdQuickSort() {
        if (tamanho < 2)
            return;

        List<Musica> musicas = coletarMusicas();
        musicas = quickSort(musicas, Comparator.comparingInt(Musica::getId));

        atualizarLista(musicas);
        System.out.println("✓ Ordenada por ID (QuickSort)");
    }

    private List<Musica> coletarMusicas() {
        List<Musica> lista = new ArrayList<>();
        NoPlaylist atual = inicio;

        while (atual != null) {
            lista.add(atual.getMusica());
            atual = atual.getProximo();
        }

        return lista;
    }

    private void atualizarLista(List<Musica> musicas) {
        // Limpa a lista atual
        inicio = fim = atual = null;
        tamanho = 0;

        // Reinsere as músicas ordenadas
        for (Musica musica : musicas) {
            adicionarNoFim(musica);
        }
    }

    private List<Musica> quickSort(List<Musica> lista, Comparator<Musica> comparador) {
        if (lista.size() <= 1) {
            return lista;
        }

        Musica pivot = lista.get(lista.size() / 2);
        List<Musica> menores = new ArrayList<>();
        List<Musica> iguais = new ArrayList<>();
        List<Musica> maiores = new ArrayList<>();

        for (Musica musica : lista) {
            int comparacao = comparador.compare(musica, pivot);
            if (comparacao < 0) {
                menores.add(musica);
            } else if (comparacao > 0) {
                maiores.add(musica);
            } else {
                iguais.add(musica);
            }
        }

        List<Musica> resultado = new ArrayList<>();
        resultado.addAll(quickSort(menores, comparador));
        resultado.addAll(iguais);
        resultado.addAll(quickSort(maiores, comparador));

        return resultado;
    }
}
