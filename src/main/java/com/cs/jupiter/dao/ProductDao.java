package com.cs.jupiter.dao;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import com.cs.jupiter.model.interfaces.ViewResult;
import com.cs.jupiter.model.jun.InventoryManageCri;
import com.cs.jupiter.model.jun.ProductSummary;
import com.cs.jupiter.model.table.Brand;
import com.cs.jupiter.model.table.Category;
import com.cs.jupiter.model.table.Condition;
import com.cs.jupiter.model.table.ImageData;
import com.cs.jupiter.model.table.PackType;
import com.cs.jupiter.model.table.Product;
import com.cs.jupiter.model.table.ProductGroup;
import com.cs.jupiter.model.table.ProductImage;
import com.cs.jupiter.model.table.ProductOptions;
import com.cs.jupiter.model.table.ProductVariant;
import com.cs.jupiter.model.table.ProductVariantOptionDetail;
import com.cs.jupiter.model.table.ProductVariantOptionHeader;
import com.cs.jupiter.utility.ComEnum;
import com.cs.jupiter.utility.CommonUtility;
import com.cs.jupiter.utility.PrepareQuery;
import com.cs.jupiter.utility.PrepareQuery.Operator;
import com.cs.jupiter.utility.PrepareQuery.Type;


public class ProductDao {
	@Autowired
	Environment env;
	
	/* stock */

	public ViewResult<Product> saveProduct(Product data, Connection conn) {
		ViewResult<Product> rtn = new ViewResult<>();
		try {
			String sql = "INSERT INTO public.stock("
					+ "id, code, name, status, cdate, mdate, fk_packtype, fk_brand, manufacture, stock_brand_title, fk_category, description, features, path,fk_image, has_variants)"
					+ " VALUES (?, ?, ?, ?, ?::timestamp, ?::timestamp, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
			PreparedStatement stmt = conn.prepareStatement(sql);
			int i = 1;
			stmt.setLong(i++, Long.parseLong(data.getId()));
			stmt.setString(i++, data.getCode());
			stmt.setString(i++, data.getName());
			stmt.setInt(i++, data.getStatus());
			stmt.setString(i++, CommonUtility.convertTimeStamp_2db(data.getCdate()));
			stmt.setString(i++, CommonUtility.convertTimeStamp_2db(data.getMdate()));
			stmt.setLong(i++, Long.parseLong(data.getPacktype().getId()));
			stmt.setLong(i++, Long.parseLong(data.getBrand().getId()));
			stmt.setString(i++, data.getManufacture());
			stmt.setString(i++, data.getStockBrandTitle());
			stmt.setLong(i++, Long.parseLong(data.getCategory().getId()));
			stmt.setString(i++, data.getDescription());
			stmt.setString(i++, data.getFeatures());
			if (data.getImage() != null) {
				stmt.setString(i++, data.getImage().getName());
				stmt.setLong(i++, Long.parseLong(data.getImage().getId()));
			} else {
				stmt.setNull(i++, Types.VARCHAR);
				stmt.setNull(i++, Types.BIGINT);
			}
			stmt.setBoolean(i++, data.isHasVariant());
			stmt.executeUpdate();
			rtn.success();

		} catch (Exception e) {
			rtn.error(e.getMessage());
			e.printStackTrace();
		}
		return rtn;

	}

	public ViewResult<Product> updateProduct(Product data, Connection conn) {
		ViewResult<Product> rtn = new ViewResult<>();
		try {
			String sql = "UPDATE public.stock";
			PrepareQuery q = new PrepareQuery(sql);
			q.addSetClause("code", data.getCode(), Operator.EQUAL, Type.VARCHAR);
			q.addSetClause("name", data.getName(), Operator.EQUAL, Type.VARCHAR);
			q.addSetClause("mdate", data.getMdate(), Operator.EQUAL, Type.DATE);
			q.addSetClause("biz_status", data.getBizStatus(), Operator.EQUAL, Type.NUMBER);
			if (data.getProductGroup() != null){
				if(data.getProductGroup().isAcceptNull()){
					q.addSetClause("fk_stock_group", null, Operator.EQUAL, Type.ID, true);
				}else{
					q.addSetClause("fk_stock_group", data.getProductGroup().getId(), Operator.EQUAL, Type.ID);
				}
			}
			q.addSetClause("has_variants", data.isHasVariant(), Operator.EQUAL, Type.BOOLEAN);

			if (data.getPacktype() != null)
				q.addSetClause("fk_packtype", data.getPacktype().getId(), Operator.EQUAL, Type.ID);

			q.addSetClause("manufacture", data.getManufacture(), Operator.EQUAL, Type.VARCHAR);
			q.addSetClause("stock_brand_title", data.getStockBrandTitle(), Operator.EQUAL, Type.VARCHAR);
			q.addSetClause("description", data.getDescription(), Operator.EQUAL, Type.VARCHAR);
			q.addSetClause("features", data.getFeatures(), Operator.EQUAL, Type.VARCHAR);
			if (data.getImage() != null) {
				q.addSetClause("fk_image", data.getImage().getId(), Operator.EQUAL, Type.ID);
				q.addSetClause("path", data.getImage().getName(), Operator.EQUAL, Type.VARCHAR);
			}

			q.addWhereClause("id", data.getId(), Operator.EQUAL, Type.ID);
			PreparedStatement stmt = q.getPrepareStatement(conn);
			stmt.executeUpdate();
			rtn.success();

		} catch (Exception e) {
			rtn.error(e.getMessage());
			e.printStackTrace();
		}
		return rtn;

	}

