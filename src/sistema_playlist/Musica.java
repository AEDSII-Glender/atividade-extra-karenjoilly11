package sistema_playlist;

public class Musica implements Comparable<Musica> {
    private int id;
    private String titulo;
    private String artista;
    private double duracao;
    
    public Musica(int id, String titulo, String artista, double duracao) {
        if (id <= 0 || titulo == null || titulo.trim().isEmpty() || 
            artista == null || artista.trim().isEmpty() || duracao <= 0) {
            throw new IllegalArgumentException("Dados inválidos para música");
        }
        this.id = id;
        this.titulo = titulo.trim();
        this.artista = artista.trim();
        this.duracao = duracao;
    }
    
    // Getters
    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getArtista() { return artista; }
    public double getDuracao() { return duracao; }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Musica outra = (Musica) obj;
        return id == outra.id;
    }
    
    @Override
    public int hashCode() {
        return id;
    }
    
    @Override
    public int compareTo(Musica outra) {
        return this.titulo.compareToIgnoreCase(outra.titulo);
    }
    
    @Override
    public String toString() {
        return String.format("%d - %s (%s) - %.2f min", id, titulo, artista, duracao);
    }
}
