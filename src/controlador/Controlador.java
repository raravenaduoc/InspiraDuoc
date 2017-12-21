/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.PreparedStatement;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import modelo.Registro;
import javax.swing.JOptionPane;
import vista.Agregar;
import vista.Consultas;
import vista.Videoclub;
import vista.Eliminar;
import vista.Mostrar;

/**
 *
 * @author Duoc UC
 */
public class Controlador implements ActionListener,MouseListener {

    Videoclub interfaz = new Videoclub();
    public static Agregar interfazagregar = new Agregar();
    public static Mostrar interfazmostrar = new Mostrar();
    public static Eliminar interfazeliminar = new Eliminar();
    public static Consultas interfazconsultas = new Consultas();
    
    //modelo
    private Registro modelo = new Registro();
    
    public enum Accion{
        boton_guardar,
        boton_eliminar,
        boton_buscar,
        boton_modificar,
        btnAgregaConsulta1,
        btnAgregaConsulta2,
        boton_limpiar,
    }
    
    public Controlador() {
         try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            SwingUtilities.updateComponentTreeUI(interfaz);
            SwingUtilities.updateComponentTreeUI(interfazagregar);
            SwingUtilities.updateComponentTreeUI(interfazmostrar);
            SwingUtilities.updateComponentTreeUI(interfazeliminar);
            this.interfaz.setTitle("VideoBusterr");
            interfazagregar.setTitle("Agregar Película");
            interfazmostrar.setTitle("Buscar / Mostrar Películas");
            interfazeliminar.setTitle("Eliminar Película");
        } catch (UnsupportedLookAndFeelException ex) {}
          catch (ClassNotFoundException ex) {}
          catch (InstantiationException ex) {}
          catch (IllegalAccessException ex) {}
        interfaz.setVisible(true);
    }
    
    public void iniciar(){
        //Escuchamos los botones
        interfazagregar.boton_guardar.setActionCommand( "boton_guardar" );
        interfazagregar.boton_guardar.addActionListener(this);
        interfazagregar.boton_limpiar.setActionCommand( "boton_limpiar" );
        interfazagregar.boton_limpiar.addActionListener(this);
        interfazeliminar.boton_eliminar.setActionCommand( "boton_eliminar" );
        interfazeliminar.boton_eliminar.addActionListener(this);
        interfazmostrar.boton_buscar.setActionCommand( "boton_buscar" );
        interfazmostrar.boton_buscar.addActionListener(this);
        interfazmostrar.boton_modificar.setActionCommand( "boton_modificar" );
        interfazmostrar.boton_modificar.addActionListener(this);
        
        
        //Interactuar con la tabla
        interfazmostrar.tabla.addMouseListener(this);
    }

    //asignamos acciones a los clicks
    @Override
    public void actionPerformed(ActionEvent e) {
        switch ( Accion.valueOf( e.getActionCommand() ) ){
            
            case boton_guardar:
               //GUARDAR NUEVA PELICULA              
                //codigo
                int codigopelicula = 0;
                if (interfazagregar.tf_codigo.getText().length() > 0){
                    codigopelicula = Integer.valueOf(interfazagregar.tf_codigo.getText());
                }
                
                // nombre
                String nombrepelicula = interfazagregar.tf_nombre.getText();
                
                 //precio
                int preciopelicula = 0;
                if (interfazagregar.tf_precio.getText().length() > 0){
                    preciopelicula = Integer.valueOf(interfazagregar.tf_precio.getText());
                }
                
                //categoria
                int idcategoria = 1;
                String categoriapelicula = interfazagregar.cb_categoria.getSelectedItem().toString();
                if (categoriapelicula.equals("Infantil")){
                    idcategoria = 2;
                } else if (categoriapelicula.equals("Documental")){
                     idcategoria = 3;
                } else if (categoriapelicula.equals("Musical")){
                    idcategoria = 4;
                }
            
                //formato4k              
                String formato4k = "S";
                if (interfazagregar.radio_no.isSelected()){
                    formato4k = "N";
                }

                // CHEQUEAMOS REGLAS DE NEGOCIO
                if (categoriapelicula.equals("Categoría")){
                    JOptionPane.showMessageDialog(null, "Debe seleccionar una categorìa", "Error", JOptionPane.WARNING_MESSAGE);
                } else {
                    if (preciopelicula < 1000){
                        JOptionPane.showMessageDialog(null, "El precio de la película debe ser al menos de 1000", "Error", JOptionPane.WARNING_MESSAGE);
                    } else {
                        if (nombrepelicula.length()<3){
                            JOptionPane.showMessageDialog(null, "El nombre debe tener al menos 3 caracteres", "Error", JOptionPane.WARNING_MESSAGE);
                        } else {
                            if (codigopelicula < 10000 || codigopelicula > 99999) {
                             JOptionPane.showMessageDialog(null, "El código debe estar entre 10000 y 99999", "Error", JOptionPane.WARNING_MESSAGE);
                            } else {
                                //si estan llenos todos los campos ejecutamos la query
                                if (categoriapelicula.equals("Categoría") == false &&
                                    codigopelicula > 0 &&
                                    nombrepelicula.length() > 0 &&
                                    preciopelicula > 0) {
                                    // hacer la query
                                    /* int codigo, int categoria, String nombre, int precio, String formato4k */
                                    this.modelo.agregar(codigopelicula, idcategoria, nombrepelicula, preciopelicula, formato4k);
                                    JOptionPane.showMessageDialog(null, "Se ha agregado el registro", "Agregar Película", JOptionPane.OK_OPTION);
                                } else {
                                    JOptionPane.showMessageDialog(null, "Por favor llenar todos los campos", "Error", JOptionPane.WARNING_MESSAGE);
                                }
                            }
                        }
                    }
                }

            break;
            case boton_eliminar:
                if (interfazeliminar.tf_eliminar.getText().length() > 0){
                    //asdf
                   if(this.modelo.eliminar(Integer.valueOf(interfazeliminar.tf_eliminar.getText())) == true) {
                       //eliminado
                       JOptionPane.showMessageDialog(null, "Se ha eliminado el registro", "Pelicula Eliminada", JOptionPane.INFORMATION_MESSAGE);
                   } else {
                       JOptionPane.showMessageDialog(null, "No existe una película con ese código", "Error", JOptionPane.WARNING_MESSAGE);
                   }
                }
                
            break;
            case boton_buscar:
                if (interfazmostrar.tf_buscar.getText().length() > 0){
                    //aa
                    interfazmostrar.tabla.setModel(this.modelo.buscar(Integer.valueOf(interfazmostrar.tf_buscar.getText())));
                    if (this.modelo.buscar(Integer.valueOf(interfazmostrar.tf_buscar.getText())).getValueAt(0, 0) == null){
                        JOptionPane.showMessageDialog(null, "No existe una película con ese código", "Error", JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    interfazmostrar.tabla.setModel(this.modelo.mostrar());
                }
            break;
            case boton_modificar:
                /* MODIFICAR PELICULA DESDE LA TABLA */
               //codigo
                int modificarcodigo = 0;
                if (interfazmostrar.tf_modificar_codigo.getText().length() > 0){
                    modificarcodigo = Integer.valueOf(interfazmostrar.tf_modificar_codigo.getText());
                }
                
                // nombre
                String modificarnombre = interfazmostrar.tf_modificar_nombre.getText();
                
                 //precio
                int modificarprecio = 0;
                if (interfazmostrar.tf_modificar_precio.getText().length() > 0){
                    modificarprecio = Integer.valueOf(interfazmostrar.tf_modificar_precio.getText());
                }
                
                //categoria
                int modificaridcategoria = 1;
                String modificarcategoria = interfazmostrar.cb_modificar_categoria.getSelectedItem().toString();
                if (modificarcategoria.equals("Infantil")){
                    modificaridcategoria = 2;
                } else if (modificarcategoria.equals("Documental")){
                     modificaridcategoria = 3;
                } else if (modificarcategoria.equals("Musical")){
                    modificaridcategoria = 4;
                }
            
                //formato4k              
                String modificarformato = "S";
                if (interfazmostrar.radio_modificar_no.isSelected()){
                    modificarformato = "N";
                }
                
                //si estan llenos todos los campos ejecutamos la query
                if (modificarcategoria.equals("Categoría") == false &&
                    modificarcodigo > 0 &&
                    modificarnombre.length() > 0 &&
                    modificarprecio > 0) {
                    /* int codigo, int categoria, String nombre, int precio, String formato4k */
                    // hacer la query
                    this.modelo.modificar(modificarcodigo,modificaridcategoria,modificarnombre,modificarprecio,modificarformato);
                    JOptionPane.showMessageDialog(null, "Se ha modificado el registro", "Modificar Pelìcula", JOptionPane.OK_OPTION);
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor llenar todos los campos", "Error", JOptionPane.WARNING_MESSAGE);
                }
                interfazmostrar.tabla.setModel(this.modelo.mostrar());
              
            break;
            case boton_limpiar:
                interfazagregar.tf_codigo.setText(null);
                interfazagregar.tf_nombre.setText(null);
                interfazagregar.tf_precio.setText(null);
                interfazagregar.cb_categoria.setSelectedItem("Categoría");
                interfazagregar.tf_codigo.grabFocus();
            break;
            
        }  
    }
    
    //asignamos acciones cuando se clickea la tabla
    @Override
    public void mouseClicked(MouseEvent e) {
        if( e.getButton()== 1){
            //Muestro datos de producto a modificar
             int fila = interfazmostrar.tabla.rowAtPoint(e.getPoint());
             if (fila > -1){
                /* aqui poblamos los campos segun el modelo de la tabla */
                
                /*
                data[i][0] = res.getString( "codigo" );
                data[i][1] = res.getString( "nombre" );
                data[i][2] = nombre_categoria (Integer.parseInt(res.getString( "id_categoria" )));
                data[i][3] = res.getString( "precio" );
                data[i][4] = res.getString( "formato4k" );
                */
                
                interfazmostrar.tf_modificar_codigo.setText(String.valueOf(interfazmostrar.tabla.getValueAt(fila, 0) ));
                interfazmostrar.tf_modificar_nombre.setText(String.valueOf(interfazmostrar.tabla.getValueAt(fila, 1) ));                
                
                
                int selectedindex = 0;
                if (interfazmostrar.tabla.getValueAt(fila, 2).toString().equals("Largometraje")){
                     selectedindex = 1;
                } else if (interfazmostrar.tabla.getValueAt(fila, 2).toString().equals("Infantil")){
                   selectedindex = 2; 
                } else if (interfazmostrar.tabla.getValueAt(fila, 2).toString().equals("Documental")){
                    selectedindex = 3;
                } else if (interfazmostrar.tabla.getValueAt(fila, 2).toString().equals("Musical")){
                    selectedindex = 4;
                }
                interfazmostrar.cb_modificar_categoria.setSelectedIndex(selectedindex);
                interfazmostrar.tf_modificar_precio.setText(String.valueOf(interfazmostrar.tabla.getValueAt(fila, 3) ));
                if (interfazmostrar.tabla.getValueAt(fila, 4).toString().equals("S")){
                    interfazmostrar.radio_modificar_si.setSelected(true);
                    interfazmostrar.radio_modificar_no.setSelected(false);
                } else {
                    interfazmostrar.radio_modificar_no.setSelected(true);
                    interfazmostrar.radio_modificar_si.setSelected(false);
                }
             }
        }
    
    
    }
    
    /* de aqui para abajo no se usa */
    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
    
}
