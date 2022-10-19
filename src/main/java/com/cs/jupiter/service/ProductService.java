package com.cs.jupiter.service;

import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.jupiter.dao.ImageDao;
import com.cs.jupiter.dao.ProductDao;
import com.cs.jupiter.dao.ProductMetaDao;
import com.cs.jupiter.model.interfaces.RequestCredential;
import com.cs.jupiter.model.interfaces.ViewResult;
import com.cs.jupiter.model.jun.InventoryManageCri;
import com.cs.jupiter.model.jun.ProductDetail;
import com.cs.jupiter.model.jun.ProductFilter;
import com.cs.jupiter.model.jun.ProductSummary;
import com.cs.jupiter.model.table.Condition;
import com.cs.jupiter.model.table.ImageData;
import com.cs.jupiter.model.table.Product;
import com.cs.jupiter.model.table.ProductGroup;
import com.cs.jupiter.model.table.ProductImage;
import com.cs.jupiter.model.table.ProductVariant;
import com.cs.jupiter.model.table.ProductVariantOptionHeader;
import com.cs.jupiter.utility.ComEnum;
import com.cs.jupiter.utility.ComEnum.RowStatus;
import com.cs.jupiter.utility.KeyFactory;

@Service
public class ProductService {
	@Autowired
	ProductDao dao;

	@Autowired
	ProductMetaDao metadao;

	@Autowired
	ImageService imageService;

	@Autowired
	ImageDao imageDao;

	private static Map<String, String> prodConst = new HashMap<>();
	static {
		prodConst.put("productName", "stock.name");
		prodConst.put("productVariantName", "stock.name");
	}

	public ViewResult<Product> saveStockSetup(Product data, RequestCredential cred, Connection conn) {
		ViewResult<Product> result;
		try {
			Date today = new Date();
			data.setCdate(today);
			data.setMdate(today);
			data.setId(KeyFactory.getStringId());
			ViewResult<ImageData> mainImageResult = imageService.completeSave(data.getImage(), cred, conn);
			data.setImage(mainImageResult.data);
			result = dao.saveProduct(data, conn);
			if (!result.isSucces()) {
				throw new Exception(result.message);
			}
			if (data.getVariant().size() == 0) {
				throw new Exception("invalid_stock_variant");
			}
			ViewResult<ProductVariant> resultVariant;
			for (short i = 0; i < data.getVariant().size(); i++) {
				data.getVariant().get(i).setId(KeyFactory.getStringId());
				data.getVariant().get(i).setProduct(new Product(data.getId()));
				data.getVariant().get(i).setCdate(today);
				data.getVariant().get(i).setMdate(today);
				if (data.getVariant().get(i).getImage() != null) {
					for (ProductImage image : data.getVariant().get(i).getImage()) {
						ViewResult<ImageData> imageSaveResult = imageService.completeSave(image.getImage(), cred, conn);
						if (imageSaveResult.isSucces()) {
							image.setImage(imageSaveResult.data);
							image.setStock(new Product(data.getId()));
							image.setVariant(data.getVariant().get(i));
							image.setCdate(today);
							image.setMdate(today);
							if (image.getImage() != null && image.getImageType() == 1) {
								// 1 is default image
								data.getVariant().get(i).setProfile(image.getImage());
							}
						} else {
							data.getVariant().get(i).setProfile(null);
						}

					}
				}
				resultVariant = dao.saveProductVariant(data.getVariant().get(i), conn);
				if (!resultVariant.isSucces()) {
					throw new Exception(resultVariant.message);
				}
				if (data.getVariant().get(i).getImage() != null) {
					for (ProductImage image : data.getVariant().get(i).getImage()) {
						if (image.getImage() != null) {
							image.setPath(image.getImage().getName());
							dao.saveStockImage(image, conn).isSucces();
						}

					}
				}

			}
			ViewResult<String> isDefaultProd = dao.setDefaultProductVari(data.getId(), conn);
			if (isDefaultProd.data == null || !isDefaultProd.isSucces()) {
				throw new Exception("fail_to_set_product_variant_default");
			}

			result.success();
		} catch (Exception e) {
			result = new ViewResult<>(e);
			e.printStackTrace();
		}
		return result;
	}

