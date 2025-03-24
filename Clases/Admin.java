package Clases;

public class Admin {
    String contrasena;
    int id;
    public Admin() {}

    public Admin(String contrasena, int id) {
        this.contrasena = contrasena;
        this.id = id;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contraseña) {
        this.contrasena = contraseña;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