	public ViewResult<Product> changeProductStatus(String id, Date modifiedDate, int status, Connection conn) {
		ViewResult<Product> rtn = new ViewResult<>();
		try {
			String sql = "UPDATE public.stock";
			PrepareQuery q = new PrepareQuery(sql);
			q.addSetClause("biz_status", status, Operator.EQUAL, Type.NUMBER);
			q.addSetClause("mdate", modifiedDate, Operator.EQUAL, Type.DATE);
			q.addWhereClause("id", id, Operator.EQUAL, Type.ID);
			PreparedStatement stmt = q.getPrepareStatement(conn);
			stmt.executeUpdate();
			rtn.success();
		} catch (Exception e) {
			rtn.error(e.getMessage());
			e.printStackTrace();
		}
		return rtn;

	}

	public ViewResult<Product> deleteProduct(String id, Connection conn) {
		ViewResult<Product> rtn = new ViewResult<>();
		try {
			String sql = "delete from public.stock";
			PrepareQuery q = new PrepareQuery(sql);
			q.addWhereClause("id", id, Operator.EQUAL, Type.ID);
			PreparedStatement stmt = q.getPrepareStatement(conn);
			stmt.executeUpdate();
			rtn.success();
		} catch (Exception e) {
			rtn.error(e.getMessage());
			e.printStackTrace();
		}
		return rtn;

	}

