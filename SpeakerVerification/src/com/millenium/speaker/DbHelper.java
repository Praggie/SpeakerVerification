package com.millenium.speaker;

import java.io.File;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;

public class DbHelper {

	private static final String DATABASE = "/opt/datos/database";

	public static void main(String[] args) throws SQLiteException {
		SQLiteConnection db = new SQLiteConnection(new File(DATABASE));
		db.open(true);

		SQLiteStatement st0 = db.prepare("CREATE TABLE if NOT EXISTS puntajes (cedula varchar(30), umbral real, media real, desviacion real)");
		st0.step();

		st0 = db.prepare("CREATE TABLE if NOT EXISTS scores (cedula varchar(30), grabacion varchar(30), valor real)");
		st0.step();

		/*SQLiteStatement st = db.prepare("INSERT INTO persona (cedula, umbral) VALUES (?,?)");
		try {
			st.bind(1, "84454757");
			st.bind(2, 1.5f);
			st.step();

			st = db.prepare("SELECT umbral FROM persona where cedula=?");
			st.bind(1, "84454757");
			st.step();
			if (st.hasRow()) {
				System.out.println("Umbral: " + st.columnValue(0));
			}

		} finally {
			st.dispose();
		}*/
		db.dispose();
	}

	public static void saveStats(String cedula, double media, double desviacion) {
		SQLiteConnection db = new SQLiteConnection(new File(DATABASE));
		try {
			db.open(true);
			SQLiteStatement st = db.prepare("SELECT COUNT(*) FROM puntajes where cedula=?");
			st.bind(1, cedula);
			st.step();
			if(st.columnInt(0) > 0) {
				st = db.prepare("UPDATE puntajes SET media=?, desviacion=?  WHERE cedula=?");
				st.bind(1, media);
				st.bind(2, desviacion);
				st.bind(3, cedula);
			} else {
				st = db.prepare("INSERT INTO puntajes (cedula, media, desviacion) VALUES (?,?,?)");
				st.bind(1, cedula);
				st.bind(2, media);
				st.bind(3, desviacion);
			}
			st.step();
			st.dispose();
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			db.dispose();
		}
	}

	public static void clearScores() {
		SQLiteConnection db = new SQLiteConnection(new File(DATABASE));
		try {
			db.open(true);
			SQLiteStatement st = db.prepare("DELETE FROM scores");
			st.step();
			st.dispose();

		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			db.dispose();
		}
	}

	public static void saveScore(String cedula, String grabacion, float valor) {
		SQLiteConnection db = new SQLiteConnection(new File(DATABASE));
		try {
			db.open(true);
			SQLiteStatement st = db.prepare("INSERT INTO scores (cedula, grabacion, valor) VALUES (?,?,?)");
			st.bind(1, cedula);
			st.bind(2, grabacion);
			st.bind(3, valor);
			st.step();
			st.dispose();

		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			db.dispose();
		}
	}

	public static void saveUmbral(String cedula, double umbral) {
		SQLiteConnection db = new SQLiteConnection(new File(DATABASE));
		try {
			db.open(true);
			SQLiteStatement st = db.prepare("SELECT COUNT(*) FROM puntajes where cedula=?");
			st.bind(1, cedula);
			st.step();
			if(st.columnInt(0) > 0) {
				st = db.prepare("UPDATE puntajes SET umbral=? WHERE cedula=?");
				st.bind(1, umbral);
				st.bind(2, cedula);
			} else {
				st = db.prepare("INSERT INTO puntajes (cedula, umbral) VALUES (?,?)");
				st.bind(1, cedula);
				st.bind(2, umbral);
			}
			st.step();
			st.dispose();

		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			db.dispose();
		}
	}

	public static double getUmbral(String cedula) {
		SQLiteConnection db = new SQLiteConnection(new File(DATABASE));
		try {
			db.open(true);
			SQLiteStatement st = db.prepare("SELECT umbral FROM puntajes where cedula=?");
			st.bind(1, cedula);
			if (st.step()) {
				return st.columnDouble(0);
			}

		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			db.dispose();
		}
		return 0d;
	}

	public static double getFirstScore(String cedula) {
		SQLiteConnection db = new SQLiteConnection(new File(DATABASE));
		try {
			db.open(true);
			SQLiteStatement st = db.prepare("SELECT valor FROM scores where  cedula=? and grabacion like  cedula || '_%' ");
			st.bind(1, cedula);
			if (st.step()) {
				return st.columnDouble(0);
			}

		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			db.dispose();
		}
		return 0d;
	}

	public static double[] getStats(String cedula) {
		SQLiteConnection db = new SQLiteConnection(new File(DATABASE));
		try {
			db.open(true);
			SQLiteStatement st = db.prepare("SELECT media, desviacion FROM puntajes where cedula=?");
			st.bind(1, cedula);
			if (st.step()) {
				return new double[]{st.columnDouble(0), st.columnDouble(1)};
			}

		} catch (SQLiteException e) {
			e.printStackTrace();

		} finally {
			db.dispose();
		}
		return new double[]{0d,1d};
	}

}
