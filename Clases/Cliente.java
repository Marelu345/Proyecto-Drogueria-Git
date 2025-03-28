package Clases;

/**
 * Esta es la clase que representa a un cliente en el sistema.
 * Aquí se guardan los datos básicos del cliente como su ID, nombre,
 * cédula, correo electrónico y número de teléfono.
 */
public class Cliente {
    int id;
    String nombre,cedula,email,telefono;

    /**
     * Constructor que crea un cliente con sus datos completos.
     *
     * @param id      El número de identificación del cliente.
     * @param nombre  El nombre del cliente.
     * @param cedula  La cédula del cliente.
     * @param email   El correo del cliente.
     * @param telefono El número de teléfono del cliente.
     */
    public Cliente(int id, String nombre, String cedula, String email, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.cedula = cedula;
        this.email = email;
        this.telefono = telefono;
    }

    public Cliente() {}
    /**
     * Obtiene el ID del cliente.
     *
     * @return El ID del cliente.
     */
    public int getId() {
        return id;
    }
    /**
     * Asigna un nuevo ID al cliente.
     * @param id Nuevo identificador del cliente.
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * Obtiene el nombre del cliente.
     * @return Nombre del cliente.
     */
    public String getNombre() {
        return nombre;
    }
    /**
     * Asigna un nuevo nombre al cliente.
     * @param nombre Nuevo nombre del cliente.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    /**
     * Obtiene la cédula del cliente.
     * @return Cédula del cliente.
     */
    public String getCedula() {
        return cedula;
    }

    /**
     * Asigna una nueva cédula al cliente.
     * @param cedula Nueva cédula del cliente.
     */
    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    /**
     * Obtiene el email del cliente.
     * @return Email del cliente.
     */
    public String getEmail() {
        return email;
    }
    /**
     * Asigna un nuevo email al cliente.
     * @param email Nuevo correo electrónico del cliente.
     */
    public void setEmail(String email) {
        this.email = email;
    }
    /**
     * Obtiene el teléfono del cliente.
     * @return Teléfono del cliente.
     */
    public String getTelefono() {
        return telefono;
    }
    /**
     * Asigna un nuevo teléfono al cliente.
     * @param telefono Nuevo número de teléfono del cliente.
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