	/**
	 stock_variant
	 */
	public ViewResult<ProductVariant> getInventoryStockVariants(InventoryManageCri data, Connection conn) {
		ViewResult<ProductVariant> result = new ViewResult<>(null, new ArrayList<>());
		try {
			String sql = "select " 
					+ "sv.id as sv_id," 
					+ "sv.biz_status as sv_biz_status,"
					+ "sv.seller_sku as seller_sku," 
					+ "sv.start_date," 
					+ "sv.end_date," 
					+ "sv.your_price,"
					+ "sv.selling_price," 
					+ "sv.quantity," 
					+ "sv.status stock_status,"
					+ "sv.path as image_path,"
					+ "sv.fk_condition as condition_id,"
					+ "sv.is_default,"
					+ "sv.description as description,"
					+ "sv.features as features,"
					+ "condition.name as condition_name, "
					+ "stock.fk_theme as theme_id, "
					+ "theme.name as theme_name,"
					+ "optionHeader1.id as optionHeader1_id," 
					+ "optionHeader1.name as optionHeader1_name,"
					+ "optionDetail1.id as optionDetail1_id," 
					+ "optionDetail1.name as optionDetail1_name,"
					+ "optionDetail1.code as optionDetail1_code," 
					+ "optionHeader2.id as optionHeader2_id,"
					+ "optionHeader2.name as optionHeader2_name," 
					+ "optionDetail2.id as optionDetail2_id,"
					+ "optionDetail2.name as optionDetail2_name," 
					+ "optionDetail2.code as optionDetail2_code,"
					+ "optionHeader3.id as optionHeader3_id," 
					+ "optionHeader3.name as optionHeader3_name,"
					+ "optionDetail3.id as optionDetail3_id," 
					+ "optionDetail3.name as optionDetail3_name,"
					+ "optionDetail3.code as optionDetail3_code,"

					+ "sv.variant1_title," + "sv.variant2_title," + "sv.variant3_title" + " from stock_variant sv "
					+ " inner join stock on stock.id = sv.fk_stock"
					+ " left join stock_variant_theme theme on theme.id = stock.fk_theme"
					+ " left join stock_variant_option_header optionHeader1 on optionHeader1.id = sv.fk_stock_variant1_option_hdr "
					+ " left join stock_variant_option_detail optionDetail1 on optionDetail1.id = sv.fk_stock_variant1_option_dtl "
					+ " left join stock_variant_option_header optionHeader2 on optionHeader2.id = sv.fk_stock_variant2_option_hdr "
					+ " left join stock_variant_option_detail optionDetail2 on optionDetail2.id = sv.fk_stock_variant2_option_dtl "
					+ " left join stock_variant_option_header optionHeader3 on optionHeader3.id = sv.fk_stock_variant3_option_hdr "
					+ " left join stock_variant_option_detail optionDetail3 on optionDetail3.id = sv.fk_stock_variant3_option_dtl "
					+ " inner join condition on condition.id = sv.fk_condition ";
			PrepareQuery q = new PrepareQuery(sql);
			q.addWhereClause("sv.fk_stock", data.getId(), Operator.EQUAL, Type.ID);
			q.addWhereClause("sv.seller_sku", data.getSellerSKU(), Operator.LIKE_ALL, Type.VARCHAR);
			q.addWhereClause("sv.id", data.getVariantId(), Operator.EQUAL, Type.ID);
			PreparedStatement stmt = q.getPrepareStatement(conn, -1, -1, "", "sv.fk_stock_variant1_option_dtl,sv.fk_stock_variant2_option_dtl,sv.fk_stock_variant3_option_dtl", "asc");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				ProductVariant var = new ProductVariant();
				var.setId(rs.getString("sv_id"));
				var.setBizStatus(rs.getInt("sv_biz_status"));
				var.setSellerSku(rs.getString("seller_sku"));
				var.setStartDate(CommonUtility.convertTimeStamp_bydb(rs.getString("start_date")));
				var.setEndDate(rs.getTimestamp("end_date"));
				var.setPrice(rs.getDouble("your_price"));
				var.setSellingPrice(rs.getDouble("selling_price"));
				var.setQuantity(rs.getInt("quantity"));
				var.setStatus(rs.getInt("stock_status"));
				var.setProfile(rs.getString("image_path").equals("") ? null
						: new ImageData("", "", rs.getString("image_path"), false, ""));
				var.setDefaultProduct(rs.getBoolean("is_default"));
				var.setCondition(new Condition(rs.getString("condition_id")));
				var.getCondition().setName(rs.getString("condition_name"));
				Array features = rs.getArray("features");
				var.setFeatures(features==null?null:(String[]) features.getArray());
				var.setDescription(rs.getString("description"));
				if (rs.getString("optionHeader1_id") != null) {
					ProductVariantOptionHeader header1 = new ProductVariantOptionHeader();
					header1.setId(rs.getString("optionHeader1_id"));
					header1.setName(rs.getString("optionHeader1_name"));
					ProductVariantOptionDetail detail1 = new ProductVariantOptionDetail();
					detail1.setId(rs.getString("optionDetail1_id"));
					detail1.setName(rs.getString("optionDetail1_name"));
					detail1.setCode(rs.getString("optionDetail1_code"));
					var.setFirstStockVariantOptionHeader(header1);
					var.setFirstStockVariantOptionDetail(detail1);
					var.setFirstVariantTitle(rs.getString("variant1_title"));
				}
				if (rs.getString("optionHeader2_id") != null) {
					ProductVariantOptionHeader header2 = new ProductVariantOptionHeader();
					header2.setId(rs.getString("optionHeader2_id"));
					header2.setName(rs.getString("optionHeader2_name"));
					ProductVariantOptionDetail detail2 = new ProductVariantOptionDetail();
					detail2.setId(rs.getString("optionDetail2_id"));
					detail2.setName(rs.getString("optionDetail2_name"));
					detail2.setCode(rs.getString("optionDetail2_code"));
					var.setSecondStockVariantOptionHeader(header2);
					var.setSecondStockVariantOptionDetail(detail2);
					var.setSecondVariantTitle(rs.getString("variant2_title"));
				}

				if (rs.getString("optionHeader3_id") != null) {
					ProductVariantOptionHeader header3 = new ProductVariantOptionHeader();
					header3.setId(rs.getString("optionHeader3_id"));
					header3.setName(rs.getString("optionHeader3_name"));
					ProductVariantOptionDetail detail3 = new ProductVariantOptionDetail();
					detail3.setId(rs.getString("optionDetail3_id"));
					detail3.setName(rs.getString("optionDetail3_name"));
					detail3.setCode(rs.getString("optionDetail3_code"));
					var.setThirdStockVariantOptionHeader(header3);
					var.setThirdStockVariantOptionDetail(detail3);
					var.setThirdVariantTitle(rs.getString("variant3_title"));
				}

				var.setImage(getImages(null,var.getId(),null, conn).list);

				if (data.getVariantId() != null && data.getVariantId().equals("-1") && data.getVariantId().equals("")) {
					result.data = var;
				} else {
					result.list.add(var);
				}

			}
			result.success();
		} catch (Exception e) {
			result.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	public ViewResult<ProductVariant> saveProductVariant(ProductVariant data, Connection conn) {
		ViewResult<ProductVariant> rtn = new ViewResult<>();
		try {

			String sql = "INSERT INTO public.stock_variant(id, seller_sku, fk_stock,"
					+ " fk_stock_variant1_option_hdr, fk_stock_variant1_option_dtl, variant1_title,"
					+ " fk_stock_variant2_option_hdr, fk_stock_variant2_option_dtl, variant2_title,"
					+ " fk_stock_variant3_option_hdr, fk_stock_variant3_option_dtl, variant3_title,"
					+ " your_price, selling_price, quantity, condition_desc,"
					+ " start_date, end_date, cdate, mdate,fk_condition," + " path,is_default,"
					+ " features,description)"
					+ "	VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
					+ " ?::timestamp, ?::timestamp, ?::timestamp, ?::timestamp, ?,?,?,?,?);";
			PreparedStatement stmt = conn.prepareStatement(sql);
			int i = 1;
			stmt.setLong(i++, Long.parseLong(data.getId()));
			stmt.setString(i++, data.getSellerSku());
			stmt.setLong(i++, Long.parseLong(data.getProduct().getId()));

			if (data.getFirstStockVariantOptionHeader() != null) {
				stmt.setLong(i++, Long.parseLong(data.getFirstStockVariantOptionHeader().getId()));
				stmt.setLong(i++, Long.parseLong(data.getFirstStockVariantOptionDetail().getId()));
				stmt.setString(i++, data.getFirstVariantTitle());
			} else {
				stmt.setNull(i++, Types.BIGINT);
				stmt.setNull(i++, Types.BIGINT);
				stmt.setString(i++, "");
			}
			if (data.getSecondStockVariantOptionHeader() != null) {
				stmt.setLong(i++, Long.parseLong(data.getSecondStockVariantOptionHeader().getId()));
				stmt.setLong(i++, Long.parseLong(data.getSecondStockVariantOptionDetail().getId()));
				stmt.setString(i++, data.getSecondVariantTitle());

			} else {
				stmt.setNull(i++, Types.BIGINT);
				stmt.setNull(i++, Types.BIGINT);
				stmt.setString(i++, "");
			}
			if (data.getThirdStockVariantOptionHeader() != null) {
				stmt.setLong(i++, Long.parseLong(data.getThirdStockVariantOptionHeader().getId()));
				stmt.setLong(i++, Long.parseLong(data.getThirdStockVariantOptionDetail().getId()));
				stmt.setString(i++, data.getThirdVariantTitle());

			} else {
				stmt.setNull(i++, Types.BIGINT);
				stmt.setNull(i++, Types.BIGINT);
				stmt.setString(i++, "");
			}

			stmt.setDouble(i++, data.getPrice());
			stmt.setDouble(i++, data.getSellingPrice());
			stmt.setInt(i++, data.getQuantity());
			stmt.setString(i++, data.getConditionDesc());
			stmt.setString(i++, CommonUtility.convertTimeStamp_2db(data.getStartDate()));
			stmt.setString(i++, CommonUtility.convertTimeStamp_2db(data.getEndDate()));
			stmt.setString(i++, CommonUtility.convertTimeStamp_2db(data.getCdate()));
			stmt.setString(i++, CommonUtility.convertTimeStamp_2db(data.getMdate()));
			stmt.setLong(i++, Long.parseLong(data.getCondition().getId()));
			stmt.setString(i++, data.getProfile() == null ? "" : data.getProfile().getName());
			stmt.setBoolean(i++, data.isDefaultProduct());
			stmt.setArray(i++,  conn.createArrayOf("text", data.getFeatures()));
			stmt.setString(i++, data.getDescription());
			stmt.executeUpdate();
			rtn.success();

		} catch (Exception e) {
			rtn.error(e.getMessage());
			e.printStackTrace();
		}
		return rtn;

	}

	public ViewResult<ProductVariant> updateProductVariant(ProductVariant data, Connection conn) {
		ViewResult<ProductVariant> result = new ViewResult<>();
		try {
			String sql = "update public.stock_variant";
			PrepareQuery q = new PrepareQuery(sql);
			q.addSetClause("mdate", CommonUtility.convertDate_2db(data.getMdate()), Operator.EQUAL, Type.DATE);
			q.addSetClause("biz_status", data.getBizStatus(), Operator.EQUAL, Type.ID);
			q.addSetClause("your_price", data.getPrice(), Operator.EQUAL, Type.NUMBER);
			q.addSetClause("selling_price", data.getSellingPrice(), Operator.EQUAL, Type.NUMBER);
			q.addSetClause("quantity", data.getQuantity(), Operator.EQUAL, Type.NUMBER);
			q.addSetClause("variant1_title", data.getFirstVariantTitle(), Operator.EQUAL, Type.VARCHAR);
			q.addSetClause("variant2_title", data.getSecondVariantTitle(), Operator.EQUAL, Type.VARCHAR);
			q.addSetClause("variant3_title", data.getThirdVariantTitle(), Operator.EQUAL, Type.VARCHAR);
			q.addSetClause("seller_sku", data.getSellerSku(), Operator.EQUAL, Type.VARCHAR);
			if (data.getProfile() != null) {
				q.addSetClause("path", data.getProfile().getName(), Operator.EQUAL, Type.VARCHAR);
			}
			if (data.getFirstStockVariantOptionHeader() != null) {
				q.addSetClause("fk_stock_variant1_option_hdr", data.getFirstStockVariantOptionHeader().getId(),
						Operator.EQUAL, Type.ID);
			}
			if (data.getFirstStockVariantOptionDetail() != null) {
				q.addSetClause("fk_stock_variant1_option_dtl", data.getFirstStockVariantOptionDetail().getId(),
						Operator.EQUAL, Type.ID);
			}
			if (data.getSecondStockVariantOptionHeader() != null) {
				q.addSetClause("fk_stock_variant2_option_hdr", data.getSecondStockVariantOptionHeader().getId(),
						Operator.EQUAL, Type.ID);
			}
			if (data.getSecondStockVariantOptionDetail() != null) {
				q.addSetClause("fk_stock_variant2_option_dtl", data.getSecondStockVariantOptionDetail().getId(),
						Operator.EQUAL, Type.ID);
			}
			if (data.getThirdStockVariantOptionHeader() != null) {
				q.addSetClause("fk_stock_variant3_option_hdr", data.getThirdStockVariantOptionHeader().getId(),
						Operator.EQUAL, Type.ID);
			}
			if (data.getThirdStockVariantOptionDetail() != null) {
				q.addSetClause("fk_stock_variant3_option_dtl", data.getThirdStockVariantOptionDetail().getId(),
						Operator.EQUAL, Type.ID);
			}
			if (data.getCondition() != null) {
				q.addSetClause("fk_condition", data.getCondition().getId(), Operator.EQUAL, Type.ID);

			}
			q.addSetClause("start_date", data.getStartDate(), Operator.EQUAL, Type.DATE);
			q.addSetClause("end_date", data.getEndDate(), Operator.EQUAL, Type.DATE);
			q.addSetClause("is_default", data.isDefaultProduct(), Operator.EQUAL, Type.BOOLEAN);
			q.addSetClause("description", data.getDescription(), Operator.EQUAL, Type.VARCHAR);
			q.addSetClause("features", data.getFeatures(), Operator.EQUAL, Type.ARRAY);
			q.addWhereClause("id", data.getId(), Operator.EQUAL, Type.ID);
			PreparedStatement stmt = q.getPrepareStatement(conn);
			stmt.executeUpdate();
			result.success();
		} catch (Exception e) {
			result.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	public ViewResult<Condition> getCondition(Connection conn) {
		ViewResult<Condition> result = new ViewResult<>(null, new ArrayList<>());
		try {
			String sql = "select id,code,name,cdate,mdate from condition";
			PrepareQuery p = new PrepareQuery(sql);
			p.addWhereClause("status", ComEnum.RowStatus.Deleted.getCode(), Operator.NOT_EQUAL, Type.NUMBER);
			PreparedStatement stmt = p.getPrepareStatement(conn, -1, -1, "", "", "");
			ResultSet rs = stmt.executeQuery();
			Condition data;
			while (rs.next()) {
				data = new Condition();
				data.setId(rs.getString("id"));
				data.setCode(rs.getString("code"));
				data.setName(rs.getString("name"));
				data.setMdate(rs.getDate("cdate"));
				data.setCdate(rs.getDate("mdate"));

				result.list.add(data);
			}
			result.success();
		} catch (Exception e) {
			result.error(e.getMessage());
		}
		return result;

	}

	public ViewResult<ProductImage> saveStockImage(ProductImage data, Connection conn) {
		ViewResult<ProductImage> result = new ViewResult<>();
		try {
			String sql = "INSERT INTO public.stock_image ("
					+ " status, cdate, mdate, fk_stock_variant, fk_stock, path,image_type,fk_image)"
					+ " VALUES ( ?, ?::timestamp, ?::timestamp, ?, ?, ?, ?, ?);";
			PreparedStatement stmt = conn.prepareStatement(sql);
			int i = 1;
			stmt.setInt(i++, data.getStatus());
			stmt.setString(i++, CommonUtility.convertTimeStamp_2db(data.getCdate()));
			stmt.setString(i++, CommonUtility.convertTimeStamp_2db(data.getMdate()));
			stmt.setLong(i++, Long.parseLong(data.getVariant().getId()));
			stmt.setLong(i++, Long.parseLong(data.getStock().getId()));
			stmt.setString(i++, data.getPath());
			stmt.setInt(i++, data.getImageType());
			stmt.setLong(i++, Long.parseLong(data.getImage().getId()));
			stmt.executeUpdate();
			result.success();
		} catch (Exception e) {
			result.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	public ViewResult<ProductImage> updateStockImage(ProductImage data, Connection conn) {
		ViewResult<ProductImage> result = new ViewResult<>();
		try {
			String sql = "UPDATE public.stock_image SET mdate=?::timestamp,fk_image=?,path=?" + " WHERE id=?;";
			PreparedStatement stmt = conn.prepareStatement(sql);
			int i = 1;
			stmt.setString(i++, CommonUtility.convertTimeStamp_2db(data.getMdate()));
			stmt.setLong(i++, Long.parseLong(data.getImage().getId()));
			stmt.setString(i++, data.getPath());
			stmt.setLong(i++, Long.parseLong(data.getId()));
			stmt.executeUpdate();
			result.success();
		} catch (Exception e) {
			result.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	public ViewResult<ProductImage> deleteImage(ProductImage data, Connection conn) {
		ViewResult<ProductImage> result = new ViewResult<>();
		try {
			String sql = "delete from stock_image";
			PrepareQuery q = new PrepareQuery(sql);
			if (data.getVariant() != null)
				q.addWhereClause("fk_stock_variant", data.getVariant().getId(), Operator.EQUAL, Type.ID);
			else if (data.getStock() != null) {
				q.addWhereClause("fk_stock", data.getStock().getId(), Operator.EQUAL, Type.ID);
			} else {
				q.addWhereClause("id", data.getId(), Operator.EQUAL, Type.ID);
			}

			PreparedStatement stmt = q.getPrepareStatement(conn);
			stmt.executeUpdate();
			result.success();
		} catch (Exception e) {
			result.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	public ViewResult<ProductImage> getImages(String productId,String variantId,String imgId, Connection conn) {
		ViewResult<ProductImage> result = new ViewResult<>(null, new ArrayList<>());
		try {
			String sql = "select stock_image.id as stkimg_id,stock_image.cdate as stkimg_cdate,stock_image.path as stkimg_path,stock_image.image_type as "
					+ "stkimg_imageType,image.id as image_id from stock_image inner join image on image.id=stock_image.fk_image ";
			PrepareQuery q = new PrepareQuery(sql);
			if (variantId != null)
				q.addWhereClause("stock_image.fk_stock_variant", variantId, Operator.EQUAL, Type.ID);
			if (productId != null)
				q.addWhereClause("stock_image.fk_stock",productId, Operator.EQUAL, Type.ID);
			q.addWhereClause("id", imgId, Operator.EQUAL, Type.ID);

			PreparedStatement stmt = q.getPrepareStatement(conn);
			ResultSet rs = stmt.executeQuery();
			ProductImage data;
			while (rs.next()) {
				data = new ProductImage();
				data.setId(rs.getString("stkimg_id"));
				data.setCdate(rs.getDate("stkimg_cdate"));
				data.setPath(rs.getString("stkimg_path"));
				data.setImageType(rs.getInt("stkimg_imageType"));
				data.setImage(new ImageData());
				data.getImage().setId(rs.getString("image_id"));
				result.list.add(data);

			}
			result.success();
		} catch (Exception e) {
			result.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	public ViewResult<Product> getProduct(InventoryManageCri cri, Connection conn) {
		ViewResult<Product> result = new ViewResult<>(null, new ArrayList<>());
		try {

			String sql = "select count(*) over() as total,"
					+ "count(sv.id) as vari_count,"
					+ "stock.id,"
					+ "stock.biz_status, "
					+ "stock.code, "
					+ "stock.name, "
					+ "stock.status, "
					+ "stock.cdate, "
					+ "stock.mdate, "
					+ "stock.fk_packtype,"
					+ "stock.manufacture, "
					+ "stock.stock_brand_title,"
					+ "stock.fk_category, "
					+ "stock.fk_brand, "
					+ "stock.path,"
					+ "stock.has_variants,"
					+ "stock.path as image_path,"
					+ "image.id as image_id,"
					+ "sg.id as prodgroup_id,"
					+ "sg.name as prodgroup_name";
			String sql2 = " from stock inner join stock_variant sv on sv.fk_stock = stock.id "
					+ " left join image on image.id = stock.fk_image" 
					+ " inner join brand on brand.id=stock.fk_brand"
					+ " left join stock_group sg on sg.id = stock.fk_stock_group";
			sql += sql2;
			PrepareQuery q = new PrepareQuery(sql);
			q.addWhereClause("stock.id", cri.getId(), Operator.EQUAL, Type.ID);
			q.addWhereClause("stock.status", cri.getStatus(), Operator.EQUAL, Type.NUMBER);
			q.addWhereClause("stock.fk_brand", cri.getBrand(), Operator.EQUAL, Type.ID);
			q.addWhereClause("brand.code", cri.getBrandCode(), Operator.EQUAL, Type.VARCHAR);
			q.addWhereClause("stock.fk_stock_group", cri.getProductGroup(), Operator.EQUAL, Type.ID);
			q.addOrClause("stock.name", cri.getName(), Operator.LIKE_ALL, Type.VARCHAR);
			q.addOrClause("sv.seller_sku", cri.getName(), Operator.LIKE_ALL, Type.VARCHAR);

			PreparedStatement stmt = q.getPrepareStatement(conn, cri.getCurrentRow(), cri.getMaxRowsPerPage(),
					"stock.id,image.id,sg.id ", cri.getOrderby(), cri.getSorting());
			ResultSet rs = stmt.executeQuery();
			Product data;
			while (rs.next()) {
				if (result.list.size() == 0) {
					result.totalItem = rs.getInt("total");
				}
				data = new Product();
				data.setVariantCount(rs.getInt("vari_count"));
				data.setId(rs.getString("id"));
				data.setBizStatus(rs.getInt("biz_status"));
				data.setCode(rs.getString("code"));
				data.setName(rs.getString("name"));
				data.setStatus(rs.getInt("status"));
				data.setCdate(rs.getDate("cdate"));
				data.setMdate(rs.getDate("mdate"));
				data.setManufacture(rs.getString("manufacture"));
				data.setStockBrandTitle(rs.getString("stock_brand_title"));
				data.setCategory(new Category(rs.getString("fk_category")));
				data.setPacktype(new PackType(rs.getString("fk_packtype")));
				if (!rs.getBoolean("has_variants")) {
					InventoryManageCri pcri = new InventoryManageCri();
					pcri.setId(data.getId());
					data.setStandAlone(getInventoryStockVariants(pcri, conn).list.get(0));
				} else {
					
				}
				if (rs.getString("prodgroup_id") != null) {
					ProductGroup pg = new ProductGroup();
					pg.setId(rs.getString("prodgroup_id"));
					pg.setName(rs.getString("prodgroup_name"));
					data.setProductGroup(pg);
				}
				if (cri.getId() != null && !cri.getId().equals("-1") && !cri.getId().equals("")) {
					result.data = data;
				} else {
					result.list.add(data);
				}
			}
			result.success();
		} catch (Exception e) {
			result.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	public ViewResult<String> getImageIdByProductId(String productId, Connection conn) {
		ViewResult<String> result = new ViewResult<>();
		try {
			String sql = "select fk_image from stock where id=?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setLong(1, Long.parseLong(productId));
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				result.data = rs.getString("fk_image");
			}
			result.success();
		} catch (Exception e) {
			e.printStackTrace();
			result.error(e.getMessage());
		}
		return result;
	}

	

	public ViewResult<ProductVariant> changeVariantStatus(String productId, String variantId, int status,
			Date modifiedDate, Connection conn) {
		ViewResult<ProductVariant> result = new ViewResult<>();
		try {
			String sql = "update public.stock_variant";
			PrepareQuery q = new PrepareQuery(sql);
			q.addSetClause("biz_status", status, Operator.EQUAL, Type.NUMBER);
			q.addSetClause("mdate", modifiedDate, Operator.EQUAL, Type.DATE);
			q.addWhereClause("id", variantId, Operator.EQUAL, Type.ID);
			q.addWhereClause("fk_stock", productId, Operator.EQUAL, Type.ID);
			PreparedStatement stmt = q.getPrepareStatement(conn);
			CommonUtility.outputLog(q.getSql(), env);
			stmt.executeUpdate();
			result.success();
		} catch (Exception e) {
			result.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	public ViewResult<ProductVariant> deleteVariant(InventoryManageCri data, Connection conn) {
		ViewResult<ProductVariant> result = new ViewResult<>();
		try {
			String sql = String.format("delete from public.stock_variant where id in (%s)",
					data.getDeleteId().stream().map(v -> "?").collect(Collectors.joining(", ")));
			PreparedStatement stmt = conn.prepareStatement(sql);
			int count = 1;
			for (int i = 0; i < data.getDeleteId().size(); i++) {
				stmt.setLong(count++, Long.parseLong(data.getDeleteId().get(i)));
			}
			stmt.executeUpdate();
			result.success();
		} catch (Exception e) {
			result.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	public ViewResult<ProductVariant> deleteVariant(String id, String fkStockId, Connection conn) {
		ViewResult<ProductVariant> result = new ViewResult<>();
		try {
			String sql = "delete from public.stock_variant";

			PrepareQuery q = new PrepareQuery(sql);
			q.addWhereClause("fk_stock", fkStockId, Operator.EQUAL, Type.ID);
			q.addWhereClause("id", id, Operator.EQUAL, Type.ID);
			PreparedStatement stmt = q.getPrepareStatement(conn);
			stmt.executeUpdate();
			result.success();
		} catch (Exception e) {
			result.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	public ViewResult<String> isStandAlone(String id, Connection conn) {
		ViewResult<String> result = new ViewResult<>();
		try {
			String sql = "select fk_theme from stock";
			PrepareQuery q = new PrepareQuery(sql);
			q.addWhereClause("id", id, Operator.EQUAL, Type.ID);
			PreparedStatement stmt = q.getPrepareStatement(conn);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				result.data = rs.getString("fk_theme");
			}
			result.success();
		} catch (Exception e) {
			result.error(e.getMessage());
		}
		return result;
	}

	public ViewResult<ProductGroup> saveProductGroup(ProductGroup data, Connection conn) {
		ViewResult<ProductGroup> result = new ViewResult<>();
		try {
			String sql = "INSERT INTO public.stock_group("
					+ "id, status, code, name, cdate, mdate, biz_status, fk_brand)"
					+ " VALUES (?, ?, ?, ?, ?::timestamp, ?::timestamp, ?, ?)";
			PreparedStatement stmt = conn.prepareStatement(sql);
			int i = 1;
			stmt.setLong(i++, Long.parseLong(data.getId()));
			stmt.setInt(i++, data.getStatus());
			stmt.setString(i++, data.getCode());
			stmt.setString(i++, data.getName());
			stmt.setString(i++, CommonUtility.convertTimeStamp_2db(data.getCdate()));
			stmt.setString(i++, CommonUtility.convertTimeStamp_2db(data.getMdate()));
			stmt.setInt(i++, data.getBizStatus());
			stmt.setLong(i++, Long.parseLong(data.getBrand().getId()));
			stmt.executeUpdate();
			result.success();
		} catch (Exception e) {
			result.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	public ViewResult<ProductGroup> updateProductGroup(ProductGroup data, Connection conn) {
		ViewResult<ProductGroup> result = new ViewResult<>();
		try {
			String sql = "update public.stock_group";
			PrepareQuery q = new PrepareQuery(sql);
			if (data.getBrand() != null)
				q.addSetClause("fk_brand", data.getBrand().getId(), Operator.EQUAL, Type.ID);
			q.addSetClause("code", data.getCode(), Operator.EQUAL, Type.VARCHAR);
			q.addSetClause("name", data.getName(), Operator.EQUAL, Type.VARCHAR);
			q.addSetClause("mdate", data.getMdate(), Operator.EQUAL, Type.DATE);
			q.addWhereClause("id", data.getId(), Operator.EQUAL, Type.ID);
			PreparedStatement stmt = q.getPrepareStatement(conn);

			stmt.executeUpdate();
			result.success();
		} catch (Exception e) {
			result.error(e.getMessage());
		}
		return result;
	}
	
	public ViewResult<ProductGroup> getProductGroup(ProductGroup data,Connection conn){
		ViewResult<ProductGroup> result = new ViewResult<>();
		try {
			String sql = "select id, status, code, name, cdate, mdate, biz_status, fk_brand from public.stock_group";
			PrepareQuery q = new PrepareQuery(sql);
			if (data.getBrand() != null)
				q.addWhereClause("fk_brand", data.getBrand().getId(), Operator.EQUAL, Type.ID);
			q.addWhereClause("code", data.getCode(), Operator.EQUAL, Type.VARCHAR);
			q.addWhereClause("name", data.getName(), Operator.EQUAL, Type.VARCHAR);
			q.addWhereClause("mdate", data.getMdate(), Operator.EQUAL, Type.DATE);
			q.addWhereClause("id", data.getId(), Operator.EQUAL, Type.ID);
			PreparedStatement stmt = q.getPrepareStatement(conn);

			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				ProductGroup pg = new ProductGroup();
				pg.setId(rs.getString("id"));
				pg.setName(rs.getString("name"));
				pg.setBizStatus(rs.getInt("biz_status"));
				pg.setBrand(new Brand(rs.getString("fk_brand")));
				
				if(data.getId().equals("-1"))
					result.list.add(pg);
				else 
					result.data = pg;
			}
			result.success();
		} catch (Exception e) {
			result.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
		
	}
	
	public ViewResult<ProductSummary> getInventoryProductSummery(ProductSummary data,Connection conn){
		ViewResult<ProductSummary> result = new ViewResult<>(new  ProductSummary(),null);
		try {
			String sql = "select total_product,totalsellingprice,totalorginalprice from inv_product_summary(?)";
			PreparedStatement stmt = conn.prepareStatement(sql);
			int i = 1;
			stmt.setLong(i++, Long.parseLong(data.getBrandId()));
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				result.data.setTotalProduct(rs.getInt("total_product"));
				result.data.setTotalSellingPrice(rs.getDouble("totalsellingprice"));
				result.data.setTotalPrice(rs.getDouble("totalorginalprice"));
			}
			result.success();
		}catch (Exception e) {
			result.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	public ViewResult<String> setDefaultProductVari(String productId,Connection conn){
		ViewResult<String> result = new ViewResult<>();
		try{
			String sql = "select set_product_default(?) as id";
			PreparedStatement stmt = conn.prepareStatement(sql);
			int i = 1;
			stmt.setLong(i++, Long.parseLong(productId));
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				result.data = rs.getString("id");
			}
			result.success();
		}catch (Exception e) {
			result.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	public ViewResult<String> insertProductOptions(
			ProductOptions data,Connection conn) {
		ViewResult<String> result = new ViewResult<>();
		try{
			String sql = "insert into public.stock_variant_option("
					+ "id, status, cdate, mdate, fk_stock, fk_option_header, fk_option_detail, path)"
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";	
			PreparedStatement stmt = conn.prepareStatement(sql);
			int i=1;
			stmt.setLong(i++, Long.parseLong(data.getId()));
			stmt.setInt(i++, data.getStatus());
			stmt.setString(i++, CommonUtility.convertTimeStamp_2db(data.getCdate()));
			stmt.setString(i++, CommonUtility.convertTimeStamp_2db(data.getMdate()));
			stmt.setLong(i++, Long.parseLong(data.getStockId()));
			stmt.setLong(i++, Long.parseLong(data.getOptionHeaderId()));
			stmt.setLong(i++, Long.parseLong(data.getOptionDetailId()));
			stmt.setString(i++, data.getPath());
			stmt.executeUpdate();
			result.success();
			
		}catch(Exception e){
			result.error(e.getMessage());
			e.printStackTrace();
		}
		return result;

	}
	
	
}
