package co.edu.uniquindio.full_marketplace.composite;

import co.edu.uniquindio.full_marketplace.model.Vendedor;

import java.util.ArrayList;
import java.util.List;

//agrupacion de contactos
public class CompositeVendedores {
    private List<Vendedor> vendedores;
    private String nombreGrupo;

    public CompositeVendedores(String nombreGrupo) {
        this.nombreGrupo = nombreGrupo;
        this.vendedores = new ArrayList<>();
    }

    public void agregarVendedor(Vendedor vendedor) {
        vendedores.add(vendedor);
    }

    public List<Vendedor> obtenerVendedores() {
        return vendedores;
    }


    public String getNombreGrupo() {
        return nombreGrupo;
    }
}