	public ViewResult<Product> updateStock(Product data, RequestCredential cred, Connection conn) {
		ViewResult<Product> result;
		try {
			if (data.getId().equals("") || data.getId().equals("0")) {
				throw new Exception("invalid_stock");
			}
			result = dao.updateProduct(data, conn);
			if (result.status != ComEnum.ErrorStatus.Success.getCode()) {
				throw new Exception("");
			}

		} catch (Exception e) {
			e.printStackTrace();
			result = new ViewResult<>(e);
		}
		return result;
	}

	public ViewResult<Condition> getConditionList(RequestCredential cred, Connection conn) {
		ViewResult<Condition> result;
		try {
			result = dao.getCondition(conn);
		} catch (Exception e) {
			result = new ViewResult<Condition>(e);
		}
		return result;
	}

	public ViewResult<ProductVariant> getInventoryStockVariants(InventoryManageCri cri, RequestCredential cred,
			Connection conn) {
		ViewResult<ProductVariant> result;
		try {
			result = dao.getInventoryStockVariants(cri, conn);
		} catch (Exception e) {
			result = new ViewResult<ProductVariant>(e);
		}
		return result;

	}

	public ViewResult<Product> getInventoryProducts(InventoryManageCri cri, RequestCredential cred, Connection conn) {
		ViewResult<Product> result;
		try {
			if (cri.getBrandId() == null || cri.getBrandId().equals("-1") || cri.getBrandId().equals("")) {
				if (cri.getBrandCode() == null || cri.getBrandCode().equals(""))
					throw new Exception("INVALID_BRAND");
			}
			switch (cri.getOrderby()) {
			case "product_name":
				cri.setOrderby("stock.name");
			case "product_date":
				cri.setOrderby("stock.cdate");
			default:
				cri.setOrderby("stock.name");
			}
			result = dao.getProduct(cri, conn);
		} catch (Exception e) {
			result = new ViewResult<Product>(e);
			e.printStackTrace();
		}
		return result;

	}

	public ViewResult<Product> getProductById(String id, RequestCredential cred, Connection conn) {
		ViewResult<Product> result;
		try {
			InventoryManageCri cri = new InventoryManageCri();
			cri.setId(id);
			cri.setRequestAdditionalDetail(true);
			result = dao.getProduct(cri, conn);
		} catch (Exception e) {
			result = new ViewResult<Product>(e);
			e.printStackTrace();
		}
		return result;

	}

