package simulation;

import OSPABA.*;

public class Mc extends IdList
{
	//meta! userInfo="Generated code: do not modify", tag="begin"
	public static final int init = 1001;
	public static final int prichodZakaznika = 1002;
	public static final int odchodZakaznika = 1003;
	public static final int zmenaRadu = 1011;
	public static final int uzavri = 1012;
	public static final int obsluhaRecepia = 1004;
	public static final int obsluhaUcesy = 1005;
	public static final int obsluhaLicenie = 1006;
	public static final int obsluhaZakaznika = 1007;
	public static final int parkovanie = 1008;
	//meta! tag="end"

	// 1..1000 range reserved for user
	public static final int novyZakaznik = 1;
	public static final int koniecObsluhyRecepcia = 2;
	public static final int koniecObsluhyUcesy = 3;
	public static final int koniecObsluhyLicenie = 4;
	public static final int zatvorenie = 5;
	public static final int parkuj = 6;
	public static final int novyZakaznikAutom = 7;
	public static final int koniecJazdy = 8;
	public static final int koniecChodze = 9;
	public static final int koniecRampy = 10;
}