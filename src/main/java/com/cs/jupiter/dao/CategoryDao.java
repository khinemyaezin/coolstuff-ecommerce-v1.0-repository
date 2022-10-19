package com.cs.jupiter.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.cs.jupiter.model.interfaces.ViewResult;
import com.cs.jupiter.model.table.Category;
import com.cs.jupiter.model.table.CategoryVariantOption;
import com.cs.jupiter.utility.CommonUtility;
import com.cs.jupiter.utility.PrepareQuery;

import ch.qos.logback.classic.Logger;


@Service
public class CategoryDao {
	
	@Autowired
	Environment env;
	private static final Logger logger = (Logger) LoggerFactory.getLogger(CategoryDao.class);
	

	public List<Category> getDepthAllNodes(Connection conn) {
		List<Category> catList = new ArrayList<>();
		try {
			String sql = "SELECT node.id,node.title, (COUNT(parent.title) - 1) AS depth "
					+ "FROM categories AS node,categories AS parent "
					+ "WHERE node.lft BETWEEN parent.lft AND parent.rgt "
					+ "GROUP BY node.title,node.lft,node.id "
//					+ "HAVING (COUNT(parent.title) - 1) <=3"
					+ "ORDER BY node.lft;";
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				Category c = new Category();
				c.setId(rs.getString("id"));
				c.setName(rs.getString("title"));
				c.setDepth(rs.getInt("depth"));
				catList.add(c);
			};
			return catList;

		} catch (
		Exception e) {
			return catList; 
		}
	}
	public int addNodeCategory(Category data, Connection conn){
		try{
			String sql = "select add_category(?,?);";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, Integer.parseInt(data.getId()));
			stmt.setString(2, data.getName());
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				return rs.getInt("add_category");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return -1;
		
	}
	public List<Category> searchCategoryLeaf(String title,Connection conn){
		try{
			String sql = "select id,title,path,depth from categories_leaf";
			PrepareQuery q = new PrepareQuery(sql);
			q.addWhereClause("path", title, PrepareQuery.Operator.LIKE_ALL, PrepareQuery.Type.VARCHAR);
		
			PreparedStatement stmt = q.getPrepareStatement(conn);
			ResultSet rs = stmt.executeQuery();
			List<Category> rtn = new ArrayList<>();
			while(rs.next()){
				Category c = new Category();
				c.setId(rs.getString("id"));
				c.setName(rs.getString("title"));
				c.setPath(rs.getString("path"));
				c.setDepth(rs.getInt("depth"));
				rtn.add(c);
			}
			return rtn;
		}catch(Exception e){
			e.printStackTrace();
			return new ArrayList<>();
		}
	}
	public ViewResult<String> generateLeaf(Connection conn){
		ViewResult<String> result = new ViewResult<>();
		try{
			String sql = "select add_category_leaf(?);";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, Integer.parseInt(env.getProperty("leaf-position-number")));
			if(stmt.execute()){
				result.success();
			}else{
				result.error();
			}
		}catch(Exception e){
			logger.info("[db-error]",e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	public List<Category> findTheImmediateSubordinatiesOfANode(String id, Connection conn){
		List<Category> rtn = new ArrayList<>();
		try {
			String sql = "SELECT node.id,node.title, (COUNT(parent.id) - (sub_tree.depth + 1)) AS depth FROM categories AS node, categories AS parent, categories AS sub_parent, ( SELECT node.id, (COUNT(parent.id) - 1) AS depth FROM categories AS node, categories AS parent WHERE node.lft BETWEEN parent.lft AND parent.rgt AND node.id = ? GROUP BY node.id,node.title,node.lft ORDER BY node.lft)AS sub_tree WHERE node.lft BETWEEN parent.lft AND parent.rgt AND node.lft BETWEEN sub_parent.lft AND sub_parent.rgt AND sub_parent.id = sub_tree.id GROUP BY node.id,node.title,sub_tree.depth,node.lft HAVING (COUNT(parent.id) - (sub_tree.depth + 1)) <= 1 ORDER BY node.lft;";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, Integer.parseInt(id));
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				
					Category c = new Category();
					c.setId(rs.getString("id"));
					c.setName(rs.getString("title"));
					c.setDepth(rs.getInt("depth"));
					rtn.add(c);
				
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rtn;
	}
	
	public ViewResult<Category> getCategoryLeaf(String name,Connection conn) {
		ViewResult<Category> result = new ViewResult<>(null,new ArrayList<Category>());
		try{
			String sql = "(SELECT * FROM categories_leaf WHERE lower(replace(path,'/',' ')) = lower('"+name+"') LIMIT 10 ) UNION"
					+ " (SELECT * FROM categories_leaf WHERE  lower(replace(path,'/',' ')) LIKE lower('"+name+"%') LIMIT 10) UNION"
					+ " (SELECT * FROM categories_leaf WHERE  lower(replace(path,'/',' ')) LIKE lower('%"+name+"%') LIMIT 10)";
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			Category c ;
			while(rs.next()){
				c = new Category();
				c.setId(rs.getString("id"));
				c.setName(rs.getString("title_leaf"));
				c.setPath(rs.getString("path").replace("root/", ""));
				c.setParentId(rs.getString("parent_id"));
				c.setDepth(rs.getInt("depth"));
				result.list.add(c);
			}
			result.success();
		}catch(Exception e){
			result.error(e.getMessage());
		}
		return result;
	}
	public ViewResult<Category> getCategoryLeafById(String id,Connection conn) {
		ViewResult<Category> result = new ViewResult<>(null,new ArrayList<Category>());
		try{
			String sql = "select * from categories_leaf where id = ? ";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setLong(1, Long.parseLong(id));
			ResultSet rs = stmt.executeQuery();
			Category c ;
			if(rs.next()){
				c = new Category();
				c.setId(rs.getString("id"));
				c.setName(rs.getString("title_leaf"));
				c.setPath(rs.getString("path").replace("root/", ""));
				c.setDepth(rs.getInt("depth"));
				result.data = c;
			}
			result.success();
		}catch(Exception e){
			result.error(e.getMessage());
		}
		return result;
	}
	public ViewResult<Category> getTopLayer(String id,Connection con){
		ViewResult<Category> result = new ViewResult<>();
		try{
			String sql = "SELECT parent.* "
					+ "FROM categories AS node, categories AS parent "
					+ "WHERE node.lft BETWEEN parent.lft AND parent.rgt "
					+ "AND node.id = ? "
					+ "ORDER BY parent.lft";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, Long.parseLong(id));
			ResultSet rs = stmt.executeQuery();
			Category c ;
			while (rs.next()) {
				
					c = new Category();
					c.setId(rs.getString("id"));
					c.setName(rs.getString("title"));
					c.setLft(rs.getInt("lft"));
					c.setRgt(rs.getInt("rgt"));

					result.list.add(c);
				
			}
			result.success();
		}catch(Exception e){
			result.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	public ViewResult<CategoryVariantOption> saveCategoryVariantOption(CategoryVariantOption data,Connection conn){
		ViewResult<CategoryVariantOption> result = new ViewResult<>();
		try{
			String sql = "INSERT INTO public.category_varoption( "
					+ " id, status, biz_status,cdate,mdate, fk_category, fk_var_option)"
					+ " values (?, ?, ?, ?::timestamp, ?::timestamp, ?, ?);";
			PreparedStatement stmt = conn.prepareStatement(sql);
			int i=1;
			stmt.setLong(i++, Long.parseLong(data.getId()));
			stmt.setInt(i++, data.getStatus());
			stmt.setInt(i++, data.getBizStatus());
			stmt.setString(i++, CommonUtility.convertDate_2db(data.getCdate()));
			stmt.setString(i++, CommonUtility.convertDate_2db(data.getCdate()));
			stmt.setLong(i++, Long.parseLong( data.getCategory().getId()));
			stmt.setLong(i++, Long.parseLong(data.getOption().getId()));
			stmt.executeUpdate();
			result.success();
			
		}catch(Exception e){
			result.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	public ViewResult<CategoryVariantOption> getCategoryVariantOption(CategoryVariantOption data,Connection conn){
		ViewResult<CategoryVariantOption> result = new ViewResult<>();
		try{
			String sql = "";
			PreparedStatement stmt = conn.prepareStatement(sql);
			int i=1;
			stmt.setLong(i++, Long.parseLong(data.getId()));
			stmt.executeUpdate();
			result.success();
		}catch(Exception e){
			result.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	public ViewResult<CategoryVariantOption> deleteCategoryVariantOption(CategoryVariantOption data,Connection conn){
		ViewResult<CategoryVariantOption> result = new ViewResult<>();
		try{
			String sql = "delete from public.category_varoption where id=?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			int i=1;
			stmt.setLong(i++, Long.parseLong(data.getId()));
			stmt.executeUpdate();
			result.success();
		}catch(Exception e){
			result.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	public ViewResult<Category> getCategoryByDepth(int depth,Connection conn) {
		ViewResult<Category> result = new ViewResult<>(null,new ArrayList<>());
		try {
			String sql = "select id,title,path,depth from cs_getcategorybydepth(?)";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, depth);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Category c = new Category();
				c.setId(rs.getString("id"));
				c.setName(rs.getString("title"));
				c.setDepth(rs.getInt("depth"));
				c.setPath(rs.getString("path").replace("root/", ""));
				result.list.add(c);
			};
			result.totalItem = result.list.size();
			result.success();
		} catch (
		Exception e) {
			result.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
}
