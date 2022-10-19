package com.cs.jupiter.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import org.springframework.stereotype.Repository;
import com.cs.jupiter.model.interfaces.ViewResult;
import com.cs.jupiter.model.table.Order;
import com.cs.jupiter.model.table.OrderItem;
import com.cs.jupiter.model.table.PaymentType;
import com.cs.jupiter.utility.CommonUtility;
import com.cs.jupiter.utility.PrepareQuery;
import com.cs.jupiter.utility.PrepareQuery.Operator;
import com.cs.jupiter.utility.PrepareQuery.Type;

@Repository
public class TransactionDao {
	public ViewResult<String> insertOrderHeader(Order data,Connection conn){
		ViewResult<String> result = new  ViewResult<>();
		try{
			String sql = "insert into public.order"
					+ "(id, cdate, mdate, status, biz_status, order_date,"
					+ "user_name, user_address, user_phoneno, user_email, "
					+ "location, fk_payment, comment, delivery_time) "
					+ "values (?, ?::timestamp, ?::timestamp, ?, ?, ?::timestamp, ?, ?, ?, ?, ST_GeomFromText(?), ?, ?, ?)";
			PreparedStatement stmt = conn.prepareStatement(sql);
			int i = 1;
			stmt.setLong(i++, Long.parseLong(data.getId()));
			stmt.setString(i++, CommonUtility.convertTimeStamp_2db(data.getMdate()));
			stmt.setString(i++, CommonUtility.convertTimeStamp_2db(data.getCdate()));
			stmt.setInt(i++, data.getStatus());
			stmt.setInt(i++, data.getBizStatus());
			stmt.setString(i++, CommonUtility.convertTimeStamp_2db(data.getOrderedDate()));
			stmt.setString(i++, data.getUserName()); 
			stmt.setString(i++, data.getUserAddress());
			stmt.setString(i++, data.getUserPhoneNo());
			stmt.setString(i++, data.getUserEmail());
			stmt.setString(i++, data.getLocation());
			stmt.setLong(i++, Long.parseLong(data.getPaymentType().getId()));
			stmt.setString(i++, data.getComment());
			stmt.setString(i++, data.getDeliveryTime());
			int count = stmt.executeUpdate();
			if(count>0){
				result.success();
			}else{
				result.error();
			}
		}catch(Exception e){
			result.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	public ViewResult<String> updateTotalPriceOrderHeader(Order data,Connection conn){
		ViewResult<String> result = new  ViewResult<>();
		try{
			String sql = "update public.order";
			PrepareQuery q = new PrepareQuery(sql);
			q.addSetClause("total", data.getTotal(), Operator.EQUAL, Type.NUMBER);
			q.addWhereClause("id", data.getId(), Operator.EQUAL, Type.ID);
			PreparedStatement stmt = q.getPrepareStatement(conn);
			if(stmt.executeUpdate()>0){
				result.success();
			}else{
				result.error("fail_to_update_amount");
			}
		}catch(Exception e){
			result.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	public ViewResult<String> insertOrderItem(OrderItem data,Connection conn){
		ViewResult<String> result = new  ViewResult<>();
		try{
			String sql = "insert into order_item("
					+ "id, cdate, mdate, status, biz_status, fk_order, fk_stock_variant, price_each, quantity, price_total, item_name)"
					+ " values (?, ?::timestamp, ?::timestamp, ?, ?, ?, ?, ?, ?, ?, ?);";
			PreparedStatement stmt = conn.prepareStatement(sql);
			int i = 1;
			stmt.setLong(i++, Long.parseLong(data.getId()));
			stmt.setString(i++, CommonUtility.convertTimeStamp_2db(data.getCdate()));
			stmt.setString(i++, CommonUtility.convertTimeStamp_2db(data.getMdate()));
			stmt.setInt(i++, data.getStatus());
			stmt.setInt(i++, data.getBizStatus());
			stmt.setLong(i++, Long.parseLong(data.getOrder().getId()));
			stmt.setLong(i++, Long.parseLong(data.getProduct().getId()));
			stmt.setDouble(i++, data.getPriceEach());
			stmt.setInt(i++, data.getQuantity());
			stmt.setDouble(i++, data.getPriceTotal());
			stmt.setString(i++, data.getProductName());
			int count = stmt.executeUpdate();
			if(count>0){
				result.success();
			}else{
				result.error();
			}
		}catch(Exception e){
			result.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	public ViewResult<PaymentType> getPaymentTypes(Connection conn){
		ViewResult<PaymentType> result = new  ViewResult<>(null,new ArrayList<>());
		try{
			String sql = "select id,code,name from payment_types where status<>?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			int i = 1;
			stmt.setInt(i++, 4);
			ResultSet rs = stmt.executeQuery();
			PaymentType data;
			while(rs.next()){
				data = new PaymentType();
				data.setId(rs.getString("id"));
				data.setCode(rs.getString("code"));
				data.setName(rs.getString("name"));
				result.list.add(data);
			}
			result.success();
		}catch(Exception e){
			result.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
}
