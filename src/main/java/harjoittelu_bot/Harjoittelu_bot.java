package harjoittelu_bot;

import java.text.DecimalFormat;
import java.util.List;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class Harjoittelu_bot extends TelegramLongPollingBot {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		

	}	
	
	// TO DO
	// SUORITETTUJEN MAKSUJEN LISTAAN RIVIN NUMERO => HELPOTTAA POISTAMISTA
	// ANNETTUJEN KOMENTOJEN VARMISTAMINEN ( IF KOMENTO.LENGTH == 3 && KOMENTO[1].EQUALS() )
	// VÄÄRIÄ KOMENTOJA ANNETTAESSA UUTTA VIESTIÄ?
	
	// SÄÄOHEJLMA ANTAA JSON TIEDOSTON
	// KÄSITTELY SÄÄTIEDOT LUOKASSA - MITÄ HALUTAAN TIETÄÄ
	// TIEDOSTO SISÄLTÄÄ IKONIN NUMERON (ID) - TÄMÄN VOISI LISÄTÄ SÄÄTIETOJA KÄYTTÄJÄLLE ANNETTAESSA
	
	// TEKISIN VIELÄ TOISIN TUON /KRISSEPAID => OMA CONTROLLERI JOTA AINA SITTEN KUTSUTAAN HARJOITTELU_BOT LUOKASTA

	public void onUpdateReceived(Update update) {
		
		DecimalFormat des = new DecimalFormat("0.00");
		String[] kuukaudet = {"tammikuu", "helmikuu", "maaliskuu", "huhtikuu", "toukokuu", "kesäkuu", "heinäkuu", "elokuu", "syyskuu", "lokakuu", "marraskuu", "joulukuu"};
		// TODO Auto-generated method stub
		String command = update.getMessage().getText();
		SendMessage mes = new SendMessage();
		mes.setChatId(String.valueOf(update.getMessage().getChatId()));
						
		Kirjoittaja kirjoittaja = new Kirjoittaja();
		
		
		
		// VIELÄ EI KÄSITELTY VIRHEITÄ ALLA OLEVASSA KOODISSA
		// LIIAN PALJON TOISTOA KOODISSA! SIITÄ VARMASTI TULEE MIINUSTA!
		// TÄMÄ ALLA OLEVA TULISI SIIRTÄÄ OMAAN LUOKKAANSA SELKEYDEN VUOKSI! - TEHDÄÄN ARVOSTELUN JÄLKEEN
		
		// jaetaan komento välilyönnillä taulukkoon
		if (command.contains(" ")) {
			String[] komento = command.split(" ");
			String tarkastus = komento[0];
			String krisse = "/krissepaid";
			
			// uuden maksun lisääminen
			if (komento[1].equals("maksu") && tarkastus.equals(krisse)) {
				//double summa = Double.valueOf(komento[komento.length - 1]);
				String maara = komento[komento.length - 1];
				if (maara.contains(",")) {
					maara = maara.replace(",", ".");
				}
				double summa = Double.valueOf(maara);
				String tallentaja = update.getMessage().getFrom().getFirstName();
				String kuvaus = "";
				for (int i = 2; i < komento.length - 1; i++) {
					kuvaus += komento[i] + " ";
				}
				kirjoittaja.kirjoitaUusi(new Maksu(summa, kuvaus, tallentaja));
				mes.setText("Uusi maksu tallennettu!");
				
			// Listataan kaikki maksut tiedostosta	
			} else if (komento[1].equals("list") && komento[2].equals("all") && tarkastus.equals(krisse)) {
				List<Maksu> maksut = kirjoittaja.lueTiedosto();
				String viesti = "Kaikki maksut:\n";
				if (maksut.isEmpty()) {
					mes.setText("Ei vielä maksuja tallennettu");
				} else {
					for (int i = 0; i < maksut.size(); i++) {
						viesti += maksut.get(i).tuoNaytolle() + "\n";
					}
					mes.setText(viesti);
					
				}
				
				
			// Kaikki maksujen summat yhteensä - ei huomioi vuotta, kuukautta tai henkilöä	
			} else if (komento[1].equals("sum") && tarkastus.equals(krisse)) {			
				mes.setText("Kaikki maksut tähän mennessä yhteensä: " + des.format(kirjoittaja.kaikkiMaksutYhteensa()) + "€");
				
			// Tietyn henkilön summat yhteensä	
			} else if (komento[1].equals("search") && tarkastus.equals(krisse)) {
				String maksaja = komento[2];
				String viesti = "";
				
				// Ei huomioida vuotta (komennon pituus 3 sanaa)
				if (komento.length == 3) {
					//mes.setText("Henkilön " + maksaja + " maksut yhteensä ovat " + des.format(kirjoittaja.tietynHenkilonMaksut(maksaja)) + "€");
					viesti = "Henkilön " + maksaja + " maksut yhteensä ovat " + des.format(kirjoittaja.tietynHenkilonMaksut(maksaja)) + "€\n";
					List<Maksu> maksut = kirjoittaja.listaHenkilonMaksuista(maksaja);
					for (int i = 0; i < maksut.size(); i++) {
						viesti += maksut.get(i).tuoNaytolle() + "\n";
					}
					mes.setText(viesti);
				// Huomioidaan vuosi (komennon pituus 4 sanaa)
				} else if (komento.length == 4) {
					//mes.setText("Henkilön " + maksaja +  " maksut yhteensä olivat " + des.format(kirjoittaja.tietynHenkilonMaksutVuodessa(maksaja, Integer.valueOf(komento[3]))) + "€ vuonna " + Integer.valueOf(komento[3]));
					viesti = "Henkilön " + maksaja +  " maksut yhteensä olivat " + des.format(kirjoittaja.tietynHenkilonMaksutVuodessa(maksaja, Integer.valueOf(komento[3]))) + "€ vuonna " + Integer.valueOf(komento[3]) + "\n";
					List<Maksu> maksut = kirjoittaja.listaHenkilonMaksuistaVuodessa(maksaja, Integer.valueOf(komento[3]));
					for (int i = 0; i < maksut.size(); i++) {
						viesti += maksut.get(i).tuoNaytolle() + "\n";
					}
					mes.setText(viesti);
				}
			
			// Etsitään tietyn kuukauden ja vuoden / pelkän tietyn vuoden maksut yhteensä (ei huomioi henkilöä)	
			} else if (komento[1].equals("find") && tarkastus.equals(krisse)) {
				String viesti = "";
				if (komento.length == 3) {
					int vuosi = Integer.valueOf(komento[2]);
					viesti = "Vuoden " + vuosi + " maksut olivat yhteensä " + des.format(kirjoittaja.vuodenMaksutYhteensa(vuosi)) + "\n";
					List<Maksu> maksut = kirjoittaja.listaTietynVuodenMaksuista(vuosi);
					for (int i = 0; i < maksut.size(); i++) {
						viesti += maksut.get(i).tuoNaytolle() + "\n";
					}
					mes.setText(viesti);
					//mes.setText("Vuoden " + vuosi + " maksut olivat yhteensä " + des.format(kirjoittaja.vuodenMaksutYhteensa(vuosi)));
				} else if (komento.length == 4) {
					int kuukausi = Integer.valueOf(komento[2]);
					int vuosi = Integer.valueOf(komento[3]);
					viesti = "Vuoden " + vuosi + " " + kuukaudet[kuukausi - 1] + "ssa maksut olivat yhteensä " + des.format(kirjoittaja.kuukaudenMaksutYhteensa(kuukausi, vuosi)) + "\n";
					List<Maksu> maksut = kirjoittaja.listaTietynKuukaudenMaksuista(kuukausi, vuosi);
					for (int i = 0; i < maksut.size(); i++) {
						viesti += maksut.get(i).tuoNaytolle() + "\n";
					}
					mes.setText(viesti);
					//mes.setText("Vuoden " + vuosi + " " + kuukaudet[kuukausi - 1] + "ssa maksut olivat yhteensä " + des.format(kirjoittaja.kuukaudenMaksutYhteensa(kuukausi, vuosi)));
				}
				
			// Tyhjennetään kaikki maksut - aloitetaan alusta	
			} else if (komento[1].equals("clean") && komento[2].equals("all") && tarkastus.equals(krisse)) {
				kirjoittaja.tyhjennaMaksut();
				mes.setText("Kaikki maksut nollattu, voit aloittaa alusta maksujen seuraamisen");
				
			// Poistetaan yksi maksu tiedostosta	
			} else if (komento[1].equals("delete") && tarkastus.equals(krisse)) {
				kirjoittaja.poistaRivi(Integer.valueOf(komento[2]));
				// System.out.println(krisse +" " + komento[1] + Integer.valueOf(komento[2]));
				mes.setText("Haluttu maksu poistettu!");
				
			// Etsitään tietyn kuukauden maksut yhteensä	
			} else if (komento[1].equals("count") && tarkastus.equals(krisse)) {
				int kuukausi = Integer.valueOf(komento[2]);
				int vuosi = Integer.valueOf(komento[3]);
				double kuukaudenMaksut = kirjoittaja.kuukaudenMaksutYhteensa(kuukausi, vuosi);
				mes.setText("Maksut vuoden " + vuosi + " " + kuukaudet[kuukausi - 1] + "ssa olivat " + des.format(kuukaudenMaksut) + "€");
			}
		}
		
		if (command.equals("/tekija")) {
			mes.setText("Tämän botin teki Niiles Kari!");
		}
		
		if (command.equals("/krissepaid")) {
			String viesti = "Kaikki komennot:\nAloita komento /krissepaid\nLisää maksu: sana maksu + kuvaus + summa\nPoista maksu: delete + monesko rivi\nListaa kaikki maksut: list all\nKaikki maksut yhteensä: sum all\nSummaa kuukauden maksut: count + 2 2020\n"
					+ "Etsi kuukauden tai vuoden maksut: esim. find + 2 2020 tai find 2020\nEtsi tietyn henkilon maksut: search + Kristiina (tai search + Kristiina + vuosi)\nAloita alusta: clean all";
			mes.setText(viesti);
		}
		
		if (command.equals("/weather")) {
			// Säätietojen hakeminen Helsinki
			Saatiedot saatiedot = new Saatiedot();
			String saa = null;
			try {
				saa = saatiedot.getSaa();
			} catch (Exception e){
				e.printStackTrace();
			}
			mes.setText(saa);
		}
		
		if (mes.getText() != null) {
			
			try {
				execute(mes);
			} catch (Exception e) {
				e.printStackTrace();
				
			}
		}
		
		
		
		
		
	}

	public String getBotUsername() {
		// TODO Auto-generated method stub
		return "harjoittelu_bot";
	}

	@Override
	public String getBotToken() {
		// TODO Auto-generated method stub
		return "1468026284:AAFisU-bMkrb8pC0II5066ZZaQVRceg4ZGE";
	}
		

}
