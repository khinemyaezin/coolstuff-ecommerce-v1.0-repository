package com.cs.jupiter.controller;

import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cs.jupiter.model.interfaces.ViewResult;
import com.cs.jupiter.model.jun.InventoryManageCri;
import com.cs.jupiter.model.jun.ProductAttribute;
import com.cs.jupiter.model.jun.ProductAttributeSetup;
import com.cs.jupiter.model.jun.ProductSummary;
import com.cs.jupiter.model.table.CategoryVariantOption;
import com.cs.jupiter.model.table.Condition;
import com.cs.jupiter.model.table.Product;
import com.cs.jupiter.model.table.ProductGroup;
import com.cs.jupiter.model.table.ProductVariant;
import com.cs.jupiter.model.table.ProductVariantOptionDetail;
import com.cs.jupiter.model.table.ProductVariantOptionHeader;
import com.cs.jupiter.model.table.ProductVariantTheme;
import com.cs.jupiter.service.CategoryService;
import com.cs.jupiter.service.ProductService;
import com.cs.jupiter.service.VariantService;
import com.cs.jupiter.utility.AuthGuard;

@RestController
@RequestMapping("/product")
public class ProductController extends RequestController {
	@Autowired
	DataSource ds;

	@Autowired
	VariantService variantService;

	@Autowired
	ProductService stockService;
	
	@Autowired
	CategoryService categoryService;
	
	@Autowired
	AuthGuard auth;

	@PostMapping(value = "/variant/option/save")
	public ViewResult<ProductVariantOptionHeader> saveStockVariant(@RequestBody ProductVariantOptionHeader data,
			HttpServletRequest req, HttpServletResponse resp) {
		ViewResult<ProductVariantOptionHeader> res;
		try (Connection con = ds.getConnection()) {
			con.setAutoCommit(false);
			res = variantService.saveStockVariantOption(data, getReqHeader(req, resp), con);
			res.completeTransaction(con);
			return res;
		} catch (SQLException e) {
			return new ViewResult<>(e);
		}

	}

	@PostMapping(value = "/variant/option/update")
	public ViewResult<ProductVariantOptionHeader> updateStockVariant(@RequestBody ProductVariantOptionHeader data,
			HttpServletRequest req, HttpServletResponse resp) {
		ViewResult<ProductVariantOptionHeader> res;
		try (Connection con = ds.getConnection()) {
			con.setAutoCommit(false);
			res = variantService.updateStockVariantOption(data, getReqHeader(req, resp), con);
			res.completeTransaction(con);
			return res;
		} catch (SQLException e) {
			return new ViewResult<>(e);
		}

	}

	@PostMapping(value = "/variant/option/header/get")
	public ViewResult<ProductVariantOptionHeader> getOptionHeader(
			@RequestParam(name = "categoryId",required = false, defaultValue = "-1") String categoryId,
			@RequestBody ProductVariantOptionHeader data,
			HttpServletRequest req, HttpServletResponse resp) {
		ViewResult<ProductVariantOptionHeader> res;
		try (Connection con = ds.getConnection()) {
			res = variantService.getOptionHeader(data,categoryId, getReqHeader(req, resp), con);
			return res;
		} catch (SQLException e) {
			return new ViewResult<>(e);
		}
	}
	

	@GetMapping(value = "/variant/option/header/delete/{id}")
	public ViewResult<String> deleteStockVariantHeader(@PathVariable("id") String id, HttpServletRequest req,
			HttpServletResponse resp) {
		ViewResult<String> res;
		try (Connection con = ds.getConnection()) {
			con.setAutoCommit(false);
			res = variantService.deleteStockVariantOption(id, getReqHeader(req, resp), con);
			res.completeTransaction(con);
			return res;
		} catch (SQLException e) {
			return new ViewResult<>(e);
		}
	}

	@GetMapping(value = "/variant/option/detail/delete/{id}")
	public ViewResult<ProductVariantOptionDetail> deleteStockVariantDetail(@PathVariable("id") String id,
			HttpServletRequest req, HttpServletResponse resp) {
		try (Connection con = ds.getConnection()) {
			ViewResult<ProductVariantOptionDetail> res;
			con.setAutoCommit(false);
			res = variantService.deleteStockVariantOptionDetail(id, getReqHeader(req, resp), con);
			res.completeTransaction(con);
			return res;
		} catch (SQLException e) {
			return new ViewResult<>(e);
		}
	}

	@PostMapping(value = "/variant/option/detail/get")
	public ViewResult<ProductVariantOptionDetail> getStockVariantDetail(@RequestBody ProductVariantOptionDetail data,
			HttpServletRequest req, HttpServletResponse resp) {
		try (Connection con = ds.getConnection()) {
			return variantService.getStockVariantOptionDetail(data, getReqHeader(req, resp), con);
		} catch (SQLException e) {
			return new ViewResult<>(e);
		}
	}

