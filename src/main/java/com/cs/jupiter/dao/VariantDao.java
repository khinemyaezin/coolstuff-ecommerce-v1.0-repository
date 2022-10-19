package com.cs.jupiter.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;

import org.springframework.stereotype.Repository;

import com.cs.jupiter.model.interfaces.ViewResult;
import com.cs.jupiter.model.jun.ProductAttribute;
import com.cs.jupiter.model.table.ProductVariantOptionDetail;
import com.cs.jupiter.model.table.ProductVariantOptionHeader;
import com.cs.jupiter.model.table.ProductVariantTheme;
import com.cs.jupiter.utility.ComEnum;
import com.cs.jupiter.utility.CommonUtility;
import com.cs.jupiter.utility.PrepareQuery;
import com.cs.jupiter.utility.PrepareQuery.Operator;
import com.cs.jupiter.utility.PrepareQuery.Type;

@Repository
public class VariantDao {
	public boolean saveVariantOptionHeader(ProductVariantOptionHeader data, Connection conn) {
		try {
			String sql = "INSERT INTO public.stock_variant_option_header(id, name)VALUES (?, ?);";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setLong(1, Long.parseLong(data.getId()));
			stmt.setString(2, data.getName());
			stmt.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

	public boolean updateVariantOptionHeader(ProductVariantOptionHeader data, Connection conn) {
		try {
			String sql = "UPDATE public.stock_variant_option_header";
			PrepareQuery q = new PrepareQuery(sql);
			q.addSetClause("name", data.getName(), Operator.EQUAL, Type.VARCHAR);
			q.addWhereClause("id", data.getId(), Operator.EQUAL, Type.ID);
			PreparedStatement stmt = q.getPrepareStatement(conn);
			stmt.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

	public boolean deleteVariantOptionHeader(String data, ViewResult<String> vres, Connection conn) {
		try {
			String sql = "DELETE public.stock_variant_option_header WHERE id=?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setLong(1, Long.parseLong(data));
			stmt.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			vres.message = e.getMessage();
		}
		return false;

	}

	public ViewResult<ProductVariantOptionHeader> getOptionHeader(ProductVariantOptionHeader data,
			Connection conn) {
		ViewResult<ProductVariantOptionHeader> rtn = new ViewResult<>();
		try {
			String sql = "SELECT row_number() over(order by name asc) as row_num, count(id) over() as total, id, name FROM public.stock_variant_option_header";
			PrepareQuery q = new PrepareQuery(sql);
			q.addWhereClause("id", data.getId(), Operator.EQUAL, Type.ID);
			q.addWhereClause("name", data.getName(), Operator.LIKE_ALL, Type.VARCHAR);
			PreparedStatement stmt = q.getPrepareStatement(conn,data.getCurrentRow(), data.getMaxRowsPerPage(),"", "name", "asc");
			ResultSet rs = stmt.executeQuery();
			rtn.list = new ArrayList<>();
			while (rs.next()) {
				ProductVariantOptionHeader h = new ProductVariantOptionHeader();
				if (rtn.list.size() == 0)
					rtn.totalItem = rs.getInt("total");
				h.setId(rs.getString("id"));
				h.setName(rs.getString("name"));
				rtn.list.add(h);
			}
			rtn.status = ComEnum.ErrorStatus.Success.getCode();
		} catch (Exception e) {
			e.printStackTrace();
			rtn.message = e.getMessage();
			rtn.status = ComEnum.ErrorStatus.DatabaseError.getCode();
		}
		return rtn;
	}
	
	public ViewResult<ProductVariantOptionHeader> getOptionHeaderUnderCategory(String categoryId,ProductVariantOptionHeader data,
			Connection conn) {
		ViewResult<ProductVariantOptionHeader> rtn = new ViewResult<>(null,new ArrayList<>());
		try {
			String sql = "select catvar.fk_category as cat_id,opthdr.id as option_id,opthdr.name as option_name,catvar.id as jun_id"
					+ " from public.stock_variant_option_header opthdr"
					+ " inner join category_varoption catvar on catvar.fk_var_option=opthdr.id and catvar.fk_category=? and opthdr.status=?"
					+ " where lower(opthdr.name) like ? order by opthdr.name asc limit ?";
			
			PreparedStatement stmt = conn.prepareStatement(sql);
			short i=1;
			stmt.setLong(i++, Long.parseLong(categoryId));
			stmt.setInt(i++, ComEnum.RowStatus.Normal.getCode());
			stmt.setString(i++, data.getName().concat("%"));
			stmt.setInt(i++, 5);
			ResultSet rs = stmt.executeQuery();
			ProductVariantOptionHeader value;
			while (rs.next()) {
				value = new ProductVariantOptionHeader();
				value.setId(rs.getString("option_id"));
				value.setName(rs.getString("option_name"));
				rtn.list.add(value);
			}
			rtn.success();
		} catch (Exception e) {
			e.printStackTrace();
			rtn.error(e.getMessage());
		}
		return rtn;
	}
	
	public ViewResult<ProductAttribute> getOptionHeaderWithJunction(String categoryId,
			Connection conn) {
		ViewResult<ProductAttribute> rtn = new ViewResult<>(null,new ArrayList<>());
		try {
			String sql = " SELECT catvar.fk_category as cat_id,opthdr.id as option_id,opthdr.name as option_name,catvar.id as jun_id,case when catvar.id is null then false else true end as status"
					+ " FROM public.stock_variant_option_header opthdr left join category_varoption catvar on catvar.fk_var_option=opthdr.id and catvar.fk_category=?"
					+ " order by opthdr.name asc;";
			
			PreparedStatement stmt = conn.prepareStatement(sql);
			short i=1;
			stmt.setLong(i++, Long.parseLong(categoryId));
			ResultSet rs = stmt.executeQuery();
			ProductAttribute value;
			while (rs.next()) {
				value = new ProductAttribute();
				value.setOptionId(rs.getString("option_id"));
				value.setOptionName(rs.getString("option_name"));
				value.setJunId(rs.getString("jun_id"));
				value.setStatus(rs.getBoolean("status"));
				value.setCategoryId(rs.getString("cat_id"));
				rtn.list.add(value);
			}
			rtn.success();
		} catch (Exception e) {
			e.printStackTrace();
			rtn.error(e.getMessage());
		}
		return rtn;
	}

	public boolean saveOptionDetail(ProductVariantOptionDetail data, Connection conn) {
		try {
			String sql = "INSERT INTO public.stock_variant_option_detail(id, fk_stock_variant_option_header, name, code)VALUES (?, ?, ?, ?);";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setLong(1, Long.parseLong(data.getId()));
			stmt.setLong(2, Long.parseLong(data.getHeader().getId()));
			stmt.setString(3, data.getName());
			stmt.setString(4, data.getCode());
			stmt.execute();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean updateOptionDetail(ProductVariantOptionDetail data, Connection conn) {
		try {
			String sql = "UPDATE public.stock_variant_option_detail";
			PrepareQuery q = new PrepareQuery(sql);
			q.addSetClause("name", data.getName(), Operator.EQUAL, Type.VARCHAR);
			q.addSetClause("code", data.getCode(), Operator.EQUAL, Type.VARCHAR);
			q.addWhereClause("id", data.getId(), Operator.EQUAL, Type.ID);
			PreparedStatement stmt = q.getPrepareStatement(conn);

			stmt.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

	public boolean deleteOptionDetailById(String data, ViewResult<ProductVariantOptionDetail> vres, Connection conn) {
		try {
			String sql = "DELETE FROM public.stock_variant_option_detail WHERE id=?";

			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setLong(1, Long.parseLong(data));
			stmt.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			vres.message = e.getMessage();
		}
		return false;

	}

	public boolean deleteOptionDetailByHeaderId(String data, ViewResult<String> vres, Connection conn) {
		try {
			String sql = "DELETE FROM public.stock_variant_option_detail WHERE fk_stock_variant_option_header=?";

			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setLong(1, Long.parseLong(data));
			stmt.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			vres.message = e.getMessage();
		}
		return false;

	}

	public ViewResult<ProductVariantOptionDetail> getOptionDetail(ProductVariantOptionDetail data,
			Connection conn) {
		ViewResult<ProductVariantOptionDetail> rtn = new ViewResult<>();
		try {
			String sql = "SELECT row_number() over(order by name asc) as row_num, count(id) over() as total, id, name, code"
					+ " FROM public.stock_variant_option_detail";
			PrepareQuery q = new PrepareQuery(sql);
			q.addWhereClause("id", data.getId(), Operator.EQUAL, Type.ID);
			if (data.getHeader() != null)
				q.addWhereClause("fk_stock_variant_option_header", data.getHeader().getId(), Operator.EQUAL,
						Type.ID);
			q.addWhereClause("name", data.getName(), Operator.LIKE_ALL, Type.VARCHAR);
			q.addWhereClause("code", data.getCode(), Operator.LIKE_ALL, Type.VARCHAR);
			PreparedStatement stmt = q.getPrepareStatement(conn,data.getCurrentRow(), data.getMaxRowsPerPage(),"", "name", "asc");
			ResultSet rs = stmt.executeQuery();
			rtn.list = new ArrayList<>();
			while (rs.next()) {
				ProductVariantOptionDetail h = new ProductVariantOptionDetail();
				if (rtn.list.size() == 0)
					rtn.totalItem = rs.getInt("total");
				h.setId(rs.getString("id"));
				h.setName(rs.getString("name"));
				h.setCode(rs.getString("code"));
				rtn.list.add(h);
			}
			rtn.success();
		} catch (Exception e) {
			rtn.error(e.getMessage());
		}
		return rtn;
	}

	public ViewResult<ProductVariantTheme> saveVariantTheme(ProductVariantTheme data,
			Connection conn) {
		ViewResult<ProductVariantTheme> result = new ViewResult<>();
		try {
			String sql = "INSERT INTO public.stock_variant_theme("
					+ " id, fk_stock_variant_option_header1, fk_stock_variant_option_header2, fk_stock_variant_option_header3, order_no, name, status, cdate, mdate)"
					+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?::date, ?::date);";
			PreparedStatement stmt = conn.prepareStatement(sql);
			int i = 1;
			stmt.setLong(i++, Long.parseLong(data.getId()));
			if(data.getFirstStockVariantOptionHeader()!=null)
				stmt.setLong(i++, Long.parseLong(data.getFirstStockVariantOptionHeader().getId()));
			else
				stmt.setNull(i++, Types.BIGINT);
			if(data.getSecondStockVariantOptionHeader()!=null)
				stmt.setLong(i++, Long.parseLong(data.getSecondStockVariantOptionHeader().getId()));
			else
				stmt.setNull(i++, Types.BIGINT);
			if(data.getThirdStockVariantOptionHeader()!=null)
				stmt.setLong(i++, Long.parseLong(data.getThirdStockVariantOptionHeader().getId()));
			else
				stmt.setNull(i++, Types.BIGINT);
			stmt.setInt(i++, data.getOrder());
			stmt.setString(i++, data.getName());
			stmt.setInt(i++, data.getStatus());
			stmt.setString(i++, CommonUtility.convertDate_2db(data.getMdate()));
			stmt.setString(i++, CommonUtility.convertDate_2db(data.getMdate()));
			stmt.executeUpdate();
			result.success();
		} catch (Exception e) {
			result.error(e.getMessage());
			e.printStackTrace();
		}
		return result;

	}

	public ViewResult<ProductVariantTheme> updateVariantTheme(ProductVariantTheme data, Connection conn) {
		ViewResult<ProductVariantTheme> result = new ViewResult<ProductVariantTheme>();
		try {
			String sql = "UPDATE public.stock_variant_theme";
			PrepareQuery q = new PrepareQuery(sql);
			q.addSetClause("name", data.getName(), Operator.EQUAL, Type.VARCHAR);
			q.addSetClause("mdate", data.getMdate(), Operator.EQUAL,
					Type.DATE);
			q.addSetClause("fk_stock_variant_option_header1", data.getFirstStockVariantOptionHeader(),
					Operator.EQUAL, Type.ID);
			q.addSetClause("fk_stock_variant_option_header2", data.getSecondStockVariantOptionHeader(),
					Operator.EQUAL, Type.ID);
			q.addSetClause("fk_stock_variant_option_header3", data.getThirdStockVariantOptionHeader(),
					Operator.EQUAL, Type.ID);
			q.addSetClause("order", data.getOrder(), Operator.EQUAL, Type.NUMBER);
			
			q.addWhereClause("id", data.getId(), Operator.EQUAL, Type.ID);
			PreparedStatement stmt = q.getPrepareStatement(conn);
			stmt.executeUpdate();
			result.success();
		} catch (Exception e) {
			result.error(e.getMessage());
		}
		return result;
	}

	public ViewResult<ProductVariantTheme> getStockVariantTheme(ProductVariantTheme data, Connection conn) {
		ViewResult<ProductVariantTheme> result = new ViewResult<>(null, new ArrayList<ProductVariantTheme>());
		try {
			String sql = "select theme.id as id," 
					+ "theme.name as name," 
					+ "oph1.id as option1_id,"
					+ "oph1.name as option1_name," 
					+ "oph2.id as option2_id," 
					+ "oph2.name as option2_name,"
					+ "oph3.id as option3_id," 
					+ "oph3.name as option3_name "
					+ " from stock_variant_theme theme  "
					+ " inner join stock_variant_option_header oph1 on oph1.id=theme.fk_stock_variant_option_header1  "
					+ " left join stock_variant_option_header oph2 on oph2.id=theme.fk_stock_variant_option_header2 "
					+ " left join stock_variant_option_header oph3 on oph3.id=theme.fk_stock_variant_option_header3 ";
			PrepareQuery q = new PrepareQuery(sql);
			q.addWhereClause("theme.id", data.getId(), Operator.EQUAL, Type.ID);

			PreparedStatement stmt = q.getPrepareStatement(conn);
			ResultSet rs = stmt.executeQuery();

			ProductVariantTheme detail;
			while (rs.next()) {
				detail = new ProductVariantTheme();
				detail.setId(rs.getString("id"));
				detail.setName(rs.getString("name"));
				detail.setFirstStockVariantOptionHeader(new ProductVariantOptionHeader());
				detail.getFirstStockVariantOptionHeader().setId(rs.getString("option1_id"));
				detail.getFirstStockVariantOptionHeader().setName(rs.getString("option1_name"));
				if (rs.getString("option2_id") != null) {
					detail.setSecondStockVariantOptionHeader(new ProductVariantOptionHeader());
					detail.getSecondStockVariantOptionHeader().setId(rs.getString("option2_id"));
					detail.getSecondStockVariantOptionHeader().setName(rs.getString("option2_name"));
				}
				if (rs.getString("option3_id") != null) {
					detail.setThirdStockVariantOptionHeader(new ProductVariantOptionHeader());
					detail.getThirdStockVariantOptionHeader().setId(rs.getString("option3_id"));
					detail.getThirdStockVariantOptionHeader().setName(rs.getString("option3_name"));
				}
				if(data.getId()!=null && !data.getId().equals("-1")){
					result.data = detail;
				}else{
					result.list.add(detail);
				}
			
			}
			result.success();
		} catch (Exception e) {
			result.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
}
