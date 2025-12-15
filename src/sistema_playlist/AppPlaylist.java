package sistema_playlist;

import java.util.Scanner;
import java.util.List;
import java.util.Random;

public class AppPlaylist {
    private static Scanner teclado = new Scanner(System.in);
    private static GerenciadorMusicas gerenciador;
    private static Playlist playlist;
    private static HistoricoReproducao historico;
    private static Random random = new Random();
    
    public static void main(String[] args) {
        inicializarSistema();
        exibirMenuPrincipal();
    }
    
    private static void inicializarSistema() {
        gerenciador = new GerenciadorMusicas();
        playlist = new Playlist();
        historico = new HistoricoReproducao();
        
        // Carregar músicas do arquivo ou exemplos
        if (!carregarDeArquivo("musicas.txt")) {
            carregarMusicasExemplo();
        }
    }
    private static boolean carregarDeArquivo(String nomeArquivo) {
        return gerenciador.carregarDeArquivo(nomeArquivo);
    }
    
    private static void carregarMusicasExemplo() {
        // Músicas variadas para demonstrar ordenação por duração
        gerenciador.adicionarMusica(new Musica(1, "Bohemian Rhapsody", "Queen", 5.55));
        gerenciador.adicionarMusica(new Musica(2, "Imagine", "John Lennon", 3.03));
        gerenciador.adicionarMusica(new Musica(3, "Billie Jean", "Michael Jackson", 4.54));
        gerenciador.adicionarMusica(new Musica(4, "Smells Like Teen Spirit", "Nirvana", 5.01));
        gerenciador.adicionarMusica(new Musica(5, "Like a Rolling Stone", "Bob Dylan", 6.13));
        gerenciador.adicionarMusica(new Musica(6, "Yesterday", "The Beatles", 2.05));
        gerenciador.adicionarMusica(new Musica(7, "Stairway to Heaven", "Led Zeppelin", 8.02));
        gerenciador.adicionarMusica(new Musica(8, "Sweet Child O' Mine", "Guns N' Roses", 5.56));
        gerenciador.adicionarMusica(new Musica(9, "Hallelujah", "Leonard Cohen", 4.39));
        gerenciador.adicionarMusica(new Musica(10, "Blinding Lights", "The Weeknd", 3.22));
        
        System.out.println("✓ Sistema inicializado com " + gerenciador.getTotalMusicas() + " músicas de exemplo.");
    }
    
    private static void exibirMenuPrincipal() {
        int opcao;
        
        do {
            limparTela();
            System.out.println("╔══════════════════════════════════╗");
            System.out.println("║     🎵 SISTEMA DE PLAYLIST 🎵     ║");
            System.out.println("╠══════════════════════════════════╣");
            System.out.println("║  1. Exibir músicas disponíveis   ║");
            System.out.println("║  2. Exibir árvore por duração    ║");
            System.out.println("║  3. Buscar por faixa de duração  ║");
            System.out.println("║  4. Adicionar música à playlist  ║");
            System.out.println("║  5. Remover música da playlist   ║");
            System.out.println("║  6. Exibir playlist atual        ║");
            System.out.println("║  7. Ordenar playlist             ║");
            System.out.println("║  8. Reproduzir em ordem          ║");
            System.out.println("║  9. Reproduzir aleatoriamente    ║");
            System.out.println("║ 10. Exibir histórico             ║");
            System.out.println("║ 11. Voltar reprodução            ║");
            System.out.println("║  0. Sair                         ║");
            System.out.println("╚══════════════════════════════════╝");
            System.out.print("Escolha: ");
            
            opcao = lerInteiro();
            
            switch (opcao) {
                case 1 -> exibirMusicasDisponiveis();
                case 2 -> exibirArvoreDuracao();
                case 3 -> buscarPorFaixaDuracao();
                case 4 -> adicionarMusicaPlaylist();
                case 5 -> removerMusicaPlaylist();
                case 6 -> exibirPlaylist();
                case 7 -> menuOrdenacao();
                case 8 -> reproduzirEmOrdem();
                case 9 -> reproduzirEscolhendo();
                case 10 -> exibirHistorico();
                case 11 -> voltarReproducao();
                case 0 -> System.out.println("Saindo do sistema...");
                default -> System.out.println("Opção inválida!");
            }
            
            if (opcao != 0) {
                pausa();
            }
            
        } while (opcao != 0);
        
        teclado.close();
    }
    
    private static void exibirMusicasDisponiveis() {
        System.out.println("\n🎼 MÚSICAS DISPONÍVEIS:");
        System.out.println(gerenciador.listarTodas());
    }
    
    private static void adicionarMusicaPlaylist() {
        exibirMusicasDisponiveis();
        System.out.print("\nDigite o ID da música para adicionar: ");
        int id = lerInteiro();
        
        Musica musica = gerenciador.buscarPorId(id);
        if (musica != null) {
            playlist.adicionarNoFim(musica);
            System.out.println("✓ Música adicionada: " + musica);
        } else {
            System.out.println("✗ Música não encontrada!");
        }
    }
    
