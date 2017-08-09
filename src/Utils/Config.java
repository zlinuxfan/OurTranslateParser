package Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class Config {
	protected ResourceBundle rb;

	public Config() {
		//
	}

	public Config(String config) {
		try {
			rb = PropertyResourceBundle.getBundle(config);
		} catch (Exception exc) {
			throw new Error(" *** Error while reading " + config + ": "
					+ exc.getMessage());
		}
	}

	public Config(File path) {
		try {
			rb = new PropertyResourceBundle(new FileInputStream(path));
		} catch (Exception exc) {
			throw new Error(" *** Error while reading "
					+ path.getAbsolutePath() + ": " + exc.getMessage());
		}
	}

	public Config(InputStream is) {
		try {
			rb = new PropertyResourceBundle(is);
		} catch (Exception exc) {
			throw new Error(" *** Error while reading input stream: "
					+ exc.getMessage());
		}
	}

	public String read(String name) {
		try {
			return rb.getString(name);
		} catch (Exception e) {
			return "";
		}
	}

	public String read(String name, String defaultVal) {
		String val = read(name);
		return val.length() == 0 ? defaultVal : val;
	}

	public String readRequired(String name) {
		try {
			return rb.getString(name);
		} catch (Exception e) {
			throw new ExceptionInInitializerError("Variable '" + name
					+ "' not defined in the config file");
		}
	}

	public boolean readBoolean(String name) {
		return readBoolean(name, false);
	}

	public boolean readBoolean(String name, boolean defaultValue) {
		String value = read(name);
		if (value.length() == 0) {
			return defaultValue;
		}
		return "yes".equalsIgnoreCase(value) || "true".equalsIgnoreCase(value);
	}

	public int readInt(String name) {
		return parseInt(name, readRequired(name));
	}

	public int readInt(String name, int defaultValue) {
		String field = read(name);
		if (field.length() == 0) {
			return defaultValue;
		}
		return parseInt(name, field);
	}

	public long readLong(String name, long defaultValue) {
		String field = read(name);
		if (field.length() == 0) {
			return defaultValue;
		}
		return parseLong(name, field);
	}

	public String[] readArray(String name) {
		try {
			String str = rb.getString(name);
			String[] strings = str.split(",");
			return strings;
		} catch (Exception e) {
			return new String[0];
		}
	}

	public int[] readIntArray(String name, int[] defaultValue) {
		String[] fields = readArray(name);
		if (fields.length == 0) {
			return defaultValue;
		}
		int[] arr = new int[fields.length];
		for (int i = 0; i < fields.length; i++) {
			arr[i] = parseInt(name, fields[i]);
		}
		return arr;
	}

	public double readDouble(String name) {
		return parseDouble(name, readRequired(name));
	}

	public double readDouble(String name, double defaultValue) {
		String field = read(name);
		if (field.length() == 0) {
			return defaultValue;
		}
		return parseDouble(name, field);
	}

	public long parseLong(String name, String value) {
		try {
			return Long.parseLong(value.trim());
		} catch (Exception e) {
			throw new ExceptionInInitializerError("Invalid value for field: "
					+ name + " - it has to be integer");
		}
	}

	public int parseInt(String name, String value) {
		try {
			return Integer.parseInt(value.trim());
		} catch (Exception e) {
			throw new ExceptionInInitializerError("Invalid value for field: "
					+ name + " - it has to be integer");
		}
	}

	public double parseDouble(String name, String value) {
		try {
			return Double.parseDouble(value.trim());
		} catch (Exception e) {
			throw new ExceptionInInitializerError("Invalid value for field: "
					+ name + " - it has to be double");
		}
	}
}