	@PostMapping(value = "/variant/theme/get")
	public ViewResult<ProductVariantTheme> getStockVariantThemeDetailOption(@RequestBody ProductVariantTheme data,
			HttpServletRequest req, HttpServletResponse resp) {
		try (Connection con = ds.getConnection()) {
			return variantService.getStockVariantTheme(data, getReqHeader(req, resp), con);
		} catch (SQLException e) {
			return new ViewResult<ProductVariantTheme>(e);
		}

	}

	@PostMapping(value = "/variant/theme/save")
	public ViewResult<ProductVariantTheme> themeSave(@RequestBody ProductVariantTheme data, HttpServletRequest req,
			HttpServletResponse resp) {
		ViewResult<ProductVariantTheme> result;
		try (Connection con = ds.getConnection()) {
			con.setAutoCommit(false);
			result = variantService.saveTheme(data, getReqHeader(req, resp), con);
			result.completeTransaction(con);
		} catch (SQLException e) {
			return new ViewResult<ProductVariantTheme>(e);
		}
		return result;
	}

	@GetMapping(value = "/condition/get")
	public ViewResult<Condition> getConditionList(HttpServletRequest req, HttpServletResponse resp) {
		try (Connection conn = ds.getConnection()) {
			return stockService.getConditionList(getReqHeader(req, resp), conn);
		} catch (SQLException e) {
			return new ViewResult<Condition>(e);
		}
	}

	@PostMapping(value = "/inventory/sku/save")
	public ViewResult<Product> saveStock(@RequestBody Product data, HttpServletRequest req, HttpServletResponse resp) {
		ViewResult<Product> rs;
		try (Connection con = ds.getConnection()) {
			con.setAutoCommit(false);
			rs = stockService.saveStockSetup(data, getReqHeader(req, resp), con);
			rs.completeTransaction(con);
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
			return new ViewResult<>(e);

		}
	}

	@PostMapping(value = "/inventory/sku/get")
	public ViewResult<Product> getInventoryProducts(@RequestBody InventoryManageCri data, HttpServletRequest req,
			HttpServletResponse resp) {
		ViewResult<Product> rs;
		try (Connection con = ds.getConnection()) {
			rs = stockService.getInventoryProducts(data, getReqHeader(req, resp), con);
			return rs;
		} catch (SQLException e) {
			return new ViewResult<>(e);
		}
	}

	@PostMapping(value = "/inventory/sku/update")
	public ViewResult<Product> getInventoryProductsUpdate(@RequestBody Product data, HttpServletRequest req,
			HttpServletResponse resp) {
		ViewResult<Product> rs;
		try (Connection con = ds.getConnection()) {
			con.setAutoCommit(false);
			rs = stockService.updateInventoryProduct(data, getReqHeader(req, resp), con);
			rs.completeTransaction(con);
			return rs;
		} catch (SQLException e) {
			return new ViewResult<>(e);
		}
	}

	@PostMapping(value = "/inventory/sku/status/update")
	public ViewResult<Product> changeProductStatus(@RequestBody Product data, HttpServletRequest req,
			HttpServletResponse resp) {
		ViewResult<Product> rs;
		try (Connection con = ds.getConnection()) {
			con.setAutoCommit(false);
			rs = stockService.changeInventoryProductStatus(data, getReqHeader(req, resp), con);
			rs.completeTransaction(con);
			return rs;
		} catch (SQLException e) {
			return new ViewResult<>(e);
		}
	}

	@GetMapping(value = "/inventory/sku/get/{id}")
	public ViewResult<Product> getInventoryProducts(@PathVariable("id") String id, HttpServletRequest req,
			HttpServletResponse resp) {
		ViewResult<Product> rs;
		try (Connection con = ds.getConnection()) {
			rs = stockService.getProductById(id, getReqHeader(req, resp), con);
			return rs;
		} catch (SQLException e) {
			return new ViewResult<>(e);
		}
	}

	@PostMapping(value = "/inventory/variant/get")
	public ViewResult<ProductVariant> getInventoryStockVariants(@RequestBody InventoryManageCri data,
			HttpServletRequest req, HttpServletResponse resp) {
		ViewResult<ProductVariant> rs;
		try (Connection con = ds.getConnection()) {
			rs = stockService.getInventoryStockVariants(data, getReqHeader(req, resp), con);
			return rs;
		} catch (SQLException e) {
			return new ViewResult<>(e);
		}
	}

	@PostMapping(value = "/inventory/variant/update")
	public ViewResult<ProductVariant> updateProductVariant(@RequestBody ProductVariant data, HttpServletRequest req,
			HttpServletResponse resp) {
		ViewResult<ProductVariant> rs;
		try (Connection con = ds.getConnection()) {
			con.setAutoCommit(false);
			rs = stockService.updateInventoryVariant(data, getReqHeader(req, resp), con);
			rs.completeTransaction(con);
			return rs;
		} catch (SQLException e) {
			return new ViewResult<>(e);
		}
	}

