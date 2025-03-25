package Clases;
/**
 * Representa al administrador del sistema.
 * No es necesario como tal crear uno nuevo porque ya viene por defecto en la base de datos.
 */
public class Admin {
    /** La contraseña del administrador. Esta Sirve para acceder al sistema. */
    String contrasena;

    /** El ID del administrador, que lo identifica dentro del sistema. */
    int id;

    /**
     * Constructor vacío.
     * No se usa porque el administrador como se sabe, ya existe en la base de datos.
     */
    public Admin() {}

    /**
     * Crea un administrador con un ID y una contraseña.
     * Esto como tal solo sirve para recuperar los datos desde la base de datos.
     *
     * @param contrasena La clave que usa el admin para ingresar al sistema.
     * @param id El número que identifica al administrador.
     */
    public Admin(String contrasena, int id) {
        this.contrasena = contrasena;
        this.id = id;
    }
    /**
     * Devuelve la contraseña del administrador.
     * La clave no se puede modificar desde el sistema, solo desde la base de datos.
     *
     * @return La clave del admin.
     */
    public String getContrasena() {
        return contrasena;
    }

    /**
     * Este metodo técnicamente permite cambiar la contraseña del administrador,
     * pero, pues en el sistema no se usa para eso.
     * Su único uso es para asignar la contraseña que viene de la base de datos
     * al momento de iniciar sesión.
     *
     * @param contrasena La contraseña del administrador obtenida de la base de datos.
     */
    public void setContrasena(String contrasena) {this.contrasena = contrasena;}

    /**
     * Devuelve el ID del administrador.
     *
     * @return El número de identificación del admin.
     */
    public int getId() {
        return id;
    }

    /**
     * Este metodo sirve para asignarle un ID al admin, pero en realidad
     * el admin siempre tiene un ID fijo porque solo hay uno en la base de datos.
     * No es algo que se cambie manualmente en el sistema.
     *
     * @param id El ID del administrador que viene de la base de datos.
     */
    public void setId(int id) {
        this.id = id;
    }
}
