package matissepersonas;

import com.matisse.MtDatabase;
import com.matisse.MtException;
import com.matisse.MtObjectIterator;
import com.matisse.MtPackageObjectFactory;
import java.util.Scanner;
import personas.Persona;

/**
 * @Daniel Migales
 */
public class MatissePersonas {

    public static void main(String[] args) {

        String hostname = "localhost";
        String dbname = "personas";

        Scanner tecladoString = new Scanner(System.in);
        Scanner tecladoInt = new Scanner(System.in);

        boolean salir = true;

        do {
            System.out.println("******Menu Principal******");
            System.out.println("1. Crear persona.");
            System.out.println("2. Modificar persona.");
            System.out.println("3. Salir.");
            System.out.println("\nQue operacion desea realizar:\n");

            int opcion = tecladoInt.nextInt();

            switch (opcion) {
                case 1:
                    creaObjetos(hostname, dbname);
                    break;
                case 2:
                    ModificaObjeto(hostname, dbname);
                    break;
                case 3:
                    salir = false;
                    break;
            }
        } while (salir);

    }

    public static void creaObjetos(String hostname, String dbname) {

        try {
            MtDatabase db = new MtDatabase(hostname, dbname, new MtPackageObjectFactory("personas", "personas"));

            db.open();
            db.startTransaction();

            Scanner tecladoString = new Scanner(System.in);
            Scanner tecladoInt = new Scanner(System.in);

            System.out.println("Introduzca los datos de la persona a crear: ");

            System.out.println("Nombre: ");
            String nombre = tecladoString.next();
            System.out.println("Apellido: ");
            String apellido = tecladoString.next();
            System.out.println("Edad: ");
            int edad = tecladoInt.nextInt();
            System.out.println("DNI: ");
            String dni = tecladoString.next();

            Persona persona1 = new Persona(db);
            persona1.setNombre(nombre);
            persona1.setApellido(apellido);
            persona1.setEdad(edad);
            persona1.setDni(dni);

            db.commit();
            db.close();

        } catch (MtException mte) {
            System.out.println("MtException : " + mte.getMessage());
        }
    }

    public static void ModificaObjeto(String hostname, String dbname) {

        int numeroPersonas = 0;

        Scanner tecladoString = new Scanner(System.in);
        Scanner tecladoInt = new Scanner(System.in);

        try {
            MtDatabase db = new MtDatabase(hostname, dbname, new MtPackageObjectFactory("personas", "personas"));
            db.open();
            db.startTransaction();

            System.out.println("\nSe han encontrado " + Persona.getInstanceNumber(db) + "  personas en la DB.\n");
            numeroPersonas = (int) Persona.getInstanceNumber(db);
            MtObjectIterator<Persona> iter = Persona.<Persona>instanceIterator(db);

            System.out.println("¿Introduzca el nombre de la persona a modificar:?");
            String personaModificar = tecladoString.nextLine();
            System.out.println("Introduzca la nueva edad de la persona.");
            int nuevaEdad = tecladoInt.nextInt();
            System.out.println("Introduzca los apellidos de la persona.");
            String nuevoApellido = tecladoString.nextLine();
            System.out.println("Introduzca el DNI de la persona.");
            String nuevoDNI = tecladoString.nextLine();

            while (iter.hasNext()) {
                Persona[] personas = iter.next(numeroPersonas);
                for (int i = 0; i < personas.length; i++) {
                    if (personas[i].getNombre().compareTo(personaModificar) == 0) {
                        personas[i].setApellido(nuevoApellido);
                        personas[i].setEdad(nuevaEdad);
                        personas[i].setDni(nuevoDNI);
                    }
                }
                iter.close();
                db.commit();
                db.close();

                System.out.println("\nHecho.\n");
            }

        } catch (MtException mte) {
            System.out.println("MtException:" + mte.getMessage());
        }
    }
}