	public ViewResult<Product> updateInventoryProduct(Product data, RequestCredential cred, Connection conn) {
		ViewResult<Product> result;
		Date today = new Date();
		try {
			if (data.getId() == null || data.getId().equals("-1") || data.getId().equals("")) {
				throw new Exception("invalid_data");
			}
			ViewResult<ImageData> profileImageResult = imageService.completeSave(data.getImage(), cred, conn);
			if (profileImageResult.isSucces()) {
				data.setImage(profileImageResult.data);
			} else {
				data.setImage(null);
			}
			data.setMdate(today);
			result = dao.updateProduct(data, conn);
			if (!result.isSucces())
				throw new Exception("fail_to_update_product");

			for (ProductVariant var : data.getVariant()) {
				if (var.getId() == null || var.getId().equals("-1")) {
					// new variant

					var.setId(KeyFactory.getStringId());
					var.setProduct(new Product(data.getId()));
					var.setCdate(today);
					var.setMdate(today);
					if (var.getImage() != null) {
						for (ProductImage image : var.getImage()) {
							ViewResult<ImageData> imageSaveResult = imageService.completeSave(image.getImage(), cred,
									conn);
							if (imageSaveResult.isSucces()) {
								image.setStock(new Product(data.getId()));
								image.setVariant(var);
								image.setCdate(today);
								image.setMdate(today);
								image.setImage(imageSaveResult.data);
								if (image.getImage() != null && image.getImageType() == 1) {
									// 1 is default image
									var.setProfile(image.getImage());
								}
							} else {
								var.setProfile(null);
							}
						}
					}
					ViewResult<ProductVariant> resultVariant = dao.saveProductVariant(var, conn);
					if (!resultVariant.isSucces()) {
						throw new Exception(resultVariant.message);
					}
					if (var.getImage() != null) {
						for (ProductImage image : var.getImage()) {
							if (image.getImage() != null) {
								image.setPath(image.getImage().getName());
								dao.saveStockImage(image, conn).isSucces();
							}

						}
					}
				} else {
					var.setMdate(today);
					if (var.getImage() != null) {
						for (ProductImage image : var.getImage()) {
							if (!image.getId().equals("-1") && image.getStatus() == 4) {
								if (!dao.deleteImage(image, conn).isSucces()) {
									throw new Exception("fail_to_remove_image");
								}
								if (!imageDao.deleteById(image.getImage().getId(), conn).isSucces()) {
									throw new Exception("fail_to_remove_inner_image");
								}
							} else {

								ViewResult<ImageData> imageSaveResult = imageService.completeSave(image.getImage(),
										cred, conn);
								if (imageSaveResult.isSucces()) {
									image.setStock(new Product(data.getId()));
									image.setVariant(var);
									image.setCdate(today);
									image.setMdate(today);
									image.setImage(imageSaveResult.data);

									boolean save = false;
									if (image.getImage() != null) {
										image.setPath(image.getImage().getName());
										if (image.getId() == null || image.getId().equals("-1")) {
											/* stock_image_id */
											save = dao.saveStockImage(image, conn).isSucces();
										} else {
											save = dao.updateStockImage(image, conn).isSucces();
										}
									}
									if (save && image.getImageType() == 1) {
										var.setProfile(image.getImage());
									}
								} else {
									var.setProfile(null);
								}

							}

						}
					}
					ViewResult<ProductVariant> varResult = dao.updateProductVariant(var, conn);
					if (!varResult.isSucces()) {
						throw new Exception("fail_to_update_product_variant");
					}

				}
			}
			/*
			 * ViewResult<String> isDefaultProd =
			 * dao.setDefaultProductVari(data.getId(), conn);
			 * if(isDefaultProd.data ==null || !isDefaultProd.isSucces()) {
			 * throw new Exception("fail_to_set_product_variant_default"); }
			 */
			result = new ViewResult<Product>();
			result.success();
		} catch (Exception e) {
			result = new ViewResult<Product>(e);
		}
		return result;

	}

	public ViewResult<Product> changeInventoryProductStatus(Product data, RequestCredential cred, Connection conn) {
		ViewResult<Product> result;
		try {
			Date today = new Date();
			if (data.getStatus() == RowStatus.Deleted.getCode()) {
				// delete image first
				if (this.deleteProductImageSequences(data.getId(), conn) == 400) {
					throw new Exception("fail_to_delete_images");
				}
				if (this.deleteVariantImageSequences(data.getId(), conn) == 400) {
					throw new Exception("fail_to_delete_images");
				}

				if (!dao.deleteVariant(null, data.getId(), conn).isSucces()) {
					throw new Exception("fail_to_delete_variant");
				}
				result = dao.deleteProduct(data.getId(), conn);
			} else {
				result = dao.changeProductStatus(data.getId(), today, data.getBizStatus(), conn);
				if (result.isSucces()) {
					if (!dao.changeVariantStatus(data.getId(), null, data.getBizStatus(), today, conn).isSucces()) {
						return new ViewResult<Product>(new Exception("cant_update_variant"));
					}
				}
			}

		} catch (Exception e) {
			result = new ViewResult<Product>(e);
		}
		return result;
	}