	@PostMapping(value = "/inventory/variant/status/update")
	public ViewResult<ProductVariant> updateInventoryVariantStatus(@RequestBody ProductVariant data,
			HttpServletRequest req, HttpServletResponse resp) {
		ViewResult<ProductVariant> rs;
		try (Connection con = ds.getConnection()) {
			con.setAutoCommit(false);
			rs = stockService.changeInventoryVariantStatus(data, getReqHeader(req, resp), con);
			rs.completeTransaction(con);
			return rs;
		} catch (SQLException e) {
			return new ViewResult<>(e);
		}
	}

	@PostMapping(value = "/inventory/variant/delete")
	public ViewResult<ProductVariant> deleteProductVariant(@RequestBody InventoryManageCri data, HttpServletRequest req,
			HttpServletResponse resp) {
		ViewResult<ProductVariant> rs;
		try (Connection con = ds.getConnection()) {
			con.setAutoCommit(false);
			rs = stockService.deleteInventoryVariant(data, getReqHeader(req, resp), con);
			rs.completeTransaction(con);
			return rs;
		} catch (SQLException e) {
			return new ViewResult<>(e);
		}
	}

	@PostMapping(value = "/inventory/product-group/save")
	public ViewResult<ProductGroup> productGroupSetup(@RequestBody ProductGroup data, HttpServletRequest req,
			HttpServletResponse resp) {
		ViewResult<ProductGroup> rs;
		try (Connection con = ds.getConnection()) {
			con.setAutoCommit(false);
			rs = stockService.saveProductGroup(data, getReqHeader(req, resp), con);
			rs.completeTransaction(con);
			return rs;
		} catch (SQLException e) {
			return new ViewResult<>(e);
		}
	}

	@PostMapping(value = "/inventory/product-group/get")
	public ViewResult<ProductGroup> getproductGroup(@RequestBody ProductGroup data, HttpServletRequest req,
			HttpServletResponse resp) {
		ViewResult<ProductGroup> rs;
		try (Connection con = ds.getConnection()) {
			rs = stockService.getProductGroup(data, getReqHeader(req, resp), con);
			return rs;
		} catch (SQLException e) {
			return new ViewResult<>(e);
		}
	}

	@PostMapping(value = "/inventory/product-group/remove-products")
	public ViewResult<ProductGroup> removeProductsFromProductGroup(@RequestBody ProductGroup data,
			HttpServletRequest req, HttpServletResponse resp) {
		ViewResult<ProductGroup> rs;
		try (Connection con = ds.getConnection()) {
			con.setAutoCommit(false);
			rs = stockService.removeProductsFromProductGroup(data, getReqHeader(req, resp), con);
			rs.completeTransaction(con);

		} catch (SQLException e) {
			return new ViewResult<>(e);
		}
		return rs;
	}

	@PostMapping(value = "/inventory/summary")
	public ViewResult<ProductSummary> inventoryProductSummary(@RequestBody ProductSummary data, HttpServletRequest req,
			HttpServletResponse resp) {
		ViewResult<ProductSummary> rs;
		try (Connection con = ds.getConnection()) {
			rs = stockService.inventoryProductSummary(data, getReqHeader(req, resp), con);
		} catch (SQLException e) {
			return new ViewResult<>(e);
		}
		return rs;
	}
	
	@PostMapping(value = "/attributes/save")
	public ViewResult<CategoryVariantOption> saveCategoryVariantOption(@RequestBody ProductAttributeSetup data, HttpServletRequest req,
			HttpServletResponse resp) {
		ViewResult<CategoryVariantOption> rs;
		try (Connection con = ds.getConnection()) {
			con.setAutoCommit(false);
			rs = categoryService.saveCategoryVariantOption(data, getReqHeader(req, resp), con);
			rs.completeTransaction(con);
		} catch (SQLException e) {
			return new ViewResult<>(e);
		}
		return rs;
	}
	
	@PostMapping(value = "/attributes/delete")
	public ViewResult<CategoryVariantOption> deleteCategoryVariantOption(@RequestBody CategoryVariantOption data, HttpServletRequest req,
			HttpServletResponse resp) {
		ViewResult<CategoryVariantOption> rs;
		try (Connection con = ds.getConnection()) {
			con.setAutoCommit(false);
			rs = categoryService.deleteCategoryVariantOption(data, getReqHeader(req, resp), con);
			rs.completeTransaction(con);
		} catch (SQLException e) {
			return new ViewResult<>(e);
		}
		return rs;
	}
	@GetMapping(value = "/attributes/option/get")
	public ViewResult<ProductAttribute> getOptionHeaderCategory(
			@RequestParam(name = "categoryid",required = true) String categoryId,
			HttpServletRequest req, HttpServletResponse resp) {
		ViewResult<ProductAttribute> res;
		try (Connection con = ds.getConnection()) {
			res = variantService.getOptionHeaderCategory(categoryId, getReqHeader(req, resp), con);
			return res;
		} catch (SQLException e) {
			return new ViewResult<>(e);
		}
	}
	
	
}
