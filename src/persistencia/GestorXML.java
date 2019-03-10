package persistencia;

import java.io.FileWriter;
import nu.xom.*;
import java.text.ParseException;

import principal.GestioVolsExcepcio;
import principal.Companyia;
import principal.Component;
import principal.Vol;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import components.Avio;
import components.Ruta;
import components.RutaIntercontinental;
import components.RutaInternacional;
import components.RutaNacional;
import components.RutaTransoceanica;
import components.TCP;
import components.Tripulant;
import components.TripulantCabina;



/**
 *
 * @author cesca
 */
public class GestorXML implements ProveedorPersistencia {
    private Document doc;
    private Companyia companyia;

    public Companyia getCompanyia() {
        return companyia;
    }

    public void setCompanyia(Companyia pCompanyia) {
        companyia = pCompanyia;
    }

    public void desarDades(String nomFitxer, Companyia pCompanyia) throws GestioVolsExcepcio {
        construirModel(pCompanyia);
        desarModel(nomFitxer);
    }

    public Companyia carregarDades(String nomFitxer) throws GestioVolsExcepcio {
        carregarFitxer(nomFitxer);
        obtenirDades();
        return companyia;
    }

    /*Paràmetres: Companyia a partir de la qual volem construir el model
     *
     *Acció:
     *Llegir els atributs de l'objecte Companyia passat per paràmetre per construir
     *un model (document XML) sobre el Document doc (atribut de GestorXML).
     *L'arrel del document XML és "companyia" i heu d'afegir-ne els valors de
     *codi i nom com atributs. Aquesta arrel, l'heu d'afegir a doc.
     *
     *Un cop fet això, heu de recórrer l'ArrayList elements de Companyia i per
     *a cada element, afegir un fill a doc. Cada fill tindrà com atributs els
     *atributs de l'objecte (codi, nom, fabricant, …)
     *
     *Si es tracta d'un avio, a més, heu d'afegir fills addicionals amb els
     *valors de les classes d'aquest avio.
     *
     *Si es tracta d'un vol, a més, heu d'afegir fills addicionals amb els
     *valors dels tripulants d'aquest vol. En el cas de l'atribut avio, heu d'assignar-li
     *el codi de l'avio del vol, i en el cas del cap dels TCP, el passport del cap.
     *
     *Retorn: cap
     */
    private void construirModel(Companyia pCompanyia){
        Element compa = new Element ("companyia");
        compa.addAttribute(new Attribute("codi", pCompanyia.getCodi()));
        compa.addAttribute(new Attribute("nom", pCompanyia.getNom()));

        for (Companyia atrr : pCompanyia.getComponents()) {
          if(atrr instanceof Avio){
            Element nouAv = new Element ("avio");
            nouAv.addAttribute(new Attribute("codi", atrr.getCodi()));
            nouAv.addAttribute(new Attribute("fabricant", atrr.getFabricant()));
            nouAv.addAttribute(new Attribute("model", atrr.getModel()));
            nouAv.addAttribute(new Attribute("capacitat", atrr.getCapacitat()));

            ArrayList<Classe> clas = ((Avio) atrr).getClasses();
            for(Classe classe: clas){
              Element classes = new Element("classe");
              classes.addAttribute(new Attribute("nom", clas.getNom()));
              classes.addAttribute(new Attribute("capacitat", clas.getCapacitat()));
            }
          }

        }

        DateFormat dataFormat = new SimpleDateFormat("DD-MM-YYYY"); // Date format
    		Element compa = new Element("companyia");
    		for (Component comp : pCompanyia.getComponents()) {
    			Element elem = null;
    			if (comp instanceof Avio) {
    				elem = new Element("avio");
    				elem.addAttribute(new Attribute("codi", ((Avio) comp).getCodi()));
    				elem.addAttribute(new Attribute("fabricant", ((Avio) comp).getFabricant()));
    				elem.addAttribute(new Attribute("model", ((Avio) comp).getModel()));
    				elem.addAttribute(new Attribute("capacitat", String.valueOf(((Avio) comp).getCapacitat())));
    				Element classes = null;
    				ArrayList<Classe> clas = ((Avio) comp).getClasses();
    				for (Classe classe : clase) {
    					classes = new Element("classe");
    					classes.addAttribute(new Attribute("nom", classe.getNom()));
    					classes.addAttribute(new Attribute("capacitat", String.valueOf(classe.getCapacitat())));
    					elem.appendChild(classes);
    				}

    			} else if (comp instanceof Ruta) {
      				if (comp instanceof RutaNacional) {
      					elem = new Element("rutaNacional");
      					elem.addAttribute(new Attribute("codi", ((RutaNacional) comp).getCodi()));
      					elem.addAttribute(new Attribute("aeroportOri", ((RutaNacional) comp).getAeroportOri()));
      					elem.addAttribute(new Attribute("aeroportDes", ((RutaNacional) comp).getAeroportDes()));
      					elem.addAttribute(new Attribute("distancia", String.valueOf(((RutaNacional) comp).getDistancia())));
      					elem.addAttribute(new Attribute("pais", ((RutaNacional) comp).getPais()));

      				} else if (comp instanceof RutaInternacional) {
      					elem = new Element("rutaInternacional");
      					elem.addAttribute(new Attribute("codi", ((RutaInternacional) comp).getCodi()));
      					elem.addAttribute(new Attribute("aeroportOri", ((RutaInternacional) comp).getAeroportOri()));
      					elem.addAttribute(new Attribute("aeroportDes", ((RutaInternacional) comp).getAeroportDes()));
      					elem.addAttribute(new Attribute("distancia", String.valueOf(((RutaInternacional) comp).getDistancia())));
      					elem.addAttribute(new Attribute("paisOri", ((RutaInternacional) comp).getPaisOri()));
      					elem.addAttribute(new Attribute("paisDes", ((RutaInternacional) comp).getPaisDes()));

      				} else if (comp instanceof RutaIntercontinental) {
      					elem = new Element("rutaIntercontinental");
      					elem.addAttribute(new Attribute("codi", ((RutaIntercontinental) comp).getCodi()));
      					elem.addAttribute(new Attribute("aeroportOri", ((RutaIntercontinental) comp).getAeroportOri()));
      					elem.addAttribute(new Attribute("aeroportDes", ((RutaIntercontinental) comp).getAeroportDes()));
      					elem.addAttribute(new Attribute("distancia", String.valueOf(((RutaIntercontinental) comp).getDistancia())));
      					elem.addAttribute(new Attribute("paisOri", ((RutaIntercontinental) comp).getPaisOri()));
      					elem.addAttribute(new Attribute("paisDes", ((RutaIntercontinental) comp).getPaisDes()));
      					elem.addAttribute(new Attribute("continentOri", ((RutaIntercontinental) comp).getContinentOri()));
      					elem.addAttribute(new Attribute("continentDes", ((RutaIntercontinental) comp).getContinentDes()));

      				} else if (comp instanceof RutaTransoceanica) {
      					elem = new Element("rutaTransoceanica");
      					elem.addAttribute(new Attribute("codi", ((RutaTransoceanica) comp).getCodi()));
      					elem.addAttribute(new Attribute("aeroportOri", ((RutaTransoceanica) comp).getAeroportOri()));
      					elem.addAttribute(new Attribute("aeroportDes", ((RutaTransoceanica) comp).getAeroportDes()));
      					elem.addAttribute(new Attribute("distancia", String.valueOf(((RutaTransoceanica) comp).getDistancia())));
      					elem.addAttribute(new Attribute("paisOri", ((RutaTransoceanica) comp).getPaisOri()));
      					elem.addAttribute(new Attribute("paisDes", ((RutaTransoceanica) comp).getPaisDes()));
      					elem.addAttribute(new Attribute("continentOri", ((RutaTransoceanica) comp).getContinentOri()));
      					elem.addAttribute(new Attribute("continentDes", ((RutaTransoceanica) comp).getContinentDes()));
      					elem.addAttribute(new Attribute("ocea", ((RutaTransoceanica) comp).getOcea()));
      				}

    			} else if (comp instanceof Vol) {
    				elem = new Element("vol");
    				elem.addAttribute(new Attribute("codi", ((Vol) comp).getCodi()));
    				elem.addAttribute(new Attribute("ruta", ((Vol) comp).getRuta().getCodi()));
    				elem.addAttribute(new Attribute("avio", ((Vol) comp).getAvio().getCodi()));
    				elem.addAttribute(new Attribute("dataSortida", dataFormat.format(((Vol) comp).getDataSortida())));
    				elem.addAttribute(new Attribute("dataArribada", dataFormat.format(((Vol) comp).getDataArribada())));
    				elem.addAttribute(new Attribute("horaSortida", ((Vol) comp).getHoraSortida().toString()));
    				elem.addAttribute(new Attribute("horaArribada", ((Vol) comp).getHoraArribada().toString()));
    				elem.addAttribute(new Attribute("durada", ((Vol) comp).getDurada()));

    				HashMap<String, Tripulant> tripulant = ((Vol) comp).getTripulacio();
    				Set<String> passaportes = tripulant.keySet();
    				Iterator iter = passaportes.iterator();
    				Element elem2 = null;
    				while (iter.hasNext()) {
    					Tripulant tripi = (Tripulant) iter.next();
    					if (tripi instanceof TCP) {
    						elem2 = new Element("TCP");
    						elem2.addAttribute(new Attribute("passaport", ((TCP) comp).getPassaport()));
    						elem2.addAttribute(new Attribute("nom", ((TCP) comp).getNom()));
    						elem2.addAttribute(new Attribute("edad", String.valueOf(((TCP) comp).getEdat())));
    						elem2.addAttribute(new Attribute("dataAlta", dataFormat.format(((TCP) comp).getDataAlta())));
    						elem2.addAttribute(new Attribute("horesVol", String.valueOf(((TCP) comp).getHoresVol())));
    						elem2.addAttribute(new Attribute("rang", ((TCP) comp).getRang()));

    					} else {
    						elem2 = new Element("tripulantCabina");
    						elem2.addAttribute(new Attribute("passaport", ((TripulantCabina) comp).getPassaport()));
    						elem2.addAttribute(new Attribute("nom", ((TripulantCabina) comp).getNom()));
    						elem2.addAttribute(new Attribute("edad", String.valueOf(((TripulantCabina) comp).getEdat())));
    						elem2.addAttribute(new Attribute("dataAlta", dataFormat.format(((TripulantCabina) comp).getDataAlta())));
    						elem2.addAttribute(new Attribute("horesVol", String.valueOf(((TripulantCabina) comp).getHoresVol())));
    						elem2.addAttribute(new Attribute("rang", ((TripulantCabina) comp).getRang()));
    						elem2.addAttribute(new Attribute("barres", String.valueOf(((TripulantCabina) comp).getBarres())));
    					}
    					elem.appendChild(elem2);
    				}

    			} else if (comp instanceof Tripulant) {
    				if (comp instanceof TCP) {
    					elem = new Element("TCP");
    					elem.addAttribute(new Attribute("passaport", ((TCP) comp).getPassaport()));
    					elem.addAttribute(new Attribute("nom", ((TCP) comp).getNom()));
    					elem.addAttribute(new Attribute("edad", String.valueOf(((TCP) comp).getEdat())));
    					elem.addAttribute(new Attribute("dataAlta", dataFormat.format(((TCP) comp).getDataAlta())));
    					elem.addAttribute(new Attribute("horesVol", String.valueOf(((TCP) comp).getHoresVol())));
    					elem.addAttribute(new Attribute("rang", ((TCP) comp).getRang()));

    				} else if (comp instanceof TripulantCabina) {
    					elem = new Element("tripulantCabina");
    					elem.addAttribute(new Attribute("passaport", ((TripulantCabina) comp).getPassaport()));
    					elem.addAttribute(new Attribute("nom", ((TripulantCabina) comp).getNom()));
    					elem.addAttribute(new Attribute("edad", String.valueOf(((TripulantCabina) comp).getEdat())));
    					elem.addAttribute(new Attribute("dataAlta", dataFormat.format(((TripulantCabina) comp).getDataAlta())));
    					elem.addAttribute(new Attribute("horesVol", String.valueOf(((TripulantCabina) comp).getHoresVol())));
    					elem.addAttribute(new Attribute("rang", ((TripulantCabina) comp).getRang()));
    					elem.addAttribute(new Attribute("barres", String.valueOf(((TripulantCabina) comp).getBarres())));
    				}
    			}
    			compa.appendChild(elem);
    		}
    	}

    }

    private void desarModel(String rutaFitxer) throws GestioVolsExcepcio {
        try {
            FileWriter fitxer = new FileWriter(rutaFitxer, false); //Obrim fitxer per sobreescriure
            fitxer.write(doc.toXML());
            fitxer.close();
        } catch (Exception e) {
            throw new GestioVolsExcepcio("GestorXML.desar");
        }
    }

    private void carregarFitxer(String rutaFitxer) throws GestioVolsExcepcio {
        Builder builder = new Builder();
        try {
            doc = builder.build("/home/cesca/NetBeansProjects/ControlPlatsV4Solucio/"+rutaFitxer);
            System.out.println(doc.toXML());
        } catch (Exception e) {
            throw new GestioVolsExcepcio("GestorXML.carregar");
        }
    }

    /*Paràmetres: cap
     *
     *Acció:
     *El mètode obtenirDades llegeix el fitxer del disc i el carrega sobre l'atribut
     *doc de GestorXML.
     *
     *L'objectiu és llegir el document per assignar valors als atributs de Companyia
     *(i la resta d'objectes). Per llegir els valors dels atributs del document
     *XML, heu de fer servir el mètode getAtributeValue().
     *Penseu que l'arrel conté els atributs de la companyia, per tant, al accedir
     *a l'arrel del document ja podeu crear l'objecte Companyia amb el mètode constructor
     *escaient de la classe companyia (fixeu-vos que s’ha afgeit un de nou).
     *
     *Un cop fet això, heu de recòrrer el document i per cada fill, haureu d'afegir un
     *element a l'ArrayList components de Companyia (nouXXX(.....)). Penseu que
     *els mètodes de la classe companyia per afegir components, els hem modificat
     *perquè es pugui afegir un component passat er paràmetre.
     *
     *Si el fill (del document) que s'ha llegit és un avió o un vol, recordeu que a més
     *d'afegir-los a la companyia, també haureu d'afegir en el l'avió les seves classes
     *i en el vol la seva tripulació.
     *
     *Retorn: cap
     */
    private void obtenirDades() throws GestioVolsExcepcio, ParseException {
  		DateFormat dataFormat = new SimpleDateFormat("DD-MM-YYYY");

  		Element arrel = doc.getRootElement();
  		companyia = new Companyia(arrel.getAttributeValue("nom"));
  		Elements llistaAvions = arrel.getChildElements("avio");


  		Element avio = null;
  		Element classe = null;
  		for (int i = 0; i < llistaAvions.size(); i++) {
  			avio = llistaAvions.get(i);
  			Avio nouAvio = new Avio(avio.getAttributeValue("codi"), avio.getAttributeValue("fabricant"), avio.getAttributeValue("model"),
  					Integer.parseInt(avio.getAttributeValue("capacitat")));

  			Elements llistaClasses = avio.getChildElements("classe");
  			for (int j = 0; j < llistaClasses.size(); j++) {
  				classe = llistaClasses.get(j);
  				Classe novaClasse = new Classe(classe.getAttributeValue("nom"), Integer.parseInt(classe.getAttributeValue("capacitat")));
  				nouAvio.getClasses().add(novaClasse);
  			}
  			companyia.afegirAvio(nouAvio);
  		}


  		Elements tripulants = arrel.getChildElements("TCP");
  		Element tripulant = null;
  		for (int i = 0; i < tripulants.size(); i++) {
  			tripulant = tripulants.get(i);
  			companyia.afegirTCP(new TCP(tripulant.getAttributeValue("passaport"), tripulant.getAttributeValue("nom"),
  					Integer.parseInt(tripulant.getAttributeValue("edat")), Integer.parseInt(tripulant.getAttributeValue("horesVol"))));
  		}

  		tripulants = arrel.getChildElements("tripulantCabina");
  		for (int i = 0; i < tripulants.size(); i++) {
  			tripulant = tripulants.get(i);
  			companyia.afegirTripulantCabina(new TripulantCabina(tripulant.getAttributeValue("passaport"), tripulant.getAttributeValue("nom"),
  					Integer.parseInt(tripulant.getAttributeValue("edat")), Integer.parseInt(tripulant.getAttributeValue("horesVol")),
  					tripulant.getAttributeValue("rang")));
  		}


  		Elements vols = arrel.getChildElements("vol");
  		Element vol = null;
  		for (int i = 0; i < vols.size(); i++) {
  			vol = vols.get(i);
  			Vol nouVol = new Vol(vol.getAttributeValue("codi"), dataFormat.parse(vol.getAttributeValue("dataSortida")),
  					dataFormat.parse(vol.getAttributeValue("dataArribada")), LocalTime.parse(vol.getAttributeValue("horaSortida")),
  					LocalTime.parse(vol.getAttributeValue("horaArribada")));

  			Elements llistaTripulants = vol.getChildElements("TCP");
  			for (int j = 0; j < llistaTripulants.size(); j++) {
  				tripulant = llistaTripulants.get(i);
  				nouVol.afegirTripulant(new TCP(tripulant.getAttributeValue("passaport"), tripulant.getAttributeValue("nom"),
  						Integer.parseInt(tripulant.getAttributeValue("edat")), Integer.parseInt(tripulant.getAttributeValue("horesVol"))));
  			}

  			llistaTripulants = vol.getChildElements("tripulantCabina");
  			for (int j = 0; j < llistaTripulants.size(); j++) {
  				tripulant = llistaTripulants.get(i);
  				nouVol.afegirTripulant(new TripulantCabina(tripulant.getAttributeValue("passaport"), tripulant.getAttributeValue("nom"),
  						Integer.parseInt(tripulant.getAttributeValue("edat")), Integer.parseInt(tripulant.getAttributeValue("horesVol")),
  						tripulant.getAttributeValue("rang")));
  			}
  			companyia.afegirVol(nouVol);
  		}


  		Elements llistaRutes = arrel.getChildElements("rutaNacional");
  		Element ruta = null;
  		for (int i = 0; i < llistaRutes.size(); i++) {
  			ruta = llistaRutes.get(i);
  			companyia.afegirRutaNacional(
  					new RutaNacional(ruta.getAttributeValue("codi"), ruta.getAttributeValue("pais"), ruta.getAttributeValue("aeroportOri"),
  							ruta.getAttributeValue("aeroportDes"), Integer.parseInt(ruta.getAttributeValue("distancia"))));
  		}

  		llistaRutes = arrel.getChildElements("rutaInternacional");
  		for (int i = 0; i < llistaRutes.size(); i++) {
  			ruta = llistaRutes.get(i);
  			companyia.afegirRutaInternacional(new RutaInternacional(ruta.getAttributeValue("codi"), ruta.getAttributeValue("aeroportOri"),
  					ruta.getAttributeValue("aeroportDes"), ruta.getAttributeValue("paisOri"), ruta.getAttributeValue("paisDes"),
  					Integer.parseInt(ruta.getAttributeValue("distancia"))));
  		}

  		llistaRutes = arrel.getChildElements("rutaContinental");
  		for (int i = 0; i < llistaRutes.size(); i++) {
  			ruta = llistaRutes.get(i);
  			companyia.afegirRutaIntercontinental(new RutaIntercontinental(ruta.getAttributeValue("codi"), ruta.getAttributeValue("aeroportOri"),
  					ruta.getAttributeValue("aeroportDes"), ruta.getAttributeValue("paisOri"), ruta.getAttributeValue("paisDes"),
  					ruta.getAttributeValue("continentOri"), ruta.getAttributeValue("continentDes"),
  					Integer.parseInt(ruta.getAttributeValue("distancia"))));
  		}

  		llistaRutes = arrel.getChildElements("rutaTransoceanica");
  		llistaRutes = arrel.getChildElements("rutaContinental");
  		for (int i = 0; i < llistaRutes.size(); i++) {
  			ruta = llistaRutes.get(i);
  			companyia.afegirRutaTransoceanica(new RutaTransoceanica(ruta.getAttributeValue("codi"), ruta.getAttributeValue("aeroportOri"),
  					ruta.getAttributeValue("aeroportDes"), ruta.getAttributeValue("paisOri"), ruta.getAttributeValue("paisDes"),
  					ruta.getAttributeValue("continentOri"), ruta.getAttributeValue("continentDes"), ruta.getAttributeValue("ocea"),
  					Integer.parseInt(ruta.getAttributeValue("distancia"))));
  		}
  	}
}
