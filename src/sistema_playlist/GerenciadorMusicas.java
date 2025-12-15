package sistema_playlist;

import sistema_produtos.ABB;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.List;
import java.util.ArrayList;

public class GerenciadorMusicas {
    private ABB<Integer, Musica> musicasPorId;
    private ABB<String, Musica> musicasPorTitulo;
    private ABB<Double, List<Musica>> musicasPorDuracao;

    public GerenciadorMusicas() {
        this.musicasPorId = new ABB<>();
        this.musicasPorTitulo = new ABB<>(String.CASE_INSENSITIVE_ORDER);
        this.musicasPorDuracao = new ABB<>();
    }

    public void adicionarMusica(Musica musica) {
        // Adicionar por ID
        musicasPorId.inserir(musica.getId(), musica);
        
        // Adicionar por título (nome)
        musicasPorTitulo.inserir(musica.getTitulo(), musica);
        
        // Adicionar por duração 
        double duracao = musica.getDuracao();
        try {
            // Tenta buscar lista existente para esta duração
            List<Musica> lista = musicasPorDuracao.pesquisar(duracao);
            lista.add(musica);
        } catch (NoSuchElementException e) {
            // Cria nova lista para esta duração
            List<Musica> lista = new ArrayList<>();
            lista.add(musica);
            musicasPorDuracao.inserir(duracao, lista);
        }
    }

