package com.cs.jupiter.dao;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.springframework.stereotype.Repository;

import com.cs.jupiter.model.interfaces.ViewResult;
import com.cs.jupiter.model.jun.ListProduct;
import com.cs.jupiter.model.jun.ProductDetail;
import com.cs.jupiter.model.jun.ProductFilter;
import com.cs.jupiter.model.table.Brand;
import com.cs.jupiter.model.table.Condition;
import com.cs.jupiter.model.table.ProductVariantOptionDetail;
import com.cs.jupiter.model.table.ProductVariantOptionHeader;

@Repository
public class ProductMetaDao extends ProductDao{
	/*********
	 get product options for product Details page
	 */
	public ViewResult<ProductVariantOptionHeader> getGroupingOptionsOfProduct(String productId, Connection conn) {
		ViewResult<ProductVariantOptionHeader> result = new ViewResult<>();
		try {
			ProductVariantOptionHeader header = null;
			ProductVariantOptionDetail optionDtl;
			String sql = "with variant as" + " (select * from stock_variant " + " where fk_stock=? and biz_status=?) "
						+ " (select distinct on (variant.fk_stock_variant1_option_dtl)"
						+ " ophdr.bz_status,ophdr.id header_id,ophdr.name header_name,opdtl.id option_id,opdtl.name option_name," 
						+ " variant.path,variant.fk_stock,variant.variant1_title as title,1 as option_type from variant" 
						+ " inner join stock_variant_option_header ophdr on ophdr.id=variant.fk_stock_variant1_option_hdr" 
						+ " inner join stock_variant_option_detail opdtl on variant.fk_stock_variant1_option_dtl = opdtl.id"
						+ " order by variant.fk_stock_variant1_option_dtl,variant.fk_stock_variant2_option_dtl,variant.fk_stock_variant3_option_dtl)" 
						+ " union all"
						+ " (select  distinct on (variant.fk_stock_variant2_option_dtl) "
						+ " ophdr.bz_status,ophdr.id header_id,ophdr.name header_name,opdtl.id option_id,opdtl.name option_name," 
						+ " variant.path,variant.fk_stock,variant.variant2_title,2 from variant" 
						+ " inner join stock_variant_option_header ophdr on ophdr.id=variant.fk_stock_variant2_option_hdr" 
						+ " inner join stock_variant_option_detail opdtl on variant.fk_stock_variant2_option_dtl = opdtl.id"
						+ " order by variant.fk_stock_variant2_option_dtl,variant.fk_stock_variant3_option_dtl)" 
						+ " union all"
						+ " (select distinct on (variant.fk_stock_variant3_option_dtl)" 
						+ " ophdr.bz_status,ophdr.id header_id,ophdr.name header_name,opdtl.id option_id,opdtl.name option_name," 
						+ " variant.path,variant.fk_stock,variant.variant3_title,3 from variant" 
						+ " inner join stock_variant_option_header ophdr on ophdr.id=variant.fk_stock_variant3_option_hdr" 
						+ " inner join stock_variant_option_detail opdtl on variant.fk_stock_variant3_option_dtl = opdtl.id)";
			PreparedStatement stmt = conn.prepareStatement(sql);
			short i=1;
			stmt.setLong(i++, Long.parseLong(productId));
			stmt.setInt(i++, 2);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				optionDtl = new ProductVariantOptionDetail();
				optionDtl.setId(rs.getString("option_id"));
				optionDtl.setName(rs.getString("title"));
				optionDtl.setPath(rs.getString("path"));
				optionDtl.setOptionType(rs.getShort("option_type"));

				if (header == null) {
					header = new ProductVariantOptionHeader();
					header.setId(rs.getString("header_id"));
					header.setName(rs.getString("header_name"));
					header.setBizStatus(rs.getInt("bz_status"));
					header.setOptionType(rs.getShort("option_type"));
					header.getDetails().add(optionDtl);
				} else if (!header.getId().equals(rs.getString("header_id"))) {
					result.list.add(header);
					header = new ProductVariantOptionHeader();
					header.setId(rs.getString("header_id"));
					header.setName(rs.getString("header_name"));
					header.setBizStatus(rs.getInt("bz_status"));
					header.setOptionType(rs.getShort("option_type"));
					header.getDetails().add(optionDtl);
				} else {
					header.setOptionType(rs.getShort("option_type"));
					header.getDetails().add(optionDtl);
				}

			}
			if (header != null) {
				result.list.add(header);
			}

			result.success();

		} catch (Exception e) {
			result.error(e.getMessage());
			e.printStackTrace();
		}
		return result;

	}

	/*********
	 get product list style
	 */
	public ViewResult<ProductDetail> getProducts(ProductFilter filter,Connection conn){
		ViewResult<ProductDetail> result = new ViewResult<>();
		try{
			String sql = " select distinct on (stock) "		
						+ " stock.id as stock_id,  "
						+ " stock.name as title,  "
						
						+ " stock.manufacture as manufacture,"
						+ " sv.id as var_id,  "
						+ " sv.fk_stock_variant1_option_hdr as option1HdrId,  "
						+ " sv.fk_stock_variant2_option_hdr as option2HdrId,  "
						+ " sv.fk_stock_variant3_option_hdr as option3HdrId,  "
						+ " sv.fk_stock_variant1_option_dtl as option1DtlId,  "
						+ " sv.fk_stock_variant2_option_dtl as option2DtlId,  "
						+ " sv.fk_stock_variant3_option_dtl as option3DtlId,  "
						+ " sv.variant1_title var1_title,  "
						+ " sv.variant2_title var2_title,  "
						+ " sv.variant3_title var3_title,  "
						+ " sv.path as img_link,  "
						+ " sv.selling_price as price,  "
						+ " sv.quantity as quantity,  "
						+ " con.name as con_name,"
						+"	brand.id as brand_id,"
						+"	brand.name as brand_name"
					
						+ " from stock"	
						
						+ " inner join stock_variant sv on stock.id = sv.fk_stock "
						+ " inner join condition con on con.id=sv.fk_condition "
						+ " inner join brand on brand.id=stock.fk_brand ";
			
			sql += "where sv.biz_status= ? ";
			
			if (filter.getId() != null) {
				sql += " and sv.id = " + filter.getId();
			}
			if (filter.getCategoryId() != null && !filter.getCategoryId().equals("")
					&& !filter.getCategoryId().equals("-1")) {
				sql += " and (stock.fk_category in (" 
						+ " select child.id "
						+ " from categories as child , categories as parent "
						+ " where child.rgt = child.lft + 1 and child.lft>parent.lft "
						+ " and child.rgt < parent.rgt and parent.id = " + filter.getCategoryId() + " ) or "
						+ " stock.fk_category in ( " + " select parent.id " + " from categories as parent "
						+ " where parent.rgt = parent.lft+1 and parent.id= " + filter.getCategoryId() + " )) ";

			}
			if (filter.getOptionDetail1Id() != null) {
				sql += " and sv.fk_stock_variant1_option_dtl = " + filter.getOptionDetail1Id();
			}
			if (filter.getOptionDetail2Id() != null) {
				sql += " and sv.fk_stock_variant2_option_dtl = " + filter.getOptionDetail2Id();
			}
			if (filter.getOptionDetail3Id() != null) {
				sql += " and sv.fk_stock_variant3_option_dtl = " + filter.getOptionDetail3Id();
			}
			if (filter.getParentId() != null) {
				sql += " and stock.id = " + filter.getParentId();
			}
			System.out.println(sql);
			PreparedStatement stmt = conn.prepareStatement(sql);
			short i=1;
			stmt.setInt(i++, 2);
			ResultSet rs = stmt.executeQuery();
			ProductDetail data;
			while(rs.next()){
				data = new ProductDetail();
				data.setId(rs.getString("var_id"));
				data.setPath(rs.getString("img_link"));
				if(rs.getString("option1HdrId")!=null){
					data.setFirstStockVariantOptionHeader(new ProductVariantOptionHeader(rs.getString("option1HdrId")));
					data.setFirstStockVariantOptionDetail(new ProductVariantOptionDetail(rs.getString("option1DtlId")));
					data.setFirstVariantTitle(rs.getString("var1_title"));
				}
				if(rs.getString("option2HdrId")!=null){
					data.setSecondStockVariantOptionHeader(new ProductVariantOptionHeader(rs.getString("option2HdrId")));
					data.setSecondStockVariantOptionDetail(new ProductVariantOptionDetail(rs.getString("option2DtlId")));
					data.setSecondVariantTitle(rs.getString("var2_title"));
				}
				if(rs.getString("option3HdrId")!=null){
					data.setThirdStockVariantOptionHeader(new ProductVariantOptionHeader(rs.getString("option3HdrId")));
					data.setThirdStockVariantOptionDetail(new ProductVariantOptionDetail(rs.getString("option3DtlId")));
					data.setThirdVariantTitle(rs.getString("var3_title"));
				}
				
				
				data.setSellingPrice(rs.getDouble("price"));
				data.setQuantity(rs.getInt("quantity"));
				data.setCondition(new Condition());
				data.getCondition().setName(rs.getString("con_name"));
				
				data.setParent(new ListProduct());
				data.getParent().setId(rs.getString("stock_id"));
				data.getParent().setTitle(rs.getString("title"));
				data.getParent().setManufacture(rs.getString("manufacture"));
				data.getParent().setBrand(new Brand(rs.getString("brand_id")));
				data.getParent().getBrand().setName(rs.getString("brand_name"));
				data.setImageList(getImages(null,data.getId(),null, conn).list);
				result.list.add(data);
			}
			result.totalItem = result.list.size();
			result.success();
		}catch (Exception e) {
			result.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	public ViewResult<ProductDetail> getProductDetail(String prodId,String variantId,Connection conn){
		ViewResult<ProductDetail> result = new ViewResult<>(new ProductDetail(),null);
		try{
			String sql = "select id,features,description from stock_variant where id=?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			short i=1;
			stmt.setLong(i++, Long.parseLong(variantId));
			ResultSet rs = stmt.executeQuery();
			
			if(rs.next()){
				result.data.setId(rs.getString("id"));
				Array features = rs.getArray("features");
				result.data.setFeatures(features==null?null:(String[])features.getArray());
				result.data.setDescription(rs.getString("description"));
				result.success();
			}
			
		}catch(Exception e){
			result.error(e.getMessage());
			e.getMessage();
			e.printStackTrace();
		}
		return result;
	}
}
