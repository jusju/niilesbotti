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
	// V��RI� KOMENTOJA ANNETTAESSA UUTTA VIESTI�?
	
	// S��OHEJLMA ANTAA JSON TIEDOSTON
	// K�SITTELY S��TIEDOT LUOKASSA - MIT� HALUTAAN TIET��
	// TIEDOSTO SIS�LT�� IKONIN NUMERON (ID) - T�M�N VOISI LIS�T� S��TIETOJA K�YTT�J�LLE ANNETTAESSA
	
	// TEKISIN VIEL� TOISIN TUON /KRISSEPAID => OMA CONTROLLERI JOTA AINA SITTEN KUTSUTAAN HARJOITTELU_BOT LUOKASTA

	public void onUpdateReceived(Update update) {
		
		DecimalFormat des = new DecimalFormat("0.00");
		String[] kuukaudet = {"tammikuu", "helmikuu", "maaliskuu", "huhtikuu", "toukokuu", "kes�kuu", "hein�kuu", "elokuu", "syyskuu", "lokakuu", "marraskuu", "joulukuu"};
		// TODO Auto-generated method stub
		String command = update.getMessage().getText();
		SendMessage mes = new SendMessage();
		mes.setChatId(String.valueOf(update.getMessage().getChatId()));
						
		Kirjoittaja kirjoittaja = new Kirjoittaja();
		
		
		
		// VIEL� EI K�SITELTY VIRHEIT� ALLA OLEVASSA KOODISSA
		// LIIAN PALJON TOISTOA KOODISSA! SIIT� VARMASTI TULEE MIINUSTA!
		// T�M� ALLA OLEVA TULISI SIIRT�� OMAAN LUOKKAANSA SELKEYDEN VUOKSI! - TEHD��N ARVOSTELUN J�LKEEN
		
		// jaetaan komento v�lily�nnill� taulukkoon
		if (command.contains(" ")) {
			String[] komento = command.split(" ");
			String tarkastus = komento[0];
			String krisse = "/krissepaid";
			
			// uuden maksun lis��minen
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
					mes.setText("Ei viel� maksuja tallennettu");
				} else {
					for (int i = 0; i < maksut.size(); i++) {
						viesti += maksut.get(i).tuoNaytolle() + "\n";
					}
					mes.setText(viesti);
					
				}
				
				
			// Kaikki maksujen summat yhteens� - ei huomioi vuotta, kuukautta tai henkil��	
			} else if (komento[1].equals("sum") && tarkastus.equals(krisse)) {			
				mes.setText("Kaikki maksut t�h�n menness� yhteens�: " + des.format(kirjoittaja.kaikkiMaksutYhteensa()) + "�");
				
			// Tietyn henkil�n summat yhteens�	
			} else if (komento[1].equals("search") && tarkastus.equals(krisse)) {
				String maksaja = komento[2];
				String viesti = "";
				
				// Ei huomioida vuotta (komennon pituus 3 sanaa)
				if (komento.length == 3) {
					//mes.setText("Henkil�n " + maksaja + " maksut yhteens� ovat " + des.format(kirjoittaja.tietynHenkilonMaksut(maksaja)) + "�");
					viesti = "Henkil�n " + maksaja + " maksut yhteens� ovat " + des.format(kirjoittaja.tietynHenkilonMaksut(maksaja)) + "�\n";
					List<Maksu> maksut = kirjoittaja.listaHenkilonMaksuista(maksaja);
					for (int i = 0; i < maksut.size(); i++) {
						viesti += maksut.get(i).tuoNaytolle() + "\n";
					}
					mes.setText(viesti);
				// Huomioidaan vuosi (komennon pituus 4 sanaa)
				} else if (komento.length == 4) {
					//mes.setText("Henkil�n " + maksaja +  " maksut yhteens� olivat " + des.format(kirjoittaja.tietynHenkilonMaksutVuodessa(maksaja, Integer.valueOf(komento[3]))) + "� vuonna " + Integer.valueOf(komento[3]));
					viesti = "Henkil�n " + maksaja +  " maksut yhteens� olivat " + des.format(kirjoittaja.tietynHenkilonMaksutVuodessa(maksaja, Integer.valueOf(komento[3]))) + "� vuonna " + Integer.valueOf(komento[3]) + "\n";
					List<Maksu> maksut = kirjoittaja.listaHenkilonMaksuistaVuodessa(maksaja, Integer.valueOf(komento[3]));
					for (int i = 0; i < maksut.size(); i++) {
						viesti += maksut.get(i).tuoNaytolle() + "\n";
					}
					mes.setText(viesti);
				}
			
			// Etsit��n tietyn kuukauden ja vuoden / pelk�n tietyn vuoden maksut yhteens� (ei huomioi henkil��)	
			} else if (komento[1].equals("find") && tarkastus.equals(krisse)) {
				String viesti = "";
				if (komento.length == 3) {
					int vuosi = Integer.valueOf(komento[2]);
					viesti = "Vuoden " + vuosi + " maksut olivat yhteens� " + des.format(kirjoittaja.vuodenMaksutYhteensa(vuosi)) + "\n";
					List<Maksu> maksut = kirjoittaja.listaTietynVuodenMaksuista(vuosi);
					for (int i = 0; i < maksut.size(); i++) {
						viesti += maksut.get(i).tuoNaytolle() + "\n";
					}
					mes.setText(viesti);
					//mes.setText("Vuoden " + vuosi + " maksut olivat yhteens� " + des.format(kirjoittaja.vuodenMaksutYhteensa(vuosi)));
				} else if (komento.length == 4) {
					int kuukausi = Integer.valueOf(komento[2]);
					int vuosi = Integer.valueOf(komento[3]);
					viesti = "Vuoden " + vuosi + " " + kuukaudet[kuukausi - 1] + "ssa maksut olivat yhteens� " + des.format(kirjoittaja.kuukaudenMaksutYhteensa(kuukausi, vuosi)) + "\n";
					List<Maksu> maksut = kirjoittaja.listaTietynKuukaudenMaksuista(kuukausi, vuosi);
					for (int i = 0; i < maksut.size(); i++) {
						viesti += maksut.get(i).tuoNaytolle() + "\n";
					}
					mes.setText(viesti);
					//mes.setText("Vuoden " + vuosi + " " + kuukaudet[kuukausi - 1] + "ssa maksut olivat yhteens� " + des.format(kirjoittaja.kuukaudenMaksutYhteensa(kuukausi, vuosi)));
				}
				
			// Tyhjennet��n kaikki maksut - aloitetaan alusta	
			} else if (komento[1].equals("clean") && komento[2].equals("all") && tarkastus.equals(krisse)) {
				kirjoittaja.tyhjennaMaksut();
				mes.setText("Kaikki maksut nollattu, voit aloittaa alusta maksujen seuraamisen");
				
			// Poistetaan yksi maksu tiedostosta	
			} else if (komento[1].equals("delete") && tarkastus.equals(krisse)) {
				kirjoittaja.poistaRivi(Integer.valueOf(komento[2]));
				// System.out.println(krisse +" " + komento[1] + Integer.valueOf(komento[2]));
				mes.setText("Haluttu maksu poistettu!");
				
			// Etsit��n tietyn kuukauden maksut yhteens�	
			} else if (komento[1].equals("count") && tarkastus.equals(krisse)) {
				int kuukausi = Integer.valueOf(komento[2]);
				int vuosi = Integer.valueOf(komento[3]);
				double kuukaudenMaksut = kirjoittaja.kuukaudenMaksutYhteensa(kuukausi, vuosi);
				mes.setText("Maksut vuoden " + vuosi + " " + kuukaudet[kuukausi - 1] + "ssa olivat " + des.format(kuukaudenMaksut) + "�");
			}
		}
		
		if (command.equals("/tekija")) {
			mes.setText("T�m�n botin teki Niiles Kari!");
		}
		
		if (command.equals("/krissepaid")) {
			String viesti = "Kaikki komennot:\nAloita komento /krissepaid\nLis�� maksu: sana maksu + kuvaus + summa\nPoista maksu: delete + monesko rivi\nListaa kaikki maksut: list all\nKaikki maksut yhteens�: sum all\nSummaa kuukauden maksut: count + 2 2020\n"
					+ "Etsi kuukauden tai vuoden maksut: esim. find + 2 2020 tai find 2020\nEtsi tietyn henkilon maksut: search + Kristiina (tai search + Kristiina + vuosi)\nAloita alusta: clean all";
			mes.setText(viesti);
		}
		
		if (command.equals("/weather")) {
			// S��tietojen hakeminen Helsinki
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