    public Musica buscarPorId(int id) {
        try {
            return musicasPorId.pesquisar(id);
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    public Musica buscarPorTitulo(String titulo) {
        try {
            return musicasPorTitulo.pesquisar(titulo);
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    public List<Musica> buscarPorDuracaoExata(double duracao) {
        try {
            return musicasPorDuracao.pesquisar(duracao);
        } catch (NoSuchElementException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Busca eficiente por faixa de duração usando percurso na árvore
     * Retorna todas as músicas cuja duração está entre min e max (inclusive)
     */
    public List<Musica> buscarPorFaixaDeDuracao(double min, double max) {
        List<List<Musica>> listasDeMusicas = musicasPorDuracao.buscarEmFaixa(min, max);
        List<Musica> resultado = new ArrayList<>();
        
        // Achata a lista de listas para uma única lista
        for (List<Musica> lista : listasDeMusicas) {
            resultado.addAll(lista);
        }
        
        return resultado;
    }

    public String listarTodas() {
        StringBuilder sb = new StringBuilder("🎵 TODAS AS MÚSICAS DISPONÍVEIS:\n");
        sb.append("(ID, Título, Artista, Duração)\n");
        sb.append("═".repeat(60)).append("\n");
        
        // Usar o percurso em ordem da árvore por ID
        String percurso = musicasPorId.percorrer();
        if (percurso != null && !percurso.isEmpty()) {
            String[] linhas = percurso.split("\n");
            int contador = 1;
            for (String linha : linhas) {
                if (!linha.trim().isEmpty()) {
                    sb.append(contador++).append(". ").append(linha).append("\n");
                }
            }
        } else {
            sb.append("Nenhuma música disponível.\n");
        }
        
        return sb.toString();
    }

    public int getTotalMusicas() {
        return musicasPorId.tamanho();
    }

    // Carregar músicas de arquivo (id, titulo, artista, duracao)
    public boolean carregarDeArquivo(String nomeArquivo) {
        try (Scanner arquivo = new Scanner(new File(nomeArquivo))) {
            // Lê o número total de músicas
            int numMusicas = Integer.parseInt(arquivo.nextLine());

            for (int i = 0; i < numMusicas; i++) {
                if (!arquivo.hasNextLine()) break;
                
                String linha = arquivo.nextLine();
                String[] partes = linha.split(";");

                if (partes.length >= 4) {
                    int id = Integer.parseInt(partes[0].trim());
                    String titulo = partes[1].trim();
                    String artista = partes[2].trim();
                    double duracao = Double.parseDouble(partes[3].replace(",", ".").trim());

                    // Cria e adiciona a música
                    Musica musica = new Musica(id, titulo, artista, duracao);
                    adicionarMusica(musica);
                } else {
                    System.out.println("⚠️  Linha mal formatada: " + linha);
                }
            }
            System.out.println(" Carregadas " + getTotalMusicas() + " músicas do arquivo.");
            return true;
        } catch (IOException e) {
            System.out.println(" Erro ao ler arquivo '" + nomeArquivo + "': " + e.getMessage());
            return false;
        } catch (NumberFormatException e) {
            System.out.println(" Erro de formato no arquivo: " + e.getMessage());
            return false;
        }
    }

    /**
     * Exibe árvore de forma hierárquica 
     * "músicas com mesma duração são hierarquicamente tratadas pelo mesmo critério
     * da de maior duração"
     */
    public String exibirArvoreHierarquica() {
        StringBuilder sb = new StringBuilder("🌳 ÁRVORE HIERÁRQUICA POR DURAÇÃO\n");
        sb.append("═".repeat(50)).append("\n");
        sb.append("(Músicas agrupadas por duração - mesmo nível para mesma duração)\n\n");
        
        // Percorrer a árvore manualmente para obter os grupos
        
        List<Double> duracoesUnicas = new ArrayList<>();
        coletarDuracoesUnicas(musicasPorDuracao, duracoesUnicas);
        
        if (duracoesUnicas.isEmpty()) {
            sb.append("Árvore vazia - nenhuma música cadastrada.\n");
            return sb.toString();
        }
        
        int nivel = 1;
        for (Double duracao : duracoesUnicas) {
            sb.append("\nNÍVEL ").append(nivel).append(": ")
              .append(String.format("%.2f", duracao)).append(" minutos\n");
            
            try {
                List<Musica> musicas = musicasPorDuracao.pesquisar(duracao);
                for (int i = 0; i < musicas.size(); i++) {
                    Musica musica = musicas.get(i);
                    String prefixo = (i == musicas.size() - 1) ? "└─ " : "├─ ";
                    sb.append("  ").append(prefixo)
                      .append(musica.getTitulo())
                      .append(" (").append(musica.getArtista()).append(")\n");
                }
            } catch (NoSuchElementException e) {
                sb.append("  (erro ao buscar músicas)\n");
            }
            
            nivel++;
        }
        
        sb.append("\n═══════════════════════════════════════════════════\n");
        sb.append("Total: ").append(getTotalMusicas()).append(" música(s) em ")
          .append(duracoesUnicas.size()).append(" nível(is) de duração diferente(s)\n");
        
        return sb.toString();
    }
    
    /**
     * Método auxiliar para coletar durações únicas em ordem crescente
     */
    private void coletarDuracoesUnicas(ABB<Double, List<Musica>> arvore, List<Double> resultado) {
        // Percurso em ordem
        String percurso = arvore.percorrer();
        if (percurso != null && !percurso.isEmpty()) {
            String[] linhas = percurso.split("\n");
            for (String linha : linhas) {
                try {
                    // Extrair a duração da linha
                    int fim = linha.indexOf(" - ");
                    if (fim > 0) {
                        String duracaoStr = linha.substring(0, fim).trim();
                        double duracao = Double.parseDouble(duracaoStr);
                        if (!resultado.contains(duracao)) {
                            resultado.add(duracao);
                        }
                    }
                } catch (Exception e) {
                    
                }
            }
        }
    }
    
    /**
     * Exibe estrutura da árvore por duração em formato simples
     */
    public String exibirArvorePorDuracao() {
        StringBuilder sb = new StringBuilder("📊 ESTRUTURA DA ÁRVORE POR DURAÇÃO\n");
        sb.append("═".repeat(50)).append("\n");
        
        String percurso = musicasPorDuracao.percorrer();
        if (percurso != null && !percurso.isEmpty()) {
            String[] linhas = percurso.split("\n");
            for (String linha : linhas) {
                sb.append("• ").append(linha).append("\n");
            }
        } else {
            sb.append("Árvore vazia\n");
        }
        
        return sb.toString();
    }
    
    /**
     * Retorna todas as músicas como uma lista
     */
    public List<Musica> getTodasMusicas() {
        List<Musica> todas = new ArrayList<>();
        
        // Coletar todas as músicas da árvore por ID
        String percurso = musicasPorId.percorrer();
        if (percurso != null && !percurso.isEmpty()) {
            String[] linhas = percurso.split("\n");
            for (String linha : linhas) {
                try {
                    // Extrair ID da linha")
                    int inicio = linha.indexOf(" - ");
                    if (inicio > 0) {
                        String idStr = linha.substring(0, inicio).trim();
                        int id = Integer.parseInt(idStr);
                        Musica musica = buscarPorId(id);
                        if (musica != null) {
                            todas.add(musica);
                        }
                    }
                } catch (Exception e) {
                    // Ignora linhas mal formatadas
                }
            }
        }
        
        return todas;
    }
    
    /**
     * Verifica se uma música com determinado ID já existe
     */
    public boolean existeMusicaComId(int id) {
        return buscarPorId(id) != null;
    }
    
    /**
     * Verifica se uma música com determinado título já existe
     */
    public boolean existeMusicaComTitulo(String titulo) {
        return buscarPorTitulo(titulo) != null;
    }
}
