
package com.emergentes.controlador;
import com.emergentes.modelo.Almacen;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet(name = "MainController", urlPatterns = {"/MainController"})
public class MainController extends HttpServlet {

   
     
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession ses=request.getSession();
        
        if(ses.getAttribute("listaalmacen")==null){
            ArrayList<Almacen> listaux = new ArrayList<Almacen>();
            ses.setAttribute("listaalmacen", listaux);
        }
        
        ArrayList<Almacen> lista=(ArrayList<Almacen>)ses.getAttribute("listaalmacen");
        //recibir, view=lista
        String op=request.getParameter("op");
        String opcion=(op!=null)? op : "view";
        
        Almacen obj1=new Almacen();
        //crear nuevos variables
        int id, posicion;
       
        switch(opcion){
            case "nuevo":
                request.setAttribute("miAlmacen", obj1);
                request.getRequestDispatcher("editar.jsp").forward(request,response);
                break;
            case "editar":
                id=Integer.parseInt(request.getParameter("id"));
                posicion=buscarIndice(request,id);
                obj1=lista.get(posicion);
                request.setAttribute("miAlmacen", obj1);
                request.getRequestDispatcher("editar.jsp").forward(request, response);
                break;
            case "eliminar":
                id=Integer.parseInt(request.getParameter("id"));
                posicion=buscarIndice(request,id);
                lista.remove(posicion);
                //actualizar la lista
                ses.setAttribute("listaalmacen", lista);
                response.sendRedirect("index.jsp");
                break;
            case "view":
                response.sendRedirect("index.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //sacar lista
        HttpSession ses=request.getSession();
        ArrayList<Almacen> lista=(ArrayList<Almacen>)ses.getAttribute("listaalmacen");
        //adicinar objeto si es nuevo vacio
        Almacen obj1=new Almacen();
        //colocar los valores que vienen desde el formulario
        obj1.setId(Integer.parseInt(request.getParameter("id")));
        obj1.setDescripcion(request.getParameter("descripcion"));
        obj1.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        obj1.setPrecio(Integer.parseInt(request.getParameter("precio")));
        
        int idt=obj1.getId();
        
        if(idt==0){
            //nuevo
            //actualizar
            int ultId;
            ultId=ultimoId(request);
            //actualizamos elobjeto
            obj1.setId(ultId);
            lista.add(obj1);
        }else{
            //edicion
            lista.set(buscarIndice(request,idt), obj1);
        } 
        //actualizando lista
        ses.setAttribute("listaalmacen", lista);
        response.sendRedirect("index.jsp");
    }
        private int buscarIndice(HttpServletRequest request, int id){
            HttpSession ses=request.getSession();
            ArrayList<Almacen> lista = (ArrayList<Almacen>)ses.getAttribute("listaalmacen");
            
            //Recorrido de la lista
             int i=0;
             
             if(lista.size()> 0){
                 while (i < lista.size()){
                   if(lista.get(i).getId()==id){
                       break;
                   }else{
                       i++;
                   }
                }
             }
            return i; 
        }
        private int ultimoId(HttpServletRequest request){
            HttpSession ses=request.getSession();
            ArrayList<Almacen> lista = (ArrayList<Almacen>)ses.getAttribute("listaalmacen");
            
            int idaux=0;
            for(Almacen item:lista){
                idaux=item.getId();
            }
            return idaux+1;
        }
        
   }