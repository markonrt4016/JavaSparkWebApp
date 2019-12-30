package main;

import static spark.Spark.*;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.nio.file.Files;
import javax.servlet.MultipartConfigElement;

import org.mozilla.javascript.ast.ForInLoop;

import com.google.gson.Gson;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;
import java.io.*;



public class Launcher {

	public static void main(String[] args) {
		port(5000);
		
		
		File uploadDir = new File("./src/public/img");
        uploadDir.mkdir(); // create the upload directory if it doesn't exist

		staticFiles.location("/public");
		
		HashMap<String,Object> polja = new HashMap<>();

		get("/", (request, response)->{
			ArrayList<Automobil> listaAutomobila = Data.readFromJSON("automobili.json");
			polja.put("automobil", listaAutomobila);
			return new ModelAndView(polja, "index.hbs");
		}, new HandlebarsTemplateEngine());
		
		get("/pretraga", (request, response) -> {
			return new ModelAndView(null, "pretraga.hbs");
		}, new HandlebarsTemplateEngine());
		
		post("/api/pretraga", (request, response) -> {
			String marka = request.queryParams("marka");
			String model = request.queryParams("model");
			Double kubOd = Double.parseDouble(request.queryParams("kubOd"));
			Double kubDo = Double.parseDouble(request.queryParams("kubDo"));
			ArrayList<Automobil> rezultatiPretrage = new ArrayList<>();
			
			for(Automobil a: Data.readFromJSON("automobili.json")) {
				if(a.getMarka().toLowerCase().contains(marka.toLowerCase()) && a.getModel().toLowerCase().contains(model.toLowerCase()) && (a.getKubikaza()>=kubOd&&a.getKubikaza()<=kubDo)) {
					rezultatiPretrage.add(a);
				}
					
			}
			Gson gson = new Gson();
			
			return gson.toJson(rezultatiPretrage);
		});
		
		get("/admin", (request, response) -> {
			return new ModelAndView(null, "admin.hbs");
		}, new HandlebarsTemplateEngine());
		
		post("/login", (request, response) -> {
			String username = request.queryParams("username");
			String password = request.queryParams("password");
			String message = "";
			if (username.equals("admin") && password.equals("password")) {
				message = "ok";
			} else {
				message = "greska";
			}
			return message;
		});
		
		get("/adminGlavniPanel", (request, response)->{
			return new ModelAndView(null, "adminGlavniPanel.hbs");
		}, new HandlebarsTemplateEngine());
		
		get("/dodajVozilo", (request, response)->{
			return new ModelAndView(null, "dodajVozilo.hbs");
		}, new HandlebarsTemplateEngine());
		
		post("/api/proveraDaLiPostoji", (request, response) -> {
			String marka = request.queryParams("marka");
			String model = request.queryParams("model");
			
			
			for(Automobil a:Data.readFromJSON("automobili.json")) {
				if(a.getMarka().toLowerCase().equals(marka.toLowerCase()) && a.getModel().toLowerCase().equals(model.toLowerCase())) {
					return "postoji";
				}
			}
			
			return "ne postoji";
		});
		
		post("/api/uploadSlike", (request, response) -> {
        	
            Path tempFile = Files.createTempFile(uploadDir.toPath(), "", "");

            request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));

            try (InputStream input = request.raw().getPart("uploaded_file").getInputStream()) { // getPart needs to use same "name" as input field in form
                Files.copy(input, tempFile, StandardCopyOption.REPLACE_EXISTING);
                
                
                TimeUnit.SECONDS.sleep(2);
                File slika = tempFile.toFile();
                
                String imeSlike = "./src/public/img/slika";
                
                int brojAutomobila = Data.readFromJSON("automobili.json").size();
                System.out.println(brojAutomobila);
                imeSlike+=brojAutomobila+".jpg";
                slika.renameTo(new File(imeSlike));
                
            }
    
            return "Uspesno uploadovana slika!";

        });
		
		post("/api/uploadKarakteristika", (request, response) -> {
	        ArrayList<Automobil> novaListaAutomobila = Data.readFromJSON("automobili.json");

	        
	        int id = novaListaAutomobila.size()+1;
	        String marka = request.queryParams("marka");
	        String model = request.queryParams("model");
	        Double kubikaza = Double.parseDouble(request.queryParams("kubikaza"));
			int snaga = Integer.parseInt(request.queryParams("snaga"));
			double cena = Double.parseDouble(request.queryParams("cena"));
			String karoserija = request.queryParams("karoserija");
			String gorivo = request.queryParams("gorivo");
			String slika = "img/slika";
			slika+=id+".jpg";
			
			novaListaAutomobila.add(new Automobil(id, snaga, kubikaza, cena, marka, model, slika, karoserija, gorivo));
			Data.writeToJSON(novaListaAutomobila, "automobili.json");
			return "";
		});
		
		
		get("/izmeniPodatke", (request, response)->{
			ArrayList<Automobil> listaAutomobila = Data.readFromJSON("automobili.json");
			polja.put("automobil", listaAutomobila);
			return new ModelAndView(polja, "izmeniPodatke.hbs");
		}, new HandlebarsTemplateEngine());
		
		post("/api/izmenaPretraga", (request, response)->{
			String model = request.queryParams("model");
			ArrayList<Automobil> lista = new ArrayList<>();
			
			for(Automobil a:Data.readFromJSON("automobili.json")) {
				if(a.getModel().toLowerCase().contains(model)) {
					lista.add(a);
				}
			}
			
			Gson gson = new Gson();
			
			return gson.toJson(lista);
		});
		
		
		
		post("/api/vratiPodatkeOVozilu", (request, response)->{
			int id = Integer.parseInt(request.queryParams("id"));
			
			Automobil nadjenAutomobil=new Automobil();
			
			for(Automobil a : Data.readFromJSON("automobili.json")) {
				if(a.getId()==id) {
					nadjenAutomobil = new Automobil(a);
					break;
				}
			}
			Gson gson = new Gson();
			
			return gson.toJson(nadjenAutomobil);
		});
		
		String imeSlikeZaZamenu = "./src/public/img/slikaZamena.jpg";
		
		post("/api/izmenaSlike", (request, response) -> {
        	
            Path tempFile = Files.createTempFile(uploadDir.toPath(), "", "");

            request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));

            try (InputStream input = request.raw().getPart("uploaded_file").getInputStream()) { // getPart needs to use same "name" as input field in form
                Files.copy(input, tempFile, StandardCopyOption.REPLACE_EXISTING);
                
                File slikaZamena = tempFile.toFile();
                
                slikaZamena.renameTo(new File(imeSlikeZaZamenu));
                
            }
         
            return "Uspesno postavlejan slika za zamenu!";

        });
		
		post("/api/izmenaKarakteristika", (request, response) -> {	
			
	        int id = Integer.parseInt(request.queryParams("id"));
	        String marka = request.queryParams("marka");
	        String model = request.queryParams("model");
	        Double kubikaza = Double.parseDouble(request.queryParams("kubikaza"));
			int snaga = Integer.parseInt(request.queryParams("snaga"));
			double cena = Double.parseDouble(request.queryParams("cena"));
			String karoserija = request.queryParams("karoserija");
			String gorivo = request.queryParams("gorivo");
			
			
			TimeUnit.SECONDS.sleep(2);
			
			File slikaZamena = new File("./src/public/img/slikaZamena.jpg");
			int vreme_spavanja = 2;
			
			String imeSlikeN = "./src/public/img/slika";
			imeSlikeN+=id;
			imeSlikeN+=".jpg";
			
			if(slikaZamena.exists()) {
			
			if(slikaZamena.length()>1000000) {
				TimeUnit.SECONDS.sleep(2);
				vreme_spavanja=3;
			}
				
				
			File staraSlika = new File(imeSlikeN);
			staraSlika.delete();
			
			slikaZamena.renameTo(new File(imeSlikeN));
			
			}
			imeSlikeN=imeSlikeN.replace("./src/public/", "");
			
			ArrayList<Automobil> lista= Data.readFromJSON("automobili.json");
			
			for (int i = 0; i < lista.size(); i++) {
				if(lista.get(i).getId()==id) {
					lista.set(i, new Automobil(id, snaga, kubikaza, cena, marka, model, imeSlikeN,
							karoserija, gorivo));
					break;
				}
			}
			
			Data.writeToJSON(lista, "automobili.json");
	
			return vreme_spavanja;
		});
		
		
	}

}