    private static void removerMusicaPlaylist() {
    if (playlist.vazia()) {
        System.out.println("Playlist vazia!");
        return;
    }
    
    System.out.println("\n🔍 REMOVER MÚSICA:");
    System.out.println("1. Remover por ID");
    System.out.println("2. Remover por título");
    System.out.print("Escolha: ");
    
    int opcao = lerInteiro();
    
    if (opcao == 1) {
        exibirPlaylist();
        System.out.print("Digite o ID da música para remover: ");
        int id = lerInteiro();
        
        if (playlist.removerPorId(id)) {
            System.out.println("Música removida com sucesso!");
        } else {
            System.out.println("Música não encontrada na playlist!");
        }
    } else if (opcao == 2) {
        exibirPlaylist();
        System.out.print("Digite o título da música para remover: ");
        teclado.nextLine(); // Limpar buffer
        String titulo = teclado.nextLine();
        
        if (playlist.removerPorTitulo(titulo)) {
            System.out.println("Música removida com sucesso!");
        } else {
            System.out.println("Música não encontrada na playlist!");
        }
    } else {
        System.out.println("Opção inválida!");
    }
}

    private static void exibirPlaylist() {
        System.out.println("\n📋 PLAYLIST ATUAL:");
        System.out.println(playlist.exibir());
        System.out.println("Total: " + playlist.getTamanho() + " música(s)");
    }
    
    private static void menuOrdenacao() {
    if (playlist.vazia()) {
        System.out.println("Playlist vazia!");
        return;
    }
    
    System.out.println("\nMÉTODO DE ORDENAÇÃO:");
    System.out.println("1. Bubble Sort (iterativo)");
    System.out.println("2. Quick Sort (particionamento)");
    System.out.print("Escolha: ");
    
    int metodo = lerInteiro();
    
    System.out.println("\nCRITÉRIO DE ORDENAÇÃO:");
    System.out.println("1. ID");
    System.out.println("2. Título");
    System.out.println("3. Artista");
    System.out.println("4. Duração");
    System.out.print("Escolha: ");
    
    int criterio = lerInteiro();
    
    if (metodo == 1) {
        // Bubble Sort
        switch (criterio) {
            case 1 -> {
                playlist.ordenarPorId();
                System.out.println("Ordenada por ID (Bubble Sort)");
            }
            case 2 -> {
                playlist.ordenarPorTitulo();
                System.out.println("Ordenada por Título (Bubble Sort)");
            }
            case 3 -> {
                playlist.ordenarPorArtista();
                System.out.println("Ordenada por Artista (Bubble Sort)");
            }
            case 4 -> {
                playlist.ordenarPorDuracao();
                System.out.println("Ordenada por Duração (Bubble Sort)");
            }
            default -> {
                System.out.println("Critério inválido!");
                return;
            }
        }
    } else if (metodo == 2) {
        // Quick Sort
        switch (criterio) {
            case 1 -> {
                playlist.ordenarPorIdQuickSort();
                System.out.println("Ordenada por ID (Quick Sort)");
            }
            case 2 -> {
                playlist.ordenarPorTituloQuickSort();
                System.out.println("Ordenada por Título (Quick Sort)");
            }
            case 3 -> {
                playlist.ordenarPorArtistaQuickSort(); 
                System.out.println("Ordenada por Artista (Quick Sort)");
            }
            case 4 -> {
                playlist.ordenarPorDuracaoQuickSort();
                System.out.println("Ordenada por Duração (Quick Sort)");
            }
            default -> {
                System.out.println("Critério inválido!");
                return;
            }
        }
    } else {
        System.out.println("Método inválido!");
    }
}

    private static void reproduzirEmOrdem() {
        if (playlist.vazia()) {
            System.out.println("Playlist vazia!");
            return;
        }
        
        System.out.println("\n▶REPRODUZIR EM ORDEM:");
        System.out.println("1. Iniciar do início");
        System.out.println("2. Iniciar do fim");
        System.out.print("Escolha: ");
        
        int opcao = lerInteiro();
        Musica atual = null;
        
        if (opcao == 1) {
            atual = playlist.iniciarReproducaoInicio();
        } else if (opcao == 2) {
            atual = playlist.iniciarReproducaoFim();
        } else {
            System.out.println("Opção inválida!");
            return;
        }
        
        if (atual != null) {
            reproduzirMusica(atual);
            menuControleReproducao();
        }
    }
    
    private static void reproduzirAleatoriamente() {
        if (playlist.vazia()) {
            System.out.println("Playlist vazia!");
            return;
        }
        
        exibirPlaylist();
        System.out.print("\nDigite o ID da próxima música: ");
        int id = lerInteiro();
        
        Musica musica = playlist.buscarPorId(id);
        if (musica != null) {
            reproduzirMusica(musica);
        } else {
            System.out.println("✗ Música não encontrada na playlist!");
        }
    }
    