	private int deleteProductImageSequences(String productId, Connection conn) {
		ViewResult<String> getImageResult = dao.getImageIdByProductId(productId, conn);
		if (getImageResult.isSucces() && getImageResult.data == null) {
			return 200;
		}
		if (!getImageResult.isSucces()) {
			return 400;
		}
		ImageData imageData = new ImageData();
		imageData.setId(getImageResult.data);
		ViewResult<ImageData> deleteImageResult = imageDao.delete(imageData, conn);
		if (!deleteImageResult.isSucces() && deleteImageResult.message.equals("invalid_query_param")) {
			return 200;
		} else if (deleteImageResult.isSucces()) {
			return 200;
		} else {
			return 400;
		}
	}

	private int deleteVariantImageSequences(String productId, Connection conn) {
		ProductImage prodImage = new ProductImage();
		prodImage.setStock(new Product(productId));
		ViewResult<ProductImage> prodImageResult = dao.getImages(productId, null, null, conn);
		ViewResult<ProductImage> prodImageDeleteResult = dao.deleteImage(prodImage, conn);
		if (!prodImageDeleteResult.isSucces() && !prodImageDeleteResult.message.equals("invalid_query_param")) {
			return 400;
		} else if (!prodImageDeleteResult.isSucces() && prodImageDeleteResult.message.equals("invalid_query_param")) {
			return 200;
		} else {
			for (ProductImage pImage : prodImageResult.list) {
				imageDao.delete(pImage.getImage(), conn);
			}
			return 200;
		}

	}

	private int deleteVariantImageSequencesById(String variantId, Connection conn) {
		ProductImage prodImage = new ProductImage();
		prodImage.setVariant(new ProductVariant(variantId));
		ViewResult<ProductImage> prodImageResult = dao.getImages(null, variantId, null, conn);
		ViewResult<ProductImage> prodImageDeleteResult = dao.deleteImage(prodImage, conn);
		if (!prodImageDeleteResult.isSucces() && !prodImageDeleteResult.message.equals("invalid_query_param")) {
			return 400;
		} else if (!prodImageDeleteResult.isSucces() && prodImageDeleteResult.message.equals("invalid_query_param")) {
			return 200;
		} else {
			for (ProductImage pImage : prodImageResult.list) {
				imageDao.delete(pImage.getImage(), conn);
			}
			return 200;
		}

	}

	public ViewResult<ProductVariant> changeInventoryVariantStatus(ProductVariant data, RequestCredential cred,
			Connection conn) {
		ViewResult<ProductVariant> result;
		try {
			if (data.getStatus() == RowStatus.Deleted.getCode()) {
				if (this.deleteVariantImageSequencesById(data.getId(), conn) == 400)
					throw new Exception("fail_to_delete_images");
				result = dao.deleteVariant(data.getId(), null, conn);
			} else {
				result = dao.changeVariantStatus(null, data.getId(), data.getBizStatus(), new Date(), conn);
			}

		} catch (Exception e) {
			result = new ViewResult<ProductVariant>(e);
		}
		return result;
	}

	public ViewResult<ProductVariant> updateInventoryVariant(ProductVariant data, RequestCredential cred,
			Connection conn) {
		ViewResult<ProductVariant> result;
		try {
			if (data.getId() == null || data.getId().equals("-1") || data.getId().equals("")) {
				throw new Exception("invalid_data");
			}
			result = dao.updateProductVariant(data, conn);
		} catch (Exception e) {
			result = new ViewResult<ProductVariant>(e);
		}
		return result;

	}

	public ViewResult<ProductVariant> deleteInventoryVariant(InventoryManageCri data, RequestCredential cred,
			Connection conn) {
		ViewResult<ProductVariant> result;
		try {
			for (String variantId : data.getDeleteId()) {
				if (this.deleteVariantImageSequencesById(variantId, conn) == 400) {
					throw new Exception("fail_to_delete_images");
				}
			}
			result = dao.deleteVariant(data, conn);
		} catch (Exception e) {
			result = new ViewResult<ProductVariant>(e);
		}
		return result;

	}

