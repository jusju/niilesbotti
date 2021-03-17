package harjoittelu_bot;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Kirjoittaja {
	
	private String tiedosto;
		
	public Kirjoittaja() {
		this.tiedosto = "maksut.csv";
	}
	
	// LIIAN PALJON TOISTOA KOODISSA! SIITÄ VARMASTI TULEE MIINUSTA!
	
	public void kirjoitaUusi(Maksu maksu) {
		
		List<Maksu> maksut = lueTiedosto();
		maksut.add(maksu);
		
		try (PrintWriter kirjoittaja = new PrintWriter(this.tiedosto)) {
			
			for (int i = 0; i < maksut.size(); i++) {
				kirjoittaja.println(maksut.get(i).toString());
			}
			
			kirjoittaja.flush();
			kirjoittaja.close();
			
			
		} catch (IOException e) {
			System.out.println(e);
		}
		
				
	}

	
	
	public void poistaRivi(int rivi) {
		List<Maksu> maksut = lueTiedosto();
		List<Maksu> kirjoitettavatMaksut = new ArrayList<>();

		for (int i = 0; i < maksut.size(); i++) {
			if (!(i == rivi - 1)) {
				kirjoitettavatMaksut.add(maksut.get(i));
			}
		}

		try (PrintWriter kirjoittaja = new PrintWriter(this.tiedosto)) {

			for (int i = 0; i < kirjoitettavatMaksut.size(); i++) {
				kirjoittaja.println(kirjoitettavatMaksut.get(i).toString());
			}

			kirjoittaja.flush();
			kirjoittaja.close();

		} catch (IOException e) {
			System.out.println(e);
		}

	}
	
	public void tyhjennaMaksut() {
		
		try (PrintWriter kirjoittaja = new PrintWriter(this.tiedosto)) {
			
			kirjoittaja.println("");
			
			kirjoittaja.flush();
			kirjoittaja.close();
			
			
		} catch (IOException e) {
			System.out.println(e);
		}
	}
	
	public List<Maksu> lueTiedosto() {
		List<Maksu> maksut = new ArrayList<>();
		
		try (Scanner lukija = new Scanner(new File(this.tiedosto))) {
			
			while (lukija.hasNextLine()) {
				String rivi = lukija.nextLine();
				String[] palat = rivi.split(";");
				LocalDate date = LocalDate.parse(palat[0]);
				double summa = Double.valueOf(palat[1]);
				String kuvaus = palat[2];
				String henkilo = palat[3];
				maksut.add(new Maksu(date, summa, kuvaus, henkilo));
				
			}
			
			lukija.close();
			
		} catch (Exception e) {
			System.out.println(e);
		}
		
		
		
		return maksut;
	}
	
	public double kaikkiMaksutYhteensa() {
		double summa = 0;
		
		List<Maksu> maksut = new ArrayList<>();
		maksut = lueTiedosto();
		
		for (int i = 0; i < maksut.size(); i++) {
			summa += maksut.get(i).getSumma();
		}
		
		return summa;
		
	}
	
	public double kuukaudenMaksutYhteensa(int kuukausi, int vuosi) {
		double summa = 0;
		
		List<Maksu> maksut = new ArrayList<>();
		maksut = lueTiedosto();
		
		for (int i = 0; i < maksut.size(); i++) {
			Maksu maksu = maksut.get(i);
			if (maksu.getKuukausi() == kuukausi && maksu.getVuosi() == vuosi) {
				summa += maksu.getSumma();
			}
		}
		
		return summa;
		
	}
	
	public double vuodenMaksutYhteensa(int vuosi) {
		double summa = 0;
		
		List<Maksu> maksut = new ArrayList<>();
		maksut = lueTiedosto();
		
		for (int i = 0; i < maksut.size(); i++) {
			Maksu maksu = maksut.get(i);
			if (maksu.getVuosi() == vuosi) {
				summa += maksu.getSumma();
			}
		}
		
		return summa;
		
	}
	
	
	public double tietynHenkilonMaksut(String henkilo) {
		double summa = 0;
				
		List<Maksu> maksut = new ArrayList<>();
		maksut = lueTiedosto();
		
		for (int i = 0; i < maksut.size(); i++) {
			Maksu maksu = maksut.get(i);
			if (maksu.getMaksaja().equals(henkilo)) {
				summa += maksu.getSumma();
			}
		}
		
		return summa;
	}
	
	public double tietynHenkilonMaksutVuodessa(String henkilo, int vuosi) {
		double summa = 0;
		
		List<Maksu> maksut = new ArrayList<>();
		maksut = lueTiedosto();
		
		for (int i = 0; i < maksut.size(); i++) {
			Maksu maksu = maksut.get(i);
			if (maksu.getMaksaja().equals(henkilo) && maksu.getVuosi() == vuosi) {
				summa += maksu.getSumma();
			}
		}
		
		return summa;
	}

	public List<Maksu> listaHenkilonMaksuistaVuodessa(String henkilo, int vuosi) {
		List<Maksu> maksut = new ArrayList<>();
		List<Maksu> henkilonMaksut = new ArrayList<>();
		maksut = lueTiedosto();
		
		for (int i = 0; i < maksut.size(); i++) {
			if (maksut.get(i).getMaksaja().equals(henkilo) && maksut.get(i).getVuosi() == vuosi) {
				henkilonMaksut.add(maksut.get(i));
			}
		}
		
		return henkilonMaksut;
		
	}
	
	public List<Maksu> listaHenkilonMaksuista(String henkilo) {
		List<Maksu> maksut = new ArrayList<>();
		List<Maksu> henkilonMaksut = new ArrayList<>();
		maksut = lueTiedosto();
		
		for (int i = 0; i < maksut.size(); i++) {
			if (maksut.get(i).getMaksaja().equals(henkilo)) {
				henkilonMaksut.add(maksut.get(i));
			}
		}
		
		return henkilonMaksut;
		
	}
	
	public List<Maksu> listaTietynKuukaudenMaksuista(int kuukausi, int vuosi) {
		List<Maksu> maksut = new ArrayList<>();
		List<Maksu> kuukaudenMaksut = new ArrayList<>();
		maksut = lueTiedosto();
		
		for (int i = 0; i < maksut.size(); i++) {
			if (maksut.get(i).getKuukausi() == kuukausi && maksut.get(i).getVuosi() == vuosi) {
				kuukaudenMaksut.add(maksut.get(i));
			}
		}
		
		return kuukaudenMaksut;
	}
	
	public List<Maksu> listaTietynVuodenMaksuista(int vuosi) {
		List<Maksu> maksut = new ArrayList<>();
		List<Maksu> vuodenMaksut = new ArrayList<>();
		maksut = lueTiedosto();
		
		for (int i = 0; i < maksut.size(); i++) {
			if (maksut.get(i).getVuosi() == vuosi) {
				vuodenMaksut.add(maksut.get(i));
			}
		}
		
		return vuodenMaksut;
	}
	
}














