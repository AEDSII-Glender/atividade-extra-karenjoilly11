package sistema_playlist;

public class NoPlaylist {
    private Musica musica;
    private NoPlaylist anterior;
    private NoPlaylist proximo;
    
    public NoPlaylist(Musica musica) {
        this.musica = musica;
        this.anterior = null;
        this.proximo = null;
    }
    
    public NoPlaylist(Musica musica, NoPlaylist anterior, NoPlaylist proximo) {
        this.musica = musica;
        this.anterior = anterior;
        this.proximo = proximo;
    }
    
    // Getters e Setters
    public Musica getMusica() { return musica; }
    public void setMusica(Musica musica) { this.musica = musica; }
    
    public NoPlaylist getAnterior() { return anterior; }
    public void setAnterior(NoPlaylist anterior) { this.anterior = anterior; }
    
    public NoPlaylist getProximo() { return proximo; }
    public void setProximo(NoPlaylist proximo) { this.proximo = proximo; }
}
