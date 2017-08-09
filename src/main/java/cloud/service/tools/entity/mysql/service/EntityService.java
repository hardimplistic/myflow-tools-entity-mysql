package cloud.service.tools.entity.mysql.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cloud.dependency.core.utils.SQLBuilder;
import cloud.service.tools.entity.mysql.jpa.entity.Columns;
import cloud.service.tools.entity.mysql.utils.ClassBuilder;

@Service
@Transactional
public class EntityService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

    public Object getEntity(String tableSchema, String tableName, String packageName) {
    	
    	SQLBuilder sql = new SQLBuilder();
    	sql.line("SELECT");
    	sql.line("  TABLE_CATALOG");
    	sql.line(", TABLE_SCHEMA");
    	sql.line(", TABLE_NAME");
    	sql.line(", COLUMN_NAME");
    	sql.line(", ORDINAL_POSITION");
    	sql.line(", COLUMN_DEFAULT");
    	sql.line(", IS_NULLABLE");
    	sql.line(", DATA_TYPE");
    	sql.line(", CHARACTER_MAXIMUM_LENGTH");
    	sql.line(", CHARACTER_OCTET_LENGTH");
    	sql.line(", NUMERIC_PRECISION");
    	sql.line(", NUMERIC_SCALE");
    	sql.line(", DATETIME_PRECISION");
    	sql.line(", CHARACTER_SET_NAME");
    	sql.line(", COLLATION_NAME");
    	sql.line(", COLUMN_TYPE");
    	sql.line(", COLUMN_KEY");
    	sql.line(", EXTRA");
    	sql.line(", PRIVILEGES");
    	sql.line(", COLUMN_COMMENT");
    	sql.line("FROM COLUMNS");
    	sql.line("WHERE");
    	sql.line("TABLE_SCHEMA = ?");
    	sql.line("AND TABLE_NAME = ?");
    	
    	List<Columns> list = jdbcTemplate.query(sql.toString(), new Object[] {tableSchema, tableName}, new RowMapper<Columns>() {
			@Override
			public Columns mapRow(ResultSet rs, int rowNum) throws SQLException {
				Columns item = new Columns();
				
				item.setTABLE_CATALOG(rs.getString("TABLE_CATALOG"));
				item.setTABLE_SCHEMA(rs.getString("TABLE_SCHEMA"));
				item.setTABLE_NAME(rs.getString("TABLE_NAME"));
				item.setCOLUMN_NAME(rs.getString("COLUMN_NAME"));
				item.setORDINAL_POSITION(rs.getLong("ORDINAL_POSITION"));
				item.setCOLUMN_DEFAULT(rs.getString("COLUMN_DEFAULT"));
				item.setIS_NULLABLE(rs.getString("IS_NULLABLE"));
				item.setDATA_TYPE(rs.getString("DATA_TYPE"));
				item.setCHARACTER_MAXIMUM_LENGTH(rs.getLong("CHARACTER_MAXIMUM_LENGTH"));
				item.setCHARACTER_OCTET_LENGTH(rs.getLong("CHARACTER_OCTET_LENGTH"));
				item.setNUMERIC_PRECISION(rs.getLong("NUMERIC_PRECISION"));
				item.setNUMERIC_SCALE(rs.getLong("NUMERIC_SCALE"));
				item.setDATETIME_PRECISION(rs.getLong("DATETIME_PRECISION"));
				item.setCHARACTER_SET_NAME(rs.getString("CHARACTER_SET_NAME"));
				item.setCOLLATION_NAME(rs.getString("COLLATION_NAME"));
				item.setCOLUMN_TYPE(rs.getString("COLUMN_TYPE"));
				item.setCOLUMN_KEY(rs.getString("COLUMN_KEY"));
				item.setEXTRA(rs.getString("EXTRA"));
				item.setPRIVILEGES(rs.getString("PRIVILEGES"));
				item.setCOLUMN_COMMENT(rs.getString("COLUMN_COMMENT"));
				
				return item;
			}
		});
        
        return format(list, packageName);
//        return list;
    }
    
    public Object format(List<Columns> list, String packageName) {
    	
    	ClassBuilder clazz = new ClassBuilder();
    	
    	// 包名
    	if (!"".equals(packageName)) {
    		clazz.append(String.format("package %s;", packageName)).enter(2);
		}

    	// 引入
    	
    	List<String> importList = getImportList(list);
    	if (importList.size() > 0) {
    		for (String im : importList) {
    			clazz.append(String.format("import %s;", im)).enter();
			}
		}
    	
    	// jpa 依赖
    	
    	clazz.append("import javax.persistence.*;").enter(2);
    	
    	// 表名
    	
    	Columns first = list.get(0);
    	String tableName = first.getTABLE_NAME();
    	clazz.append("@Entity").enter();
    	clazz.append(String.format("@Table(name = \"%s\")", tableName)).enter();
    	clazz.append(String.format("public class %s {", getNameCamelCase(tableName, true))).enter(2);
    	
    	// 字段
    	
    	String dataType = "";
    	String columnName = "";
    	for (Columns col : list) {
    		if ("PRI".equals(col.getCOLUMN_KEY())) {
    			clazz.tab().append("@Id").enter();
			}
    		if ("auto_increment".equals(col.getEXTRA())) {
    			clazz.tab().append("@GeneratedValue(strategy = GenerationType.AUTO)").enter();
    		}
    		dataType = getDataType(col.getDATA_TYPE());
    		columnName = col.getCOLUMN_NAME();
			clazz.tab().append(String.format("@Column(name = \"`%s`\")", columnName)).enter();
			clazz.tab().append(String.format("private %s %s;", dataType, getNameCamelCase(columnName))).enter();
		}
    	
    	clazz.enter();
    	
    	// getters and setters
    	
    	for (Columns col : list) {
    		dataType = getDataType(col.getDATA_TYPE());
    		columnName = getNameCamelCase(col.getCOLUMN_NAME());
    		clazz.tab().append(String.format("public %s get%s() {", dataType, firstUpperCase(columnName))).enter();
    		clazz.tab(2).append(String.format("return this.%s;", getNameCamelCase(columnName))).enter();
    		clazz.tab().append("}").enter(2);
    		clazz.tab().append(String.format("public void set%s(%s %s) {", firstUpperCase(columnName), dataType, columnName)).enter();
    		clazz.tab(2).append(String.format("this.%s = %s;", columnName, columnName)).enter();
    		clazz.tab().append("}").enter(2);
    	}
    	
    	clazz.append("}").enter();
    	
    	return clazz.toString();
    }
    
    public List<String> getImportList(List<Columns> list) {
    	
    	List<String> importList = new ArrayList<>();
    	
    	String dataType = null;
    	
    	for (Columns col : list) {
    		dataType = col.getDATA_TYPE();
    		
    		switch (dataType) {
    		case "datetime":
    			importList.add("java.util.Date");
    			break;
    		}
    	}
    	
    	return importList;
    }
    
    public String getDataType(String dataType) {
    	
    	switch (dataType) {
		case "int":
			dataType = "int";
			break;
		case "varchar":
			dataType = "String";
			break;
		case "datetime":
			dataType = "Date";
			break;
		}
    	
    	return dataType;
    }
    
    public String getNameCamelCase(String name) {
    	
    	if (name.indexOf("_") == -1) {
			return name;
		}
    	
    	String _name = "";
    	
    	int nameLength = name.length();
    	
    	boolean isUpperCase = false;
    	
    	for (int i = 0; i < nameLength; i++) {
    		char c = name.charAt(i);
    		if (c == '_') {
    			isUpperCase = true;
    			continue;
			}
    		if (isUpperCase) {
    			isUpperCase = false;
    			_name += String.valueOf(c).toUpperCase();
			} else {
				_name += String.valueOf(c);
			}
		}
    	
    	return _name;
    }
    
    public String getNameCamelCase(String name, boolean isFirstUpperCase) {
    	
    	name = getNameCamelCase(name);
    	
    	if (isFirstUpperCase) {
    		name = String.valueOf(name.charAt(0)).toUpperCase() + name.substring(1);
		}
    	
    	return name;
    }
    
    public String firstUpperCase(String name) {
    	return String.valueOf(name.charAt(0)).toUpperCase() + name.substring(1);
    }

}
