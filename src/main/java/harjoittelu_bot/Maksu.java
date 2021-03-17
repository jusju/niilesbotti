package harjoittelu_bot;

import java.text.DecimalFormat;
import java.time.LocalDate;

public class Maksu {

	private String maksaja;
	private LocalDate date;
	private double summa;
	private String kuvaus;
	
	public Maksu(double summa, String kuvaus, String maksaja) {
		this.maksaja = maksaja;		
		this.summa = summa;
		this.kuvaus = kuvaus;
		this.date = LocalDate.now();
	}
	
	public Maksu(LocalDate date, double summa, String kuvaus, String maksaja) {
		this.maksaja = maksaja;		
		this.summa = summa;
		this.kuvaus = kuvaus;
		this.date = date;
	}
	
	@Override
	public String toString() {
		return this.date + ";" + this.summa + ";" + this.kuvaus + ";" + this.maksaja;
	}
	
	public String tuoNaytolle() {
		DecimalFormat des = new DecimalFormat("0.00");
		return this.date + "\t" + des.format(this.summa) + "€\t\t" + this.kuvaus + "\t\t" + "(" + this.maksaja + ")";
	}
	
	public double getSumma() {
		return this.summa;
	}
	
	public int getVuosi() {
		return this.date.getYear();
	}
	
	public int getKuukausi() {
		return this.date.getMonthValue();
	}
	
	public String getMaksaja() {
		return this.maksaja;
	}
	
}