    private static double lerDouble() {
    while (true) {
        try {
            return Double.parseDouble(teclado.nextLine().replace(",", "."));
        } catch (NumberFormatException e) {
            System.out.print("Digite um número válido (ex: 3.45): ");
        }
    }
}

    private static void menuControleReproducao() {
    int opcao;
    
    do {
        System.out.println("\n🎮 CONTROLES DE REPRODUÇÃO:");
        System.out.println("1. Próxima música");
        System.out.println("2. Música anterior");
        System.out.println("3. Reiniciar playlist");
        System.out.println("4. Escolher música aleatória");
        System.out.println("5. Voltar ao menu");
        System.out.print("Escolha: ");
        
        opcao = lerInteiro();
        Musica proxima = null;
        
        switch (opcao) {
            case 1 -> proxima = playlist.proxima();
            case 2 -> proxima = playlist.anterior();
            case 3 -> {
                playlist.iniciarReproducaoInicio();
                proxima = playlist.getAtual();
                System.out.println("Playlist reiniciada");
            }
            case 4 -> {
                reproduzirEscolhendo();
                continue; // Pula para próxima iteração
            }
            case 5 -> { return; }
            default -> System.out.println("Opção inválida!");
        }
        
        if (proxima != null) {
            reproduzirMusica(proxima);
        } else if (opcao == 1 || opcao == 2) {
            verificarEReiniciarPlaylist();
            return; // Volta ao menu após tentativa de reinício
        }
        
    } while (opcao != 5);
}
    private static void reproduzirMusica(Musica musica) {
        if (musica != null) {
            System.out.println("\n REPRODUZINDO: " + musica.getTitulo() + " - " + musica.getArtista());
            historico.adicionar(musica);
        }
    }
    
    private static void exibirHistorico() {
        System.out.println("\n HISTÓRICO DE REPRODUÇÃO:");
        System.out.println(historico);
    }
    
    private static void voltarReproducao() {
        Musica ultima = historico.voltar();
        if (ultima != null) {
            System.out.println("Voltando para: " + ultima);
        } else {
            System.out.println("✗ Histórico vazio!");
        }
    }
    
    // Utilitários
    
    private static void limparTela() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    
    private static void pausa() {
        System.out.print("\nPressione Enter para continuar...");
        teclado.nextLine();
    }
    
    private static int lerInteiro() {
        while (true) {
            try {
                return Integer.parseInt(teclado.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Digite um número válido: ");
            }
        }
    }

    private static void exibirArvoreDuracao() {
    System.out.println("\nESTRUTURA HIERÁRQUICA POR DURAÇÃO:");
    System.out.println("(Músicas organizadas por duração na árvore de busca)");
    
    if (gerenciador != null) {
        System.out.println(gerenciador.exibirArvorePorDuracao());
    } else {
        System.out.println("Gerenciador não inicializado!");
    }
}

private static void buscarPorFaixaDuracao() {
    System.out.println("\nBUSCAR POR FAIXA DE DURAÇÃO");
    System.out.print("Duração mínima (minutos): ");
    double min = lerDouble();
    System.out.print("Duração máxima (minutos): ");
    double max = lerDouble();
    
    if (min > max) {
        System.out.println("Mínima não pode ser maior que máxima!");
        return;
    }
    
    List<Musica> resultado = gerenciador.buscarPorFaixaDeDuracao(min, max);
    
    if (resultado.isEmpty()) {
        System.out.println(" Nenhuma música encontrada na faixa " + min + " - " + max + " min");
    } else {
        System.out.println("ok " + resultado.size() + " música(s) encontrada(s):");
        for (int i = 0; i < resultado.size(); i++) {
            System.out.println((i + 1) + ". " + resultado.get(i));
        }
    }
}


private static void reproduzirEscolhendo() {
    if (playlist.vazia()) {
        System.out.println("Playlist vazia!");
        return;
    }
    
    System.out.println("\n REPRODUÇÃO (ESCOLHA A PRÓXIMA MÚSICA)");
    exibirPlaylist();
    System.out.print("Digite o ID da próxima música que deseja reproduzir: ");
    int id = lerInteiro();
    
    Musica musica = playlist.buscarPorId(id);
    if (musica != null) {
        reproduzirMusica(musica);
        // Atualizar ponteiro de reprodução para esta música
  
    } else {
        System.out.println(" Música não encontrada na playlist!");
    }
}
private static void verificarEReiniciarPlaylist() {
    if (playlist.vazia()) {
        System.out.println("\n PLAYLIST FINALIZADA!");
        System.out.print("Deseja reiniciar do início? (S/N): ");
        String resposta = teclado.nextLine().trim().toUpperCase();
        
        if (resposta.equals("S") || resposta.equals("SIM")) {
            playlist.iniciarReproducaoInicio();
            Musica atual = playlist.getAtual();
            if (atual != null) {
                reproduzirMusica(atual);
                System.out.println(" Playlist reiniciada!");
            }
        } else {
            System.out.println("  Voltando ao menu principal...");
        }
    }
}
}
