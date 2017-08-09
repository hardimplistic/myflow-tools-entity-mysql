package cloud.tools.mysql.jpa.entity;

import javax.persistence.*;

@Entity
@Table(name = "COLUMNS")
public class Columns {

	@Id
	private int id;

	private String TABLE_CATALOG;
	private String TABLE_SCHEMA;
	private String TABLE_NAME;
	private String COLUMN_NAME;
	private long ORDINAL_POSITION;
	private String COLUMN_DEFAULT;
	private String IS_NULLABLE;
	private String DATA_TYPE;
	private long CHARACTER_MAXIMUM_LENGTH;
	private long CHARACTER_OCTET_LENGTH;
	private long NUMERIC_PRECISION;
	private long NUMERIC_SCALE;
	private long DATETIME_PRECISION;
	private String CHARACTER_SET_NAME;
	private String COLLATION_NAME;
	private String COLUMN_TYPE;
	private String COLUMN_KEY;
	private String EXTRA;
	private String PRIVILEGES;
	private String COLUMN_COMMENT;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTABLE_CATALOG() {
		return TABLE_CATALOG;
	}

	public void setTABLE_CATALOG(String tABLE_CATALOG) {
		TABLE_CATALOG = tABLE_CATALOG;
	}

	public String getTABLE_SCHEMA() {
		return TABLE_SCHEMA;
	}

	public void setTABLE_SCHEMA(String tABLE_SCHEMA) {
		TABLE_SCHEMA = tABLE_SCHEMA;
	}

	public String getTABLE_NAME() {
		return TABLE_NAME;
	}

	public void setTABLE_NAME(String tABLE_NAME) {
		TABLE_NAME = tABLE_NAME;
	}

	public String getCOLUMN_NAME() {
		return COLUMN_NAME;
	}

	public void setCOLUMN_NAME(String cOLUMN_NAME) {
		COLUMN_NAME = cOLUMN_NAME;
	}

	public long getORDINAL_POSITION() {
		return ORDINAL_POSITION;
	}

	public void setORDINAL_POSITION(long oRDINAL_POSITION) {
		ORDINAL_POSITION = oRDINAL_POSITION;
	}

	public String getCOLUMN_DEFAULT() {
		return COLUMN_DEFAULT;
	}

	public void setCOLUMN_DEFAULT(String cOLUMN_DEFAULT) {
		COLUMN_DEFAULT = cOLUMN_DEFAULT;
	}

	public String getIS_NULLABLE() {
		return IS_NULLABLE;
	}

	public void setIS_NULLABLE(String iS_NULLABLE) {
		IS_NULLABLE = iS_NULLABLE;
	}

	public String getDATA_TYPE() {
		return DATA_TYPE;
	}

	public void setDATA_TYPE(String dATA_TYPE) {
		DATA_TYPE = dATA_TYPE;
	}

	public long getCHARACTER_MAXIMUM_LENGTH() {
		return CHARACTER_MAXIMUM_LENGTH;
	}

	public void setCHARACTER_MAXIMUM_LENGTH(long cHARACTER_MAXIMUM_LENGTH) {
		CHARACTER_MAXIMUM_LENGTH = cHARACTER_MAXIMUM_LENGTH;
	}

	public long getCHARACTER_OCTET_LENGTH() {
		return CHARACTER_OCTET_LENGTH;
	}

	public void setCHARACTER_OCTET_LENGTH(long cHARACTER_OCTET_LENGTH) {
		CHARACTER_OCTET_LENGTH = cHARACTER_OCTET_LENGTH;
	}

	public long getNUMERIC_PRECISION() {
		return NUMERIC_PRECISION;
	}

	public void setNUMERIC_PRECISION(long nUMERIC_PRECISION) {
		NUMERIC_PRECISION = nUMERIC_PRECISION;
	}

	public long getNUMERIC_SCALE() {
		return NUMERIC_SCALE;
	}

	public void setNUMERIC_SCALE(long nUMERIC_SCALE) {
		NUMERIC_SCALE = nUMERIC_SCALE;
	}

	public long getDATETIME_PRECISION() {
		return DATETIME_PRECISION;
	}

	public void setDATETIME_PRECISION(long dATETIME_PRECISION) {
		DATETIME_PRECISION = dATETIME_PRECISION;
	}

	public String getCHARACTER_SET_NAME() {
		return CHARACTER_SET_NAME;
	}

	public void setCHARACTER_SET_NAME(String cHARACTER_SET_NAME) {
		CHARACTER_SET_NAME = cHARACTER_SET_NAME;
	}

	public String getCOLLATION_NAME() {
		return COLLATION_NAME;
	}

	public void setCOLLATION_NAME(String cOLLATION_NAME) {
		COLLATION_NAME = cOLLATION_NAME;
	}

	public String getCOLUMN_TYPE() {
		return COLUMN_TYPE;
	}

	public void setCOLUMN_TYPE(String cOLUMN_TYPE) {
		COLUMN_TYPE = cOLUMN_TYPE;
	}

	public String getCOLUMN_KEY() {
		return COLUMN_KEY;
	}

	public void setCOLUMN_KEY(String cOLUMN_KEY) {
		COLUMN_KEY = cOLUMN_KEY;
	}

	public String getEXTRA() {
		return EXTRA;
	}

	public void setEXTRA(String eXTRA) {
		EXTRA = eXTRA;
	}

	public String getPRIVILEGES() {
		return PRIVILEGES;
	}

	public void setPRIVILEGES(String pRIVILEGES) {
		PRIVILEGES = pRIVILEGES;
	}

	public String getCOLUMN_COMMENT() {
		return COLUMN_COMMENT;
	}

	public void setCOLUMN_COMMENT(String cOLUMN_COMMENT) {
		COLUMN_COMMENT = cOLUMN_COMMENT;
	}

}
