package pe.edu.cibertec.catalogo.service;

import pe.edu.cibertec.catalogo.model.Categoria;
import pe.edu.cibertec.catalogo.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> listarTodos() {
        return categoriaRepository.findByEstadoTrue();
    }

    public Categoria obtenerPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
    }

    public Categoria crear(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public Categoria actualizar(Long id, Categoria categoria) {
        Categoria existente = obtenerPorId(id);
        existente.setNombreCategoria(categoria.getNombreCategoria());
        existente.setDescripcion(categoria.getDescripcion());
        existente.setImagenUrl(categoria.getImagenUrl());
        return categoriaRepository.save(existente);
    }

    public void eliminar(Long id) {
        Categoria categoria = obtenerPorId(id);
        categoria.setEstado(false);
        categoriaRepository.save(categoria);
    }
}