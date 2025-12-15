package sistema_playlist;

import java.util.Stack;

public class HistoricoReproducao {
    private Stack<Musica> historico;
    
    public HistoricoReproducao() {
        this.historico = new Stack<>();
    }
    
    public void adicionar(Musica musica) {
        if (musica != null) {
            historico.push(musica);
        }
    }
    
    public Musica voltar() {
        if (!historico.isEmpty()) {
            return historico.pop();
        }
        return null;
    }
    
    public Musica verUltima() {
        if (!historico.isEmpty()) {
            return historico.peek();
        }
        return null;
    }
    
    public void limpar() {
        historico.clear();
    }
    
    public boolean vazio() {
        return historico.isEmpty();
    }
    
    public int tamanho() {
        return historico.size();
    }
    
    @Override
    public String toString() {
        if (historico.isEmpty()) {
            return "Histórico vazio";
        }
        
        StringBuilder sb = new StringBuilder("Histórico (mais recente primeiro):\n");
        Stack<Musica> temp = new Stack<>();
        int count = 1;
        
        // Inverter para exibir do mais recente ao mais antigo
        while (!historico.isEmpty()) {
            Musica m = historico.pop();
            sb.append(String.format("%d. %s\n", count++, m));
            temp.push(m);
        }
        
        // Restaurar pilha original
        while (!temp.isEmpty()) {
            historico.push(temp.pop());
        }
        
        return sb.toString();
    }
}