	public ViewResult<ProductGroup> saveProductGroup(ProductGroup data, RequestCredential cred, Connection conn) {
		ViewResult<ProductGroup> result;
		try {
			Date today = new Date();
			data.setMdate(today);
			data.setCdate(today);
			data.setStatus(RowStatus.Normal.getCode());

			if (data.getId().equals("-1")) {
				data.setId(KeyFactory.getStringId());
				result = dao.saveProductGroup(data, conn);
			} else {
				result = dao.updateProductGroup(data, conn);
			}

			if (!result.isSucces()) {
				throw new Exception("fail_to_save_productgroup");
			}
			Product product;
			ViewResult<Product> productResult = null;
			for (Product pro : data.getProducts()) {
				product = new Product();
				product.setMdate(today);
				product.setId(pro.getId());
				product.setProductGroup(data);
				productResult = dao.updateProduct(product, conn);
				if (!productResult.isSucces()) {
					throw new Exception("fail_to_update_product");
				}
			}
			result.data = data;
		} catch (Exception e) {
			result = new ViewResult<ProductGroup>(e);
		}
		return result;
	}

	public ViewResult<ProductGroup> getProductGroup(ProductGroup data, RequestCredential cred, Connection con) {
		ViewResult<ProductGroup> result;
		try {
			result = dao.getProductGroup(data, con);
		} catch (Exception e) {
			result = new ViewResult<ProductGroup>(e);
			e.printStackTrace();
		}
		return result;
	}

	public ViewResult<ProductGroup> removeProductsFromProductGroup(ProductGroup data, RequestCredential cred,
			Connection conn) {
		ViewResult<ProductGroup> result = new ViewResult<>();
		try {
			Product prod;
			for (Product p : data.getProducts()) {
				prod = new Product();
				prod.setId(p.getId());
				prod.setProductGroup(new ProductGroup());
				prod.getProductGroup().setAcceptNull(true);
				if (!dao.updateProduct(prod, conn).isSucces()) {
					throw new Exception("fail to update product");
				}
			}
			result.success();
		} catch (Exception e) {
			result = new ViewResult<ProductGroup>(e);
			e.printStackTrace();
		}
		return result;
	}

	public ViewResult<ProductSummary> inventoryProductSummary(ProductSummary data, RequestCredential cred,
			Connection conn) {
		ViewResult<ProductSummary> result;
		try {
			if (data.getBrandId() == null || data.getBrandId().equals("-1")) {
				throw new Exception("invalid_request");
			}
			result = dao.getInventoryProductSummery(data, conn);
			if (result.isSucces()) {

				result.data.setProfit(result.data.getTotalSellingPrice() - result.data.getTotalPrice());
			}
		} catch (Exception e) {
			result = new ViewResult<ProductSummary>(e);
			e.printStackTrace();
		}
		return result;
	}

	/***
	 * product meta dao -->
	 */
	public ViewResult<ProductDetail> getProducts(ProductFilter filter, RequestCredential cred, Connection conn) {
		ViewResult<ProductDetail> result;
		try {
			if (filter.getSearchKeyWord() == null || filter.getSearchKeyWord().equals(""))
				filter.setDefaultProduct(true);
			else
				filter.setDefaultProduct(false);
			result = metadao.getProducts(filter, conn);
		} catch (Exception e) {
			result = new ViewResult<>(e);
			e.printStackTrace();
		}
		return result;
	}

	public ViewResult<ProductVariantOptionHeader> getProductVariantOptions(String productId, RequestCredential cred,
			Connection conn) {
		ViewResult<ProductVariantOptionHeader> result;
		try {
			result = metadao.getGroupingOptionsOfProduct(productId, conn);
		} catch (Exception e) {
			result = new ViewResult<>(e);
			e.printStackTrace();
		}
		return result;
	}

	
}
