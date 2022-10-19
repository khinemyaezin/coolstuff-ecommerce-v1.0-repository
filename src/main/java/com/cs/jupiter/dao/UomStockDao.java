package com.cs.jupiter.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.stereotype.Service;

import com.cs.jupiter.model.interfaces.ViewResult;
import com.cs.jupiter.model.table.Currency;
import com.cs.jupiter.model.table.PriceType;
import com.cs.jupiter.model.table.Uom;
import com.cs.jupiter.model.table.UomStock;
import com.cs.jupiter.utility.ComEnum;
import com.cs.jupiter.utility.CommonUtility;

@Service
public class UomStockDao {

	public ViewResult<UomStock> insert(UomStock data,Connection conn) {
		ViewResult<UomStock> rtn = new ViewResult<>();
		try  {
			String sql = "INSERT INTO public.uom_stock("
					+ "id, code, name, status, cdate, mdate, fk_uom, fk_pricetype, fk_stock, specific_price, base, ratio)"
					+ "VALUES (?, ?, ?, ?, ?::date, ?::date, ?, ?, ?, ?, ?, ?);";
			PreparedStatement stmt = conn.prepareStatement(sql);
			int i = 1;
			stmt.setLong(i++, Long.parseLong(data.getId()));
			stmt.setString(i++, data.getCode());
			stmt.setString(i++, data.getName());
			stmt.setInt(i++, data.getStatus());
			stmt.setString(i++, CommonUtility.convertDate_2db(data.getCdate()));;
			stmt.setString(i++, CommonUtility.convertDate_2db(data.getMdate()));
			stmt.setLong(i++, Long.parseLong(data.getUom().getId()));
			stmt.setLong(i++, Long.parseLong(data.getPriceType().getId()));
			stmt.setLong(i++, Long.parseLong(data.getStock().getId()));
			stmt.setDouble(i++, data.getSpecficPrice());
			stmt.setInt(i++, data.getBase());
			stmt.setInt(i++, data.getRatio());
			rtn.status = stmt.executeUpdate() > 0 ? ComEnum.ErrorStatus.Success.getCode() : ComEnum.ErrorStatus.DatabaseError.getCode();
		} catch (SQLException e) {
			rtn.status = ComEnum.ErrorStatus.DatabaseError.getCode();
			rtn.message = e.getMessage();
			e.printStackTrace();
		}
		return rtn;

	}

