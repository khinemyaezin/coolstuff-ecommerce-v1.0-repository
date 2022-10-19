package com.cs.jupiter.utility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PrepareQuery {
	private String sql;
	private PreparedStatement stmt;
	private Connection conn;
	private List<Data> metadataSet = new ArrayList<>();
	private List<Data> metadataWhere = new ArrayList<>();
	private List<Data> metadataOr = new ArrayList<>();

	public PrepareQuery(String query) {
		this.sql = query;
	}

	public class Data {
		public String column;
		public Type dataType;
		public Object value;
		public Operator operator;
		public String output;
	}

	public enum Operator {
		EQUAL(0, "="), LESSTHEN(1, "<"), LESSTHEN_EQUAL(2, "<="), GREATERTHEN(3, ">"), GREATERTHEN_EQUAL(4,
				">="), LIKE_START(5,
						"like"), LIKE_END(6, "like"), LIKE_ALL(7, "like"), NOT_EQUAL(8, "<>"), BETWEEN(9, "and");

		Operator(int code, String description) {
			this.code = code;
			this.description = description;
		}

		private final int code;
		private final String description;

		public int getCode() {
			return code;
		}

		public String getDescription() {
			return description;
		}
	}

	public enum Type {
		ID, VARCHAR, DATE, NUMBER, BOOLEAN, CHAR,ARRAY;
		Type() {
		}
	}

	// private String getDesc(Operator code) {
	// if (code == Operator.EQUAL) {
	// return Operator.EQUAL.getDescription();
	// } else if (code == Operator.LESSTHEN) {
	// return Operator.LESSTHEN.getDescription();
	// } else if (code == Operator.LESSTHEN_EQUAL) {
	// return Operator.LESSTHEN_EQUAL.getDescription();
	// } else if (code == Operator.GREATERTHEN) {
	// return Operator.GREATERTHEN.getDescription();
	// } else if (code == Operator.GREATERTHEN_EQUAL) {
	// return Operator.GREATERTHEN_EQUAL.getDescription();
	//
	// } else if (code == Operator.NOT_EQUAL) {
	// return Operator.NOT_EQUAL.getDescription();
	// } else {
	// return Operator.LIKE_START.getDescription();
	// }
	// }

	public void addSetClause(String col, Object val, Operator e, Type type) {
		PrepareQuery.Data meta = setItem(col, val, e, type, false);
		if (meta != null)
			this.metadataSet.add(meta);
	}

	public void addWhereClause(String col, Object val, Operator e, Type type) {
		PrepareQuery.Data meta = setItem(col, val, e, type, false);
		if (meta != null)
			this.metadataWhere.add(meta);
	}

	public void addOrClause(String col, Object val, Operator e, Type type) {
		PrepareQuery.Data meta = setItem(col, val, e, type, false);
		if (meta != null)
			this.metadataOr.add(meta);
	}

	public void addSetClause(String col, Object val, Operator e, Type type, boolean nullable) {
		PrepareQuery.Data meta = setItem(col, val, e, type, nullable);
		if (meta != null)
			this.metadataSet.add(meta);
	}

	public void addWhereClause(String col, Object val, Operator e, Type type, boolean nullable) {
		PrepareQuery.Data meta = setItem(col, val, e, type, nullable);
		if (meta != null)
			this.metadataWhere.add(meta);
	}

	public void addOrClause(String col, Object val, Operator e, Type type, boolean nullable) {
		PrepareQuery.Data meta = setItem(col, val, e, type, nullable);
		if (meta != null)
			this.metadataOr.add(meta);
	}

	private PrepareQuery.Data setItem(String col, Object val, Operator e, Type type, boolean nullable) {
		if (val == null && !nullable) {
			return null;
		}
		if (!nullable) {

			if (type == Type.ID) {
				if (val.getClass() != String.class)
					return null;
				if (val.equals("-1") || val.equals(""))
					return null;
			} else if (type == Type.VARCHAR) {
				if (val.getClass() != String.class)
					return null;
				if (val.equals(""))
					return null;
			} else if (type == Type.DATE) {
				if (val.getClass() != Date.class)
					return null;
				if (val.equals(""))
					return null;
			} else if (type == Type.NUMBER) {
				if (val.getClass() != Integer.class && val.getClass() != Double.class)
					return null;
				if (val.getClass() == Integer.class && (int) val < 0)
					return null;
				if (val.getClass() == Double.class && (double) val < 0)
					return null;
			} else if (type == Type.BOOLEAN) {
				if (val.getClass() != Boolean.class)
					return null;
			} else if(type == Type.CHAR){
				if (val.getClass() != Character.class)
					return null;
				if ((char) val == '\u0000')
					return null;
			}else if (type == Type.ARRAY) {
				
			}
		}
		PrepareQuery.Data meta = new PrepareQuery.Data();
		meta.column = col;
		meta.dataType = type;
		meta.value = val;
		meta.operator = e;

		return meta;
	}

	private String getSetQuery() {
		StringBuilder sb = new StringBuilder();
		if (metadataSet.size() != 0)
			sb.append(" set");

		for (int i = 0; i < metadataSet.size(); i++) {
			PrepareQuery.Data meta = metadataSet.get(i);
			String column = meta.column + " " + meta.operator.getDescription();
			String ref = " ? ";
			if (meta.dataType == Type.DATE) {
				ref = " ?::timestamp ";
			}
			sb.append(" " + column + ref);
			if (i != metadataSet.size() - 1)
				sb.append(" , ");
		}
		return sb.toString();
	}

	private String getWhereQuery() {
		StringBuilder sb = new StringBuilder();
		if (metadataWhere.size() != 0 || metadataOr.size() != 0)
			sb.append(" where ");

		for (int i = 0; i < metadataWhere.size(); i++) {
			PrepareQuery.Data meta = metadataWhere.get(i);
			String column = meta.column + " " + meta.operator.getDescription();
			String ref = " ? ";
			if (meta.dataType == Type.DATE) {
				ref = " ?::timestamp ";
			}
			if (meta.dataType == Type.VARCHAR) {
				if (meta.operator == Operator.LIKE_ALL || meta.operator == Operator.LIKE_START
						|| meta.operator == Operator.LIKE_START)
					column = "lower(" + meta.column + ") " + meta.operator.getDescription() + " ";
			}
			sb.append(" " + column + ref);
			if (i != metadataWhere.size() - 1)
				sb.append(" and ");
		}
		if (metadataWhere.size() > 0 && metadataOr.size() > 0) {
			sb.append(" and ");
		}
		for (int i = 0; i < metadataOr.size(); i++) {
			PrepareQuery.Data meta = metadataOr.get(i);
			String column = meta.column + " " + meta.operator.getDescription();
			String ref = " ? ";
			if (meta.dataType == Type.DATE) {
				ref = " ?::timestamp ";
			}
			if (meta.dataType == Type.VARCHAR) {
				column = "lower(" + meta.column + ") " + meta.operator.getDescription() + " ";
			}
			sb.append(" " + column + ref);
			if (i != metadataOr.size() - 1)
				sb.append(" or ");
		}
		return sb.toString();
	}

	public PreparedStatement getPrepareStatement(Connection conn, int current, int max, String groupby, String orderbyCol,
			String sorting) throws Exception {
		if (!this.sql.toLowerCase().startsWith("select")) {
			throw new Exception("invalid_query");
		}
		this.sql += this.getSetQuery();
		this.sql += this.getWhereQuery();
		if (!groupby.equals("")) {
			this.sql += (" group by ").concat(groupby);
		}
		if (!orderbyCol.equals("") && !sorting.equals("")) {
			this.sql += " order by " + orderbyCol + " " + sorting;
		}
		if (current != -1 && max != -1) {
			this.sql += " offset " + current + " rows fetch next " + max + " rows only";
		}
		System.out.println(this.sql);
		this.stmt = conn.prepareStatement(this.sql);
		this.prepareStatement(this.stmt);
		return this.stmt;
	}

	public PreparedStatement getPrepareStatement(Connection conn) throws Exception {
		this.conn = conn;
		if (this.metadataSet.size() == 0 && this.metadataWhere.size() == 0 && this.metadataOr.size() == 0
				&& !this.sql.toLowerCase().startsWith("select")) {
			throw new Exception("invalid_query_param");
		}
		this.sql += this.getSetQuery();
		this.sql += this.getWhereQuery();
		this.stmt = conn.prepareStatement(this.sql);
		this.prepareStatement(this.stmt);
		return this.stmt;
	}

	private void prepareStatement(PreparedStatement stmt) throws NumberFormatException, SQLException {

		int count = 1;
		count = this.setValueToPrepareStatement(this.metadataSet, stmt, count);
		count = this.setValueToPrepareStatement(this.metadataWhere, stmt, count);
		count = this.setValueToPrepareStatement(this.metadataOr, stmt, count);
	}

	private int setValueToPrepareStatement(List<PrepareQuery.Data> metadata, PreparedStatement stmt, int count)
			throws NumberFormatException, SQLException {
		for (int i = 0; i < metadata.size(); i++) {
			PrepareQuery.Data meta = metadata.get(i);

			if (meta.dataType == Type.ID) {
				if (meta.value == null)
					stmt.setNull(count++, Types.BIGINT);
				else
					stmt.setLong(count++, Long.parseLong((String) meta.value));
			} else if (meta.dataType == Type.VARCHAR) {
				if (meta.value == null)
					stmt.setNull(count++, Types.VARCHAR);
				else {
					if (meta.operator == Operator.LIKE_START) {
						stmt.setString(count++, "%" + ((String) meta.value).toLowerCase());
					} else if (meta.operator == Operator.LIKE_END) {
						stmt.setString(count++, ((String) meta.value).toLowerCase() + "%");
					} else if (meta.operator == Operator.LIKE_ALL) {
						stmt.setString(count++, "%" + ((String) meta.value).toLowerCase() + "%");
					} else {
						stmt.setString(count++, (String) meta.value);
					}
				}
			} else if (meta.dataType == Type.DATE) {
				if (meta.value == null)
					stmt.setNull(count++, Types.TIMESTAMP);
				else
					stmt.setString(count++, CommonUtility.convertTimeStamp_2db((Date) meta.value));
			} else if (meta.dataType == Type.NUMBER) {
				
					if (meta.value.getClass() == Double.class) {
						if (meta.value == null)
							stmt.setNull(count++, Types.DOUBLE);
						else 
							stmt.setDouble(count++, (double) meta.value);
						
					} else {
						if (meta.value == null)
							stmt.setNull(count++, Types.INTEGER);
						else 
							stmt.setInt(count++, (int) meta.value);
					}
				
			} else if (meta.dataType == Type.BOOLEAN) {
				if (meta.value == null)
					stmt.setNull(count++, Types.BOOLEAN);
				else 
					stmt.setBoolean(count++, (boolean) meta.value);
			} else if(meta.dataType == Type.CHAR){
				if (meta.value == null)
					stmt.setNull(count++, Types.CHAR);
				else 
					stmt.setString(count++, String.valueOf(meta.value));
			}else if(meta.dataType == Type.ARRAY){
				if(meta.value == null)
					stmt.setNull(count++, Types.ARRAY);
				else 
					stmt.setArray(count++, this.conn.createArrayOf("text", (String[]) meta.value));
			}

		}
		return count;
	}

	public String getSql() {
		return this.sql;
	}

}