	public ViewResult<UomStock> getAll(String data,Connection conn){
		ViewResult<UomStock> rtn = new ViewResult<>();
		try{
			String sql = "select "
					+ "us.id as us_id,us.code as us_code,us.name as us_name,us.status as us_status,us.base as us_base,us.ratio as us_ratio,pricing.price as us_price,pricing.rate as us_rate,pt.id as pt_id,pt.code as pt_code,pt.name as pt_name,pt.status as pt_status,uom.id as uom_id,uom.code as uom_code,uom.name as uom_name,currency.id as currency_id,currency.code as currency_code,currency.name as currency_name,"
					+ "currency.id as cur_id,"
					+ "currency.code as cur_code,"
					+ "currency.name as cur_name,"
					+ "currency.rate as cur_rate"
					+ " from uom_stock us"
					+ " inner join uom on uom.id = us.fk_uom"
					+ " inner join stock on stock.id = us.fk_stock"
					+ " inner join pricing on pricing.fk_uomstock = us.id"
					+ " inner join currency on currency.id = pricing.fk_currency"
					+ " inner join pricetype pt on pt.id = us.fk_pricetype"
					+ " where stock.id=?";
			int i = 1;
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setLong(i++, Long.parseLong(data));
			UomStock uom ;
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				uom = new UomStock();
				uom.setId(rs.getString("us_id"));
				uom.setCode(rs.getString("us_code"));
				uom.setName(rs.getString("us_name"));
				uom.setStatus(rs.getInt("us_status"));
				uom.setBase(rs.getInt("us_base"));
				uom.setRatio(rs.getInt("us_ratio"));
				uom.setPrice(rs.getDouble("us_price"));
				uom.setRate(rs.getDouble("us_rate"));
				
				PriceType pt = new PriceType();
				pt.setId(rs.getString("pt_id"));
				pt.setCode(rs.getString("pt_code"));
				pt.setName(rs.getString("pt_name"));
				pt.setStatus(rs.getInt("pt_status"));
				uom.setPriceType(pt);
				
				Uom u = new Uom();
				u.setId(rs.getString("uom_id"));
				u.setCode(rs.getString("uom_code"));
				u.setName(rs.getString("uom_name"));

				uom.setUom(u);
				
				Currency cur = new Currency();
				cur.setId(rs.getString("cur_id"));
				cur.setCode(rs.getString("cur_code"));
				cur.setName(rs.getString("cur_name"));
				cur.setRate(rs.getInt("cur_rate"));
				uom.setCurrency(cur);
				
				rtn.list.add(uom);
			}
			rtn.status=ComEnum.ErrorStatus.Success.getCode();
			
		}catch(Exception e){
			rtn.status = ComEnum.ErrorStatus.DatabaseError.getCode();
			rtn.message = e.getMessage();
			e.printStackTrace();
		}
		return rtn;
	}
	public ViewResult<UomStock> update(UomStock data,Connection conn){
		ViewResult<UomStock> rtn = new ViewResult<>();
		/*try {
			String sql = "UPDATE public.uom_stock";

			PrepareQuery q = new PrepareQuery();
			q.add("status", data.getStatus(), PrepareQuery.Operator.EQUAL, PrepareQuery.Type.NUMBER);
			q.add("mdate", CommonUtility.convertDate_2db(data.getMdate()), PrepareQuery.Operator.EQUAL,
					PrepareQuery.Type.DATE);
			if (data.getUom() != null)
				q.add("fk_uom", data.getUom().getId(), PrepareQuery.Operator.EQUAL, PrepareQuery.Type.ID);
			if (data.getPriceType() != null)
				q.add("fk_pricetype", data.getPriceType().getId(), PrepareQuery.Operator.EQUAL, PrepareQuery.Type.ID);
			q.add("base", data.getBase(), PrepareQuery.Operator.EQUAL, PrepareQuery.Type.NUMBER);
			q.add("ratio", data.getBase(), PrepareQuery.Operator.EQUAL, PrepareQuery.Type.NUMBER);
			sql += q.createSetStatement();
			q = new PrepareQuery();
			q.add("id", Long.parseLong(data.getId()), PrepareQuery.Operator.EQUAL, PrepareQuery.Type.ID);
			sql += q.createWhereStatement();
			
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.executeUpdate();
			rtn.status = ComEnum.ErrorStatus.Success.getCode();
			
		} catch (Exception e) {
			rtn.status = ComEnum.ErrorStatus.DatabaseError.getCode();
			rtn.message = e.getMessage();
			e.printStackTrace();
		}*/
		return rtn;
	}
	public ViewResult<UomStock> delete(String stockId,Connection conn) throws NumberFormatException, Exception {
		ViewResult<UomStock> rtn = new ViewResult<>();
		/*try  {

			String sql = "DELETE FROM public.uom_stock";
			PrepareQuery ps = new PrepareQuery();
			ps.add("fk_stock", Long.parseLong(stockId), PrepareQuery.Operator.EQUAL, PrepareQuery.Type.ID);
			sql += ps.createWhereStatement();
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.executeUpdate();
			rtn.status = ComEnum.ErrorStatus.Success.getCode();
		} catch (Exception e) {
			rtn.status = ComEnum.ErrorStatus.DatabaseError.getCode();
			rtn.message = e.getMessage();
			e.printStackTrace();
		}*/
		return rtn;

	}
}
